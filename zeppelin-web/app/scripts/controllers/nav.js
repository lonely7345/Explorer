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
angular.module('zeppelinWebApp').controller('NavCtrl',['$scope', '$rootScope', '$routeParams', 'AuthenticationService',
function($scope, $rootScope, $routeParams, AuthenticationService) {
  /** Current list of notes (ids) */
  $scope.notes = [];
  $rootScope.active = "none";
  $rootScope.creationDate="none";


  $scope.activate = function (noteId, noteDate){
      if($rootScope.active !== noteId){
         $rootScope.active=noteId;
         $rootScope.creationDate= noteDate;
      }else{ $rootScope.active="none";
         $rootScope.creationDate="none";
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

    /** Remove the note and go back tot he main page */
    /** TODO(anthony): In the nearly future, go back to the main page and telle to the dude that the note have been remove */
    $scope.removeNote = function(noteId) {
//      var result = confirm('Do you want to delete this notebook? '+noteId);
  //    if (result) {
        $rootScope.$emit('sendNewEvent', {op: 'DEL_NOTE', data: {id: noteId}});
  //      $location.path('/#');
  //    }
    };

  /** Check if the note url is equal to the current note */
  $rootScope.isActive = function(noteId) {
    if ($rootScope.active === noteId) {
      return true;
    }
    return false;
  };

  $scope.logout = function(){
     AuthenticationService.ClearCredentials();
  };
}]);
