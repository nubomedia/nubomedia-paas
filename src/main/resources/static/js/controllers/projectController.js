/*
 *
 *  * Copyright (c) 2016 Open Baton
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

var app = angular.module('app');
app.controller('ProjectCtrl', function ($scope, serviceAPI, $routeParams, http, $cookieStore, AuthService, $rootScope) {

    var url = $cookieStore.get('URLNb') + "/api/v1/projects/";
    var urlUsers = $cookieStore.get('URLNb') + "/api/v1/users/";

    $scope.alerts = [];
    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
    $scope.roles = [
        'ADMIN',
        'USER',
        'GUEST'
    ];

    loadTable();

    $scope.projectObj = {
        'name': '',
        'description':'',
        'users':{},
        'usersPairs':[]
    };
    $scope.users = [];

    $scope.projectUpd = {};
    http.get(urlUsers)
        .success(function (response) {
            console.log('users');
            usersObj = JSON.parse(JSON.stringify(response));
            console.log(usersObj);
            for (i = 0; i < usersObj.length; i++) {
              $scope.users.push(usersObj[i].username);

            }
            console.log($scope.users);
            //$scope.projects.push({name: ''});
        })
        .error(function (response, status) {
            showError(response, status);
        });
    /* -- multiple delete functions Start -- */

    $scope.multipleDeleteReq = function(){
        var ids = [];
        angular.forEach($scope.selection.ids, function (value, k) {
            if (value) {
                ids.push(k);
            }
        });
        //console.log(ids);
        http.post(url + 'multipledelete', ids)
            .success(function (response) {
                showOk('Projects ' + ids.toString() + ' deleted.');
                loadTable();
            })
            .error(function (response, status) {
                showError(response, status);
            });

    };

    $scope.main = {checkbox: false};
    $scope.$watch('main', function (newValue, oldValue) {
        ////console.log(newValue.checkbox);
        ////console.log($scope.selection.ids);
        angular.forEach($scope.selection.ids, function (value, k) {
            $scope.selection.ids[k] = newValue.checkbox;
        });
        //console.log($scope.selection.ids);
    }, true);

    $scope.$watch('selection', function (newValue, oldValue) {
        //console.log(newValue);
        var keepGoing = true;
        angular.forEach($scope.selection.ids, function (value, k) {
            if (keepGoing) {
                if ($scope.selection.ids[k]) {
                    $scope.multipleDelete = false;
                    keepGoing = false;
                }
                else {
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


    $scope.types = ['REST', 'RABBIT'];
    $scope.deleteProject = function (data) {
        http.delete(url + data.id)
            .success(function (response) {
                showOk('Project ' + data.name + ' deleted.');
                loadTable();
            })
            .error(function (response, status) {
                showError(response, status);
            });
    };

    $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
    };
    $scope.addUserCreate = function() {
      var newUser = {
          name : "",
          role : ""
      };
      $scope.projectObj.usersPairs.push(newUser);
      console.log($scope.projectObj);
    };

    $scope.save = function () {
        //console.log($scope.projectObj);
        postObj = {};
        postObj.name = $scope.projectObj.name;
        postObj.description = $scope.projectObj.description;
        postObj.users = {};
        toPush = {};
        for (i = 0; i < $scope.projectObj.usersPairs.length; i++) {
          toPush[$scope.projectObj.usersPairs[i].name] = $scope.projectObj.usersPairs[i].role;

        }
        postObj.users = toPush;
        http.post(url, postObj)
            .success(function (response) {
                showOk('Project ' + postObj.name + ' created.');
                loadTable();
            })
            .error(function (response, status) {
                showError(response, status);
            });
    };
    function loadTable() {
            http.get(url)
                .success(function (response) {
                    $rootScope.projects = response;
                    //console.log(response);
                })
                .error(function (data, status) {
                    showError(data, status);
                });
    }

    function showError(data, status) {
        $scope.alerts.push({
            type: 'danger',
            msg: 'ERROR: <strong>HTTP status</strong>: ' + status + ' response <strong>data</strong> : ' + JSON.stringify(data)
        });
        $('.modal').modal('hide');
        if (status === 401) {
            console.error(status + ' Status unauthorized');
            AuthService.logout();
        }
    }

    function showOk(msg) {
        $scope.alerts.push({type: 'success', msg: msg});
        loadTable();
        $('.modal').modal('hide');
    }
    $scope.startUpdate = function(data) {
        $scope.projectUpd = JSON.parse(JSON.stringify(data));
        $scope.projectUpd.usersPairs = [];

        for (var key in  $scope.projectUpd.users) {
          var newRole = {name: key, role:$scope.projectUpd.users[key]};
          console.log("here is new role " + newRole);
          $scope.projectUpd.usersPairs.push(newRole);
        }

        console.log($scope.projectUpd);
    };
    $scope.updateSave = function () {
        //console.log($scope.userUpdate);
        updateObj = {};
        updateObj.id = $scope.projectUpd.id;
        updateObj.name = $scope.projectUpd.name;
        updateObj.description = $scope.projectUpd.description;
        updateObj.users = {};
        toPush = {};
        for (i = 0; i < $scope.projectUpd.usersPairs.length; i++) {


          toPush[$scope.projectUpd.usersPairs[i].name] = $scope.projectUpd.usersPairs[i].role;

        }
        updateObj.users = toPush;
        console.log("Copied");
        console.log(updateObj);
        http.put(url + updateObj.id, updateObj)
            .success(function (response) {
                    showOk('User ' + updateObj.name + ' updated.');
                    loadTable();
            })
            .error(function (response, status) {
                    showError(response, status);
            });
    };

    $scope.addUser = function() {
      var newUser = {
          name : "",
          role : ""
      };
      $scope.projectUpd.usersPairs.push(newUser);
      console.log($scope.projectUpd);
    };


});
