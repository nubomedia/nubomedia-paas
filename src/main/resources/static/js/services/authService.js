/**
 * Angular Service for managing the login of the user
 */

angular.module('app').factory('AuthService', function($http, Session, $location, http, $cookieStore, $window, $q) {
    var authService = {};

    authService.login = function(credentials, URL) {
        console.log(credentials);
        return $http({
            method: 'POST',
            url:URL + '/api/v1/nubomedia/paas/auth',
            headers: {
                'Content-type': 'application/json'
            },
            data: credentials})
            .then(function(res) {
                console.log(res);
                Session.create(URL, res.data.token, credentials.username, true);
                $location.path("/");
                $window.location.reload();
                return;
            });
    };

    authService.loginGuest = function(URL) {
        Session.create(URL,'', 'guest', true);
        $location.path("/");
        $window.location.reload();
        return ;
    };




    authService.isAuthenticated = function() {
        return !!Session.userName;
    };

    authService.removeSession = function() {
        Session.destroy();
    };

    authService.logout = function() {
        Session.destroy();
        $window.location.reload();
    };

    authService.isAuthorized = function(authorizedRoles) {
        if (!angular.isArray(authorizedRoles)) {
            authorizedRoles = [authorizedRoles];
        }
        return (authService.isAuthenticated() &&
        authorizedRoles.indexOf(Session.userName) !== -1);
    };

    return authService;


    /**
     * Angular Service for managing the session and cookies of the user
     */

}).service('Session', function($cookieStore) {


    this.create = function(URL, token, userName, logged) {
        this.URL = URL;
        this.token = token;
        this.userName = userName;
        this.logged = logged;
        $cookieStore.put('loggedNb', logged);
        $cookieStore.put('userNameNb', userName);
        $cookieStore.put('tokenNb', token);
        $cookieStore.put('URLNb', URL);
//        console.log($cookieStore.get('token'));

    };
    this.destroy = function() {
        this.URL = null;
        this.token = null;
        this.userName = null;
        this.logged = false;
        $cookieStore.remove('loggedNb');
        $cookieStore.remove('userNameNb');
        $cookieStore.remove('tokenNb');
        $cookieStore.remove('URLNb');

    };
    return this;
});