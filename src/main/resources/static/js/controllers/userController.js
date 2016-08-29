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
app.controller('UserCtrl', function($scope, serviceAPI, $routeParams, http, $cookieStore, AuthService, $window, $location) {

  var url = $cookieStore.get('URLNb') + "/api/v1/users/";
  var urlprojects = $cookieStore.get('URLNb') + "/api/v1/projects/";

  $scope.alerts = [];

  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };

  $scope.clearAlerts = function() {
    $scope.alerts = [];
  };

  $scope.deleteUserModal = function(data) {
    $scope.user = data;
    $('#modalDeleteUser').modal('show');
  };

  $scope.roles = [
    'ADMIN',
    'USER',
    'GUEST'
  ];

  $scope.addRoleUpdate = function() {
    var newRole = {
      "role": "USER",
      "project": ""
    };
    $scope.userUpdate.roles.push(newRole);
  };

  loadTable();

  $scope.roleAdd = {
    "role": "USER",
    "project": ""
  };

  $scope.addRole = function() {
    var newRole = {
      "role": "USER",
      "project": ""
    };
    $scope.userObj.roles.push(newRole);
  };

  $scope.loadCurrentUser = function() {
    http.get(url + 'current')
      .success(function(response) {
        console.log(response);
        $scope.currentUser = response;
      })
      .error(function(response, status) {
        showError(status, response);
      });
  };

  // Toggle password visibility
  $scope.userPassInputType = 'password';
  $scope.userPassInputAlt = 'Show password';

  $scope.togglePassInputType = function() {
    $scope.userPassInputToggle = !$scope.userPassInputToggle;
    if ($scope.userPassInputToggle) {
      $scope.userPassInputAlt = 'Hide password';
      $scope.userPassInputType = 'text';
    } else {
      $scope.userPassInputAlt = 'Show password';
      $scope.userPassInputType = 'password';
    }
  };

  // Change user pass by Admin
  $scope.userPassInputToggle = false;

  $scope.changeUserPassModal = function(username) {
    $scope.selectedUsername = username;
    $scope.userNewPassword = '';
    $('#changeUserPassModal').modal('show');
  };

  $scope.adminChangeUserPass = function(username, newPassword) {
    var newPassJson = {
      'password': newPassword
    };

    http.post(url + username + '/reset', newPassJson)
      .success(function(data) {
        showOk(selectedUsername + ' \'s password changed successfully');
      }).error(function(data) {
        showError('Password change failed!', 'danger');
      });
  };
  // End Change user pass by Admin

  http.get(urlprojects)
    .success(function(response) {
      //console.log(response);
      $scope.projects = response;
      //$scope.projects.push({name: ''});
    })
    .error(function(response, status) {
      showError(response, status);
    });


  $scope.userObj = {
    "username": "",
    "password": "",
    "email": "",
    "enabled": true,
    "roles": []
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
        showOk('Users ' + ids.toString() + ' deleted.');
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
    ////console.log(newValue.checkbox);
    ////console.log($scope.selection.ids);
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
  $scope.userUpdate = "";

  $scope.selection = {};
  $scope.selection.ids = {};
  /* -- multiple delete functions END -- */

  $scope.deleteuser = function(data, location) {
    http.delete(url + data.username)
      .success(function(response) {
        showOk('User ' + data.username + ' deleted.');
        loadTable();
        if (location) {
          $location.path('/' + location);
        }
      })
      .error(function(response, status) {
        showError(response, status);
      });
  };

  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };

  $scope.update = function(data) {
    $scope.userUpdate = JSON.parse(JSON.stringify(data));
    console.log(data);
  };

  $scope.save = function() {
    //console.log($scope.userObj);
    http.post(url, $scope.userObj)
      .success(function(response) {
        showOk('User ' + $scope.userObj.username + ' created.');
        loadTable();
        $scope.toggleCreateFormView();
      })
      .error(function(response, status) {
        showError(response, status);
      });
  };

  $scope.updateSave = function() {
    console.log($scope.userUpdate);
    updateObj = {};
    updateObj.id = $scope.userUpdate.id;
    updateObj.username = $scope.userUpdate.username;
    updateObj.email = $scope.userUpdate.email;
    updateObj.enabled = $scope.userUpdate.enabled;
    updateObj.roles = [];
    for (i = 0; i < $scope.userUpdate.roles.length; i++) {
      var newRole = {
        "id": $scope.userUpdate.roles[i].id,
        "role": $scope.userUpdate.roles[i].role,
        "project": $scope.userUpdate.roles[i].project
      };
      updateObj.roles.push(newRole);
    }
    console.log("Copied");
    console.log(updateObj);
    http.put(url + updateObj.username, updateObj)
      .success(function(response) {
        showOk('User ' + $scope.userObj.username + ' updated.');
        loadTable();
      })
      .error(function(response, status) {
        showError(response, status);
      });
  };

  $scope.update = function(data) {
    console.log(data);
    $scope.userUpdate = JSON.parse(JSON.stringify(data));
  };

  function loadTable() {
    //console.log($routeParams.userId);
    if (!angular.isUndefined($routeParams.userId))
      http.get(url + $routeParams.userId)
      .success(function(response, status) {
        //console.log(response);
        $scope.user = response;
        $scope.userJSON = JSON.stringify(response, undefined, 4);

      }).error(function(data, status) {
        showError(data, status);
      });
    else {
      http.get(url)
        .success(function(response) {
          $scope.users = response;
          console.log(response);
        })
        .error(function(data, status) {
          showError(data, status);
        });
    }
  }

  function showError(data, status) {
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

    if (status === 401) {
      console.log(status + ' Status unauthorized');
      AuthService.logout();
    }
  }

  function showOk(msg) {
    $scope.alerts.push({
      type: 'success',
      msg: msg
    });

    loadTable();
    $('.modal').modal('hide');
  }

});
