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

angular.module('app', ['ngRoute', 'ngSanitize', 'ui.bootstrap', 'ngCookies', 'angular-clipboard'])
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
            when('/marketapps', {
                templateUrl: 'pages/applications/marketApp.html',
                controller: 'applicationsCtrl'
            }).
            when('/marketapps/:appId', {
                templateUrl: 'pages/applications/appmarketinfo.html',
                controller: 'applicationsCtrl'
            }).
            when('/applications/:applicationId', {
                templateUrl: 'pages/applications/applicationinfo.html',
                controller: 'applicationsCtrl'
            }).
            when('/projects', {
                templateUrl: 'pages/projects.html',
                controller: 'ProjectCtrl'
            }).
            when('/users', {
                templateUrl: 'pages/users/users.html',
                controller: 'UserCtrl'
            }).
            when('/users/:userId', {
                templateUrl: 'pages/users/userinfo.html',
                controller: 'UserCtrl'
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
