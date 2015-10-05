/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


'use strict';

/**
 * @ngdoc overview
 * @name explorerWebApp
 * @description
 * # explorerWebApp
 *
 * Main module of the application.
 *
 * @author anthonycorbacho & adapted by idiaz
 */

angular.module('Authentication', []);
var app = angular
  .module('explorerWebApp', [
    'Authentication',
    'ngAnimate',
    'ngCookies',
    'ngRoute',
    'ngSanitize',
    'angular-websocket',
    'ui.ace',
    'ui.bootstrap',
    'ngTouch',
    'ngDragDrop'

  ])
  .config(function ($routeProvider) {

    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl',
        hideMenus: true
      })
      .when('/explorer/:noteId/paragraph/:paragraphId?', {
        templateUrl: 'views/main.html'
      })
      .otherwise({
        redirectTo: '/login'
      });
  }).run(['$rootScope', '$location', '$cookieStore', '$http',
       function ($rootScope, $location, $cookieStore, $http) {
         // keep user logged in after page refresh
         $rootScope.globals = $cookieStore.get('globals') || {};
         if ($rootScope.globals.currentUser) {
           $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata; // jshint ignore:line
         }

         $rootScope.$on('$locationChangeStart', function (event, next, current) {
           // redirect to login page if not logged in
           if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
             $location.path('/login');
           }
         });
       }]);
