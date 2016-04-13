var app = angular.module('app');


/**
 *
 * Manages the login page
 *
 */

app.controller('LoginController', function ($scope, AuthService, Session, $rootScope, $location, $cookieStore, $http) {
    $scope.currentUser = null;
    //$scope.URL = 'http://localhost:8081';
    //$scope.URL= 'http://80.96.122.73:8081';
    $scope.URL = '';
    $scope.credential = {
        "username": '',
        "password": ''
    };

    if (angular.isUndefined($cookieStore.get('loggedNb'))) {
        $scope.logged = false;
        $rootScope.logged = false;
    }

    else if ($cookieStore.get('loggedNb')) {
        $scope.logged = $cookieStore.get('loggedNb');
        $rootScope.logged = $cookieStore.get('loggedNb');
    }
    $location.replace();
    console.log($scope.logged);
    $scope.loggedF = function () {
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
    $scope.login = function (credential) {
        AuthService.login(credential, $scope.URL);
        setTimeout(showLoginError, 2000);
    };

    function showLoginError() {
        $scope.$apply(function () {
            $scope.loginError = angular.isUndefined($cookieStore.get('loggedNb'));
            console.log($scope.loginError);
        });
    }
});


app.controller('IndexCtrl', function ($scope, $cookieStore, $location, AuthService,http) {


    var url = $cookieStore.get('URLNb');
    $scope.logged = $cookieStore.get('loggedNb');
    console.log($scope.logged);
    $location.replace();

    /**
     * Checks if the user is logged
     * @returns {unresolved}
     */
    $scope.loggedF = function () {
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
    $scope.logout = function () {
        AuthService.logout();
    };

    $scope.orchestratorURL = http.getOrchestratorURL();
    $scope.saveSetting = function () {
        console.log($scope.orchestratorURL);
        http.setOrchestratorURL($scope.orchestratorURL);
        $('.modal').modal('hide');
        window.location.reload();

    };
    $scope.numberApp=0;
    $scope.numberSecGroup=0;
    $scope.numberService=0;

    http.get(url + '/api/v1/nubomedia/paas/app/').success(function(data) {
        $scope.numberApp = data.length;

    });

   /* http.get(url + '/applications').success(function(data) {
        $scope.numberApp = data.length;

    });


    http.get(url + '/secgroups').success(function(data) {
        $scope.numberSecGroup = data.length;

    });*/

});



