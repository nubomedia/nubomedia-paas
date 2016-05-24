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

angular.module('app')
    .factory('http', function ($http, $q, $cookieStore) {

        var http = {};
        var URL = 'http://emm-dev.nubomedia.eu:8090';
        var customHeaders = {};

        if ($cookieStore.get('tokenNb') === '')
            customHeaders = {
                'Accept': 'application/json',
                'Content-type': 'application/json'
            };
        else {

            customHeaders = {
                'Accept': 'application/json',
                'Content-type': 'application/json',
                'Auth-token': $cookieStore.get('tokenNb')
            };
        }


        http.get = function (url) {
            return $http({
                url: url,
                method: 'GET',
                headers: customHeaders
            })
        };

        http.getOrchestratorURL = function () {
            if (!angular.isUndefined($cookieStore.get('orchestratorURL')))
                return $cookieStore.get('orchestratorURL');
            else
                return URL;
        };

        http.setOrchestratorURL = function (orcURL) {
            $cookieStore.put('orchestratorURL', orcURL);
            URL = orcURL;
        };

        http.post = function (url, data, type) {
            //if (!angular.isUndefined(type)) {
            //
            //    customHeaders['Content-type'] = 'text/plain';
            //    customHeaders['Accept'] = 'text/plain';
            //} else {
            //    customHeaders['Accept'] = 'application/json';
            //    customHeaders['Content-type'] = 'application/json';
            //}
            $('#modalSend').modal('show');
            console.log(customHeaders);
            console.log(data);
            return $http({
                url: url,
                method: 'POST',
                data: data,
                headers: customHeaders
            });
        };
        http.postXML = function (url, data) {
            $('#modalSend').modal('show');
            return $http({
                url: url,
                dataType: 'xml',
                method: 'POST',
                data: data,
                headers: {
                    "Content-Type": "application/xml"
                }

            });
        };
        http.put = function (url, data) {
            $('#modalSend').modal('show');
            return $http({
                url: url,
                method: 'PUT',
                data: data,
                headers: customHeaders
            });
        };

        http.delete = function (url) {
            $('#modalSend').modal('show');
            return $http({
                url: url,
                method: 'DELETE',
                headers: customHeaders
            });
        };

        http.syncGet = function (url) {
            var deferred = $q.defer();
            http.get(url).success(function (data, status) {
                deferred.resolve(data);
            });
            return deferred.promise;
        };


        return http;
    });
