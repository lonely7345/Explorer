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
