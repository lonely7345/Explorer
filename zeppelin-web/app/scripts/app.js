/* Copyright 2014 NFlabs
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

'use strict';

/** get the current port pf the websocket */
function getPort() {
  var port = Number(location.port);
  /** case of binding default port (80 / 443) */
  if (port === 'undifined' || port === 0) {
    port = 80;
    if (location.protocol === 'https:') {
      port = 443;
    }
  }
  // brunch port
  if (port === 3333 || port === 9000) {
    port = 8080;
  }
  return port+1;
}

function getWebsocketProtocol() {
  var protocol = 'ws';
  if (location.protocol === 'https:') {
    protocol = 'wss';
  }
  return protocol;
}

/**
 * @ngdoc overview
 * @name zeppelinWebApp
 * @description
 * # zeppelinWebApp
 *
 * Main module of the application.
 *
 * @author anthonycorbacho & adapted by idiaz
 */

angular.module('Authentication', []);
var app = angular
  .module('zeppelinWebApp', [
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
  .config(function ($routeProvider, WebSocketProvider) {
    WebSocketProvider
      .prefix('')
      .uri(getWebsocketProtocol() + '://' + location.hostname + ':' + getPort());

    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html'
      })
      .when('/login', {
        templateUrl: '/views/login.html',
        controller: 'LoginController',
        hideMenus: true
      })
      .when('/notebook/:noteId', {
        templateUrl: 'views/notebooks.html',
        controller: 'NotebookCtrl'
      })
      .when('/notebook/:noteId/paragraph/:paragraphId?', {
        templateUrl: 'views/notebooks.html',
        controller: 'NotebookCtrl'
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

  app.config(['$httpProvider', function ($httpProvider) {
         $httpProvider.defaults.useXDomain = true;
         delete $httpProvider.defaults.headers.common['X-Requested-With'];
         $httpProvider.defaults.headers.common["Accept"] = "*/*";
         $httpProvider.defaults.headers.common["Content-Type"] = "text/plain";
  }]).factory('featuresData', function ($http) {
         return{
             doCrossDomainGet: function() {
                 return $http({
                     url:'http://localhost:9000',
                     method: 'POST'
                 })
             }
         }
  });




