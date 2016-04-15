angular.module('app', ['ngRoute', 'ngSanitize', 'ui.bootstrap', 'ngCookies'])
    .config(function ($routeProvider) {

        $routeProvider.
            when('/login', {
                templateUrl: 'login.html',
                controller: 'LoginController'
            }).
            when('/', {
                templateUrl: 'pages/contents.html',
                controller: 'IndexCtrl'
            }).
           when('/applications', {
                templateUrl: 'pages/applications/applications.html',
                controller: 'applicationsCtrl'
            }).
            when('/applications/:applicationId', {
                templateUrl: 'pages/applications/applicationinfo.html',
                controller: 'applicationsCtrl'
            }).
            otherwise({
                redirectTo: '/'
            });

    });



/**
 *
 * Redirects an user not logged in
 *
 */

angular.module('app').run(function ($rootScope, $location, $cookieStore, $route) {
    //$route.reload();
    $rootScope.$on('$routeChangeStart', function (event, next) {

//        console.log($cookieStore.get('loggedNb'));
        if ($cookieStore.get('loggedNb') === false || angular.isUndefined($cookieStore.get('loggedNb'))) {
            // no logged user, we should be going to #login
            if (next.templateUrl === "login.html") {
                // already going to #login, no redirect needed
            } else {
                // not going to #login, we should redirect now
                $location.path("/login");

            }
        }

    });
});

/*
 * MenuCtrl
 *
 * shows the modal for changing the Settings
 */
angular.module('app').controller('MenuCtrl', function ($scope, http) {




});

/*

angular.module('app').directive('chart',function() {

    return {
        restrict: 'E',
        link: function (scope, elem, attrs) {
            elem.addClass('plotStyle');
            var chart = null,
                opts = {};

            scope.$watch(attrs.ngModel, function (v) {
                if (!chart) {
                    chart = $.plot(elem, v, opts);
                    elem.show();
                } else {
                    chart.setData(v);
                    chart.setupGrid();
                    chart.draw();
                }
            });
        }
    }
});*/
