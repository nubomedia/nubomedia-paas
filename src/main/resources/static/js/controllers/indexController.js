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
  $rootScope.googleCharIsLoaded = false;
  $scope.URL = '';
  $scope.userIsAdmin = false;
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
  loadNumbers();

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

  $scope.alerts = [];

  $scope.closeAlert = function(index) {
    $scope.alerts.splice(index, 1);
  };

  var showOk = function showOk(msg) {
    $scope.alerts.push({
      type: 'success',
      msg: msg
    });
    $('#modalSend').modal('hide');
  };

  var showError = function showError(data, status) {
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

    $('#modalSend').modal('hide');
  };

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

  // This is needed in order to show users and projects pages only for admin users
  http.get(url + '/users/current')
    .success(function(data) {
      if (data.roles[0].role === 'ADMIN') {
        $scope.userIsAdmin = true;
      }
    });

  // Dashboard info
  http.get(url + '/users')
    .success(function(data) {
      $rootScope.userList = data;
    });

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

  $scope.changePassword = function() {
    $scope.oldPassword = '';
    $scope.newPassword = '';
    $scope.newPassword1 = '';

    $('#modalChangePassword').modal('show');
  };

  $scope.postNew = function() {
    if ($scope.newPassword.localeCompare($scope.newPassword1) == 0) {
      $scope.passwordData = {};
      $scope.passwordData.old_pwd = $scope.oldPassword;
      $scope.passwordData.new_pwd = $scope.newPassword;
      http.put(url + '/users/changepwd', JSON.stringify($scope.passwordData))
        .success(function(response) {
          showOk('The passwrod was changed successfully!');
        })
        .error(function(data, status) {
          showError(data, 'danger');
        });
    } else {
      alert("The new passwords are not the same");
    }
  };

  $scope.setActiveLink = function(path) {
    if ($location.url() === '/' && path === 'home') {
      return 'is-active';
    } else {
      return ($location.url().substr(0, path.length) === path) ? 'is-active' : '';
    }
  };

});
