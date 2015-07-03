/* Copyright 2014 NFLabs
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
/**
 * @ngdoc function
 * @name zeppelinWebApp.controller:MainCtrl
 * @description
 * # MainCtrl
 * Controller of the zeppelinWebApp
 *
 * @author anthonycorbacho
 */
angular.module('zeppelinWebApp').controller('MainCtrl', function($scope, WebSocket, $rootScope, $route, $routeParams, $window) {
    $scope.WebSocketWaitingList = [];
    $rootScope.connected = true;
    $scope.looknfeel = 'simple';
    $scope.lastChangedNotebookId = "";
    $scope.lastChangedNotebookDate = "";
    $scope.lastParagraphRunId="";
    var init = function() {
            $scope.asIframe = (($window.location.href.indexOf('asIframe') > -1) ? true : false);
        };
    init();
    /**
     * Web socket
     */
    WebSocket.onopen(function() {
        console.log('Websocket created');
        $rootScope.connected = true;
        if ($scope.WebSocketWaitingList.length > 0) {
            for (var o in $scope.WebSocketWaitingList) {
                WebSocket.send(JSON.stringify($scope.WebSocketWaitingList[o]));
            }
        }
    });
    WebSocket.onmessage(function(event) {
        var payload;
        if (event.data) {
            payload = angular.fromJson(event.data);
        }
        console.log('Receive << %o, %o', payload.op, payload);
        var op = payload.op;
        var data = payload.data;
        if (op === 'NOTE') {
            $rootScope.$emit('setNoteContent', data.note);
        } else if (op === 'NOTES_INFO') {
            if ($scope.asIframe) {
                //                console.log($routeParams);
                $rootScope.$emit('sendNewEvent', {
                    op: 'GET_NOTE',
                    data: {
                        id: $routeParams.noteId
                    }
                });
            } else {
                $rootScope.$emit('setNoteMenu', data.notes);
            }
        } else if (op === 'PARAGRAPH') {
            $rootScope.$emit('updateParagraph', data);
//            console.log(data.paragraph.id);
            if(data.paragraph.id === $scope.lastParagraphRunId && (data.paragraph.status ==='RUNNING' || data.paragraph
            .status ==='FINISHED')){
                $rootScope.$emit('focusParagraph', data.paragraph.id);
            }
        } else if (op === 'PROGRESS') {
            $rootScope.$emit('updateProgress', data);
        } else if (op === 'COMPLETION_LIST') {
            $rootScope.$emit('completionList', data);
        } else if (op === 'EXPORT_INFO' || op === 'IMPORT_INFO') {
            alert(data.info);
        }
    });
    WebSocket.onerror(function(event) {
        console.log('error message: ', event);
        $rootScope.connected = false;
    });
    WebSocket.onclose(function(event) {
        console.log('close message: ', event);
        $rootScope.connected = false;
    });
    /** Send info to the websocket server */
    var send = function(data) {
            if (WebSocket.currentState() !== 'OPEN') {
                $scope.WebSocketWaitingList.push(data);
            } else {
                console.log('Send >> %o, %o', data.op, data);
                WebSocket.send(JSON.stringify(data));
            }
        };
    /** get the childs event and send to the websocket server */
    $rootScope.$on('sendNewEvent', function(event, data) {
        if (!event.defaultPrevented) {
            send(data);
            event.preventDefault();
        }
    });
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
        //    console.log(event);
        //    console.log("### MAIN.JS -> $scope.lastChangedNotebookId "+ $scope.lastChangedNotebookId+ " $scope.lastChangedNotebookDate"+$scope.lastChangedNotebookDate );
        //    console.log("### MAIN.JS -> data.id "+data.id+" data.date "+data.date);
        if (!($scope.lastChangedNotebookId === data.id && $scope.lastChangedNotebookDate === data.date)) {
            $scope.lastChangedNotebookId = data.id;
            $scope.lastChangedNotebookDate = data.date;
            //      console.log("### MAIN.JS -> onChangeActiveNotebook "+data.id+" "+data.date);
            $rootScope.$broadcast('rootChangeActiveNotebook', data);
        }
    });
    $rootScope.$on('lastParagraphRunId', function(event, data) {
        console.log(data);
        $scope.lastParagraphRunId = data;
    });
});