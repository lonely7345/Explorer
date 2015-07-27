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
 */'use strict';
/**
 * @ngdoc function
 * @name zeppelinWebApp.controller:NavCtrl
 * @description
 * # NavCtrl
 * Controller of the top navigation, mainly use for the dropdown menu
 *
 * @author anthonycorbacho
 */
angular.module('zeppelinWebApp').controller('NavCtrl', ['$scope', '$rootScope', '$routeParams', 'AuthenticationService', '$modal', '$http', function($scope, $rootScope, $routeParams, AuthenticationService, $modal, $http) {
    /** Current list of notes (ids) */
    $scope.notes = [];
    $scope.active = "none";
    $scope.activeDate = "none";
    $scope.activate = function(noteId, noteDate) {
        //        console.log("### NAV.JS -> activate " + noteId);
        if (noteId !== $scope.active) {
            //            console.log("### NAV.JS -> emit changeActiveNotebook else " + noteId);
            $rootScope.$emit('changeActiveNotebook', {
                id: noteId,
                date: noteDate
            });
            $scope.active = noteId;
            $scope.activeDate = noteDate;
        } else {
            //            console.log("### NAV.JS -> emit changeActiveNotebook else " + noteId);
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
    $rootScope.$on('openPropertiesEditor', function(event, path) {
        $scope.openPropertiesEditor(path);
    });
    var loadNotes = function() {
            $rootScope.$emit('sendNewEvent', {
                op: 'LIST_NOTES'
            });
        };
    loadNotes();
    /** Create a new note */
    $scope.createNewNote = function() {
        $rootScope.$emit('sendNewEvent', {
            op: 'NEW_NOTE'
        });
    };
    $scope.refreshNoteList = function() {
        $rootScope.$emit('sendNewEvent', {
            op: 'LIST_NOTES'
        });
    };
    $scope.removeNote = function(noteId) {
        $rootScope.$emit('sendNewEvent', {
            op: 'DEL_NOTE',
            data: {
                id: noteId
            }
        });
        $scope.active = "none";
        $scope.activeDate = "none";
        $rootScope.$emit('changeActiveNotebook', {
            id: 'none',
            date: 'none'
        });
    };
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
            controller: 'modalSettingsController',
            templateUrl: '/views/modal-settings.html',
            windowClass: 'center-modal'
        });
    };
    $scope.openPropertiesEditor = function(path) {
        console.log(path);
        var modalInstance = $modal.open({
            animation: true,
            controller: 'modalEditController',
            templateUrl: '/views/modal-editor.html',
            windowClass: 'center-modal',
            resolve: {
                properties: function() {
                    return $http.get(getRestApiBase() + '/interpreter/settings/editor?path=' + path).
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
}]);
angular.module('zeppelinWebApp').controller('modalEditController', function($scope, $modalInstance, properties, resolvePath, $http) {
    $scope.file = {};
    $scope.file.text = properties.data.body;
    $scope.saveEditorSettings = function() {
        var message = resolvePath + "~" + $scope.file.text;
        console.log("saveEditorSettings");
        $http.put(getRestApiBase() + '/interpreter/settings/editor', message).
        success(function(data, status, headers, config) {
            alert('Editor settings saved');
            console.log('Settings saved');
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
angular.module('zeppelinWebApp').controller('modalSettingsController', function($scope, $modalInstance, $http) {
    $scope.showCrossdataProperties = false;
    $scope.showIngestionProperties = false;
    $scope.interpreterSettings = "";
    $scope.getCrossdataInterpreterSettings = function() {
        if (!$scope.showCrossdataProperties) {
            $scope.showCrossdataProperties = true;
            $http.get(getRestApiBase() + '/interpreter/settings/crossdata').
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
            $http.get(getRestApiBase() + '/interpreter/settings/ingestion').
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
        $http.put(getRestApiBase() + '/interpreter/settings/crossdata', $scope.interpreterSettings).
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
        $http.put(getRestApiBase() + '/interpreter/settings/ingestion', $scope.interpreterSettings).
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
        $http.put(getRestApiBase() + '/interpreter/reset', true).
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