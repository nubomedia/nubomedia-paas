/*
 *
 *  * (C) Copyright 2016 NUBOMEDIA (http://www.nubomedia.eu)
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *   http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *  *
 *
 */

angular.module('app').controller('applicationsCtrl', function($scope, http, $routeParams, serviceAPI, $window, $cookieStore, $http, $sce, $timeout, $location, $rootScope, $q) {

  var ip = $cookieStore.get('URLNb');
  var url = ip + '/api/v1/nubomedia/paas/app/';
  var urlPK = ip + '/api/v1/nubomedia/paas/';
  var urlMediaManager = '';
  var maxLoad = 100;
  var marketurl = $cookieStore.get('marketplaceIP');
  $scope.file = '';
  $scope.appJson = '';
  $scope.appNewService = {};
  $scope.mediaServeHostName = '';
  $scope.actions = {};
  $scope.alerts = [];
  $scope.applications = [];
  $rootScope.mediaServers = [];
  $rootScope.bigDataMediaServer = [];
  $scope.vnfrId = '';
  $scope.numberValue = 1;
  $scope.loadValue = 0;
  $scope.disableButton = false;
  $scope.classVar = 'panel-default';
  $scope.input = {
    numberRows: 35
  };
  $scope.main = {
    checkbox: false
  };
  $scope.multipleDelete = true;
  $scope.selection = {};
  $scope.selection.ids = {};
  $scope.flavors = ["SMALL", "MEDIUM", "LARGE"];
  $scope._qualityOfService = ["BRONZE", "SILVER", "GOLD"];
  $scope.privateKeyReq = {
    projectName: 'nubomedia',
    privateKey: ''
  };
  $scope._turnServer = {
    'turnServerUrl': '',
    'turnServerUsername': '',
    'turnServerPassword': ''
  };
  $scope._stunServer = {
    'stunServerIp': '',
    'stunServerPort': ''
  };
  $scope.qosValue = {
    _qos: ''
  };
  $scope._threshold = {
    'scaleInOut': 0,
    'scale_out_threshold': 0
  };
  $scope.toggle = {
    turnServer: false,
    threshold: false,
    cloudRepository: false,
    qualityOfService: false
  };

  String.prototype.replaceAll = function(target, replacement) {
    return this.split(target).join(replacement);
  };

  //var marketurl = 'http://localhost:8082/api/v1/app/';
  //console.log('$cookieStore.get(\'URLNb\') ==  '+$cookieStore.get('URLNb') );
  //console.log('$cookieStore.get(\'server-ip\') ==  '+$cookieStore.get('server-ip') );



  // Public methods
  // -------------------------------------------------------------------------
  $scope.dropdownTextChange = dropdownTextChange;

  // Media Servers
  $scope.selectMediaServer = selectMediaServer;
  $scope.toggleMediaServer = toggleMediaServer;
  $scope.showMediaServeerLogs = showMediaServeerLogs;
  $scope.showMediaServeerLogsURL = showMediaServeerLogsURL;

  // Modals
  $scope.deleteAppModal = deleteAppModal;
  $scope.deleteMarketAppModal = deleteMarketAppModal;
  $scope.createApp = createApp;

  // Services
  $scope.addNewService = addNewService;
  $scope.removeNewService = removeNewService;
  $scope.addServicePort = addServicePort;
  $scope.deleteServicePort = deleteServicePort;
  $scope.addServiceEnvVar = addServiceEnvVar;
  $scope.deleteServiceEnvVar = deleteServiceEnvVar;

  $scope.resize = resize;
  $scope.createMarketApp = createMarketApp;
  $scope.saveApp = saveApp;
  $scope.changeCdn = changeCdn;

  $scope.drawColumnMediaServer = drawColumnMediaServer;
  $scope.updateGraphMediaServer = updateGraphMediaServer;
  $scope.getInfos = getInfos;
  $scope.sendPK = sendPK;

  $scope.clearAlerts = clearAlerts;
  $scope.closeAlert = closeAlert;
  $scope.launch = launch;
  $scope.sendApp = sendApp;
  $scope.addPort = addPort;
  $scope.deletePort = deletePort;
  $scope.deleteData = deleteData;
  $scope.deleteAppMarket = deleteAppMarket;
  $scope.deleteAllApp = deleteAllApp;
  $scope.changeText = changeText;
  $scope.loadLog = loadLog;
  $scope.loadAppLog = loadAppLog;
  $scope.checkStatus = checkStatus;
  $scope.setFile = setFile;
  $scope.multipleDeleteReq = multipleDeleteReq;

  $scope.changedTurnServerBool = changedTurnServerBool;
  $scope.checkTurn = checkTurn;
  $scope.showPlot = showPlot;



  // Invoke private methods
  // -------------------------------------------------------------------------

  if (angular.isUndefined($cookieStore.get('server-ip'))) {
    http.get($cookieStore.get('URLNb') + '/api/v1/nubomedia/paas/server-ip/')
      .success(function(data) {
        var serverIpString = data.replaceAll("\"", "");
        urlMediaManager = 'http://' + serverIpString + ':9000/vnfr/';
        $cookieStore.put('server-ip', serverIpString);
      });
  } else {
    urlMediaManager = 'http://' + $cookieStore.get('server-ip') + ':9000/vnfr/';
  }

  $http.get('json/infos.json')
    .then(function(res) {
      $scope.infosObj = angular.copy(res.data);
    });

  if (!angular.isUndefined($routeParams.appId)) {
    http.get(marketurl + $routeParams.appId)
      .success(function(data) {
        console.log('jsonApp appId: ', data);
        $scope.application = data;
        $scope.applicationJSON = JSON.stringify(data, undefined, 4);
        mergeMediaServer(data.mediaServerGroup);
        $rootScope.myMediaServer = $rootScope.mediaServers[0]; // first floatingIps
        getDataFromMediaServer($rootScope.myMediaServer.hostname);
        renderGraphMediaServer();
      });
  }

  //  GET APPLICATION INFO
  if (!angular.isUndefined($routeParams.applicationId)) {
    http.get(url + $routeParams.applicationId)
      .success(function(data) {
        $scope.application = data;
        $scope.applicationJSON = JSON.stringify(data, undefined, 4);
        loadMediaManeger();
        getAllMediaServers();
        $scope.mediaServerProgress = function() {
          var value = $scope.application.mediaServerGroup.hosts.length * 100 / $scope.application.scaleOutLimit;

          if (value % 1 !== 0) {
            value = (value).toFixed(2);
          }

          return value + '%';
        };

        if (!$rootScope.googleChartisLoaded) {
          google.charts.load('current', {
            'packages': ['corechart']
          });
          $rootScope.googleChartisLoaded = true;
        }

        mergeMediaServer(data.mediaServerGroup);
        $rootScope.myMediaServer = $rootScope.mediaServers[0]; // first floatingIps
        getDataFromMediaServer($rootScope.myMediaServer.hostname);
      });
  } else {
    loadTable();
  }



  // !!Public methods declaration
  // -------------------------------------------------------------------------
  function dropdownTextChange(text) {
    $scope.dropdownText = text;
  };


  // !!Start/Stop media server
  // Media servers
  // -------------------------------------
  function selectMediaServer(hostname) {
    var url = ip + '/api/v2/nubomedia/paas/app/' + $routeParams.applicationId + '/media-server/' + hostname + '/';
    http.get(url)
      .success(function(response, status) {
        if (response.status === 'INACTIVE') {
          $scope.actions.start = true;
          $scope.actions.stop = !$scope.actions.start;
        } else if (response.status === 'ACTIVE') {
          $scope.actions.start = false;
          $scope.actions.stop = !$scope.actions.start;
        }
      }).error(function(data, status) {
        console.log(data);
      });
  };

  function toggleMediaServer(state, hostname) {
    var url = ip + '/api/v2/nubomedia/paas/app/' + $routeParams.applicationId + '/media-server/' + hostname + '/';
    switch (state) {
      case 'start':
        url = url + 'start';
        http.put(url)
          .success(function(response, status) {
            showOk(hostname + ' media server started!');
            setTimeout(function() {
              getAllMediaServers();
            }, 6000)
            $scope.actions.start = false;
            $scope.actions.stop = !$scope.actions.start;
          }).error(function(data, status) {
            showError(status, data);
          });
        break;

      case 'stop':
        url = url + 'stop';
        http.put(url)
          .success(function(response, status) {
            showOk(hostname + ' media server stoped!');
            setTimeout(function() {
              getAllMediaServers();
            }, 6000);
            $scope.actions.start = true;
            $scope.actions.stop = !$scope.actions.start;
          }).error(function(data, status) {
            showError(status, data);
          });
        break;

      case 'delete':
        http.delete(url)
          .success(function(response, status) {
            showOk(hostname + ' media server stoped!');
            setTimeout(function() {
              getAllMediaServers();
            }, 6000);
          }).error(function(data, status) {
            showError(status, data);
          });
        break;

      case 'scale':
        url = ip + '/api/v2/nubomedia/paas/app/' + $routeParams.applicationId + '/media-server/';
        http.post(url)
          .success(function(response, status) {
            showOk('Added scale out limit');
          }).error(function(data, status) {
            showError(status, data);
          });
        break;
    }
  };

  function showMediaServeerLogs(mediaServer) {
    var url = "http://80.96.122.69:5601/#/discover?_g=(time:(from:now-30d,mode:quick,to:now))&_a=(columns:!(_source),filters:!(!n,(meta:(index:'logstash-*',negate:!f),query:(match:(host:(query:" + mediaServer.hostname + ",type:phrase))))),index:'logstash-*',interval:auto,query:(query_string:(analyze_wildcard:!t,query:'*')),sort:!('@timestamp',desc))";
    return url;
  }

  function showMediaServeerLogsURL(mediaServer) {
    var index = 0;
    var url = 'http://80.96.122.69:9200/logstash-*/_search?q=host:' + mediaServer.hostname + '&size=100&pretty=true';
    var logs;

    http.get(url)
      .then(function(res) {
        logs = '';
        for (index; index < res.data.hits.hits.length; index++) {
          logs += (res.data.hits.hits[index]._source.message) + '\n';
        }
        $scope.log = $sce.trustAsHtml(n2br(logs));
      });
  }

  // !!Modals
  // -------------------------------------
  function deleteAppModal(data) {
    $scope.application = data;
    $('#modalDeleteApplication').modal('show');
  };

  function deleteMarketAppModal(data) {
    $scope.application = data;
    $('#modalDeleteMarketApplication').modal('show');
  };

  /*
   * @name createApp
   * @description
   */
  function createApp() {
    $http.get('json/request.json')
      .then(function(res) {
        $scope.appNewService = angular.copy(res.data.services[0]);
        $scope.appCreate = angular.copy(res.data);
        $scope.appCreate.services = [];
        $scope.appCreate.numberOfInstances = 1;
      });
  };

  // !!Services
  // -------------------------------------
  function addNewService() {
    $scope.appCreate.services.push($scope.appNewService);
  };

  function removeNewService(index) {
    $scope.appCreate.services.splice(index, 1);
  };

  function addServicePort(item) {
    item.push({
      "port": 8080,
      "targetPort": 8080,
      "protocol": "TCP"
    });
  };

  function deleteServicePort(item, index) {
    item.splice(index, 1);
  };

  function addServiceEnvVar(item) {
    item.push({
      "name": "",
      "value": ""
    });
  };

  function deleteServiceEnvVar(item, index) {
    item.splice(index, 1);
  };
  // !!END Services
  // -------------------------------------

  function resize() {
    $timeout(function() {
      $('body').resize();
    }, 500);
  };

  function createMarketApp() {
    $http.get('json/app.json')
      .then(function(res) {
        console.log(res.data);
        $scope.appCreate = angular.copy(res.data);
      });
    $('#modalT').modal('show');
  };

  function saveApp(data) {
    $scope.application = data;
  };

  function changeCdn() {
    $scope.appCreate.cloudRepository = $scope.appCreate.cdnConnector;
  };

  function drawColumnMediaServer(nameCol) {
    var allColumns = [0, 1, 2, 3];
    switch (nameCol) {
      case 'memory':
        $rootScope.viewMediaServerGraph.setColumns([0, 1]);
        $rootScope.chartMediaServerGraph.draw($rootScope.viewMediaServerGraph, $rootScope.optionsMediaServerGraph);
        break;
      case 'elements':
        $rootScope.viewMediaServerGraph.setColumns([0, 2]);
        $rootScope.chartMediaServerGraph.draw($rootScope.viewMediaServerGraph, $rootScope.optionsMediaServerGraph);
        break;
      case 'pipelines':
        $rootScope.viewMediaServerGraph.setColumns([0, 3]);
        $rootScope.chartMediaServerGraph.draw($rootScope.viewMediaServerGraph, $rootScope.optionsMediaServerGraph);
        break;
      case 'none':
        $rootScope.viewMediaServerGraph.setColumns(allColumns);
        $rootScope.chartMediaServerGraph.draw($rootScope.viewMediaServerGraph, $rootScope.optionsMediaServerGraph);
        break;
    }
  };

  function updateGraphMediaServer(urlHostnameMediaServer) {
    getDataFromMediaServer(urlHostnameMediaServer);
  };

  function getInfos(key) {
    console.log($scope.infosObj[key]);
    console.log(key);
    $scope.textInfo = $scope.infosObj[key];
    if (key === 'scale_out_threshold') {
      createCustomPopover();
    }
    return $scope.textInfo;
  };

  function sendPK(privateKeyReq) {
    console.log(urlPK + 'secret');
    http.post(urlPK + 'secret', privateKeyReq, 'text')
      .success(function(data) {
        console.log(data);
        $scope.appCreate.secretName = data.slice(1, -1);
        $('#modalSend').modal('hide');
        $('#modalPrivateKey').modal('hide');

      })
      .error(function(data, status) {
        showError(status, data);
      });
  };

  function clearAlerts() {
    $scope.alerts = [];
  };

  function closeAlert(index) {
    $scope.alerts.splice(index, 1);
  };

  function launch(id) {
    http.syncGet(marketurl + id)
      .then(function(result) {
        console.log(result);
        http.post(url, result)
          .success(function(data, status) {
            console.log(data);
            showOk("App launched correctly.");
          })
          .error(function(data, status) {
            showError(status, data);
          });
      });
  };

  function sendApp(value, location) {
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
      $scope.appCreate.scaleOutLimit = $scope._threshold.scaleInOut;
      $scope.appCreate.scaleOutThreshold = $scope._threshold.scale_out_threshold;
    }


    if ($scope.toggle.qualityOfService) {
      $scope.appCreate.qualityOfService = $scope.qosValue._qos;

    }
    if ($scope.appCreate.secretName === "")
      delete $scope.appCreate.secretName;

    console.log('$scope.appCreate: ', JSON.stringify($scope.appCreate));

    if ($scope.file !== '') {
      postTopology = $scope.file;
      console.log("It's a File!");
    } else if ($scope.appJson !== '') {
      postTopology = $scope.appJson;
      console.log("It's a TextArea!");

    } else if (angular.isUndefined(postTopology)) {
      postTopology = $scope.appCreate;
      console.log("It's a Form!");

    } else {
      alert('Problem with Topology');
      sendOk = false;
    }
    console.log(postTopology);

    if (sendOk) {
      console.log(JSON.stringify(postTopology));

      if (value !== '')
        http.post(marketurl, postTopology)
        .success(function(response) {
          showOk('App Saved!');
          loadTable();
          $scope.file = '';
          $scope.appJson = '';
          $scope.toggleCreateFormView();
        })
        .error(function(data, status) {
          showError(status, data);
        });
      else
        http.post(url, postTopology)
        .success(function(response) {
          showOk('App created!');
          loadTable();
          $scope.file = '';
          $scope.appJson = '';
          $scope.toggleCreateFormView();
        })
        .error(function(data, status) {
          showError(status, data);
        });
    }
  };

  function addPort() {
    $scope.appCreate.ports.push({
      "port": 8080,
      "targetPort": 8080,
      "protocol": "TCP"
    });
  };

  function deletePort(index) {
    $scope.appCreate.ports.splice(index, 1);
  };

  function deleteData(id, location) {
    http.delete(url + id)
      .success(function(response) {
        showOk('Deleted App with id: ' + id + ' done.');
        loadTable();
        if (location) {
          $location.path('/' + location);
        }
      })
      .error(function(data, status) {
        showError(status, data);
      });
  };

  function deleteAppMarket(id, location) {
    http.delete(marketurl + id)
      .success(function(response) {
        showOk('Deleted App with id: ' + id + ' done.');
        loadTable();
        if (location) {
          $location.path('/' + location);
        }
      })
      .error(function(data, status) {
        showError(status, data);
      });
  };

  function deleteAllApp() {
    http.delete(url)
      .success(function(response) {
        showOk('Deleted all Apps in the ' + $rootScope.projectSelected.name + ' project.');
        loadTable();
      })
      .error(function(data, status) {
        showError(status, data);
      });
  };

  function changeText(text) {
    $scope.appJson = text;
    console.log($scope.appJson);
  };

  function loadLog() {
    http.get(url + $routeParams.applicationId + '/buildlogs')
      .success(function(response) {
        //$scope.log = response;
        $scope.log = $sce.trustAsHtml(n2br(response.log));
      })
      .error(function(data, status) {
        showError(status, data);
      });
  };

  function loadAppLog(podName, index) {
    http.get(url + $routeParams.applicationId + '/logs/' + podName)
      .success(function(response) {
        var stringArray = response.split('\\n');
        var subLog = stringArray.slice(stringArray.length - $scope.input.numberRows, -1);
        var string = subLog.join('\\n');
        $scope.log = $sce.trustAsHtml(n2br(string));
      })
      .error(function(data, status) {
        showError(status, data);
      });
  };

  function checkStatus() {
    console.log(($scope.application.status !== 'RUNNING'));
    if ($scope.application.status !== 'RUNNING')
      return true;
    else return false;
  };

  function setFile(element) {
    $scope.$apply(function($scope) {

      var f = element.files[0];
      if (f) {
        var r = new FileReader();
        r.onload = function(element) {
          var contents = element.target.result;
          $scope.file = contents;
        };
        r.readAsText(f);
      } else {
        alert("Failed to load file");
      }
    });
  };

  // !!Delete multiple applications
  // -------------------------------------
  function multipleDeleteReq(url) {
    var urlToUse = '';
    var ids = [];

    if (url === 'market') {
      urlToUse = marketurl;
    } else {
      urlToUse = urlPK;
    }

    angular.forEach($scope.selection.ids, function(value, k) {
      if (value) {
        ids.push(k);
      }
    });

    http.post(urlToUse + 'multipledelete', ids)
      .success(function(response) {
        showOk('Applications: ' + ids.toString() + ' deleted.');
        loadTable();
      })
      .error(function(response, status) {
        showError(response, status);
      });
  };

  $scope.$watch('main', function(newValue, oldValue) {
    //console.log(newValue.checkbox);
    //console.log($scope.selection.ids);
    angular.forEach($scope.selection.ids, function(value, k) {
      $scope.selection.ids[k] = newValue.checkbox;
    });
    //console.log($scope.selection.ids);
  }, true);

  $scope.$watch('selection', function(newValue, oldValue) {
    //console.log(newValue);
    var keepGoing = true;
    angular.forEach($scope.selection.ids, function(value, k) {
      if (keepGoing) {
        if ($scope.selection.ids[k]) {
          $scope.multipleDelete = false;
          keepGoing = false;
        } else {
          $scope.multipleDelete = true;
        }
      }
    });

    if (keepGoing)
      $scope.mainCheckbox = false;
  }, true);
  // !!END Delete multiple applications
  // -------------------------------------

  function changedTurnServerBool() {
    $scope.disableButton = false;
    $scope.classVar = 'panel-default';
  };

  function checkTurn() {
    console.log($scope._turnServer);
    checkTURNServer({
      url: 'turn:' + $scope._turnServer.turnServerUrl,
      username: $scope._turnServer.turnServerUsername,
      credential: $scope._turnServer.turnServerPassword
    }).then(function(bool) {
      console.log('is TURN server active? ', bool ? 'yes' : 'no');
      if (bool) {
        disableButton(false);
        changeDivPane('panel-success');
      } else {
        disableButton(true);
        changeDivPane('panel-danger');
      }
    }).catch(function(reason) {
      console.error.bind(console);
      console.log(reason);
      disableButton(true);
    });
  };

  function showPlot() {
    if ($('#numberFlot').find('div.vis-timeline').length == 0) {
      drawGraph('numberFlot');
      drawGraph('capacityFlot');
      google.charts.setOnLoadCallback(drawGraphMediaServer);
      renderGraphMediaServer();
    }
  };



  // !!Private methods declaration
  // -------------------------------------------------------------------------

  function getAllMediaServers() {
    var url = ip + '/api/v2/nubomedia/paas/app/' + $routeParams.applicationId + '/media-server';
    http.get(url)
      .success(function(response, status) {
        $scope.allMediaServers = response;

        $scope.allActiveMediaServers = $scope.allMediaServers.filter(function(item) {
          return item.status === 'ACTIVE';
        })

      }).error(function(data, status) {
        console.log('get all media servers error');
      });
  }

  function createCustomPopover() {
    $(".popover-custom[data-toggle=popover]").popover({
      html: true,
      placement: 'bottom',
      content: function() {
        return '<a href="http://nubomedia.readthedocs.io/en/latest/paas/autoscaling/" target="_blank">http://nubomedia.readthedocs.io/en/latest/paas/autoscaling/</a>';
      }
    });
  }

  function renderGraphMediaServer() {
    $timeout(function() {
      $rootScope.viewMediaServerGraph.setColumns([0, 1, 2, 3]);
      $rootScope.chartMediaServerGraph.draw($rootScope.viewMediaServerGraph, $rootScope.optionsMediaServerGraph);
    }, 4000);
  }

  function getDataFromMediaServer(mediaServer) {
    var prefixUrl = 'http://80.96.122.69/graphlot/rawdata?from=-1hours&until=-0hour&target=server.';
    var suffixUrl = '&step=10';

    var apiList = ["memory", "elements", "pipelines"];

    return $q.all(apiList.map(function(item) {
        if (item === 'memory') {
          return $http({
            method: 'GET',
            url: prefixUrl + mediaServer + '.memory.memory-free' + suffixUrl
          });
        } else {
          return $http({
            method: 'GET',
            url: prefixUrl + mediaServer + '.' + item + suffixUrl
          });
        }
      }))
      .then(function(results) {
        var resultObj = {};
        results.forEach(function(val, i) {
          resultObj[apiList[i]] = val.data;
        });
        $rootScope.bigDataMediaServer = zipJsonMediaServer(resultObj.memory, resultObj.elements, resultObj.pipelines);

        $timeout(function() {
          $rootScope.bigDataResponse = google.visualization.arrayToDataTable($rootScope.bigDataMediaServer);

          $rootScope.viewMediaServerGraph = new google.visualization.DataView($rootScope.bigDataResponse);
          $rootScope.viewMediaServerGraph.setColumns([0, 1, 2, 3]);

          $rootScope.chartMediaServerGraph.draw($rootScope.viewMediaServerGraph, $rootScope.optionsMediaServerGraph);
        }, 3000);
        return resultObj;
      });
  }

  function drawGraphMediaServer() {
    var dataDemo = [
      ['Time', 'Free Memory', 'Media Elements', 'Media Pipelines'],
      ['2013', 1000, 400, 100],
      ['2016', 1030, 540, 1461]
    ];

    var bigDataResponse = google.visualization.arrayToDataTable(dataDemo);

    $rootScope.viewMediaServerGraph = new google.visualization.DataView(bigDataResponse);
    $rootScope.viewMediaServerGraph.setColumns([0, 1, 2, 3]);

    $rootScope.optionsMediaServerGraph = {
      title: 'Media Server Monitoring',
      hAxis: {
        title: 'Time',
        titleTextStyle: {
          color: '#333'
        }
      },
      vAxis: {
        minValue: 0
      },
      legend: {
        position: 'right',
        textStyle: {
          fontSize: 14
        }
      }
    };

    $rootScope.chartMediaServerGraph = new google.visualization.AreaChart(document.getElementById('chart_div'));
    $rootScope.chartMediaServerGraph.draw($rootScope.viewMediaServerGraph, $rootScope.optionsMediaServerGraph);
  }

  function estimateTimestamp(dataMemory) {
    var arrayTimestamp = [];
    for (var i = dataMemory[0].start; i < dataMemory[0].end + 1; i++) {
      arrayTimestamp.push(i);
    }
    return arrayTimestamp;
  }

  function removeNull(dataArry) {
    var cleanData = [];
    for (var i = 0; i < dataArry[0].data.length; i++) {
      if (dataArry[0].data[i] !== null) {
        cleanData.push(dataArry[0].data[i]);
      } else {
        cleanData.push(0);
      }
    }
    return cleanData;
  }

  function zipJsonMediaServer(dataMemory, dataElements, dataPipelines) {
    var zipData = [],
      timestamp = [],
      cleanDataMemory = [],
      cleanDataElements = [],
      cleanDataPiepelines = [],
      subVals = [];

    timestamp = estimateTimestamp(dataMemory);
    cleanDataMemory = removeNull(dataMemory, cleanDataMemory);
    cleanDataElements = removeNull(dataElements, cleanDataElements);
    cleanDataPiepelines = removeNull(dataPipelines, cleanDataPiepelines);

    zipData.push(['Time', 'Free memory', 'Media Elements', 'Media Pipelines']);

    for (var i = 0; i < cleanDataMemory.length; i++) {
      subVals.push(timestamp[i]);
      subVals.push(cleanDataMemory[i]);
      subVals.push(cleanDataElements[i]);
      subVals.push(cleanDataPiepelines[i]);
      zipData.push(subVals);
      subVals = [];
    }

    return zipData;
  }

  function mergeMediaServer(mediaServerGroup) {
    var mergedServer;
    for (var i = 0; i < mediaServerGroup.hosts.length; i++) {
      mergedServer = {
        floatingIPs: mediaServerGroup.hosts[i].floatingIp,
        hostname: mediaServerGroup.hosts[i].hostname
      };
      $rootScope.mediaServers.push(mergedServer);
    }
  }

  function loadTable() {
    console.log($location.path());
    console.log(marketurl);
    if ($location.path() === '/marketapps')
      http.get(marketurl)
      .success(function(response, status) {
        $scope.applications = response;
        console.log(response);

      }).error(function(data, status) {
        if (status === 404)
          showError(status, "The Server is offline");
        else
          showError(status, data);

      });
    else
      http.get(url)
      .success(function(response, status) {
        $scope.applications = response;
        console.log(response);

      }).error(function(data, status) {
        showError(status, data);
      });
  }

  function n2br(str) {
    str = str.replace(/(?:\\[rn]|[\r\n]+)+/g, "<br />");
    //return str.replace(/\r\n|\r|\n//g, "<br />");
    var x = str;
    var r = /\\u([\d\w]{4})/gi;
    x = x.replace(r, function(match, grp) {
      return String.fromCharCode(parseInt(grp, 16));
    });
    x = unescape(x);
    return x;
  }

  function showError(status, data) {
    // !Important: To check if there can be multiple messages in data object
    var message = '';

    if (typeof data === 'string') {
      message = data;
    } else {
      message = data.message;
    }

    $scope.alerts.push({
      type: 'danger',
      msg: message
    });

    $('.modal').modal('hide');
  }

  function showOk(msg) {
    $scope.alerts.push({
      type: 'success',
      msg: msg
    });
    $('.modal').modal('hide');
  }

  function disableButton(value) {
    if (value)
      if ($scope._turnServer.turnServerUsername !== '' || $scope._turnServer.turnServerPassword !== '' || $scope._turnServer.turnServerUrl !== '')
        $scope.$apply(function() {
          $scope.disableButton = value;
        });
  }

  function changeDivPane(classValue) {
    $scope.$apply(function() {
      $scope.classVar = classValue;
    });

  }

  function checkTURNServer(turnConfig, timeout) {
    return new Promise(function(resolve, reject) {
      setTimeout(function() {
        if (promiseResolved) return;
        resolve(false);
        promiseResolved = true;
      }, timeout || 5000);

      var promiseResolved = false;
      var myPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection; //compatibility for firefox and chrome,
      var pc = new myPeerConnection({
        iceServers: [turnConfig]
      });
      var noop = function() {};

      pc.createDataChannel(""); //create a bogus data channel
      pc.createOffer(function(sdp) {
        if (sdp.sdp.indexOf('typ relay') > -1) { // sometimes sdp contains the ice candidates...
          promiseResolved = true;
          resolve(true);
        }
        pc.setLocalDescription(sdp, noop, noop);
      }, noop); // create offer and set local description
      pc.onicecandidate = function(ice) { //listen for candidate events
        if (promiseResolved || !ice || !ice.candidate || !ice.candidate.candidate || !(ice.candidate.candidate.indexOf('typ relay') > -1)) return;
        promiseResolved = true;
        resolve(true);
      };
    });
  }

  function loadMediaManeger() {
    http.get(urlMediaManager)
      .success(function(data) {
        //console.log(data);
        $scope.vnfrs = data;
        angular.forEach($scope.vnfrs, function(vnfr, index) {
          if ($scope.application.mediaServerGroup.nsrID == vnfr.nsrId) {
            $scope.vnfrId = vnfr.vnfrId;
            loadCapacityHistory();
            loadNumberHistory();
          }
        });
      });
  }

  function loadNumberHistory() {
    if (!angular.isUndefined($scope.vnfrId))
      http.get(urlMediaManager + $scope.vnfrId + '/media-server/number/history')
      .success(function(data) {
        $scope.numbersHistory = data;
        angular.forEach($scope.numbersHistory, function(number, index) {
          number.id = index;
          number.x = new Date(number.timestamp);
          number.y = number.value;
          $scope.numberValue = number.value;

        });
        console.log($scope.numbersHistory);
      });
  }

  function loadCapacityHistory() {
    if (!angular.isUndefined($scope.vnfrId)) {
      http.get(urlMediaManager + $scope.vnfrId + '/media-server/load/history')
        .success(function(data) {
          $scope.loadHistory = data;
          angular.forEach($scope.loadHistory, function(load, index) {
            load.id = index;
            load.x = new Date(load.timestamp);
            load.y = load.value;
            $scope.loadValue = load.value;
            if (load.value > maxLoad)
              maxLoad = load.value + 50.0;
          });
          console.log($scope.loadHistory);
        });
    }
  }

  function getValue(type) {
    //console.log(urlMediaManager + $scope.vnfrId + '/media-server/' + type);
    if (!angular.isUndefined($scope.vnfrId))
      http.get(urlMediaManager + $scope.vnfrId + '/media-server/' + type)
      .success(function(data) {
        console.log(data);
        if (type === 'number')
          $scope.numberValue = data;
        else
          $scope.loadValue = data;
      });
    else
      console.error('vnfrId == undefined');

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

    var options = {
      start: vis.moment().add(-60, 'seconds'), // changed so its faster
      end: vis.moment(),
      dataAxis: {
        left: {
          range: {
            min: 0
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

    var dataset = '';

    if (id === 'numberFlot') {
      delete options.shaded;

      dataset = new vis.DataSet($scope.numbersHistory);

    }
    if (id === 'capacityFlot') {
      options.dataAxis.left.range.max = maxLoad;
      dataset = new vis.DataSet($scope.loadHistory);
    }

    var graph2d = new vis.Graph2d(container, dataset, options);

    function y(x) {
      if (id === 'capacityFlot') {
        var value = getValue('load');
        //value = (Math.floor((Math.random() * 1000) + 1));
        if (value > options.dataAxis.left.range.max) {
          options.dataAxis.left.range.max = value + 50.0;
          console.log(value + 50.0);
          graph2d.setOptions(options);
        }

        return value;

      } else {
        return getValue('number');
      }
      //return (Math.floor((Math.random() * 6) + 1));
    }

    function renderStep() {
      // move the window (you can think of different strategies).
      var now = vis.moment();
      var range = graph2d.getWindow();
      var interval = range.end - range.start;
      graph2d.setWindow(now - interval, now, {
        animation: false
      });
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
      //var range = graph2d.getWindow();
      //var interval = range.end - range.start;
      //var oldIds = dataset.getIds({
      //    filter: function (item) {
      //        return item.x < range.start - interval;
      //    }
      //});
      //dataset.remove(oldIds);

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

});
