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
//    angular.module('explorerWebApp').controller('NotebookSelectorCtrl', NotebookSelectorCtrl);
    /**
     * @ngdoc function
     * @name explorerWebApp.controller:NotebookSelectorCtrl
     * @description
     * # NotebookSelectorCtrl
     * Controller of left side notebook's navigation
     *
     * @author ivdiaz
     * @author anthonycorbacho
     *
     */
//    NotebookSelectorCtrl.$inject = ['$scope', '$rootScope', '$http', 'websocketMsgSrv'];
//    function NotebookSelectorCtrl ($scope, $rootScope, $http, websocketMsgSrv){

    angular.module('explorerWebApp').controller('ExplorerSelectorCtrl',function($scope, $rootScope, $http,
    websocketMsgSrv){
        $scope.active = "none";
        $scope.activeDate = "none";
        $scope.activate = function(noteId, noteDate) {
            //        console.log("### NOTEBOOK-SELECTOR.JS -> activate " + noteId);
            if (noteId !== $scope.active) {
                //            console.log("### NOTEBOOK-SELECTOR.JS -> emit changeActiveNotebook else " + noteId);
                $rootScope.$emit('changeActiveNotebook', {
                    id: noteId,
                    date: noteDate
                });
                $scope.active = noteId;
                $scope.activeDate = noteDate;
            } else {
                //            console.log("### NOTEBOOK-SELECTOR.JS -> emit changeActiveNotebook else " + noteId);
                $rootScope.$emit('changeActiveNotebook', {
                    id: 'none',
                    date: 'none'
                });
                $scope.active = "none";
                $scope.activeDate = "none";
            }
        };
        /** Set the new menu */
        $rootScope.$on('setNoteMenu', function(event, notes) {
            $scope.notes = notes;
        });
        var loadNotes = function() {
            websocketMsgSrv.getNotebookList();
            };
        loadNotes();

        /** Create a new note */
        $scope.createNewNote = function() {
            websocketMsgSrv.createNotebook();
        };
        $scope.refreshNoteList = function() {
            websocketMsgSrv.getNotebookList();
        };
        $scope.removeNote = function(noteId) {
            websocketMsgSrv.deleteNotebook(noteId);
            $scope.active = "none";
            $scope.activeDate = "none";
            $rootScope.$emit('changeActiveNotebook', {
                id: 'none',
                date: 'none'
            });
        };
    });
//})()
