angular.module('app').controller('applicationsCtrl', function ($scope, http, $routeParams, serviceAPI, $window, $cookieStore, $http, $sce) {

        var url = $cookieStore.get('URLNb') + '/api/v1/nubomedia/paas/app/';
        var urlPK = $cookieStore.get('URLNb') + '/api/v1/nubomedia/paas/';
        var urlMediaManager = 'http://80.96.122.73:9000/vnfr/';

        if (angular.isUndefined($cookieStore.get('server-ip'))) {
            http.get($cookieStore.get('URLNb') + '/api/v1/nubomedia/paas/server-ip/')
                .success(function (data) {
                    var serverIpString = data.toString();
                    var ip = serverIpString.substring(0, serverIpString.length - 6);
                    console.log(ip);
                    urlMediaManager = ip + ':9000/vnfr/';
                    $cookieStore.put('server-ip', ip);
                });
        }


        $scope.alerts = [];
        $scope.apllications = [];
        $scope.flavors = ["MEDIUM", "LARGE"];
        $scope._qualityOfService = ["BRONZE", "SILVER", "GOLD"];
        $scope._turnServer = {
            'turnServerUrl': '',
            'turnServerUsername': '',
            'turnServerPassword': ''
        };
        $scope._stunServer = {
            'stunServerIp': '',
            'stunServerPort': ''
        };
        $scope.qosValue = {_qos: ''};
        $scope._threshold = {
            'scaleInOut': 0,
            'scale_out_threshold': 0
        };
        $scope.createApp = function () {
            $http.get('json/request.json')
                .then(function (res) {
                    console.log(res.data);
                    $scope.appCreate = angular.copy(res.data);
                });
            $('#modalT').modal('show');
        };


        $http.get('json/infos.json')
            .then(function (res) {
                console.log(res.data);
                $scope.infosObj = angular.copy(res.data);
            });

        $scope.getInfos = function (key) {
            console.log($scope.infosObj[key]);
            console.log(key);
            $scope.textInfo = $scope.infosObj[key];
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
            turnServer: false,
            threshold: false,
            cloudRepository: false,
            qualityOfService: false
        };

        $scope.sendApp = function () {
            var postTopology;
            var sendOk = true;


            if ($scope.appCreate.stunServerActivate) {
                if ($scope._stunServer.stunServerIp !== '')
                    $scope.appCreate.stunServerIp = $scope._stunServer.stunServerIp;
                if ($scope._stunServer.stunServerPort !== '')
                    $scope.appCreate.stunServerPort = $scope._stunServer.stunServerPort;

            }
            if ($scope.appCreate.turnServerActivate) {
                if ($scope._turnServer.turnServerUrl !== '')
                    $scope.appCreate.turnServerUrl = $scope._turnServer.turnServerUrl;
                if ($scope._turnServer.turnServerUsername !== '')
                    $scope.appCreate.turnServerUsername = $scope._turnServer.turnServerUsername;
                if ($scope._turnServer.turnServerPassword !== '')
                    $scope.appCreate.turnServerPassword = $scope._turnServer.turnServerPassword;
            }
            if ($scope.toggle.threshold) {
                $scope.appCreate.scaleInOut = $scope._threshold.scaleInOut;
                $scope.appCreate.scale_out_threshold = $scope._threshold.scale_out_threshold;
            }


            if ($scope.toggle.qualityOfService) {
                $scope.appCreate.qualityOfService = $scope.qosValue._qos;

            }
            if ($scope.appCreate.secretName === "")
                delete $scope.appCreate.secretName;


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

        $scope.loadAppLog = function (podName) {
            console.log(podName);
            http.get(url + $routeParams.applicationId + '/logs/' + podName)
                .success(function (response) {

                    $scope.log = $sce.trustAsHtml(n2br(response));
                })
                .error(function (data, status) {
                    showError(status, data);
                });
        };

        $scope.checkStatus = function () {
            console.log(($scope.application.status !== 'RUNNING'));
            if ($scope.application.status !== 'RUNNING')
                return true;
            else return false;
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
            str = str.replace(/(?:\\[rn]|[\r\n]+)+/g, "<br />");
            //return str.replace(/\r\n|\r|\n//g, "<br />");
            var x = str;
            var r = /\\u([\d\w]{4})/gi;
            x = x.replace(r, function (match, grp) {
                return String.fromCharCode(parseInt(grp, 16));
            });
            x = unescape(x);
            return x
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

        $scope.disableButton = false;
        $scope.classVar = 'panel-default';
        $scope.changedTurnServerBool = function () {
            $scope.disableButton = false;
            $scope.classVar = 'panel-default';
        };
        $scope.checkTurn = function () {
            console.log($scope._turnServer);
            checkTURNServer({
                url: 'turn:' + $scope._turnServer.turnServerUrl,
                username: $scope._turnServer.turnServerUsername,
                credential: $scope._turnServer.turnServerPassword
            }).then(function (bool) {
                console.log('is TURN server active? ', bool ? 'yes' : 'no');
                if (bool) {
                    disableButton(false);
                    changeDivPane('panel-success');
                }
                else {
                    disableButton(true);
                    changeDivPane('panel-danger');
                }


            }).catch(function (reason) {
                console.error.bind(console);
                console.log(reason);
                disableButton(true);
            });


        };
        function disableButton(value) {
            if (value)
                if ($scope._turnServer.turnServerUsername !== '' || $scope._turnServer.turnServerPassword !== ''
                    || $scope._turnServer.turnServerUrl !== '')
                    $scope.$apply(function () {
                        $scope.disableButton = value;
                    });
        }

        function changeDivPane(classValue) {
            $scope.$apply(function () {
                $scope.classVar = classValue;
            });

        }

        function checkTURNServer(turnConfig, timeout) {

            return new Promise(function (resolve, reject) {

                setTimeout(function () {
                    if (promiseResolved) return;
                    resolve(false);
                    promiseResolved = true;
                }, timeout || 5000);

                var promiseResolved = false
                    , myPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection   //compatibility for firefox and chrome
                    , pc = new myPeerConnection({iceServers: [turnConfig]})
                    , noop = function () {
                };
                pc.createDataChannel("");    //create a bogus data channel
                pc.createOffer(function (sdp) {
                    if (sdp.sdp.indexOf('typ relay') > -1) { // sometimes sdp contains the ice candidates...
                        promiseResolved = true;
                        resolve(true);
                    }
                    pc.setLocalDescription(sdp, noop, noop);
                }, noop);    // create offer and set local description
                pc.onicecandidate = function (ice) {  //listen for candidate events
                    if (promiseResolved || !ice || !ice.candidate || !ice.candidate.candidate || !(ice.candidate.candidate.indexOf('typ relay') > -1))  return;
                    promiseResolved = true;
                    resolve(true);
                };
            });
        }


        $scope.showPlot = function () {
            if ($('#numberFlot').find('div.vis-timeline').length == 0) {
                drawGraph('numberFlot');
                drawGraph('capacityFlot');
                loadMediaManeger()
            }
        };

        $scope.vnfrId;
        $scope.numberValue = 1;
        $scope.loadValue = 0;
        function loadMediaManeger() {
            http.get(urlMediaManager)
                .success(function (data) {
                    //console.log(data);
                    $scope.vnfrs = data;
                    console.log($scope.vnfrs);
                    angular.forEach($scope.vnfrs, function (vnfr, index) {
                        if ($scope.application.nsrID === vnfr.nsrId) {
                            console.log(vnfr);
                            $scope.vnfrId = vnfr.vnfrId;

                        }
                    });

                });
        }


        function getValue(type) {
            console.log(urlMediaManager + $scope.vnfrId + '/media-server/' + type);
            if (!angular.isUndefined($scope.vnfrId))
                http.get(urlMediaManager + $scope.vnfrId + '/media-server/' + type)
                    .success(function (data) {
                        console.log(data);
                        if (type === 'number')
                            $scope.numberValue = data;
                        else
                            $scope.loadValue = data;

                    });
            if (type === 'number')
                return $scope.numberValue;
            else {
                return $scope.loadValue;
            }
        }


        function drawGraph(id) {
            var DELAY = 10000; // delay in ms to add new data points


            // create a graph2d with an (currently empty) dataset
            var container = document.getElementById(id);

            var dataset = new vis.DataSet();

            var options = {
                start: vis.moment().add(-30, 'seconds'), // changed so its faster
                end: vis.moment(),
                dataAxis: {
                    left: {
                        range: {
                            min: 0, max: 10
                        }
                    }
                },

                drawPoints: {
                    style: 'circle', // square, circle
                    size: 10
                },
                shaded: {
                    orientation: 'bottom' // top, bottom
                }

            };


            if (id === 'numberFlot') {
                delete options.shaded;
            }
            if (id === 'capacityFlot') {
                options.dataAxis.left.range.max = 100;
            }

            var graph2d = new vis.Graph2d(container, dataset, options);

            function y(x) {
                if (id === 'capacityFlot') {
                    var value = getValue('load');
                    //value = (Math.floor((Math.random() * 1000) + 1));
                    if (value > options.dataAxis.left.range.max) {
                        options.dataAxis.left.range.max = value;
                        console.log(value);
                        graph2d.setOptions(options);
                    }
                    return value;

                }
                else {
                    return getValue('number');
                }
                //return (Math.floor((Math.random() * 6) + 1));
            }

            function renderStep() {
                // move the window (you can think of different strategies).
                var now = vis.moment();
                var range = graph2d.getWindow();
                var interval = range.end - range.start;
                graph2d.setWindow(now - interval, now, {animation: false});
                setTimeout(renderStep, DELAY);

            }

            renderStep();

            /**
             * Add a new datapoint to the graph
             */
            function addDataPoint() {
                // add a new data point to the dataset
                var now = vis.moment();
                dataset.add({
                    x: now,
                    y: y(now / 1000)
                });

                // remove all data points which are no longer visible
                var range = graph2d.getWindow();
                var interval = range.end - range.start;
                var oldIds = dataset.getIds({
                    filter: function (item) {
                        return item.x < range.start - interval;
                    }
                });
                dataset.remove(oldIds);

                setTimeout(addDataPoint, DELAY);
            }

            addDataPoint();
        }

//# example usage:

        /*  checkTURNServer({
         url: 'turn:127.0.0.1',
         username: 'test',
         credential: 'test'
         }).then(function(bool){
         console.log('is TURN server active? ', bool? 'yes':'no');
         }).catch(console.error.bind(console));*/


    }
);

