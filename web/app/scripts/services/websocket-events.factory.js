 * based on NFlabs Zeppelin originaly forked on Nov'14
 */
//(function() {
    'use strict';
    angular
        .module('explorerWebApp')
        .factory('websocketEvents',function($rootScope, $websocket, baseUrlSrv, $window, $routeParams) {

//    websocketEvents.$inject = ['$rootScope', '$websocket' ,'baseUrlSrv', '$window', '$routeParams'];
//    function websocketEvents($rootScope, $websocket, baseUrlSrv, $window, $routeParams) {

        var asIframe = (($window.location.href.indexOf('asIframe') > -1) ? true : false);
        var websocketCalls = {};
        websocketCalls.ws = $websocket(baseUrlSrv.getWebsocketUrl());
        websocketCalls.ws.reconnectIfNotNormalClose = true;
        websocketCalls.ws.onOpen(function() {
            console.log('Websocket created');
            $rootScope.$broadcast('setConnectedStatus', true);
            setInterval(function() {
                websocketCalls.sendNewEvent({
                    op: 'PING'
                });
            }, 60000)
        });
        websocketCalls.sendNewEvent = function(data) {
            console.log('Send >> %o, %o', data.op, data);
            websocketCalls.ws.send(JSON.stringify(data));
        };
        websocketCalls.isConnected = function() {
            return (websocketCalls.ws.socket.readyState === 1);
        };
        websocketCalls.ws.onMessage(function(event) {
            var payload;
            if (event.data) {
                payload = angular.fromJson(event.data);
            }
            console.log('Receive << %o, %o', payload.op, payload);
            var op = payload.op;
            var data = payload.data;
            if (op === 'NOTE') {
                $rootScope.$broadcast('setNoteContent', data.note);
            } else if (op === 'NOTES_INFO') {
                if(asIframe){

                    websocketCalls.sendNewEvent({
                        op: 'GET_NOTE',
                        data: {
                           id: $routeParams.noteId
                        }
                    });
                }
                $rootScope.$broadcast('setNoteMenu', data.notes);
            } else if (op === 'PARAGRAPH') {
                $rootScope.$broadcast('updateParagraph', data);
            } else if (op === 'PROGRESS') {
                $rootScope.$broadcast('updateProgress', data);
            } else if (op === 'COMPLETION_LIST') {
                $rootScope.$broadcast('completionList', data);
            } else if (op === 'EXPORT_NOTE' || op === 'IMPORT_NOTE') {
                alert(data.info);
            }
        });
        websocketCalls.ws.onError(function(event) {
            console.log('error message: ', event);
            $rootScope.$broadcast('setConnectedStatus', false);
        });
        websocketCalls.ws.onClose(function(event) {
            console.log('close message: ', event);
            $rootScope.$broadcast('setConnectedStatus', false);
        });
        return websocketCalls;
    });
//})();
