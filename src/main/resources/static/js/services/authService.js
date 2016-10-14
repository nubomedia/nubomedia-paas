/**
 * Angular Service for managing the login of the user
 */

angular.module('app').factory('AuthService', function ($http, Session, $location, http, $cookieStore, $window, $q) {
    var authService = {};

    var clientId = "openbatonOSClient";
    var clientPass = "secret";

    authService.login = function (credentials, URL) {
        var basic = "Basic " + btoa(clientId + ":" + clientPass);

        console.log(credentials);
        return $http({
            method: 'POST',
            url: URL + '/oauth/token',
            headers: {
                "Authorization": basic,
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            data: "username=" + credentials.username + "&password=" + credentials.password + "&grant_type=" + credentials.grant_type
        })
            .then(function (res) {
                console.log(res);
                Session.create(URL, res.data.value, credentials.username, true);
		console.log(URL)
                http.syncGet(URL + '/api/v1/nubomedia/config')
                    .then(function (data) {
                        console.log(data);
                        //$cookieStore.put('marketplaceIP', 'http://' + data.replace(/['"]+/g, '') + '/api/v1/app/');
                        $cookieStore.put('marketplaceIP', 'http://80.96.122.104:8082/api/v1/app/');
                    });
                $location.path("/");
                $window.location.reload();
                return;
            });

    };

    authService.loginGuest = function (URL) {
        Session.create(URL, '', 'guest', true);
        $location.path("/");
        $window.location.reload();
        return;
    };


    authService.isAuthenticated = function () {
        return !!Session.userName;
    };

    authService.removeSession = function () {
        Session.destroy();
    };

    authService.logout = function () {
        Session.destroy();
        $window.location.reload();
    };

    authService.isAuthorized = function (authorizedRoles) {
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

}).service('Session', function ($cookieStore) {


    this.create = function (URL, token, userName, logged) {
        this.URL = URL;
        this.token = token;
        this.userName = userName;
        this.logged = logged;
        $cookieStore.put('loggedNb', logged);
        $cookieStore.put('userNameNb', userName);
        $cookieStore.put('tokenNb', token);
        $cookieStore.put('URLNb', URL);
        $cookieStore.put('projectNb', {name: 'default', id: ''});
        $cookieStore.put('marketplaceIP', 'http://80.96.122.104:8082/api/v1/app/');

    };
    this.destroy = function () {
        this.URL = null;
        this.token = null;
        this.userName = null;
        this.logged = false;
        $cookieStore.remove('loggedNb');
        $cookieStore.remove('userNameNb');
        $cookieStore.remove('tokenNb');
        $cookieStore.remove('URLNb');
        $cookieStore.remove('projectNb');

    };
    return this;
});
