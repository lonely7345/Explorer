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
 * based on NFlabs Zeppelin originaly forked on Nov'14
 */

//(function() {
    'use strict';
//    angular.module('Authentication').controller('LoginCtrl', LoginCtrl);
    /**
         * @ngdoc function
         * @name Authentication.controller:LoginCtrl
         * @description
         * # LoginCtrl
         * Controller of the login screen
         *
         * @author ivdiaz
         *
         */
//    LoginCtrl.$inject = ['$scope', '$rootScope', '$location', 'AuthenticationService'];
//    function LoginCtrl($scope, $rootScope, $location, AuthenticationService) {

    angular.module('Authentication').controller('LoginCtrl', function($scope, $rootScope, $location,
    AuthenticationService){
        // reset login status
        AuthenticationService.ClearCredentials();
        $scope.login = function() {
            $scope.dataLoading = true;
            AuthenticationService.Login($scope.username, $scope.password, function(response) {
                if (response.success) {
                    AuthenticationService.SetCredentials($scope.username, $scope.password);
                    $location.path('/');
                } else {
                    $scope.error = response.message;
                    $scope.dataLoading = false;
                }
            });
        };
    });
//})()