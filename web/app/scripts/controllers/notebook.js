/* global confirm:false, alert:false */
/* jshint loopfunc: true */

//(function() {
    'use strict';
//    angular.module('explorerWebApp').controller('NotebookCtrl', NotebookCtrl);
    /**
     * @ngdoc function
     * @name explorerWebApp.controller:NotebookCtrl
     * @description
     * # NotebookCtrl
     * Controller of notes, manage the note (update)
     *
     * @author anthonycorbacho
     * @author ivdiaz
     *
     */
//    NotebookCtrl.$inject = ['$scope', '$http', '$route', '$routeParams', '$location', '$rootScope', 'websocketMsgSrv',
//    'baseUrlSrv', '$timeout'];
//    function NotebookCtrl($scope, $http, $route, $routeParams, $location, $rootScope, websocketMsgSrv, baseUrlSrv,
//    $timeout) {

    angular.module('explorerWebApp').controller('NotebookCtrl',function ($scope, $http, $route, $routeParams,
    $location,$rootScope, websocketMsgSrv, baseUrlSrv,$timeout){

        $scope.note = null;
        $scope.settings = {};
        $scope.settings.interpreter = 'crossdata';
        $scope.settings.interpreters = ['crossdata', 'ingestion', 'spark', 'spark-sql', 'markdown', 'streaming', 'shell'];
        $scope.active = 'none';
        $scope.showEditor = false;
        $scope.editorToggled = true;
        $scope.tableToggled = true;
        $scope.looknfeelOption = ['default', 'simple'];
        $scope.exportFilename = 'mynotebook';
        $scope.importPath = 'full/path/to/file';
        $scope.cronOption = [{
            name: 'None',
            value: undefined
        }, {
            name: '1m',
            value: '0 0/1 * * * ?'
        }, {
            name: '5m',
            value: '0 0/5 * * * ?'
        }, {
            name: '1h',
            value: '0 0 0/1 * * ?'
        }, {
            name: '3h',
            value: '0 0 0/3 * * ?'
        }, {
            name: '6h',
            value: '0 0 0/6 * * ?'
        }, {
            name: '12h',
            value: '0 0 0/12 * * ?'
        }, {
            name: '1d',
            value: '0 0 0 * * ?'
        }];
        $scope.getCronOptionNameFromValue = function(value) {
            if (!value) {
                return '';
            }
            for (var o in $scope.cronOption) {
                if ($scope.cronOption[o].value === value) {
                    return $scope.cronOption[o].name;
                }
            }
            return value;
        };
        /** Init the new controller */
        var initNotebook = function(notebookId) {
                websocketMsgSrv.getNotebook(notebookId);
            };
        $rootScope.$on('rootChangeActiveNotebook', function(event, data) {
            //        console.log('onRootChangeActiveNotebook ' + data.id+ ' active '+$scope.active);
            if (data.id !== $scope.active) {
                $scope.active = data.id;
                if (data.id !== 'none') {
                    initNotebook(data.id);
                }
            }
        });
        $scope.runNote = function() {
            var result = confirm('Run all paragraphs?');
            if (result) {
                _.forEach($scope.note.paragraphs, function(n, key) {
                        angular.element('#' + n.id + '_paragraphColumn_main').scope().runParagraph(n.text);
                });
            }
        };
        $scope.toggleAllEditor = function() {
            if ($scope.editorToggled) {
                $rootScope.$broadcast('closeEditor');
            } else {
                $rootScope.$broadcast('openEditor');
            }
            $scope.editorToggled = !$scope.editorToggled;
        };
        $scope.showAllEditor = function() {
            $rootScope.$broadcast('openEditor');
        };
        $scope.hideAllEditor = function() {
            $rootScope.$broadcast('closeEditor');
        };
        $scope.toggleAllTable = function() {
            if ($scope.tableToggled) {
                $rootScope.$broadcast('closeTable');
            } else {
                $rootScope.$broadcast('openTable');
            }
            $scope.tableToggled = !$scope.tableToggled;
        };
        $scope.showAllTable = function() {
            $rootScope.$broadcast('openTable');
        };
        $scope.hideAllTable = function() {
            $rootScope.$broadcast('closeTable');
        };
        $scope.exportNote = function(noteId) {
            console.log('note id = ' + noteId + ' filename ' + $scope.exportFilename);
            websocketMsgSrv.exportNote($scope.note.id, $scope.exportFilename);
        };
        $scope.importNote = function() {
            console.log(' filename ' + $scope.importPath);
            websocketMsgSrv.importNote($scope.importPath);
        };
        $scope.fileNameChanged = function(element) {
            console.log(element.files);
        };
        $scope.isNoteRunning = function() {
            var running = false;
            if (!$scope.note) {
                return false;
            }
            for (var i = 0; i < $scope.note.paragraphs.length; i++) {
                if ($scope.note.paragraphs[i].status === 'PENDING' || $scope.note.paragraphs[i].status === 'REFRESH_RESULT' || $scope.note.paragraphs[i].status === 'RUNNING') {
                    running = true;
                    break;
                }
            }
            return running;
        };
        $scope.setLookAndFeel = function(looknfeel) {
            $scope.note.config.looknfeel = looknfeel;
            $scope.setConfig();
            $rootScope.$broadcast('setLookAndFeel', $scope.note.config.looknfeel);
        };
        /** Set cron expression for this note **/
        $scope.setCronScheduler = function(cronExpr) {
            $scope.note.config.cron = cronExpr;
            $scope.setConfig();
        };
        /** Update note config **/
        $scope.setConfig = function(config) {
           if(config) {
                 $scope.note.config = config;
               }
               websocketMsgSrv.updateNotebook($scope.note.id, $scope.note.name, $scope.note.config);
        };
        /** Update the note name */
        $scope.sendNewName = function() {
             $scope.showEditor = false;
                if ($scope.note.name) {
                  websocketMsgSrv.updateNotebook($scope.note.id, $scope.note.name, $scope.note.config);
                }
        };
        /** update the current note */
        $rootScope.$on('setNoteContent', function(event, note) {
            //        console.log('### NOTEBOOK.JS -> on setNoteContent ' + $routeParams.asIframe);
            $scope.paragraphUrl = $routeParams.paragraphId;
            $scope.asIframe = $routeParams.asIframe;
            if ($scope.paragraphUrl) {
                //            console.log('### NOTEBOOK.JS -> on setNoteContent ');
                //            console.log(note);
                note = cleanParagraphExcept($scope.paragraphUrl, note);
                console.log('### NOTEBOOK.JS -> setIframe' + $scope.asIframe);
                $rootScope.$broadcast('setIframe', $scope.asIframe);
            }
            $scope.note = note;
            if ($scope.note === null) {
//                console.log($scope.note);
                initialize();
            } else {
                updateNote(note);
            }
            /** set look n feel */
            $rootScope.$broadcast('setLookAndFeel', note.config.looknfeel);
        });
        var initialize = function() {
                if (!$scope.note.config.looknfeel) {
                    $scope.note.config.looknfeel = 'simple';
                }
            };
        var cleanParagraphExcept = function(paragraphId, note) {
                var noteCopy = {};
//                console.log(note.id);
                noteCopy.id = note.id;
                noteCopy.name = note.name;
                noteCopy.config = note.config;
                noteCopy.info = note.info;
                noteCopy.paragraphs = [];
                for (var i = 0; i < note.paragraphs.length; i++) {
                    if (note.paragraphs[i].hasOwnProperty('id') && note.paragraphs[i].id === paragraphId) {
                        noteCopy.paragraphs[0] = note.paragraphs[i];
                        if (!noteCopy.paragraphs[0].config) {
                            noteCopy.paragraphs[0].config = {};
                        }
                        noteCopy.paragraphs[0].config.editorHide = true;
                        noteCopy.paragraphs[0].config.tableHide = false;
                        break;
                    }
                }
                return noteCopy;
            };
        $rootScope.$on('moveParagraphUp', function(event, paragraphId) {
            var newIndex = -1;
            for (var i = 0; i < $scope.note.paragraphs.length; i++) {
                if ($scope.note.paragraphs[i].id === paragraphId) {
                    newIndex = i - 1;
                    break;
                }
            }
            if (newIndex < 0 || newIndex >= $scope.note.paragraphs.length) {
                return;
            }
            websocketMsgSrv.moveParagraph(paragraphId, newIndex);
        });
        $rootScope.$on('split', function(event, data) {
            var newIndex = -1;
            var paragraphId = data.id;
            var paragraphData = data.paragraph;
            for (var i = 0; i < $scope.note.paragraphs.length; i++) {
                if ($scope.note.paragraphs[i].id === paragraphId) {
                    newIndex = i + 1;
                    break;
                }
            }
            //    if (newIndex === $scope.note.paragraphs.length) {
            //      alert('Cannot insert after the last paragraph.');
            //      return;
            //    }
            if (newIndex < 0 || newIndex > $scope.note.paragraphs.length) {
                return;
            }
            websocketMsgSrv.splitParagraph(newIndex, paragraphId, paragraphData);

        });
        // create new paragraph on current position
        $rootScope.$on('insertParagraph', function(event, paragraphId) {
            var newIndex = -1;
            for (var i = 0; i < $scope.note.paragraphs.length; i++) {
                if ($scope.note.paragraphs[i].id === paragraphId) {
                    newIndex = i + 1;
                    break;
                }
            }
            if (newIndex < 0 || newIndex > $scope.note.paragraphs.length) {
                return;
            }
            websocketMsgSrv.insertParagraph(newIndex);
        });
        $rootScope.$on('moveParagraphDown', function(event, paragraphId) {
            var newIndex = -1;
            for (var i = 0; i < $scope.note.paragraphs.length; i++) {
                if ($scope.note.paragraphs[i].id === paragraphId) {
                    newIndex = i + 1;
                    break;
                }
            }
            if (newIndex < 0 || newIndex >= $scope.note.paragraphs.length) {
                return;
            }
            websocketMsgSrv.moveParagraph(paragraphId, newIndex);
        });
        $rootScope.$on('moveFocusToPreviousParagraph', function(event, currentParagraphId) {
            var focus = false;
            for (var i = $scope.note.paragraphs.length - 1; i >= 0; i--) {
                if (focus === false) {
                    if ($scope.note.paragraphs[i].id === currentParagraphId) {
                        focus = true;
                        continue;
                    }
                } else {
                    var p = $scope.note.paragraphs[i];
                    if (!p.config.hide && !p.config.editorHide && !p.config.tableHide) {
                        $rootScope.$broadcast('focusParagraph', $scope.note.paragraphs[i].id);
                        break;
                    }
                }
            }
        });
        $rootScope.$on('moveFocusToNextParagraph', function(event, currentParagraphId) {
            var focus = false;
            for (var i = 0; i < $scope.note.paragraphs.length; i++) {
                if (focus === false) {
                    if ($scope.note.paragraphs[i].id === currentParagraphId) {
                        focus = true;
                        continue;
                    }
                } else {
                    var p = $scope.note.paragraphs[i];
                    if (!p.config.hide && !p.config.editorHide && !p.config.tableHide) {
                        $rootScope.$broadcast('focusParagraph', $scope.note.paragraphs[i].id);
                        break;
                    }
                }
            }
        });
        $scope.removeNotebookResults = function(){
            console.log('removeNotebookResults');
            websocketMsgSrv.resetResults();
        };
        $scope.saveNotebookStatus = function(){
            var paragraphMap ={};
                 _.forEach($scope.note.paragraphs, function(n, key) {
                        paragraphMap[n.id]=n.text;
                    });
                 websocketMsgSrv.saveNote(paragraphMap);
                };
        var updateNote = function(note) {
                /** update Note name */
                //    console.log('Note updated --- ' + JSON.stringify(note));
                if (note.name !== $scope.note.name) {
                    console.log('change note name: %o to %o', $scope.note.name, note.name);
                    $scope.note.name = note.name;
                }
                $scope.note.config = note.config;
                $scope.note.info = note.info;
                var newParagraphIds = note.paragraphs.map(function(x) {
                    return x.id;
                });
                var oldParagraphIds = $scope.note.paragraphs.map(function(x) {
                    return x.id;
                });
                var numNewParagraphs = newParagraphIds.length;
                var numOldParagraphs = oldParagraphIds.length;
                /** add a new paragraph */
                if (numNewParagraphs > numOldParagraphs) {
                    for (var index in newParagraphIds) {
                        if (oldParagraphIds[index] !== newParagraphIds[index]) {
                            $scope.note.paragraphs.splice(index, 0, note.paragraphs[index]);
                            break;
                        }
                    }
                }
                /** update or move paragraph */
                if (numNewParagraphs === numOldParagraphs) {
                    for (var idx in newParagraphIds) {
                        var newEntry = note.paragraphs[idx];
                        if (oldParagraphIds[idx] === newParagraphIds[idx]) {
                            $rootScope.$broadcast('updateParagraph', {
                                paragraph: newEntry
                            });
                        } else {
                            // move paragraph
                            var oldIdx = oldParagraphIds.indexOf(newParagraphIds[idx]);
                            $scope.note.paragraphs.splice(oldIdx, 1);
                            $scope.note.paragraphs.splice(idx, 0, newEntry);
                            // rebuild id list since paragraph has moved.
                            oldParagraphIds = $scope.note.paragraphs.map(function(x) {
                                return x.id;
                            });
                        }
                    }
                }
                /** remove paragraph */
                if (numNewParagraphs < numOldParagraphs) {
                    for (var oldidx in oldParagraphIds) {
                        if (oldParagraphIds[oldidx] !== newParagraphIds[oldidx]) {
                            $scope.note.paragraphs.splice(oldidx, 1);
                            break;
                        }
                    }
                }
            };
        $scope.changeDefaultInterpreter = function(interpreter) {
            $scope.settings.interpreter = interpreter;
            $rootScope.$broadcast('changeDefaultInterpreter', interpreter);
        };
    });
//})()
