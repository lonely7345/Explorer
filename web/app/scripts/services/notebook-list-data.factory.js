 * based on NFlabs Zeppelin originaly forked on Nov'14
 */

'use strict';

angular.module('explorerWebApp').factory('notebookListDataFactory', function() {
  var notes = {};

  notes.list = [];

  notes.setNotes = function(notesList) {
    notes.list = angular.copy(notesList);
  };

  return notes;
});
