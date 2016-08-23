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

require({
    baseUrl: 'js',
    paths: {
        jquery: 'libs/jquery/jquery.min',
        jquery_ui: 'libs/jquery/jquery-ui.min',
        angularclipboard: '../bower_components/angular-clipboard/angular-clipboard',
        bootstrapJS: "../bower_components/bootstrap/dist/js/bootstrap.min",
        metisMenu: "../bower_components/metisMenu/dist/metisMenu.min",
        sb_admin_2: "../dist/js/sb-admin-2",
        vis: "../dist/js/vis",
        underscore: "libs/underscore/underscore-min",
        angular: "libs/angular/angular.min",
        angular_route: "libs/angular/angular-route.min",
        angular_cookies: "libs/angular/angular-cookies.min",
        ui_bootstrap: "libs/angular/ui-bootstrap-tpls-0.10.0.min",
        app: "app",
        authService: "services/authService",
        jsonHuman: "json.human",
        angular_sanitize: "libs/angular/angular-sanitize",
        tables: 'tablesorter/tables',
        tablesorter: 'tablesorter/jquery.tablesorter',
        httpService: "services/httpService",
        servicesServices: "services/servicesServices",
        applicationController: "controllers/applicationController",
        indexController: "controllers/indexController",
        projectController: "controllers/projectController",
        userController: "controllers/userController"

    },
    shim: {
        jquery: {
            exports: '$'
        },
        sb_admin_2: {
            deps: ['jquery', 'metisMenu']
        },
        bootstrapJS: {
            deps: ['jquery']
        },
        angular: {
            exports: 'angular',
            deps: ['jquery', 'bootstrapJS', 'sb_admin_2']
        },
        metisMenu: {
            deps: ['jquery']
        },
        jQueryRotate: {
            deps: ['jquery']
        },
        boot: {
            deps: ['jquery']
        },
        jquery_ui: {
            deps: ['jquery']
        },
        bootstrap: {
            deps: ['app']
        },
        projectController: {
            deps: ['app', 'servicesServices', 'httpService', 'angular_cookies', 'authService']
        },
        userController: {
            deps: ['app', 'servicesServices', 'httpService', 'angular_cookies', 'authService']
        },
        underscore: {
            exports: '_',
            deps: ['jquery']
        },
        authService: {
            deps: ['app']
        },
        app: {
            deps: ['angular', 'angular_route', 'angular_sanitize', 'ui_bootstrap']
        },
        angularclipboard: {
            deps: ['angular']
        },
        angular_route: {
            deps: ['angular']
        },
        angular_cookies: {
            deps: ['angular']
        },
        angular_sanitize: {
            deps: ['angular']
        },
        ui_bootstrap: {
            deps: ['angular']
        },
        servicesServices: {
            deps: ['app']
        },
        applicationController: {
            deps: ['app', 'servicesServices', 'httpService', 'underscore', 'angular_cookies']
        },

        indexController: {
            deps: ['app', 'httpService', 'servicesServices', 'authService']
        },
        httpService: {
            deps: ['app']
        },
        topologiesServices: {
            deps: ['app', 'httpService', 'servicesServices']
        }
    }
}), require([
    'require',
    'bootstrapJS',
    'metisMenu',
    'sb_admin_2',
    'underscore',
    'angular',
    'angular_route',
    'vis',
    'indexController',
    'projectController',
    'userController',
    'applicationController',
    'angularclipboard'
], function(require) {
    return require(['bootstrap', 'vis']);
});
