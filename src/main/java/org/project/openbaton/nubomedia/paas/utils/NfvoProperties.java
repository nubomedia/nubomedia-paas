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

package org.project.openbaton.nubomedia.paas.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

/**
 * Created by maa on 22.01.16.
 */
@Service
@ConfigurationProperties(prefix="nfvo")
public class NfvoProperties {

    private String openbatonIP;
    private String openbatonPort;
    private String openbatonUsername;
    private String openbatonPasswd;
    private String imageName;

    public String getOpenbatonIP() {
        return openbatonIP;
    }

    public void setOpenbatonIP(String openbatonIP) {
        this.openbatonIP = openbatonIP;
    }

    public String getOpenbatonPort() {
        return openbatonPort;
    }

    public void setOpenbatonPort(String openbatonPort) {
        this.openbatonPort = openbatonPort;
    }

    public String getOpenbatonUsername() {
        return openbatonUsername;
    }

    public void setOpenbatonUsername(String openbatonUsername) {
        this.openbatonUsername = openbatonUsername;
    }

    public String getOpenbatonPasswd() {
        return openbatonPasswd;
    }

    public void setOpenbatonPasswd(String openbatonPasswd) {
        this.openbatonPasswd = openbatonPasswd;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
