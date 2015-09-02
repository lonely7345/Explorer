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

 * based on NFlabs Zeppelin originaly forked on Nov'14
 */
//(function() {
    'use strict';
    angular
        .module('notebookWebApp')
        .service('websocketMsgSrv',function($rootScope, websocketEvents) {



//    websocketMsgSrv.$inject = ['$rootScope', 'websocketEvents'];
//    function websocketMsgSrv($rootScope, websocketEvents) {
        var service = {
            createNotebook: createNotebook,
            deleteNotebook: deleteNotebook,
            getNotebookList: getNotebookList,
            getNotebook: getNotebook,
            runParagraph: runParagraph,
            updateNotebook: updateNotebook,
            moveParagraph: moveParagraph,
            insertParagraph: insertParagraph,
            cancelParagraphRun: cancelParagraphRun,
            splitParagraph: splitParagraph,
            exportNote: exportNote,
            importNote: importNote,
            removeParagraph: removeParagraph,
            commitParagraph: commitParagraph,
            isConnected: isConnected,
            saveNote: saveNote,
            resetResults: resetResults
        };
        return service;

        function createNotebook() {
            websocketEvents.sendNewEvent({
                op: 'NEW_NOTE',
            });
        }
        function deleteNotebook(noteId) {
            websocketEvents.sendNewEvent({
                op: 'DEL_NOTE',
                data: {
                    id: noteId
                }
            });
        }
        function getNotebookList() {
            websocketEvents.sendNewEvent({
                op: 'LIST_NOTES'
            });
        }
        function getNotebook(noteId) {
            websocketEvents.sendNewEvent({
                op: 'GET_NOTE',
                data: {
                    id: noteId
                }
            });
        }
        function updateNotebook(noteId, noteName, noteConfig) {
            websocketEvents.sendNewEvent({
                op: 'NOTE_UPDATE',
                data: {
                    id: noteId,
                    name: noteName,
                    config: noteConfig
                }
            });
        }
        function moveParagraph(paragraphId, newIndex) {
            websocketEvents.sendNewEvent({
                op: 'MOVE_PARAGRAPH',
                data: {
                    id: paragraphId,
                    index: newIndex
                }
            });
        }
        function insertParagraph(newIndex) {
            websocketEvents.sendNewEvent({
                op: 'INSERT_PARAGRAPH',
                data: {
                    index: newIndex
                }
            });
        }
        function cancelParagraphRun(paragraphId) {
            websocketEvents.sendNewEvent({
                op: 'CANCEL_PARAGRAPH',
                data: {
                    id: paragraphId
                }
            });
        }
        function splitParagraph(newIndex, paragraphId, paragraphData) {
            websocketEvents.sendNewEvent({
                op: 'SPLIT_INTO_PARAGRAPHS',
                data: {
                    index: newIndex,
                    id: paragraphId,
                    paragraph: paragraphData
                }
            });
        }
        function exportNote(noteId, name) {
            websocketEvents.sendNewEvent({
                op: 'EXPORT_NOTE',
                data: {
                    id: noteId,
                    filename: name
                }
            });
        }
        function importNote(importPath) {
            websocketEvents.sendNewEvent({
                op: 'IMPORT_NOTE',
                data: {
                    path: importPath
                }
            });
        }
        function runParagraph(paragraphId, paragraphTitle, paragraphData, paragraphConfig, paragraphParams) {
            websocketEvents.sendNewEvent({
                op: 'RUN_PARAGRAPH',
                data: {
                    id: paragraphId,
                    title: paragraphTitle,
                    paragraph: paragraphData,
                    config: paragraphConfig,
                    params: paragraphParams
                }
            });
        }
        function removeParagraph(paragraphId) {
            websocketEvents.sendNewEvent({
                op: 'PARAGRAPH_REMOVE',
                data: {
                    id: paragraphId
                }
            });
        }
        function commitParagraph(paragraphId, paragraphTitle, paragraphData, paragraphConfig, paragraphParams) {
//            console.log('commitParagraph@service');
//            console.dir(paragraphConfig);
            websocketEvents.sendNewEvent({
                op: 'COMMIT_PARAGRAPH',
                data: {
                    id: paragraphId,
                    title: paragraphTitle,
                    paragraph: paragraphData,
                    config: paragraphConfig,
                    params: paragraphParams
                }
            });
        }
        function isConnected() {
            return websocketEvents.isConnected();
        }

        function saveNote(paragraphMap) {
            websocketEvents.sendNewEvent({
                op: 'SAVE_NOTE',
                data: {
                    paragraphsText: paragraphMap
                }
            });
        }
        function resetResults(paragraphMap) {
            websocketEvents.sendNewEvent({
                op: 'RESET_RESULTS'
            });
        }
    });
//})();