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

//(function() {
    'use strict';
    angular
        .module('explorerWebApp')
        .controller('ModalEditorCtrl', function( $modalInstance, properties, resolvePath, $http) {
    /**
     * @ngdoc function
     * @name explorerWebApp.controller:ModalEditorCtrl
     * @description
     * # ModalEditorCtrl
     * Controller of modal window for file edition
     *
     * @author ivdiaz
     *
     */
        $scope.file = {};
        $scope.file.text = properties.data.body;
        $scope.saveEditorSettings = function() {
            var message = resolvePath + "~" + $scope.file.text;
            console.log("saveEditorSettings");
            $http.put(getRestApiBase() + '/interpreter/settings/editor', message).
            success(function(data, status, headers, config) {
                alert('Editor settings saved');
            }).
            error(function(data, status, headers, config) {
                alert('Error ' + status + " " + data.message);
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
