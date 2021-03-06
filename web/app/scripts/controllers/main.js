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
     * @ngdoc function
     * @name explorerWebApp.controller:MainCtrl
     * @description
     * # MainCtrl
     * Root controller of aplication
     *
     * @author anthonycorbacho
     * @author ivdiaz
     *
     */

    angular.module('explorerWebApp').controller('MainCtrl', function($scope, $rootScope, $window){
        $rootScope.connected = true;
        $scope.looknfeel = 'simple';
        $scope.lastChangedNotebookId = ''
        $scope.lastChangedNotebookDate = '';
        //    $scope.lastParagraphRunId = "";
        var init = function() {
                $scope.asIframe = (($window.location.href.indexOf('asIframe') > -1) ? true : false);
            };
        init();

        $rootScope.$on('setIframe', function(event, data) {
            if (!event.defaultPrevented) {
                $scope.asIframe = data;
                event.preventDefault();
            }
        });
        $rootScope.$on('setLookAndFeel', function(event, data) {
            if (!event.defaultPrevented && data && data !== '') {
                $scope.looknfeel = data;
                event.preventDefault();
            }
        });
        $rootScope.$on('changeActiveNotebook', function(event, data) {
            if (!($scope.lastChangedNotebookId === data.id && $scope.lastChangedNotebookDate === data.date)) {
                $scope.lastChangedNotebookId = data.id;
                $scope.lastChangedNotebookDate = data.date;
                $rootScope.$emit('rootChangeActiveNotebook', data);
            }
        });
    });

