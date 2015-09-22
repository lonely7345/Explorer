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
    angular
        .module('notebookWebApp')
        .controller('ModalSettingsCtrl',function($scope, $modalInstance, $http, baseUrlSrv) {
    /**
     * @ngdoc function
     * @name notebookWebApp.controller:ModalSettingsCtrl
     * @description
     * # ModalSettingsCtrl
     * Controller of modal window for settings
     *
     * @author ivdiaz
     *
     */

//    ModalSettingsCtrl.$inject = ['$scope', '$modalInstance', '$http', 'baseUrlSrv'];
//    function ModalSettingsCtrl($scope, $modalInstance, $http, baseUrlSrv) {

        $scope.showCrossdataProperties = false;
        $scope.showIngestionProperties = false;
        $scope.interpreterSettings = "";
        $scope.getCrossdataInterpreterSettings = function() {
            if (!$scope.showCrossdataProperties) {
                $scope.showCrossdataProperties = true;
                $http.get(baseUrlSrv.getRestApiBase() + '/interpreter/settings/crossdata').
                success(function(data, status, headers, config) {
                    var receivedData = data.body;
                    $scope.interpreterSettings = receivedData;
                }).
                error(function(data, status, headers, config) {
                    console.log('Error %o %o', status, data.message);
                });
            } else $scope.showCrossdataProperties = false;
        };
        $scope.getIngestionInterpreterSettings = function() {
            if (!$scope.showIngestionProperties) {
                $scope.showIngestionProperties = true;
                $http.get(baseUrlSrv.getRestApiBase() + '/interpreter/settings/ingestion').
                success(function(data, status, headers, config) {
                    var receivedData = data.body;
                    $scope.interpreterSettings = receivedData;
                }).
                error(function(data, status, headers, config) {
                    console.log('Error %o %o', status, data.message);
                });
            } else $scope.showIngestionProperties = false;
        };
        $scope.saveCrossdataInterpreterSettings = function() {
            console.log("saveCrossdataIntepreterSettings");
            $http.put(baseUrlSrv.getRestApiBase() + '/interpreter/settings/crossdata', $scope.interpreterSettings).
            success(function(data, status, headers, config) {
                //            console.log($scope.interpreterSettings);
                alert('Crossdata settings saved');
                console.log('Settings saved');
            }).
            error(function(data, status, headers, config) {
                alert('Error ' + status + " " + data.message);
                console.log('Error %o %o', status, data.message);
            });
        };
        $scope.saveIngestionInterpreterSettings = function() {
            console.log("saveIngestionIntepreterSettings");
            $http.put(baseUrlSrv.getRestApiBase() + '/interpreter/settings/ingestion', $scope.interpreterSettings).
            success(function(data, status, headers, config) {
                //            console.log($scope.interpreterSettings);
                alert('Ingestion settings saved');
                console.log('Settings saved');
            }).
            error(function(data, status, headers, config) {
                alert('Error ' + status + " " + data.message);
                console.log('Error %o %o', status, data.message);
            });
        };
        $scope.restartInterpreterConnection = function() {
            console.log("restartInterpreterConnection");
        };
        $scope.resetToDefault = function() {
            console.log("reset to default settings");
            $http.put(baseUrlSrv.getRestApiBase() + '/interpreter/reset', true).
            success(function(data, status, headers, config) {
                console.log('Settings restored to default');
                $scope.getInterpreterSettings();
            }).
            error(function(data, status, headers, config) {
                console.log('Error %o %o', status, data.message);
            });
        };
        $scope.ok = function() {
            $modalInstance.close();
        };
        $scope.cancel = function() {
            $modalInstance.dismiss('cancel');
        };
    });
//})()