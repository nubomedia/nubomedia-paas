angular.module('app')
    .factory('http', function ($http, $q, $cookieStore) {

        var http = {};
        var URL = 'http://emm-dev.nubomedia.eu:8090';
        var customHeaders = {};

        if ($cookieStore.get('token') === '')
            customHeaders = {
                'Accept': 'application/json',
                'Content-type': 'application/json'
            };
        else {

            customHeaders = {
                'Accept': 'application/json',
                'Content-type': 'application/json',
                'Auth-token': $cookieStore.get('token')
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

        http.post = function (url, data) {
            $('#modalSend').modal('show');
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
