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

angular.module('app').controller('applicationsCtrl', function($scope, http, $routeParams, serviceAPI, $window, $cookieStore, $http, $sce, $timeout, $location, $rootScope, $q) {

  var ip = $cookieStore.get('URLNb');
  var url = ip + '/api/v1/nubomedia/paas/app/';
  var urlPK = ip + '/api/v1/nubomedia/paas/';
  var urlMediaManager = '';
  var maxLoad = 100;
  var marketurl = $cookieStore.get('marketplaceIP');
  $scope.file = '';
  $scope.appJson = '';

  //var marketurl = 'http://localhost:8082/api/v1/app/';
  //console.log('$cookieStore.get(\'URLNb\') ==  '+$cookieStore.get('URLNb') );
  //console.log('$cookieStore.get(\'server-ip\') ==  '+$cookieStore.get('server-ip') );


  if (angular.isUndefined($cookieStore.get('server-ip'))) {
    http.get($cookieStore.get('URLNb') + '/api/v1/nubomedia/paas/server-ip/')
      .success(function(data) {
        var serverIpString = data.replaceAll("\"", "");
        console.log(serverIpString);
        urlMediaManager = 'http://' + serverIpString + ':9000/vnfr/';
        $cookieStore.put('server-ip', serverIpString);
      });
  } else {
    urlMediaManager = 'http://' + $cookieStore.get('server-ip') + ':9000/vnfr/';
  }

  String.prototype.replaceAll = function(target, replacement) {
    return this.split(target).join(replacement);
  };

  $scope.alerts = [];
  $scope.applications = [];
  $scope.flavors = ["SMALL", "MEDIUM", "LARGE"];
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
  $scope.qosValue = {
    _qos: ''
  };
  $scope._threshold = {
    'scaleInOut': 0,
    'scale_out_threshold': 0
  };

  $rootScope.mediaServers = [];
  $rootScope.bigDataMediaServer = [];

  $scope.createApp = function() {
    $http.get('json/request.json')
      .then(function(res) {
        console.log(res.data);
        $scope.appCreate = angular.copy(res.data);
      });
    // $('#modalT').modal('show');
  };

  $scope.resize = function() {
    $timeout(function() {
      $('body').resize();
    }, 500);
  };
  $scope.createMarketApp = function() {
    $http.get('json/app.json')
      .then(function(res) {
        console.log(res.data);
        $scope.appCreate = angular.copy(res.data);
      });
    $('#modalT').modal('show');
  };

  $scope.saveApp = function(data) {
    $scope.application = data;
  };

  $scope.changeCdn = function() {
    $scope.appCreate.cloudRepository = $scope.appCreate.cdnConnector;
  };
  $http.get('json/infos.json')
    .then(function(res) {
      //console.log(res.data);
      $scope.infosObj = angular.copy(res.data);
    });

  $scope.getInfos = function(key) {
    console.log($scope.infosObj[key]);
    console.log(key);
    $scope.textInfo = $scope.infosObj[key];
    return $scope.textInfo;
  };
  $scope.privateKeyReq = {
    projectName: 'nubomedia',
    privateKey: ''

  };

  $scope.sendPK = function(privateKeyReq) {

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
  if (!angular.isUndefined($routeParams.applicationId)) {
    http.get(url + $routeParams.applicationId)
      .success(function(data) {
        console.log('jsonApp applicationId: ', data);
        $scope.application = data;
        $scope.applicationJSON = JSON.stringify(data, undefined, 4);
        loadMediaManeger();
        google.charts.load('current', {
          'packages': ['corechart']
        });
        google.charts.setOnLoadCallback(drawGraphMediaServer);
        mergeMediaServer(data.mediaServerGroup);
        $rootScope.myMediaServer = $rootScope.mediaServers[0]; // first floatingIps
        getDataFromMediaServer($rootScope.myMediaServer.hostname);
        renderGraphMediaServer();
      });
  } else {
    loadTable();
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

  $scope.drawColumnMediaServer = function(nameCol) {
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
  }

  $scope.updateGraphMediaServer = function(urlHostnameMediaServer) {
    getDataFromMediaServer(urlHostnameMediaServer);
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
  };

  function estimateTimestamp(dataMemory) {
    var arrayTimestamp = [];
    for (var i = dataMemory[0].start; i < dataMemory[0].end + 1; i++) {
      arrayTimestamp.push(i);
    }
    return arrayTimestamp;
  };

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
  };

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
  };

  function mergeMediaServer(mediaServerGroup) {
    var mergedServer;
    for (var i = 0; i < mediaServerGroup.floatingIPs.length; i++) {
      mergedServer = {
        floatingIPs: mediaServerGroup.floatingIPs[i],
        hostname: mediaServerGroup.hostnames[i]
      };
      $rootScope.mediaServers.push(mergedServer);
    }
  };


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

  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };

  $scope.toggle = {
    turnServer: false,
    threshold: false,
    cloudRepository: false,
    qualityOfService: false
  };

  $scope.launch = function(id) {
    http.syncGet(marketurl + id)
      .then(function(result) {
        console.log(result);
        http.post(url, result)
          .success(function(data, status) {
            console.log(data);
            showOk("App launched correctly.")
          })
          .error(function(data, status) {
            showError(status, data);
          })
      })
  };

  $scope.sendApp = function(value) {
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


    //console.log(JSON.stringify($scope.appCreate));

    if ($scope.file !== '') {
      postTopology = $scope.file;
      console.log("It's a File!")
    } else if ($scope.appJson !== '') {
      postTopology = $scope.appJson;
      console.log("It's a TextArea!")

    } else if (angular.isUndefined(postTopology)) {
      postTopology = $scope.appCreate;
      console.log("It's a Form!")

    } else {
      alert('Problem with Topology');
      sendOk = false;
    }
    console.log(postTopology);

    if (sendOk) {
      console.log(JSON.stringify(postTopology));

      if (arguments.length > 0)
        http.post(marketurl, postTopology)
        .success(function(response) {
          showOk('App Saved!');
          loadTable();
          $scope.file = '';
          $scope.appJson = '';
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
        })
        .error(function(data, status) {
          showError(status, data);
        });
    }

  };

  $scope.addPort = function() {
    $scope.appCreate.ports.push({
      "port": 8080,
      "targetPort": 8080,
      "protocol": "TCP"
    });
  };

  $scope.deletePort = function(index) {
    $scope.appCreate.ports.splice(index, 1);
  };
  $scope.deleteData = function(id) {
    http.delete(url + id)
      .success(function(response) {
        showOk('Deleted App with id: ' + id + ' done.');
        loadTable();
      })
      .error(function(data, status) {
        showError(status, data);
      });
  };
  $scope.deleteAppMarket = function(id) {
    http.delete(marketurl + id)
      .success(function(response) {
        showOk('Deleted App with id: ' + id + ' done.');
        loadTable();
      })
      .error(function(data, status) {
        showError(status, data);
      });
  };
  $scope.deleteAllApp = function() {
    http.delete(url)
      .success(function(response) {
        showOk('Deleted all Apps in the ' + $rootScope.projectSelected.name + ' project.');
        loadTable();
      })
      .error(function(data, status) {
        showError(status, data);
      });
  };


  $scope.changeText = function(text) {
    $scope.appJson = text;
    console.log($scope.appJson);
  };

  $scope.loadLog = function() {

    http.get(url + $routeParams.applicationId + '/buildlogs')
      .success(function(response) {
        //$scope.log = response;
        $scope.log = $sce.trustAsHtml(n2br(response.log));
      })
      .error(function(data, status) {
        showError(status, data);
      });
  };

  $scope.input = {
    numberRows: 35
  };
  $scope.loadAppLog = function(podName) {
    console.log(podName);
    http.get(url + $routeParams.applicationId + '/logs/' + podName)
      .success(function(response) {

        var stringArray = response.split('\\n');

        var subLog = stringArray.slice(stringArray.length - $scope.input.numberRows, -1);
        var string = subLog.join('\\n');
        //console.log(subLog);
        //console.log($scope.input.numberRows);

        $scope.log = $sce.trustAsHtml(n2br(string));
      })
      .error(function(data, status) {
        showError(status, data);
      });
  };

  $scope.checkStatus = function() {
    console.log(($scope.application.status !== 'RUNNING'));
    if ($scope.application.status !== 'RUNNING')
      return true;
    else return false;
  };

  $scope.setFile = function(element) {
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


  /* -- multiple delete functions Start -- */

  $scope.multipleDeleteReq = function() {
    var ids = [];
    angular.forEach($scope.selection.ids, function(value, k) {
      if (value) {
        ids.push(k);
      }
    });
    //console.log(ids);
    http.post(url + 'multipledelete', ids)
      .success(function(response) {
        showOk('Applications: ' + ids.toString() + ' deleted.');
        loadTable();
      })
      .error(function(response, status) {
        showError(response, status);
      });

  };

  $scope.main = {
    checkbox: false
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

  $scope.multipleDelete = true;

  $scope.selection = {};
  $scope.selection.ids = {};
  /* -- multiple delete functions END -- */


  function n2br(str) {
    str = str.replace(/(?:\\[rn]|[\r\n]+)+/g, "<br />");
    //return str.replace(/\r\n|\r|\n//g, "<br />");
    var x = str;
    var r = /\\u([\d\w]{4})/gi;
    x = x.replace(r, function(match, grp) {
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
    $scope.alerts.push({
      type: 'success',
      msg: msg
    });
    $('.modal').modal('hide');
  }

  $scope.disableButton = false;
  $scope.classVar = 'panel-default';
  $scope.changedTurnServerBool = function() {
    $scope.disableButton = false;
    $scope.classVar = 'panel-default';
  };
  $scope.checkTurn = function() {
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

      var promiseResolved = false,
        myPeerConnection = window.RTCPeerConnection || window.mozRTCPeerConnection || window.webkitRTCPeerConnection //compatibility for firefox and chrome
        ,
        pc = new myPeerConnection({
          iceServers: [turnConfig]
        }),
        noop = function() {};
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


  $scope.showPlot = function() {

    if ($('#numberFlot').find('div.vis-timeline').length == 0) {
      drawGraph('numberFlot');
      drawGraph('capacityFlot');
    }
  };


  //$scope.vnfrId = 'ed5c8629-36bb-402a-8481-49b3a8d3d6a3';
  $scope.vnfrId;
  $scope.numberValue = 1;
  $scope.loadValue = 0;

  function loadMediaManeger() {
    console.log($scope.vnfrId);
    console.log(urlMediaManager);

    http.get(urlMediaManager)
      .success(function(data) {
        //console.log(data);
        $scope.vnfrs = data;
        console.log($scope.vnfrs);
        angular.forEach($scope.vnfrs, function(vnfr, index) {
          if ($scope.application.mediaServerGroup.nsrID == vnfr.nsrId) {
            console.log(vnfr);
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
            min: 0,
            max: 10
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
      var dataset = new vis.DataSet($scope.numbersHistory);

    }
    if (id === 'capacityFlot') {
      options.dataAxis.left.range.max = maxLoad;
      var dataset = new vis.DataSet($scope.loadHistory);

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
