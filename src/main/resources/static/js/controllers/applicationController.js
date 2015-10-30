angular.module('app').controller('applicationsCtrl', function ($scope, http, $routeParams, serviceAPI, $window, $cookieStore,$http,$sce) {

        var url = $cookieStore.get('URLNb') + '/api/v1/nubomedia/paas/app/';

        $scope.alerts = [];
        $scope.apllications=[];
        $scope.createApp = function () {
            $http.get('json/request.json')
                .then(function (res) {
                    console.log(res.data);
                    $scope.appCreate = angular.copy(res.data);
                });
            $('#modalT').modal('show');
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




        $scope.sendApp = function () {
            var postTopology;
            var sendOk = true;

            $scope.appCreate.ports = $scope.appCreate.ports.split(",");
            $scope.appCreate.targetPorts = $scope.appCreate.targetPorts.split(",");
            $scope.appCreate.protocols = $scope.appCreate.protocols.split(",");
            console.log($scope.appCreate);

            if ($scope.file !== '') {
                postTopology = $scope.file;
            }
            else if ($scope.textTopologyJson !== '')
                postTopology = $scope.textTopologyJson;
            else if(angular.isUndefined(postTopology))
                postTopology = $scope.appCreate;
            else {
                alert('Problem with Topology');
                sendOk = false;
            }
            console.log(postTopology);

            if (sendOk) {
                console.log(postTopology);
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

        $scope.loadLog = function(){
            http.get(url + $routeParams.applicationId+'/buildlogs')
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

