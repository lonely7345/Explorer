/*
 *  Licensed to STRATIO (C) under one or more contributor license agreements.
 *  See the NOTICE file distributed with this work for additional information
 *  regarding copyright ownership.  The STRATIO (C) licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
 * based on NFlabs Zeppelin originaly forked on Nov'14
 */

    'use strict';
    /**
     * @ngdoc function
     * @name explorerWebApp.controller:TopBarCtrl
     * @description
     * # TopBarCtrl
     * Controller of the top navigation, mainly use for the dropdown menu
     *
     * @author anthonycorbacho
     * @author ivdiaz
     *
     */

    angular.module('explorerWebApp').controller('TopBarCtrl', function($scope, $rootScope, AuthenticationService,
    $modal, $http, baseUrlSrv){


        $scope.logout = function() {
            AuthenticationService.ClearCredentials();
        };
        $scope.openHelp = function() {
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: '/views/modal-shortcut.html',
                windowClass: 'center-modal'
            });
        };
        $scope.openSettings = function() {
            var modalInstance = $modal.open({
                animation: true,
                templateUrl: '/views/modal-settings.html',
                windowClass: 'center-modal'
            });
        };
        $rootScope.$on('openPropertiesEditor', function(event, path) {
                    $scope.openPropertiesEditor(path);
        });
        $scope.openPropertiesEditor = function(path) {
            console.log(path);
            var modalInstance = $modal.open({
                animation: true,
                controller: 'ModalEditCtrl',
                templateUrl: '/views/modal-editor.html',
                windowClass: 'center-modal',
                resolve: {
                    properties: function() {
                        return $http.get(baseUrlSrv.getRestApiBase() + '/interpreter/settings/editor?path=' + path).
                        success(function(data, status, headers, config) {
                            console.log("success");
                        }).
                        error(function(data, status, headers, config) {
                            console.log('Error %o %o', status, data.message);
                        });
                    },
                    resolvePath: function() {
                        return path;
                    }
                }
            });
        };
    });
