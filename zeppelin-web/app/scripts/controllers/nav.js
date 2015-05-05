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
 * @name zeppelinWebApp.controller:NavCtrl
 * @description
 * # NavCtrl
 * Controller of the top navigation, mainly use for the dropdown menu
 *
 * @author anthonycorbacho
 */

angular.module('zeppelinWebApp').controller('NavCtrl',['$scope', '$rootScope', '$routeParams',
'AuthenticationService','$modal',
function($scope, $rootScope, $routeParams, AuthenticationService, $modal) {
  /** Current list of notes (ids) */
  $scope.notes = [];
  $scope.active = "none";
  $scope.activeDate ="none";


  $scope.activate = function (noteId, noteDate){
    console.log("### NAV.JS -> activate "+noteId);
      if(noteId!== $scope.active ){
        $rootScope.$emit('changeActiveNotebook', {id:noteId, date:noteDate});
        $scope.active = noteId;
        $scope.activeDate=noteDate;
      } else {
        $rootScope.$emit('changeActiveNotebook', {id:'none', date:'none'});
        $scope.active = "none";
        $scope.activeDate= "none";
      }
  };

//TODO: remove this when the new menu is finished
  /** Set the new menu */
  $rootScope.$on('setNoteMenu', function(event, notes) {
      $scope.notes = notes;
  });

  var loadNotes = function() {
    $rootScope.$emit('sendNewEvent', {op: 'LIST_NOTES'});
  };
  loadNotes();

  /** Create a new note */
  $scope.createNewNote = function() {
    $rootScope.$emit('sendNewEvent', {op: 'NEW_NOTE'});
  };

  $scope.removeNote = function(noteId) {
      $rootScope.$emit('sendNewEvent', {op: 'DEL_NOTE', data: {id: noteId}});
      $scope.active = "none";
      $scope.activeDate = "none";
      $rootScope.$emit('changeActiveNotebook', {id:'none', date:'none'});
  };


  $scope.logout = function(){
     AuthenticationService.ClearCredentials();
  };

  $scope.openHelp = function(){
     var modalInstance = $modal.open({animation: true, templateUrl: '/views/modal-shortcut.html', windowClass:
     'center-modal'});
  };

}]);
