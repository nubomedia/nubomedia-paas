angular.module('app').controller('applicationsCtrl', function ($scope, http, $routeParams, serviceAPI, $window, $cookieStore, $http, $sce) {

        var url = $cookieStore.get('URLNb') + '/api/v1/nubomedia/paas/app/';
        var urlPK = $cookieStore.get('URLNb') + '/api/v1/nubomedia/paas/';

        $scope.alerts = [];
        $scope.apllications = [];
        $scope.flavors = ["SMALL", "MEDIUM", "LARGE"];
        $scope._qualityOfService = ["BRONZE", "SILVER", "GOLD"];
        $scope._turnServer ={
            'turnServerIp':'',
            'turnServerUser':'',
            'turnServerPassword':''
        };
        $scope.qosValue={_qos:''};
        $scope._threshold = {
          'scale_in_threshold':0,
          'scale_out_threshold':0
        };
        $scope.createApp = function () {
            $http.get('json/request.json')
                .then(function (res) {
                    console.log(res.data);
                    $scope.appCreate = angular.copy(res.data);
                });
            $('#modalT').modal('show');
        };

        $scope.privateKeyReq = {
            projectName: 'nubomedia',
            privateKey: ''

        };

        $scope.sendPK = function (privateKeyReq) {

            console.log(urlPK + 'secret');
            http.post(urlPK + 'secret', privateKeyReq, 'text')
                .success(function (data) {
                    console.log(data);
                    $scope.appCreate.secretName = data.slice(1, -1);
                    $('#modalSend').modal('hide');
                    $('#modalPrivateKey').modal('hide');

                })
                .error(function (data, status) {
                    showError(status, data);
                });
        };
        if (!angular.isUndefined($routeParams.applicationId)) {
            http.get(url + $routeParams.applicationId)
                .success(function (data) {
                    console.log(data);
                    $scope.application = data;
                    $scope.applicationJSON = JSON.stringify(data, undefined, 4);
                });
        }
        else {
            loadTable();
        }

        function loadTable() {
            http.get(url)
                .success(function (response, status) {
                    $scope.apllications = response;
                    console.log(response);
                }).error(function (data, status) {
                    showError(status, data);
                });
        }

        $scope.closeAlert = function (index) {
            $scope.alerts.splice(index, 1);
        };

        $scope.toggle = {
            turnServer : false,
            threshold : false,
            cloudRepository : false,
            qualityOfService : false
        };

        $scope.sendApp = function () {
            var postTopology;
            var sendOk = true;


            if($scope.toggle.turnServer){
                $scope.appCreate.turnServerIp = $scope._turnServer.turnServerIp;
                $scope.appCreate.turnServerUser= $scope._turnServer.turnServerUser;
                $scope.appCreate.turnServerPassword= $scope._turnServer.turnServerPassword;

            }
            if($scope.toggle.threshold){
                $scope.appCreate.scale_in_threshold= $scope._threshold.scale_in_threshold;
                $scope.appCreate.scale_out_threshold= $scope._threshold.scale_out_threshold;
            }
            $scope.cloudRepository = {
                cloudRepoPort:'27018',
                cloudRepoSecurity:true
            };
            if($scope.appCreate.cloudRepository){
                $scope.appCreate.cloudRepoPort= $scope.cloudRepository.cloudRepoPort;
                $scope.appCreate.cloudRepoSecurity= $scope.cloudRepository.cloudRepoSecurity;
            }
            if($scope.toggle.qualityOfService){
                $scope.appCreate.qualityOfService= $scope.qosValue._qos;

            }


            console.log(JSON.stringify($scope.appCreate));

            if ($scope.file !== '') {
                postTopology = $scope.file;
            }
            else if ($scope.textTopologyJson !== '')
                postTopology = $scope.textTopologyJson;
            else if (angular.isUndefined(postTopology))
                postTopology = $scope.appCreate;
            else {
                alert('Problem with Topology');
                sendOk = false;
            }
            console.log(postTopology);

            if (sendOk) {
                console.log(JSON.stringify(postTopology));
                http.post(url, $scope.appCreate)
                    .success(function (response) {
                        showOk('App created!');
                        loadTable();
                        $scope.file = '';
                        $scope.textTopologyJson = '';
                    })
                    .error(function (data, status) {
                        showError(status, data);
                    });
            }

        };

        $scope.addPort = function () {
            $scope.appCreate.ports.push({
                "port": 8080,
                "targetPort": 8080,
                "protocol": "TCP"
            });
        };

        $scope.deletePort = function (index) {
            $scope.appCreate.ports.splice(index, 1);
        };
        $scope.deleteData = function (id) {
            http.delete(url + id)
                .success(function (response) {
                    showOk('Deleted App with id: ' + id + ' done.');
                    loadTable();
                })
                .error(function (data, status) {
                    showError(status, data);
                });
        };

        $scope.changeText = function (text) {
            $scope.textTopologyJson = text;
            console.log($scope.textTopologyJson);
        };

        $scope.loadLog = function () {
            http.get(url + $routeParams.applicationId + '/buildlogs')
                .success(function (response) {
                    //$scope.log = response;
                    $scope.log = $sce.trustAsHtml(n2br(response.log));
                })
                .error(function (data, status) {
                    showError(status, data);
                });
        };

        $scope.setFile = function (element) {
            $scope.$apply(function ($scope) {

                var f = element.files[0];
                if (f) {
                    var r = new FileReader();
                    r.onload = function (element) {
                        var contents = element.target.result;
                        $scope.file = contents;
                    };
                    r.readAsText(f);
                } else {
                    alert("Failed to load file");
                }
            });
        };

        function n2br(str) {
            return str.replace(/\n/g, "<br />");
        }

        function showError(status, data) {
            $scope.alerts.push({
                type: 'danger',
                msg: 'ERROR: <strong>HTTP status</strong>: ' + status + ' response <strong>data</strong>: ' + JSON.stringify(data)
            });

            $('.modal').modal('hide');
        }

        function showOk(msg) {
            $scope.alerts.push({type: 'success', msg: msg});
            $('.modal').modal('hide');
        }

    }
);

