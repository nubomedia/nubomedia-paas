/*
 * Copyright (c) 2015-2016 Fraunhofer FOKUS
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.project.openbaton.nubomedia.paas.core.util;

import org.openbaton.catalogue.mano.common.*;
import org.openbaton.catalogue.mano.descriptor.InternalVirtualLink;
import org.openbaton.catalogue.mano.descriptor.NetworkServiceDescriptor;
import org.openbaton.catalogue.mano.descriptor.VirtualDeploymentUnit;
import org.openbaton.catalogue.mano.descriptor.VirtualNetworkFunctionDescriptor;
import org.openbaton.catalogue.nfvo.ConfigurationParameter;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.StunServerException;
import org.project.openbaton.nubomedia.paas.exceptions.openbaton.turnServerException;
import org.project.openbaton.nubomedia.paas.model.openbaton.Flavor;
import org.project.openbaton.nubomedia.paas.model.openbaton.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by gca on 27/05/16.
 */
@Service
@ConfigurationProperties(prefix="nfvo")
public class NSDUtil {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private String imageName;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public NetworkServiceDescriptor getNetworkServiceDescriptor(NetworkServiceDescriptor nsd, Flavor flavor, QoS Qos, boolean turnServerActivate, String mediaServerTurnIP, String mediaServerTurnUsername, String mediaServerTurnPassword, boolean stunServerActivate, String stunServerAddress, String stunServerPort, int scaleInOut, double scale_out_threshold) throws turnServerException, StunServerException {
        logger.debug("Start configure");
        nsd = this.injectFlavor(flavor.getValue(), this.imageName, nsd);
        logger.debug("After flavor the nsd is\n" + nsd.toString() + "\n****************************");

        if (Qos != null){
            logger.debug("\nSetting QoS");
            nsd = this.injectQoS(Qos.toString(),nsd);
            logger.debug("After QOS the nsd is\n" + nsd.toString() + "\n############################");
        }

        logger.debug("Setting Configuration parameters for mediaserver");
        nsd = this.setConfigurationParameters(turnServerActivate,mediaServerTurnIP,mediaServerTurnUsername,mediaServerTurnPassword,stunServerActivate,stunServerAddress,stunServerPort,nsd);
        logger.debug("Settled Configuration parameters for mediaserver, the new NSD is " + nsd.toString());

        if (scaleInOut > 1) {
            logger.debug("Setting autoscaling policies");
            nsd = this.enableAutoscaling(scaleInOut,scale_out_threshold,nsd);
        }

        return nsd;
    }


    private NetworkServiceDescriptor setConfigurationParameters(boolean turnServerActivate, String mediaServerTurnIP, String mediaServerTurnUsername, String mediaServerTurnPassword, boolean stunServerActivate, String stunServerAddress, String stunServerPort, NetworkServiceDescriptor nsd) throws turnServerException, StunServerException {
        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : nsd.getVnfd()) {
            if (vnfd.getEndpoint().equals("media-server")){

                org.openbaton.catalogue.nfvo.Configuration configuration = new org.openbaton.catalogue.nfvo.Configuration();
                configuration.setName("mediaserver");
                HashSet<ConfigurationParameter> cps = new HashSet<>();

                if (turnServerActivate) {
                    ConfigurationParameter cpTurnActivate = new ConfigurationParameter();
                    cpTurnActivate.setConfKey("mediaserver.turn-server.activate");
                    cpTurnActivate.setValue("true");
                    cps.add(cpTurnActivate);

                    if (mediaServerTurnIP != null) {

                        if (mediaServerTurnUsername == null || mediaServerTurnPassword == null) {
                            throw new turnServerException("No Authentication for Turn Server");
                        }
                        ConfigurationParameter cpIp = new ConfigurationParameter();
                        cpIp.setConfKey("mediaserver.turn-server.url");
                        cpIp.setValue(mediaServerTurnIP);
                        cps.add(cpIp);

                        ConfigurationParameter cpUser = new ConfigurationParameter();
                        cpUser.setConfKey("mediaserver.turn-server.username");
                        cpUser.setValue(mediaServerTurnUsername);
                        cps.add(cpUser);

                        ConfigurationParameter cpPassword = new ConfigurationParameter();
                        cpPassword.setConfKey("mediaserver.turn-server.password");
                        cpPassword.setValue(mediaServerTurnPassword);
                        cps.add(cpPassword);
                    }
                }
                else{
                    ConfigurationParameter cpTurnActivate = new ConfigurationParameter();
                    cpTurnActivate.setConfKey("mediaserver.turn-server.activate");
                    cpTurnActivate.setValue("false");
                    cps.add(cpTurnActivate);
                }

                if (stunServerActivate){
                    ConfigurationParameter cpStunAct = new ConfigurationParameter();
                    cpStunAct.setConfKey("mediaserver.stun-server.activate");
                    cpStunAct.setValue("true");
                    cps.add(cpStunAct);

                    if ((stunServerAddress != null && stunServerPort == null) || (stunServerAddress == null && stunServerPort != null)){
                        throw new StunServerException("Is mandatory to specify Stun server address and Stun server port");
                    }
                    else if (stunServerAddress != null && stunServerPort != null){
                        ConfigurationParameter cpStunAddr = new ConfigurationParameter();
                        cpStunAddr.setConfKey("mediaserver.stun-server.address");
                        cpStunAddr.setValue(stunServerAddress);
                        cps.add(cpStunAddr);

                        ConfigurationParameter cpStunPort = new ConfigurationParameter();
                        cpStunPort.setConfKey("mediaserver.stun-server.port");
                        cpStunPort.setValue(stunServerPort);
                        cps.add(cpStunPort);
                    }

                }else{
                    ConfigurationParameter cpStunAct = new ConfigurationParameter();
                    cpStunAct.setConfKey("mediaserver.stun-server.activate");
                    cpStunAct.setValue("false");
                    cps.add(cpStunAct);
                }

                configuration.setConfigurationParameters(cps);
                vnfd.setConfigurations(configuration);
                vnfds.add(vnfd);
            }
            else
                vnfds.add(vnfd);
        }
        nsd.setVnfd(vnfds);

        return nsd;
    }

    private NetworkServiceDescriptor injectQoS(String qos, NetworkServiceDescriptor nsd) {

        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : nsd.getVnfd()) {
            if (vnfd.getEndpoint().equals("media-server")){
                Set<InternalVirtualLink> vlds = new HashSet<>();
                for (InternalVirtualLink vld : vnfd.getVirtual_link()) {
                    Set<String> qoss = new HashSet<>();
                    qoss.add("minimum_bandwith:" + qos);
                    vld.setQos(qoss);
                    vlds.add(vld);
                }
                vnfd.setVirtual_link(vlds);
                vnfds.add(vnfd);
            }
            else
                vnfds.add(vnfd);
        }
        nsd.setVnfd(vnfds);
        return nsd;
    }

    private NetworkServiceDescriptor injectFlavor(String flavour, String imageName, NetworkServiceDescriptor networkServiceDescriptor){

        Set<VirtualNetworkFunctionDescriptor> vnfds = new HashSet<>();

        for (VirtualNetworkFunctionDescriptor vnfd : networkServiceDescriptor.getVnfd()) {
            if (vnfd.getEndpoint().equals("media-server")){
                Set<VirtualDeploymentUnit> virtualDeploymentUnits = new HashSet<>();
                for (VirtualDeploymentUnit vdu : vnfd.getVdu()){
                    Set<String> images = new HashSet<>();
                    images.add(imageName);
                    vdu.setVm_image(images);
                    virtualDeploymentUnits.add(vdu);
                }
                vnfd.setVdu(virtualDeploymentUnits);
                Set<VNFDeploymentFlavour> flavours = new HashSet<>();
                VNFDeploymentFlavour newFlavour = new VNFDeploymentFlavour();
                newFlavour.setFlavour_key(flavour);
                flavours.add(newFlavour);
                vnfd.setDeployment_flavour(flavours);
                vnfds.add(vnfd);
            }
        }

        networkServiceDescriptor.setVnfd(vnfds);
        return networkServiceDescriptor;
    }
    private NetworkServiceDescriptor enableAutoscaling(int scaleInOut, double scale_out_threshold, NetworkServiceDescriptor nsd) {

        AutoScalePolicy scaleOutPolicy = new AutoScalePolicy();
        scaleOutPolicy.setName("scale-out");
        scaleOutPolicy.setComparisonOperator(">=");
        scaleOutPolicy.setPeriod(30);
        scaleOutPolicy.setCooldown(60);
        scaleOutPolicy.setThreshold(100);
        scaleOutPolicy.setMode(ScalingMode.REACTIVE);
        scaleOutPolicy.setType(ScalingType.VOTED);
        ScalingAlarm scaleOutAlarm = new ScalingAlarm();
        scaleOutAlarm.setComparisonOperator(">=");
        scaleOutAlarm.setStatistic("avg");
        scaleOutAlarm.setMetric("CONSUMED_CAPACITY");

        if (scale_out_threshold > 0){
            scaleOutAlarm.setThreshold(scale_out_threshold);
        }
        else{
            scaleOutAlarm.setThreshold(140);
        }
        scaleOutAlarm.setWeight(1);
        Set<ScalingAlarm> scaleOutalarms = new HashSet<>();
        scaleOutalarms.add(scaleOutAlarm);
        Set<ScalingAction> scaleOutActions = new HashSet<>();
        ScalingAction scaleOutAction = new ScalingAction();
        scaleOutAction.setType(ScalingActionType.SCALE_OUT);
        scaleOutAction.setValue("1");
        scaleOutActions.add(scaleOutAction);
        scaleOutPolicy.setActions(scaleOutActions);
        scaleOutPolicy.setAlarms(scaleOutalarms);


        AutoScalePolicy scaleInPolicy = new AutoScalePolicy();
        scaleInPolicy.setName("scale-in");
        scaleInPolicy.setComparisonOperator(">=");
        scaleInPolicy.setPeriod(30);
        scaleInPolicy.setCooldown(60);
        scaleInPolicy.setThreshold(100);
        scaleInPolicy.setMode(ScalingMode.REACTIVE);
        scaleInPolicy.setType(ScalingType.VOTED);
        ScalingAlarm scaleInAlarm = new ScalingAlarm();
        scaleInAlarm.setComparisonOperator("<=");
        scaleInAlarm.setStatistic("avg");
        scaleInAlarm.setMetric("CONSUMED_CAPACITY");
        scaleInAlarm.setThreshold(60);
        scaleInAlarm.setWeight(1);
        Set<ScalingAlarm> scaleInalarms = new HashSet<>();
        scaleInalarms.add(scaleInAlarm);
        Set<ScalingAction> scaleInActions = new HashSet<>();
        ScalingAction scaleInAction = new ScalingAction();
        scaleInAction.setType(ScalingActionType.SCALE_IN);
        scaleInAction.setValue("1");
        scaleInActions.add(scaleInAction);
        scaleInPolicy.setAlarms(scaleInalarms);
        scaleInPolicy.setActions(scaleInActions);

        Set<AutoScalePolicy> autoscaling = new HashSet<>();
        autoscaling.add(scaleOutPolicy);
        autoscaling.add(scaleInPolicy);

        Set<VirtualNetworkFunctionDescriptor> virtualNetworkFunctionDescriptors = new HashSet<>();
        for (VirtualNetworkFunctionDescriptor vnfd : nsd.getVnfd()){
            if(vnfd.getEndpoint().equals("media-server")){
                vnfd.setAuto_scale_policy(autoscaling);
                Set<VirtualDeploymentUnit> vdus = new HashSet<>();
                for (VirtualDeploymentUnit vdu : vnfd.getVdu()){
                    vdu.setScale_in_out(scaleInOut);
                    vdus.add(vdu);
                }
                vnfd.setVdu(vdus);
            }
            virtualNetworkFunctionDescriptors.add(vnfd);
        }
        nsd.setVnfd(virtualNetworkFunctionDescriptors);

        return nsd;
    }


}
