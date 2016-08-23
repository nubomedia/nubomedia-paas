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

var app = angular.module('app');

/**
 *
 * Manages the login page
 *
 */
app.controller('LoginController', function($scope, AuthService, Session, $rootScope, $location, $cookieStore, $http) {
  $scope.currentUser = null;
  //$scope.URL = 'http://localhost:8081';
  //$scope.URL= 'http://80.96.122.73:8081';
  $scope.URL = '';
  $scope.credential = {
    "username": '',
    "password": '',
    "grant_type": "password"
  };

  // Show create app page and hide other stuff
  $scope.createFormViewIsVisible = false;
  $scope.toggleCreateFormView = function() {
    $scope.createFormViewIsVisible = !$scope.createFormViewIsVisible;
  };

  $scope.resetCreateFormView = function() {
    $scope.createFormViewIsVisible = false;
  };

  // Copy to clipboard functions
  $scope.clipboardSuccess = function() {
    var clipboardMessage = angular.element('.js-form-clipboard-info').addClass('show');
    $scope.showCopyClipboardMessage = true;
    setTimeout(function() {
      clipboardMessage.removeClass('show');
    }, 1500);

  };

  $scope.clipboardFail = function(err) {
    console.error('Error!', err);
  };
  // END Copy to clipboard functions

  if (angular.isUndefined($cookieStore.get('loggedNb'))) {
    $scope.logged = false;
    $rootScope.logged = false;
  } else if ($cookieStore.get('loggedNb')) {
    $scope.logged = $cookieStore.get('loggedNb');
    $rootScope.logged = $cookieStore.get('loggedNb');
  }
  $location.replace();
  console.log($scope.logged);
  $scope.loggedF = function() {
    return $scope.logged;
  };


  /*$scope.checkSecurity = function () {
   console.log($scope.URL + "/api/v1/security");
   AuthService.removeSession();
   $http.get($scope.URL + "/api/v1/security")
   .success(function (data) {

   })
   .error(function (data, status) {
   if (status == 404) {
   AuthService.loginGuest($scope.URL);
   }
   console.info(('status != 404'));
   console.error('Response error', status, data);
   })

   };*/

  /**
   * Calls the AuthService Service for retrieving the token access
   *
   * @param {type} credential
   * @returns {undefined}
   */
  $scope.login = function(credential) {
    AuthService.login(credential, $scope.URL);
    setTimeout(showLoginError, 2000);
  };

  function showLoginError() {
    $scope.$apply(function() {
      $scope.loginError = angular.isUndefined($cookieStore.get('loggedNb'));
      console.log($scope.loginError);
    });
  }
});


app.controller('IndexCtrl', function($scope, $cookieStore, $location, AuthService, http, $rootScope, $window) {
  $('#side-menu').metisMenu();

  var url = $cookieStore.get('URLNb') + "/api/v1";
  $scope.logged = $cookieStore.get('loggedNb');
  console.log($scope.logged);
  $location.replace();

  $scope.$watch('projectSelected', function(newValue, oldValue) {
    console.log(newValue);
    if (!angular.isUndefined(newValue) && !angular.isUndefined(oldValue)) {
      $cookieStore.put('projectNb', newValue);
    }
    if (!angular.isUndefined(newValue) && angular.isUndefined(oldValue)) {
      $cookieStore.put('projectNb', newValue);
      loadNumbers();
    }
  });

  /**
   * Checks if the user is logged
   * @returns {unresolved}
   */
  $scope.loggedF = function() {
    return $scope.logged;
  };

  if ($scope.logged)
    console.log('Ok Logged');
  $location.replace();
  $scope.username = $cookieStore.get('userNameNb');

  console.log($scope.username);


  /**
   * Delete the session of the user
   * @returns {undefined}
   */
  $scope.logout = function() {
    AuthService.logout();
  };


  $scope.numberApp = 0;
  $scope.numberSecGroup = 0;
  $scope.numberService = 0;

  function loadNumbers() {
    http.get(url + '/nubomedia/paas/app/').success(function(data) {
      console.log(data);
      $scope.numberApp = data.length;

    });
  }

  $scope.changeProject = function(project) {
    if (arguments.length === 0) {
      http.syncGet(url + '/projects/')
        .then(function(response) {
          if (angular.isUndefined($cookieStore.get('projectNb')) || $cookieStore.get('projectNb').id === '') {
            $rootScope.projectSelected = response[0];
            $cookieStore.put('projectNb', response[0])
          } else {
            $rootScope.projectSelected = $cookieStore.get('projectNb');
          }
          $rootScope.projects = response;
        });
    } else {
      $rootScope.projectSelected = project;
      console.log(project);
      $cookieStore.put('projectNb', project);
      $window.location.reload();
    }


  };


  // Active menu link

  $(document).on('click', '.js-menu-link', function() {
    $('.js-menu-link').removeClass('is-active');
    $(this).addClass('is-active');
  });

});
