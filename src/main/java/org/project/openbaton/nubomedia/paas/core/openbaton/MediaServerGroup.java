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

package org.project.openbaton.nubomedia.paas.core.openbaton;

/**
 * Created by maa on 21.10.15.
 */
public class MediaServerGroup {

    private String mediaServerGroupID;
    private String eventAllocatedID;
    private String eventErrorID;
    private String nsdID;
    private String token;

    public MediaServerGroup() {
    }

    public String getNsdID() {
        return nsdID;
    }

    public void setNsdID(String nsdID) {
        this.nsdID = nsdID;
    }

    public String getMediaServerGroupID() {
        return mediaServerGroupID;
    }

    public void setMediaServerGroupID(String mediaServerGroupID) {
        this.mediaServerGroupID = mediaServerGroupID;
    }

    public String getEventAllocatedID() {
        return eventAllocatedID;
    }

    public void setEventAllocatedID(String eventAllocatedID) {
        this.eventAllocatedID = eventAllocatedID;
    }

    public String getEventErrorID() {
        return eventErrorID;
    }

    public void setEventErrorID(String eventErrorID) {
        this.eventErrorID = eventErrorID;
    }

    public String getToken(){
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "OpenbatonCreateServer{" +
                "mediaServerGroupID='" + mediaServerGroupID + '\'' +
                ", eventAllocatedID='" + eventAllocatedID + '\'' +
                ", eventErrorID='" + eventErrorID + '\'' +
                ", nsdID='" + nsdID + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
