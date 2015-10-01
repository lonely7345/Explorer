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
'use strict';
/**
 * @ngdoc overview
 * @name notebookWebApp
 * @description
 * # notebookWebApp
 *
 * Main module of the application.
 *
 * @author anthonycorbacho & adapted by idiaz
 */
angular.module('Authentication', []);
var app = angular.module('notebookWebApp', [
    'Authentication',
    'ngAnimate',
    'ngCookies',
    'ngRoute',
    'ngSanitize',
    'angular-websocket',
    'ui.ace',
    'ui.bootstrap',
    'ngTouch',
    'ngDragDrop'
  ]).config([
    '$routeProvider',
    function ($routeProvider) {
      $routeProvider.when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      }).when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl',
        hideMenus: true
      }).when('/notebook/:noteId/paragraph/:paragraphId?', { templateUrl: 'views/main.html' }).otherwise({ redirectTo: '/login' });
    }
  ]).run([
    '$rootScope',
    '$location',
    '$cookieStore',
    '$http',
    function ($rootScope, $location, $cookieStore, $http) {
      // keep user logged in after page refresh
      $rootScope.globals = $cookieStore.get('globals') || {};
      if ($rootScope.globals.currentUser) {
        $http.defaults.headers.common['Authorization'] = 'Basic ' + $rootScope.globals.currentUser.authdata;  // jshint ignore:line
      }
      $rootScope.$on('$locationChangeStart', function (event, next, current) {
        // redirect to login page if not logged in
        if ($location.path() !== '/login' && !$rootScope.globals.currentUser) {
          $location.path('/login');
        }
      });
    }
  ]);
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
//    angular.module('notebookWebApp').controller('MainCtrl', MainCtrl);
/**
     * @ngdoc function
     * @name notebookWebApp.controller:MainCtrl
     * @description
     * # MainCtrl
     * Root controller of aplication
     *
     * @author anthonycorbacho
     * @author ivdiaz
     *
     */
//    MainCtrl.$inject = ['$scope', '$rootScope', '$window'];
//    function MainCtrl($scope, $rootScope, $window) {
angular.module('notebookWebApp').controller('MainCtrl', [
  '$scope',
  '$rootScope',
  '$window',
  function ($scope, $rootScope, $window) {
    $rootScope.connected = true;
    $scope.looknfeel = 'simple';
    $scope.lastChangedNotebookId = '';
    $scope.lastChangedNotebookDate = '';
    //    $scope.lastParagraphRunId = "";
    var init = function () {
      $scope.asIframe = $window.location.href.indexOf('asIframe') > -1 ? true : false;
    };
    init();
    $rootScope.$on('setIframe', function (event, data) {
      if (!event.defaultPrevented) {
        $scope.asIframe = data;
        event.preventDefault();
      }
    });
    $rootScope.$on('setLookAndFeel', function (event, data) {
      if (!event.defaultPrevented && data && data !== '') {
        //            console.log("MAIN.JS -setLookAndFeel - "+ data);
        $scope.looknfeel = data;
        event.preventDefault();
      }
    });
    $rootScope.$on('changeActiveNotebook', function (event, data) {
      //    console.log(event);
      //    console.log("### MAIN.JS -> $scope.lastChangedNotebookId "+ $scope.lastChangedNotebookId+ " $scope.lastChangedNotebookDate"+$scope.lastChangedNotebookDate );
      //    console.log("### MAIN.JS -> data.id "+data.id+" data.date "+data.date);
      if (!($scope.lastChangedNotebookId === data.id && $scope.lastChangedNotebookDate === data.date)) {
        $scope.lastChangedNotebookId = data.id;
        $scope.lastChangedNotebookDate = data.date;
        //      console.log("### MAIN.JS -> onChangeActiveNotebook "+data.id+" "+data.date);
        $rootScope.$emit('rootChangeActiveNotebook', data);
      }
    });
  }
]);
//})()
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
'use strict';
/**
     * @ngdoc function
     * @name notebookWebApp.controller:TopBarCtrl
     * @description
     * # TopBarCtrl
     * Controller of the top navigation, mainly use for the dropdown menu
     *
     * @author anthonycorbacho
     * @author ivdiaz
     *
     */
angular.module('notebookWebApp').controller('TopBarCtrl', [
  '$scope',
  '$rootScope',
  'AuthenticationService',
  '$modal',
  '$http',
  'baseUrlSrv',
  function ($scope, $rootScope, AuthenticationService, $modal, $http, baseUrlSrv) {
    $scope.logout = function () {
      AuthenticationService.ClearCredentials();
    };
    $scope.openHelp = function () {
      var modalInstance = $modal.open({
          animation: true,
          templateUrl: '/views/modal-shortcut.html',
          windowClass: 'center-modal'
        });
    };
    $scope.openSettings = function () {
      var modalInstance = $modal.open({
          animation: true,
          templateUrl: '/views/modal-settings.html',
          windowClass: 'center-modal'
        });
    };
    $rootScope.$on('openPropertiesEditor', function (event, path) {
      $scope.openPropertiesEditor(path);
    });
    $scope.openPropertiesEditor = function (path) {
      console.log(path);
      var modalInstance = $modal.open({
          animation: true,
          controller: 'ModalEditCtrl',
          templateUrl: '/views/modal-editor.html',
          windowClass: 'center-modal',
          resolve: {
            properties: function () {
              return $http.get(baseUrlSrv.getRestApiBase() + '/interpreter/settings/editor?path=' + path).success(function (data, status, headers, config) {
                console.log('success');
              }).error(function (data, status, headers, config) {
                console.log('Error %o %o', status, data.message);
              });
            },
            resolvePath: function () {
              return path;
            }
          }
        });
    };
  }
]);
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
angular.module('notebookWebApp').controller('ModalEditorCtrl', [
  '$modalInstance',
  'properties',
  'resolvePath',
  '$http',
  function ($modalInstance, properties, resolvePath, $http) {
    /**
     * @ngdoc function
     * @name notebookWebApp.controller:ModalEditorCtrl
     * @description
     * # ModalEditorCtrl
     * Controller of modal window for file edition
     *
     * @author ivdiaz
     *
     */
    $scope.file = {};
    $scope.file.text = properties.data.body;
    $scope.saveEditorSettings = function () {
      var message = resolvePath + '~' + $scope.file.text;
      console.log('saveEditorSettings');
      $http.put(getRestApiBase() + '/interpreter/settings/editor', message).success(function (data, status, headers, config) {
        alert('Editor settings saved');
      }).error(function (data, status, headers, config) {
        alert('Error ' + status + ' ' + data.message);
        console.log('Error %o %o', status, data.message);
      });
    };
    $scope.ok = function () {
      $modalInstance.close();
    };
    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  }
]);
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
'use strict';
angular.module('notebookWebApp').controller('ModalSettingsCtrl', [
  '$scope',
  '$modal',
  '$http',
  'baseUrlSrv',
  function ($scope, $modal, $http, baseUrlSrv) {
    /**
     * @ngdoc function
     * @name notebookWebApp.controller:ModalSettingsCtrl
     * @description
     * # ModalSettingsCtrl
     * Controller of modal window for settings
     *
     * @author ivdiaz
     *
     */
    $scope.showCrossdataProperties = false;
    $scope.showIngestionProperties = false;
    $scope.showCassandraProperties = false;
    $scope.interpreterSettings = '';
    return {
      getCrossdataInterpreterSettings: function () {
        if (!$scope.showCrossdataProperties) {
          $scope.showCrossdataProperties = true;
          $http.get(baseUrlSrv.getRestApiBase() + '/interpreter/settings/list?nameFile=driver-application').success(function (data) {
            var receivedData = data.body;
            $scope.interpreterSettingsCrossdata = receivedData;
          }).error(function (data, status) {
            console.log('Error %o %o', status, data.message);
          });
        } else {
          $scope.showCrossdataProperties = false;
        }
      },
      saveCrossdataInterpreterSettings: function () {
        $http.put(baseUrlSrv.getRestApiBase() + '/interpreter/settings/crossdata' + $scope.interpreterSettingsCrossdata).success(function (data) {
          alert('Crossdata settings saved');
        }).error(function (data, status) {
          alert('Error ' + status + ' ' + data.message);
        });
      },
      getIngestionInterpreterSettings: function () {
        if (!$scope.showIngestionProperties) {
          $scope.showIngestionProperties = true;
          $http.get(baseUrlSrv.getRestApiBase() + '/interpreter/settings/list?nameFile=ingestion').success(function (data) {
            var receivedData = data.body;
            $scope.interpreterSettingsIngestion = receivedData;
          }).error(function (data, status) {
            console.log('Error %o %o', status, data.message);
          });
        } else
          $scope.showIngestionProperties = false;
      },
      getCassandraInterpreterSettings: function () {
        if (!$scope.showCassandraProperties) {
          $scope.showCassandraProperties = true;
          $http.get(baseUrlSrv.getRestApiBase() + '/interpreter/settings/list?nameFile=cassandra').success(function (data) {
            var receivedData = data.body;
            $scope.interpreterSettingsCassandra = receivedData;
          }).error(function (data, status) {
            console.log('Error %o %o', status, data.message);
          });
        } else {
          $scope.showCassandraProperties = false;
        }
      },
      saveIngestionInterpreterSettings: function () {
        console.log('saveIngestionIntepreterSettings');
        $http.put(baseUrlSrv.getRestApiBase() + '/interpreter/settings/ingestion', $scope.interpreterSettingsIngestion).success(function () {
          alert('Ingestion settings saved');
          console.log('Settings saved');
        }).error(function (data, status) {
          alert('Error ' + status + ' ' + data.message);
          console.log('Error %o %o', status, data.message);
        });
      },
      restartInterpreterConnection: function () {
        console.log('restartInterpreterConnection');
      },
      resetToDefault: function () {
        console.log('reset to default settings');
        $http.put(baseUrlSrv.getRestApiBase() + '/interpreter/reset', true).success(function () {
          console.log('Settings restored to default');
          $scope.getInterpreterSettings();
        }).error(function (data, status) {
          console.log('Error %o %o', status, data.message);
        });
      },
      ok: function () {
        $modal.close();
      },
      cancel: function () {
        $modal.dismiss('cancel');
      },
      saveCassandraInterpreterSettings: function () {
        $http.put(baseUrlSrv.getRestApiBase() + '/interpreter/settings/cassandra', $scope.interpreterSettingsCassandra).success(function (data) {
          alert('cassandra settings saved');
        }).error(function (data, status) {
          alert('Error ' + status + ' ' + data.message);
        });
      }
    };
  }
]);
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
//    angular.module('notebookWebApp').controller('NotebookSelectorCtrl', NotebookSelectorCtrl);
/**
     * @ngdoc function
     * @name notebookWebApp.controller:NotebookSelectorCtrl
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
angular.module('notebookWebApp').controller('NotebookSelectorCtrl', [
  '$scope',
  '$rootScope',
  '$http',
  'websocketMsgSrv',
  function ($scope, $rootScope, $http, websocketMsgSrv) {
    $scope.active = 'none';
    $scope.activeDate = 'none';
    $scope.activate = function (noteId, noteDate) {
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
        $scope.active = 'none';
        $scope.activeDate = 'none';
      }
    };
    /** Set the new menu */
    $rootScope.$on('setNoteMenu', function (event, notes) {
      $scope.notes = notes;
    });
    var loadNotes = function () {
      websocketMsgSrv.getNotebookList();
    };
    loadNotes();
    /** Create a new note */
    $scope.createNewNote = function () {
      websocketMsgSrv.createNotebook();
    };
    $scope.refreshNoteList = function () {
      websocketMsgSrv.getNotebookList();
    };
    $scope.removeNote = function (noteId) {
      websocketMsgSrv.deleteNotebook(noteId);
      $scope.active = 'none';
      $scope.activeDate = 'none';
      $rootScope.$emit('changeActiveNotebook', {
        id: 'none',
        date: 'none'
      });
    };
  }
]);
//})()
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
//    angular.module('Authentication').controller('LoginCtrl', LoginCtrl);
/**
         * @ngdoc function
         * @name Authentication.controller:LoginCtrl
         * @description
         * # LoginCtrl
         * Controller of the login screen
         *
         * @author ivdiaz
         *
         */
//    LoginCtrl.$inject = ['$scope', '$rootScope', '$location', 'AuthenticationService'];
//    function LoginCtrl($scope, $rootScope, $location, AuthenticationService) {
angular.module('Authentication').controller('LoginCtrl', [
  '$scope',
  '$rootScope',
  '$location',
  'AuthenticationService',
  function ($scope, $rootScope, $location, AuthenticationService) {
    // reset login status
    AuthenticationService.ClearCredentials();
    $scope.login = function () {
      $scope.dataLoading = true;
      AuthenticationService.Login($scope.username, $scope.password, function (response) {
        if (response.success) {
          AuthenticationService.SetCredentials($scope.username, $scope.password);
          $location.path('/');
        } else {
          $scope.error = response.message;
          $scope.dataLoading = false;
        }
      });
    };
  }
]);
//})()
/* global confirm:false, alert:false */
/* jshint loopfunc: true */
/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License'); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.

 * based on NFlabs Zeppelin originaly forked on Nov'14
 */
//(function() {
'use strict';
//    angular.module('notebookWebApp').controller('NotebookCtrl', NotebookCtrl);
/**
     * @ngdoc function
     * @name notebookWebApp.controller:NotebookCtrl
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
angular.module('notebookWebApp').controller('NotebookCtrl', [
  '$scope',
  '$http',
  '$route',
  '$routeParams',
  '$location',
  '$rootScope',
  'websocketMsgSrv',
  'baseUrlSrv',
  '$timeout',
  function ($scope, $http, $route, $routeParams, $location, $rootScope, websocketMsgSrv, baseUrlSrv, $timeout) {
    $scope.note = null;
    $scope.settings = {};
    $scope.settings.interpreter = 'crossdata';
    $scope.settings.interpreters = [
      'crossdata',
      'ingestion',
      'spark',
      'spark-sql',
      'markdown',
      'streaming',
      'shell'
    ];
    $scope.active = 'none';
    $scope.showEditor = false;
    $scope.editorToggled = true;
    $scope.tableToggled = true;
    $scope.looknfeelOption = [
      'default',
      'simple'
    ];
    $scope.exportFilename = 'mynotebook';
    $scope.importPath = 'full/path/to/file';
    $scope.cronOption = [
      {
        name: 'None',
        value: undefined
      },
      {
        name: '1m',
        value: '0 0/1 * * * ?'
      },
      {
        name: '5m',
        value: '0 0/5 * * * ?'
      },
      {
        name: '1h',
        value: '0 0 0/1 * * ?'
      },
      {
        name: '3h',
        value: '0 0 0/3 * * ?'
      },
      {
        name: '6h',
        value: '0 0 0/6 * * ?'
      },
      {
        name: '12h',
        value: '0 0 0/12 * * ?'
      },
      {
        name: '1d',
        value: '0 0 0 * * ?'
      }
    ];
    $scope.getCronOptionNameFromValue = function (value) {
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
    var initNotebook = function (notebookId) {
      websocketMsgSrv.getNotebook(notebookId);
    };
    $rootScope.$on('rootChangeActiveNotebook', function (event, data) {
      //        console.log('onRootChangeActiveNotebook ' + data.id+ ' active '+$scope.active);
      if (data.id !== $scope.active) {
        $scope.active = data.id;
        if (data.id !== 'none') {
          initNotebook(data.id);
        }
      }
    });
    $scope.runNote = function () {
      var result = confirm('Run all paragraphs?');
      if (result) {
        _.forEach($scope.note.paragraphs, function (n, key) {
          angular.element('#' + n.id + '_paragraphColumn_main').scope().runParagraph(n.text);
        });
      }
    };
    $scope.toggleAllEditor = function () {
      if ($scope.editorToggled) {
        $rootScope.$broadcast('closeEditor');
      } else {
        $rootScope.$broadcast('openEditor');
      }
      $scope.editorToggled = !$scope.editorToggled;
    };
    $scope.showAllEditor = function () {
      $rootScope.$broadcast('openEditor');
    };
    $scope.hideAllEditor = function () {
      $rootScope.$broadcast('closeEditor');
    };
    $scope.toggleAllTable = function () {
      if ($scope.tableToggled) {
        $rootScope.$broadcast('closeTable');
      } else {
        $rootScope.$broadcast('openTable');
      }
      $scope.tableToggled = !$scope.tableToggled;
    };
    $scope.showAllTable = function () {
      $rootScope.$broadcast('openTable');
    };
    $scope.hideAllTable = function () {
      $rootScope.$broadcast('closeTable');
    };
    $scope.exportNote = function (noteId) {
      console.log('note id = ' + noteId + ' filename ' + $scope.exportFilename);
      websocketMsgSrv.exportNote($scope.note.id, $scope.exportFilename);
    };
    $scope.importNote = function () {
      console.log(' filename ' + $scope.importPath);
      websocketMsgSrv.importNote($scope.importPath);
    };
    $scope.fileNameChanged = function (element) {
      console.log(element.files);
    };
    $scope.isNoteRunning = function () {
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
    $scope.setLookAndFeel = function (looknfeel) {
      $scope.note.config.looknfeel = looknfeel;
      $scope.setConfig();
      $rootScope.$broadcast('setLookAndFeel', $scope.note.config.looknfeel);
    };
    /** Set cron expression for this note **/
    $scope.setCronScheduler = function (cronExpr) {
      $scope.note.config.cron = cronExpr;
      $scope.setConfig();
    };
    /** Update note config **/
    $scope.setConfig = function (config) {
      if (config) {
        $scope.note.config = config;
      }
      websocketMsgSrv.updateNotebook($scope.note.id, $scope.note.name, $scope.note.config);
    };
    /** Update the note name */
    $scope.sendNewName = function () {
      $scope.showEditor = false;
      if ($scope.note.name) {
        websocketMsgSrv.updateNotebook($scope.note.id, $scope.note.name, $scope.note.config);
      }
    };
    /** update the current note */
    $rootScope.$on('setNoteContent', function (event, note) {
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
    var initialize = function () {
      if (!$scope.note.config.looknfeel) {
        $scope.note.config.looknfeel = 'simple';
      }
    };
    var cleanParagraphExcept = function (paragraphId, note) {
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
    $rootScope.$on('moveParagraphUp', function (event, paragraphId) {
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
    $rootScope.$on('split', function (event, data) {
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
    $rootScope.$on('insertParagraph', function (event, paragraphId) {
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
    $rootScope.$on('moveParagraphDown', function (event, paragraphId) {
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
    $rootScope.$on('moveFocusToPreviousParagraph', function (event, currentParagraphId) {
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
    $rootScope.$on('moveFocusToNextParagraph', function (event, currentParagraphId) {
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
    $scope.removeNotebookResults = function () {
      console.log('removeNotebookResults');
      websocketMsgSrv.resetResults();
    };
    $scope.saveNotebookStatus = function () {
      var paragraphMap = {};
      _.forEach($scope.note.paragraphs, function (n, key) {
        paragraphMap[n.id] = n.text;
      });
      websocketMsgSrv.saveNote(paragraphMap);
    };
    var updateNote = function (note) {
      /** update Note name */
      //    console.log('Note updated --- ' + JSON.stringify(note));
      if (note.name !== $scope.note.name) {
        console.log('change note name: %o to %o', $scope.note.name, note.name);
        $scope.note.name = note.name;
      }
      $scope.note.config = note.config;
      $scope.note.info = note.info;
      var newParagraphIds = note.paragraphs.map(function (x) {
          return x.id;
        });
      var oldParagraphIds = $scope.note.paragraphs.map(function (x) {
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
            $rootScope.$broadcast('updateParagraph', { paragraph: newEntry });
          } else {
            // move paragraph
            var oldIdx = oldParagraphIds.indexOf(newParagraphIds[idx]);
            $scope.note.paragraphs.splice(oldIdx, 1);
            $scope.note.paragraphs.splice(idx, 0, newEntry);
            // rebuild id list since paragraph has moved.
            oldParagraphIds = $scope.note.paragraphs.map(function (x) {
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
    $scope.changeDefaultInterpreter = function (interpreter) {
      $scope.settings.interpreter = interpreter;
      $rootScope.$broadcast('changeDefaultInterpreter', interpreter);
    };
  }
]);
//})()
/* global $:false, jQuery:false, ace:false, confirm:false, d3:false, nv:false*/
/*jshint loopfunc: true, unused:false */
/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * 'License'); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.

 * based on NFlabs Zeppelin originaly forked on Nov'14
 */
/**
     * @ngdoc function
     * @name notebookWebApp.controller:ParagraphCtrl
     * @description
     * # ParagraphCtrl
     * Controller of the paragraph, manage everything related to the paragraph
     *
     * @author anthonycorbacho
     * @author ivdiaz
     *
     */
angular.module('notebookWebApp').controller('ParagraphCtrl', [
  '$scope',
  '$rootScope',
  '$route',
  '$window',
  '$http',
  '$modal',
  '$routeParams',
  '$location',
  '$timeout',
  'websocketMsgSrv',
  function ($scope, $rootScope, $route, $window, $http, $modal, $routeParams, $location, $timeout, websocketMsgSrv) {
    $scope.paragraph = null;
    $scope.editor = null;
    var editorMode = {
        ingestion: 'ace/mode/svg',
        shell: 'ace/mode/text',
        crossdata: 'ace/mode/xdql',
        streaming: 'ace/mode/streaming',
        scala: 'ace/mode/scala',
        sql: 'ace/mode/sql',
        markdown: 'ace/mode/markdown',
        cassandra: 'ace/mode/lucene'
      };
    $scope.editorModeMap = {};
    $scope.editorModeMap[editorMode.ingestion] = 'ingestion';
    $scope.editorModeMap[editorMode.shell] = 'shell';
    $scope.editorModeMap[editorMode.crossdata] = 'crossdata';
    $scope.editorModeMap[editorMode.streaming] = 'streaming';
    $scope.editorModeMap[editorMode.sql] = 'spark-sql';
    $scope.editorModeMap[editorMode.scala] = 'spark';
    $scope.editorModeMap[editorMode.markdown] = 'markdown';
    $scope.editorModeMap[editorMode.cassandra] = 'cassandra';
    $scope.forms = {};
    // Controller init
    $scope.init = function (newParagraph, noteId) {
      $scope.paragraph = newParagraph;
      $scope.paragraph.noteId = noteId;
      $scope.chart = {};
      $scope.colWidthOption = [
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10,
        11,
        12
      ];
      $scope.showTitleEditor = false;
      if (!$scope.paragraph.config) {
        $scope.paragraph.config = {};
      }
      initializeDefault();
      if (!$scope.lastData) {
        $scope.lastData = {};
      }
      if ($scope.getResultType() === 'TABLE') {
        $scope.lastData.settings = angular.copy($scope.paragraph.settings);
        $scope.lastData.config = angular.copy($scope.paragraph.config);
        $scope.loadTableData($scope.paragraph.result);
        $scope.setGraphMode($scope.getGraphMode(), false, false);
      } else if ($scope.getResultType() === 'HTML') {
        $scope.renderHtml();
      }
    };
    $scope.renderHtml = function () {
      var retryRenderer = function () {
        if ($('#p' + $scope.paragraph.id + '_html').length) {
          try {
            $('#p' + $scope.paragraph.id + '_html').html($scope.paragraph.result.msg);
          } catch (err) {
            console.log('HTML rendering error %o', err);
          }
        } else {
          $timeout(retryRenderer, 10);
        }
      };
      $timeout(retryRenderer);
    };
    var initializeDefault = function () {
      var paragraphConfig = $scope.paragraph.config;
      if (!paragraphConfig.interpreter) {
        paragraphConfig.interpreter = $scope.editorModeMap[editorMode.crossdata];
      }
      if (!paragraphConfig.looknfeel) {
        paragraphConfig.looknfeel = 'default';
      }
      if (!paragraphConfig.colWidth) {
        paragraphConfig.colWidth = 12;
      }
      if (!paragraphConfig.graph) {
        paragraphConfig.graph = {};
      }
      if (!paragraphConfig.graph.mode) {
        paragraphConfig.graph.mode = 'table';
      }
      if (!paragraphConfig.graph.height) {
        paragraphConfig.graph.height = 300;
      }
      if (!paragraphConfig.graph.optionOpen) {
        paragraphConfig.graph.optionOpen = false;
      }
      if (!paragraphConfig.graph.keys) {
        paragraphConfig.graph.keys = [];
      }
      if (!paragraphConfig.graph.values) {
        paragraphConfig.graph.values = [];
      }
      if (!paragraphConfig.graph.groups) {
        paragraphConfig.graph.groups = [];
      }
    };
    $scope.getIframeDimensions = function () {
      if ($scope.asIframe) {
        var paragraphid = '#' + $routeParams.paragraphId + '_container';
        var height = $(paragraphid).height();
        return height;
      }
      return 0;
    };
    $scope.$watch($scope.getIframeDimensions, function (newValue, oldValue) {
      if ($scope.asIframe && newValue) {
        var message = {};
        message.height = newValue;
        message.url = $location.$$absUrl;
        $window.parent.postMessage(angular.toJson(message), '*');
      }
    });
    // TODO: this may have impact on performance when there are many paragraphs in a note.
    $rootScope.$on('updateParagraph', function (event, data) {
      //            console.log('updateParagraph@data');
      //            console.log(data);
      //            console.log('updateParagraph@paragraph.config');
      //            console.log($scope.paragraph.config);
      //            console.log('updateParagraph@paragraph.settings');
      //            console.log($scope.paragraph.settings);
      $scope.lastData.config = angular.copy($scope.paragraph.config);
      $scope.lastData.settings = angular.copy($scope.paragraph.settings);
      if (data.paragraph.id === $scope.paragraph.id && (data.paragraph.dateCreated !== $scope.paragraph.dateCreated || data.paragraph.dateFinished !== $scope.paragraph.dateFinished || data.paragraph.dateStarted !== $scope.paragraph.dateStarted || data.paragraph.status !== $scope.paragraph.status || data.paragraph.jobName !== $scope.paragraph.jobName || data.paragraph.title !== $scope.paragraph.title || data.paragraph.errorMessage !== $scope.paragraph.errorMessage || !angular.equals(data.paragraph.settings, $scope.lastData.settings) || !angular.equals(data.paragraph.config, $scope.lastData.config))) {
        //              console.log('updateParagraph');
        var oldType = $scope.getResultType();
        var newType = $scope.getResultType(data.paragraph);
        var oldGraphMode = $scope.getGraphMode();
        var newGraphMode = $scope.getGraphMode(data.paragraph);
        var resultRefreshed = data.paragraph.dateFinished !== $scope.paragraph.dateFinished;
        //      console.log('updateParagraph oldData %o, newData %o. type %o -> %o, mode %o -> %o', $scope.paragraph, data, oldType, newType, oldGraphMode, newGraphMode);
        if ($scope.paragraph.text !== data.paragraph.text) {
          if ($scope.dirtyText) {
            // check if editor has local update
            if ($scope.dirtyText === data.paragraph.text) {
              // when local update is the same from remote, clear local update
              $scope.paragraph.text = data.paragraph.text;
              $scope.dirtyText = undefined;
            } else {
              // if there're local update, keep it.
              $scope.paragraph.text = $scope.dirtyText;
            }
          } else {
            $scope.paragraph.text = data.paragraph.text;
          }
        }
        /** push the rest */
        $scope.paragraph.aborted = data.paragraph.aborted;
        $scope.paragraph.dateCreated = data.paragraph.dateCreated;
        $scope.paragraph.dateFinished = data.paragraph.dateFinished;
        $scope.paragraph.dateStarted = data.paragraph.dateStarted;
        $scope.paragraph.errorMessage = data.paragraph.errorMessage;
        $scope.paragraph.jobName = data.paragraph.jobName;
        $scope.paragraph.title = data.paragraph.title;
        if (data.paragraph.status === 'REFRESH_RESULT') {
          $scope.paragraph.status = 'RUNNING';
        } else {
          $scope.paragraph.status = data.paragraph.status;
        }
        $scope.paragraph.result = data.paragraph.result;
        $scope.paragraph.settings = data.paragraph.settings;
        if (!$scope.asIframe) {
          $scope.paragraph.config = data.paragraph.config;
          initializeDefault();
        } else {
          data.paragraph.config.editorHide = true;
          data.paragraph.config.tableHide = false;
          $scope.paragraph.config = data.paragraph.config;
        }
        console.log(newType);
        if (newType === 'TABLE') {
          $scope.loadTableData($scope.paragraph.result);
          if (oldType !== 'TABLE' || resultRefreshed) {
            clearUnknownColsFromGraphOption();
            selectDefaultColsForGraphOption();
          }
          /** User changed the chart type? */
          if (oldGraphMode !== newGraphMode) {
            $scope.setGraphMode(newGraphMode, false, false);
          } else {
            $scope.setGraphMode(newGraphMode, false, true);
          }
        } else if (newType === 'HTML') {
          $scope.renderHtml();
        }
      }
    });
    $scope.setInterpreter = function (interpreter) {
      $scope.paragraph.config.interpreter = interpreter;
    };
    $scope.isRunning = function () {
      if ($scope.paragraph.status === 'RUNNING' || $scope.paragraph.status === 'REFRESH_RESULT' || $scope.paragraph.status === 'PENDING') {
        return true;
      } else {
        return false;
      }
    };
    $scope.cancelParagraph = function () {
      console.log('Cancel %o', $scope.paragraph.id);
      websocketMsgSrv.cancelParagraphRun($scope.paragraph.id);
    };
    $scope.runParagraph = function (data) {
      //console.log('send new paragraph: %o with %o', $scope.paragraph.id, data);
      var code = $scope.editor.getSession().getValue();
      var path;
      //allow to open modal-window for editing plain text files only under ingestion interpreter
      if (String(code).startsWith('%ing edit ')) {
        path = String(code).replace('%ing edit ', '');
        $rootScope.$broadcast('openPropertiesEditor', path);
      } else if (String(code).startsWith('edit ') && $scope.paragraph.config.interpreter === $scope.editorModeMap[editorMode.ingestion]) {
        path = String(code).replace('edit ', '');
        $rootScope.$broadcast('openPropertiesEditor', path);
      } else {
        websocketMsgSrv.runParagraph($scope.paragraph.id, $scope.paragraph.title, data, $scope.paragraph.config, $scope.paragraph.settings.params);
        $scope.dirtyText = undefined;
      }
    };
    $scope.moveUp = function () {
      $rootScope.$emit('moveParagraphUp', $scope.paragraph.id);
    };
    $scope.moveDown = function () {
      $rootScope.$emit('moveParagraphDown', $scope.paragraph.id);
    };
    $scope.insertNew = function () {
      $rootScope.$emit('insertParagraph', $scope.paragraph.id);
    };
    $scope.split = function (data) {
      var paragraphData = {
          id: $scope.paragraph.id,
          paragraph: data
        };
      $rootScope.$emit('split', paragraphData);
    };
    $scope.removeParagraph = function () {
      var result = confirm('Do you want to delete this paragraph?');
      if (result) {
        websocketMsgSrv.removeParagraph($scope.paragraph.id);
      }
    };
    $scope.toggleEditor = function () {
      if ($scope.paragraph.config.editorHide) {
        $scope.openEditor();
      } else {
        $scope.closeEditor();
      }
    };
    $scope.closeEditor = function () {
      console.log('close the note');
      var newParams = angular.copy($scope.paragraph.settings.params);
      var newConfig = angular.copy($scope.paragraph.config);
      console.log('old config');
      console.log($scope.paragraph.config);
      console.log('new config');
      newConfig.editorHide = true;
      console.log(newConfig);
      websocketMsgSrv.commitParagraph($scope.paragraph.id, $scope.paragraph.title, $scope.paragraph.text, newConfig, newParams);
    };
    $scope.openEditor = function () {
      console.log('open the note');
      var newParams = angular.copy($scope.paragraph.settings.params);
      var newConfig = angular.copy($scope.paragraph.config);
      console.log('old config');
      console.log($scope.paragraph.config);
      console.log('new config');
      newConfig.editorHide = false;
      console.log(newConfig);
      websocketMsgSrv.commitParagraph($scope.paragraph.id, $scope.paragraph.title, $scope.paragraph.text, newConfig, newParams);
    };
    $scope.toggleOutput = function () {
      if ($scope.paragraph.config.tableHide) {
        $scope.openTable();
      } else {
        $scope.closeTable();
      }
    };
    $scope.closeTable = function () {
      //            console.log('close the output');
      var newParams = angular.copy($scope.paragraph.settings.params);
      var newConfig = angular.copy($scope.paragraph.config);
      //            console.log('old config');
      //            console.log($scope.paragraph.config);
      //            console.log('new config');
      newConfig.tableHide = true;
      //            console.log(newConfig);
      websocketMsgSrv.commitParagraph($scope.paragraph.id, $scope.paragraph.title, $scope.paragraph.text, newConfig, newParams);
    };
    $scope.openTable = function () {
      //            console.log('open the output');
      var newParams = angular.copy($scope.paragraph.settings.params);
      var newConfig = angular.copy($scope.paragraph.config);
      //            console.log('old config');
      //            console.log($scope.paragraph.config);
      //            console.log('new config');
      newConfig.tableHide = false;
      //            console.log(newConfig);
      websocketMsgSrv.commitParagraph($scope.paragraph.id, $scope.paragraph.title, $scope.paragraph.text, newConfig, newParams);
    };
    $scope.showTitle = function () {
      var newParams = angular.copy($scope.paragraph.settings.params);
      var newConfig = angular.copy($scope.paragraph.config);
      newConfig.title = true;
      websocketMsgSrv.commitParagraph($scope.paragraph.id, $scope.paragraph.title, $scope.paragraph.text, newConfig, newParams);
    };
    $scope.hideTitle = function () {
      var newParams = angular.copy($scope.paragraph.settings.params);
      var newConfig = angular.copy($scope.paragraph.config);
      newConfig.title = false;
      websocketMsgSrv.commitParagraph($scope.paragraph.id, $scope.paragraph.title, $scope.paragraph.text, newConfig, newParams);
    };
    $scope.setTitle = function () {
      var newParams = angular.copy($scope.paragraph.settings.params);
      var newConfig = angular.copy($scope.paragraph.config);
      websocketMsgSrv.commitParagraph($scope.paragraph.id, $scope.paragraph.title, $scope.paragraph.text, newConfig, newParams);
    };
    $scope.changeColWidth = function () {
      var newParams = angular.copy($scope.paragraph.settings.params);
      var newConfig = angular.copy($scope.paragraph.config);
      websocketMsgSrv.commitParagraph($scope.paragraph.id, $scope.paragraph.title, $scope.paragraph.text, newConfig, newParams);
    };
    $scope.toggleGraphOption = function () {
      var newConfig = angular.copy($scope.paragraph.config);
      if (newConfig.graph.optionOpen) {
        newConfig.graph.optionOpen = false;
      } else {
        newConfig.graph.optionOpen = true;
      }
      var newParams = angular.copy($scope.paragraph.settings.params);
      websocketMsgSrv.commitParagraph($scope.paragraph.id, $scope.paragraph.title, $scope.paragraph.text, newConfig, newParams);
    };
    $scope.loadForm = function (formulaire, params) {
      var value = formulaire.defaultValue;
      if (params[formulaire.name]) {
        value = params[formulaire.name];
      }
      $scope.forms[formulaire.name] = value;
    };
    $scope.aceChanged = function () {
      $scope.dirtyText = $scope.editor.getSession().getValue();
      var code = $scope.editor.getSession().getValue();
      if (String(code).startsWith('%sql ')) {
        $scope.paragraph.config.interpreter = $scope.editorModeMap[editorMode.sql];
        console.log(String(code) + ' -> ' + $scope.paragraph.config.interpreter);
      } else if (String(code).startsWith('%ing ')) {
        $scope.paragraph.config.interpreter = $scope.editorModeMap[editorMode.ingestion];
        console.log(String(code) + ' -> ' + $scope.paragraph.config.interpreter);
      } else if (String(code).startsWith('%sh ')) {
        $scope.paragraph.config.interpreter = $scope.editorModeMap[editorMode.shell];
        console.log(String(code) + ' -> ' + $scope.paragraph.config.interpreter);
      } else if (String(code).startsWith('%md ')) {
        $scope.paragraph.config.interpreter = $scope.editorModeMap[editorMode.markdown];
        console.log(String(code) + ' -> ' + $scope.paragraph.config.interpreter);
      } else if (String(code).startsWith('%s ')) {
        $scope.paragraph.config.interpreter = $scope.editorModeMap[editorMode.scala];
        console.log(String(code) + ' -> ' + $scope.paragraph.config.interpreter);
      } else if (String(code).startsWith('%str ')) {
        $scope.paragraph.config.interpreter = $scope.editorModeMap[editorMode.streaming];
        console.log(String(code) + ' -> ' + $scope.paragraph.config.interpreter);
      } else if (String(code).startsWith('%xdql ')) {
        $scope.paragraph.config.interpreter = $scope.editorModeMap[editorMode.crossdata];
        console.log(String(code) + ' -> ' + $scope.paragraph.config.interpreter);
      } else if (String(code).startsWith('%cql ')) {
        $scope.paragraph.config.interpreter = $scope.editorModeMap[editorMode.cassandra];
        console.log(String(code) + ' -> ' + $scope.paragraph.config.interpreter);
      }
    };
    $scope.aceLoaded = function (_editor) {
      var langTools = ace.require('ace/ext/language_tools');
      var Range = ace.require('ace/range').Range;
      $scope.editor = _editor;
      if (_editor.container.id !== '{{paragraph.id}}_editor') {
        $scope.editor.renderer.setShowGutter(false);
        $scope.editor.setHighlightActiveLine(false);
        $scope.editor.focus();
        var hight = $scope.editor.getSession().getScreenLength() * $scope.editor.renderer.lineHeight + $scope.editor.renderer.scrollBar.getWidth();
        setEditorHeight(_editor.container.id, hight);
        $scope.editor.getSession().setMode(editorMode.crossdata);
        $scope.editor.getSession().setUseWrapMode(true);
        if (navigator.appVersion.indexOf('Mac') !== -1) {
          $scope.editor.setKeyboardHandler('ace/keyboard/emacs');
        } else if (navigator.appVersion.indexOf('Win') !== -1 || navigator.appVersion.indexOf('X11') !== -1 || navigator.appVersion.indexOf('Linux') !== -1) {
        }
        $scope.editor.setOptions({
          enableBasicAutocompletion: true,
          enableSnippets: false,
          enableLiveAutocompletion: false
        });
        var remoteCompleter = {
            getCompletions: function (editor, session, pos, prefix, callback) {
              if (!$scope.editor.isFocused()) {
                return;
              }
              pos = getTextRange(new Range(0, 0, pos.row, pos.column));
              var buf = session.getValue();
              $scope.setParagraphMode(session, buf);
              websocketMsgSrv.completion($scope.paragraph.id, buf, pos);
              $rootScope.$on('completionList', function (event, data) {
                if (data.completions) {
                  var completions = [];
                  for (var c in data.completions) {
                    var v = data.completions[c];
                    completions.push({
                      name: v,
                      value: v,
                      score: 300
                    });
                  }
                  callback(null, completions);
                }
              });
            }
          };
        //                langTools.setCompleters([remoteCompleter, langTools.keyWordCompleter, langTools.snippetCompleter, langTools.textCompleter]);
        $scope.editor.setOptions({
          enableBasicAutocompletion: true,
          enableSnippets: false,
          enableLiveAutocompletion: false
        });
        $scope.handleFocus = function (value) {
          $scope.paragraphFocused = value;
          // Protect against error in case digest is already running
          $timeout(function () {
            // Apply changes since they come from 3rd party library
            $scope.$digest();
          });
        };
        $scope.editor.on('focus', function () {
          $scope.handleFocus(true);
        });
        $scope.editor.on('blur', function () {
          $scope.handleFocus(false);
        });
        $scope.editor.getSession().on('change', function (e, editSession) {
          hight = editSession.getScreenLength() * $scope.editor.renderer.lineHeight + $scope.editor.renderer.scrollBar.getWidth();
          setEditorHeight(_editor.container.id, hight);
          $scope.editor.resize();
        });
        var getModeByValue = function (value) {
          for (var key in $scope.editorModeMap) {
            if ($scope.editorModeMap[key] === value) {
              return key;
            }
          }
        };
        $scope.$watch(function () {
          return $scope.paragraph.config.interpreter;
        }, function (newValue, oldValue) {
          if (newValue !== oldValue) {
            var newMode = getModeByValue(newValue);
            $scope.editor.getSession().setMode(newMode);  //          console.log('watch interpreter -> '+ $scope.paragraph.config.interpreter + ' old '+ oldValue + ' new '
                                                          //          +newValue + 'new mode -> '+newMode );
          }
        });
        //                $scope.setParagraphMode($scope.editor.getSession(), $scope.editor.getSession().getValue());
        $scope.editor.commands.addCommand({
          name: 'run',
          bindKey: {
            win: 'Shift-Enter',
            mac: 'Shift-Enter'
          },
          exec: function (editor) {
            var editorValue = editor.getValue();
            if (editorValue) {
              $scope.runParagraph(editorValue);
            }
          },
          readOnly: false
        });
        // autocomplete on 'ctrl+.'
        $scope.editor.commands.bindKey('ctrl-space', 'startAutocomplete');
        $scope.editor.commands.bindKey('ctrl-.', null);
        // handle cursor moves
        $scope.editor.keyBinding.origOnCommandKey = $scope.editor.keyBinding.onCommandKey;
        $scope.editor.keyBinding.onCommandKey = function (e, hashId, keyCode) {
          if ($scope.editor.completer && $scope.editor.completer.activated) {
          } else {
            var numRows;
            var currentRow;
            if (keyCode === 38 || keyCode === 80 && e.ctrlKey) {
              // UP
              numRows = $scope.editor.getSession().getLength();
              currentRow = $scope.editor.getCursorPosition().row;
              if (currentRow === 0) {
                // move focus to previous paragraph
                $rootScope.$emit('moveFocusToPreviousParagraph', $scope.paragraph.id);
              }
            } else if (keyCode === 40 || keyCode === 78 && e.ctrlKey) {
              // DOWN
              numRows = $scope.editor.getSession().getLength();
              currentRow = $scope.editor.getCursorPosition().row;
              if (currentRow === numRows - 1) {
                // move focus to next paragraph
                $rootScope.$emit('moveFocusToNextParagraph', $scope.paragraph.id);
              }
            }
          }
          this.origOnCommandKey(e, hashId, keyCode);
        };
      }
    };
    var setEditorHeight = function (id, height) {
      $('#' + id).height(height.toString() + 'px');
    };
    $scope.getEditorValue = function () {
      return $scope.editor.getValue();
    };
    $scope.getProgress = function () {
      return $scope.currentProgress ? $scope.currentProgress : 0;
    };
    $scope.getExecutionTime = function () {
      var pdata = $scope.paragraph;
      var timeMs = Date.parse(pdata.dateFinished) - Date.parse(pdata.dateStarted);
      return 'Took ' + timeMs / 1000 + ' seconds';
    };
    $rootScope.$on('updateProgress', function (event, data) {
      if (data.id === $scope.paragraph.id) {
        $scope.currentProgress = data.progress;
      }
    });
    $rootScope.$on('focusParagraph', function (event, paragraphId) {
      if ($scope.paragraph.id === paragraphId) {
        $scope.editor.focus();
        $('.note-workspace').scrollTo('#' + paragraphId + '_editor', 300, { offset: -60 });
        console.log('focusParagraph');  //            $rootScope.$emit('lastParagraphRunId', '');
      }
    });
    $rootScope.$on('runParagraph', function (event) {
      $scope.runParagraph($scope.editor.getValue());
    });
    $rootScope.$on('openEditor', function (event) {
      $scope.openEditor();
    });
    $rootScope.$on('closeEditor', function (event) {
      $scope.closeEditor();
    });
    $rootScope.$on('openTable', function (event) {
      $scope.openTable();
    });
    $rootScope.$on('closeTable', function (event) {
      $scope.closeTable();
    });
    $rootScope.$on('changeDefaultInterpreter', function (event, data) {
      $scope.changeDefaultInterpreter(data);
    });
    $scope.changeDefaultInterpreter = function (interpreter) {
      var newInterpreter = editorMode.crossdata;
      switch (interpreter) {
      case 'crossdata':
        newInterpreter = editorMode.crossdata;
        break;
      case 'markdown':
        newInterpreter = editorMode.markdown;
        break;
      case 'ingestion':
        newInterpreter = editorMode.ingestion;
        break;
      case 'streaming':
        newInterpreter = editorMode.streaming;
        break;
      case 'spark':
        newInterpreter = editorMode.scala;
        break;
      case 'spark-sql':
        newInterpreter = editorMode.sql;
        break;
      case 'shell':
        newInterpreter = editorMode.shell;
        break;
      default:
        newInterpreter = editorMode.crossdata;
      }
      $scope.paragraph.config.interpreter = $scope.editorModeMap[newInterpreter];
    };
    $scope.getResultType = function (paragraph) {
      var pdata = paragraph ? paragraph : $scope.paragraph;
      if (pdata.result && pdata.result.type) {
        return pdata.result.type;
      } else {
        return 'TEXT';
      }
    };
    $scope.getBase64ImageSrc = function (base64Data) {
      return 'data:image/png;base64,' + base64Data;
    };
    $scope.getGraphMode = function (paragraph) {
      var pdata = paragraph ? paragraph : $scope.paragraph;
      if (pdata.config.graph && pdata.config.graph.mode) {
        return pdata.config.graph.mode;
      } else {
        return 'table';
      }
    };
    $scope.loadTableData = function (result) {
      if (!result) {
        return;
      }
      if (result.type === 'TABLE') {
        var columnNames = [];
        var rows = [];
        var array = [];
        var textRows = result.msg.split('\n');
        result.comment = '';
        var comment = false;
        for (var i = 0; i < textRows.length; i++) {
          var textRow = textRows[i];
          if (comment) {
            result.comment += textRow;
            continue;
          }
          if (textRow === '') {
            if (rows.length > 0) {
              comment = true;
            }
            continue;
          }
          var textCols = textRow.split('\t');
          var cols = [];
          var cols2 = [];
          for (var j = 0; j < textCols.length; j++) {
            var col = textCols[j];
            if (i === 0) {
              columnNames.push({
                name: col,
                index: j,
                aggr: 'sum'
              });
            } else {
              cols.push(col);
              cols2.push({
                key: columnNames[i] ? columnNames[i].name : undefined,
                value: col
              });
            }
          }
          if (i !== 0) {
            rows.push(cols);
            array.push(cols2);
          }
        }
        result.msgTable = array;
        result.columnNames = columnNames;
        result.rows = rows;
      }
    };
    $scope.setGraphMode = function (type, emit, refresh) {
      if (emit) {
        setNewMode(type);
      } else {
        clearUnknownColsFromGraphOption();
        // set graph height
        var height = $scope.paragraph.config.graph.height;
        $('#p' + $scope.paragraph.id + '_graph').height(height);
        if (!type || type === 'table') {
          setTable($scope.paragraph.result, refresh);
        } else if (type === 'multiBarChart') {
          setD3Chart(type, $scope.paragraph.result, refresh);
        } else if (type === 'pieChart') {
          setD3Chart(type, $scope.paragraph.result, refresh);
        } else if (type === 'stackedAreaChart') {
          setD3Chart(type, $scope.paragraph.result, refresh);
        } else if (type === 'lineChart') {
          setD3Chart(type, $scope.paragraph.result, refresh);
        }
      }
    };
    var setNewMode = function (newMode) {
      var newConfig = angular.copy($scope.paragraph.config);
      var newParams = angular.copy($scope.paragraph.settings.params);
      // graph options
      newConfig.graph.mode = newMode;
      websocketMsgSrv.commitParagraph($scope.paragraph.id, $scope.paragraph.title, $scope.paragraph.text, newConfig, newParams);
    };
    var setTable = function (type, data, refresh) {
      var getTableContentFormat = function (d) {
        if (isNaN(d)) {
          if (d.length > '%html'.length && '%html ' === d.substring(0, '%html '.length)) {
            return 'html';
          } else {
            return '';
          }
        } else {
          return '';
        }
      };
      var formatTableContent = function (d) {
        return d;
      };
      var renderTable = function () {
        var html = '';
        html += '<table class=\'table table-hover table-condensed\'>';
        html += '  <thead>';
        html += '    <tr style=\'background-color: #F6F6F6; font-weight: bold;\'>';
        for (var c in $scope.paragraph.result.columnNames) {
          html += '<th>' + $scope.paragraph.result.columnNames[c].name + '</th>';
        }
        html += '    </tr>';
        html += '  </thead>';
        for (var r in $scope.paragraph.result.msgTable) {
          var row = $scope.paragraph.result.msgTable[r];
          html += '    <tr>';
          for (var index in row) {
            var v = row[index].value;
            if (getTableContentFormat(v) !== 'html') {
              v = v.replace(/[\u00A0-\u9999<>\&]/gim, function (i) {
                return '&#' + i.charCodeAt(0) + ';';
              });
            }
            html += '      <td>' + formatTableContent(v) + '</td>';
          }
          html += '    </tr>';
        }
        html += '</table>';
        $('#p' + $scope.paragraph.id + '_table').html(html);
        $('#p' + $scope.paragraph.id + '_table').perfectScrollbar();
        // set table height
        var height = $scope.paragraph.config.graph.height;
        $('#p' + $scope.paragraph.id + '_table').height(height);
      };
      var retryRenderer = function () {
        if ($('#p' + $scope.paragraph.id + '_table').length) {
          try {
            renderTable();
          } catch (err) {
            console.log('Chart drawing error %o', err);
          }
        } else {
          $timeout(retryRenderer, 10);
        }
      };
      $timeout(retryRenderer);
    };
    var setD3Chart = function (type, data, refresh) {
      if (!$scope.chart[type]) {
        var chart = nv.models[type]();
        $scope.chart[type] = chart;
      }
      var p = pivot(data);
      var xColIndexes = $scope.paragraph.config.graph.keys;
      var yColIndexes = $scope.paragraph.config.graph.values;
      var d3g = [];
      // select yColumns.
      if (type === 'pieChart') {
        var d = pivotDataToD3ChartFormat(p, true).d3g;
        $scope.chart[type].x(function (d) {
          return d.label;
        }).y(function (d) {
          return d.value;
        });
        if (d.length > 0) {
          for (var i = 0; i < d[0].values.length; i++) {
            var e = d[0].values[i];
            d3g.push({
              label: e.x,
              value: e.y
            });
          }
        }
      } else if (type === 'multiBarChart') {
        d3g = pivotDataToD3ChartFormat(p, true).d3g;
        $scope.chart[type].yAxis.axisLabelDistance(50);
      } else {
        var pivotdata = pivotDataToD3ChartFormat(p);
        var xLabels = pivotdata.xLabels;
        d3g = pivotdata.d3g;
        $scope.chart[type].xAxis.tickFormat(function (d) {
          if (xLabels[d]) {
            return xLabels[d];
          } else {
            return d;
          }
        });
        $scope.chart[type].yAxis.axisLabelDistance(50);
        $scope.chart[type].useInteractiveGuideline(true);
        // for better UX and performance issue. (https://github.com/novus/nvd3/issues/691)
        $scope.chart[type].forceY([0]);  // force y-axis minimum to 0 for line chart.
      }
      var renderChart = function () {
        if (!refresh) {
        }
        var height = $scope.paragraph.config.graph.height;
        var animationDuration = 300;
        var numberOfDataThreshold = 150;
        // turn off animation when dataset is too large. (for performance issue)
        // still, since dataset is large, the chart content sequentially appears like animated.
        try {
          if (d3g[0].values.length > numberOfDataThreshold) {
            animationDuration = 0;
          }
        } catch (ignoreErr) {
        }
        var chartEl = d3.select('#p' + $scope.paragraph.id + '_' + type + ' svg').attr('height', $scope.paragraph.config.graph.height).datum(d3g).transition().duration(animationDuration).call($scope.chart[type]);
        d3.select('#p' + $scope.paragraph.id + '_' + type + ' svg').style.height = height + 'px';
        nv.utils.windowResize($scope.chart[type].update);
      };
      var retryRenderer = function () {
        if ($('#p' + $scope.paragraph.id + '_' + type + ' svg').length !== 0) {
          try {
            renderChart();
          } catch (err) {
            console.log('Chart drawing error %o', err);
          }
        } else {
          $timeout(retryRenderer, 10);
        }
      };
      $timeout(retryRenderer);
    };
    var setPieChart = function (data, refresh) {
      var xColIndex = 0;
      var yColIndexes = [];
      var d3g = [];
      // select yColumns.
      for (var colIndex = 0; colIndex < data.columnNames.length; colIndex++) {
        if (colIndex !== xColIndex) {
          yColIndexes.push(colIndex);
        }
      }
      for (var rowIndex = 0; rowIndex < data.rows.length; rowIndex++) {
        var row = data.rows[rowIndex];
        var xVar = row[xColIndex];
        var yVar = row[yColIndexes[0]];
        d3g.push({
          label: isNaN(xVar) ? xVar : parseFloat(xVar),
          value: parseFloat(yVar)
        });
      }
      if ($scope.d3.pieChart.data === null || !refresh) {
        $scope.d3.pieChart.data = d3g;
        $scope.d3.pieChart.options.chart.height = $scope.paragraph.config.graph.height;
        if ($scope.d3.pieChart.api) {
          $scope.d3.pieChart.api.updateWithOptions($scope.d3.pieChart.options);
        }
      } else {
        if ($scope.d3.pieChart.api) {
          $scope.d3.pieChart.api.updateWithData(d3g);
        }
      }
    };
    $scope.isGraphMode = function (graphName) {
      if ($scope.getResultType() === 'TABLE' && $scope.getGraphMode() === graphName) {
        return true;
      } else {
        return false;
      }
    };
    $scope.onGraphOptionChange = function () {
      clearUnknownColsFromGraphOption();
      $scope.setGraphMode($scope.paragraph.config.graph.mode, true, false);
    };
    $scope.removeGraphOptionKeys = function (idx) {
      $scope.paragraph.config.graph.keys.splice(idx, 1);
      clearUnknownColsFromGraphOption();
      $scope.setGraphMode($scope.paragraph.config.graph.mode, true, false);
    };
    $scope.removeGraphOptionValues = function (idx) {
      $scope.paragraph.config.graph.values.splice(idx, 1);
      clearUnknownColsFromGraphOption();
      $scope.setGraphMode($scope.paragraph.config.graph.mode, true, false);
    };
    $scope.removeGraphOptionGroups = function (idx) {
      $scope.paragraph.config.graph.groups.splice(idx, 1);
      clearUnknownColsFromGraphOption();
      $scope.setGraphMode($scope.paragraph.config.graph.mode, true, false);
    };
    $scope.setGraphOptionValueAggr = function (idx, aggr) {
      $scope.paragraph.config.graph.values[idx].aggr = aggr;
      clearUnknownColsFromGraphOption();
      $scope.setGraphMode($scope.paragraph.config.graph.mode, true, false);
    };
    /* Clear unknown columns from graph option */
    var clearUnknownColsFromGraphOption = function () {
      var unique = function (list) {
        for (var i = 0; i < list.length; i++) {
          for (var j = i + 1; j < list.length; j++) {
            if (angular.equals(list[i], list[j])) {
              list.splice(j, 1);
            }
          }
        }
      };
      var removeUnknown = function (list) {
        for (var i = 0; i < list.length; i++) {
          // remove non existing column
          var found = false;
          for (var j = 0; j < $scope.paragraph.result.columnNames.length; j++) {
            var a = list[i];
            var b = $scope.paragraph.result.columnNames[j];
            if (a.index === b.index && a.name === b.name) {
              found = true;
              break;
            }
          }
          if (!found) {
            list.splice(i, 1);
          }
        }
      };
      unique($scope.paragraph.config.graph.keys);
      removeUnknown($scope.paragraph.config.graph.keys);
      removeUnknown($scope.paragraph.config.graph.values);
      unique($scope.paragraph.config.graph.groups);
      removeUnknown($scope.paragraph.config.graph.groups);
    };
    /* select default key and value if there're none selected */
    var selectDefaultColsForGraphOption = function () {
      if ($scope.paragraph.config.graph.keys.length === 0 && $scope.paragraph.result.columnNames.length > 0) {
        $scope.paragraph.config.graph.keys.push($scope.paragraph.result.columnNames[0]);
      }
      if ($scope.paragraph.config.graph.values.length === 0 && $scope.paragraph.result.columnNames.length > 1) {
        $scope.paragraph.config.graph.values.push($scope.paragraph.result.columnNames[1]);
      }
    };
    var pivot = function (data) {
      var keys = $scope.paragraph.config.graph.keys;
      var groups = $scope.paragraph.config.graph.groups;
      var values = $scope.paragraph.config.graph.values;
      var aggrFunc = {
          sum: function (a, b) {
            var varA = a !== undefined ? isNaN(a) ? 1 : parseFloat(a) : 0;
            var varB = b !== undefined ? isNaN(b) ? 1 : parseFloat(b) : 0;
            return varA + varB;
          },
          count: function (a, b) {
            var varA = a !== undefined ? a : 0;
            var varB = b !== undefined ? 1 : 0;
            return varA + varB;
          },
          min: function (a, b) {
            var varA = a !== undefined ? isNaN(a) ? 1 : parseFloat(a) : 0;
            var varB = b !== undefined ? isNaN(b) ? 1 : parseFloat(b) : 0;
            return Math.min(varA, varB);
          },
          max: function (a, b) {
            var varA = a !== undefined ? isNaN(a) ? 1 : parseFloat(a) : 0;
            var varB = b !== undefined ? isNaN(b) ? 1 : parseFloat(b) : 0;
            return Math.max(varA, varB);
          },
          avg: function (a, b, c) {
            var varA = a !== undefined ? isNaN(a) ? 1 : parseFloat(a) : 0;
            var varB = b !== undefined ? isNaN(b) ? 1 : parseFloat(b) : 0;
            return varA + varB;
          }
        };
      var aggrFuncDiv = {
          sum: false,
          count: false,
          min: false,
          max: false,
          avg: true
        };
      var schema = {};
      var rows = {};
      for (var i = 0; i < data.rows.length; i++) {
        var row = data.rows[i];
        var newRow = {};
        var s = schema;
        var p = rows;
        for (var k = 0; k < keys.length; k++) {
          var key = keys[k];
          // add key to schema
          if (!s[key.name]) {
            s[key.name] = {
              order: k,
              index: key.index,
              type: 'key',
              children: {}
            };
          }
          s = s[key.name].children;
          // add key to row
          var keyKey = row[key.index];
          if (!p[keyKey]) {
            p[keyKey] = {};
          }
          p = p[keyKey];
        }
        for (var g = 0; g < groups.length; g++) {
          var group = groups[g];
          var groupKey = row[group.index];
          // add group to schema
          if (!s[groupKey]) {
            s[groupKey] = {
              order: g,
              index: group.index,
              type: 'group',
              children: {}
            };
          }
          s = s[groupKey].children;
          // add key to row
          if (!p[groupKey]) {
            p[groupKey] = {};
          }
          p = p[groupKey];
        }
        for (var v = 0; v < values.length; v++) {
          var value = values[v];
          var valueKey = value.name + '(' + value.aggr + ')';
          // add value to schema
          if (!s[valueKey]) {
            s[valueKey] = {
              type: 'value',
              order: v,
              index: value.index
            };
          }
          // add value to row
          if (!p[valueKey]) {
            p[valueKey] = {
              value: row[value.index],
              count: 1
            };
          } else {
            p[valueKey] = {
              value: aggrFunc[value.aggr](p[valueKey].value, row[value.index], p[valueKey].count + 1),
              count: aggrFuncDiv[value.aggr] ? p[valueKey].count + 1 : p[valueKey].count
            };
          }
        }
      }
      //console.log('schema=%o, rows=%o', schema, rows);
      return {
        schema: schema,
        rows: rows
      };
    };
    var pivotDataToD3ChartFormat = function (data, allowTextXAxis) {
      // construct d3 data
      var d3g = [];
      var schema = data.schema;
      var rows = data.rows;
      var values = $scope.paragraph.config.graph.values;
      var d = {};
      var concat = function (o, n) {
        if (!o) {
          return n;
        } else {
          return o + '.' + n;
        }
      };
      var traverse = function (sKey, s, rKey, r, func, rowName, rowValue, colName) {
        //console.log('TRAVERSE sKey=%o, s=%o, rKey=%o, r=%o, rowName=%o, rowValue=%o, colName=%o', sKey, s, rKey, r, rowName, rowValue, colName);
        if (s.type === 'key') {
          rowName = concat(rowName, sKey);
          rowValue = concat(rowValue, rKey);
        } else if (s.type === 'group') {
          colName = concat(colName, sKey);
        } else if (s.type === 'value') {
          colName = concat(colName, rKey);
          func(rowName, rowValue, colName, r);
        }
        for (var c in s.children) {
          if (s.type === 'group' && sKey !== rKey) {
            traverse(c, s.children[c], c, undefined, func, rowName, rowValue, colName);
            continue;
          }
          for (var j in r) {
            traverse(c, s.children[c], j, r[j], func, rowName, rowValue, colName);
          }
        }
      };
      var keys = $scope.paragraph.config.graph.keys;
      var groups = $scope.paragraph.config.graph.groups;
      values = $scope.paragraph.config.graph.values;
      var valueOnly = keys.length === 0 && groups.length === 0 && values.length > 0;
      var sKey = Object.keys(schema)[0];
      var rowNameIndex = {};
      var rowIdx = 0;
      var colNameIndex = {};
      var colIdx = 0;
      var rowIndexValue = {};
      for (var k in rows) {
        traverse(sKey, schema[sKey], k, rows[k], function (rowName, rowValue, colName, value) {
          //console.log('RowName=%o, row=%o, col=%o, value=%o', rowName, rowValue, colName, value);
          if (rowNameIndex[rowValue] === undefined) {
            rowIndexValue[rowIdx] = rowValue;
            rowNameIndex[rowValue] = rowIdx++;
          }
          if (colNameIndex[colName] === undefined) {
            colNameIndex[colName] = colIdx++;
          }
          var i = colNameIndex[colName];
          if (valueOnly) {
            i = 0;
          }
          if (!d3g[i]) {
            d3g[i] = {
              values: [],
              key: valueOnly ? 'values' : colName
            };
          }
          var xVar = isNaN(rowValue) ? allowTextXAxis ? rowValue : rowNameIndex[rowValue] : parseFloat(rowValue);
          var yVar = 0;
          if (xVar === undefined) {
            xVar = colName;
          }
          if (value !== undefined) {
            yVar = isNaN(value.value) ? 0 : parseFloat(value.value) / parseFloat(value.count);
          }
          d3g[i].values.push({
            x: xVar,
            y: yVar
          });
        });
      }
      // clear aggregation name, if possible
      var namesWithoutAggr = {};
      // TODO - This part could use som refactoring - Weird if/else with similar actions and variable names
      for (var colName in colNameIndex) {
        var withoutAggr = colName.substring(0, colName.lastIndexOf('('));
        if (!namesWithoutAggr[withoutAggr]) {
          namesWithoutAggr[withoutAggr] = 1;
        } else {
          namesWithoutAggr[withoutAggr]++;
        }
      }
      if (valueOnly) {
        for (var valueIndex = 0; valueIndex < d3g[0].values.length; valueIndex++) {
          var colName = d3g[0].values[valueIndex].x;
          if (!colName) {
            continue;
          }
          var withoutAggr = colName.substring(0, colName.lastIndexOf('('));
          if (namesWithoutAggr[withoutAggr] <= 1) {
            d3g[0].values[valueIndex].x = withoutAggr;
          }
        }
      } else {
        for (var d3gIndex = 0; d3gIndex < d3g.length; d3gIndex++) {
          var colName = d3g[d3gIndex].key;
          var withoutAggr = colName.substring(0, colName.lastIndexOf('('));
          if (namesWithoutAggr[withoutAggr] <= 1) {
            d3g[d3gIndex].key = withoutAggr;
          }
        }
        // use group name instead of group.value as a column name, if there're only one group and one value selected.
        if (groups.length === 1 && values.length === 1) {
          for (d3gIndex = 0; d3gIndex < d3g.length; d3gIndex++) {
            var colName = d3g[d3gIndex].key;
            colName = colName.split('.')[0];
            d3g[d3gIndex].key = colName;
          }
        }
      }
      return {
        xLabels: rowIndexValue,
        d3g: d3g
      };
    };
    $scope.setGraphHeight = function () {
      var height = $('#p' + $scope.paragraph.id + '_graph').height();
      var newParams = angular.copy($scope.paragraph.settings.params);
      var newConfig = angular.copy($scope.paragraph.config);
      newConfig.graph.height = height;
      websocketMsgSrv.commitParagraph($scope.paragraph.id, $scope.paragraph.title, $scope.paragraph.text, newConfig, newParams);
    };
    /** Utility function */
    if (typeof String.prototype.startsWith !== 'function') {
      String.prototype.startsWith = function (str) {
        return this.slice(0, str.length) === str;
      };
    }
    $scope.goToSingleParagraph = function () {
      console.log('### PARAGRAPH.JS -> goToSingleParagraph (active note) ' + $scope.paragraph.noteId);
      console.log('### PARAGRAPH.JS -> goToSingleParagraph ' + location.protocol + '//' + location.host + '/#/notebook/' + $scope.paragraph.noteId + '/paragraph/' + $scope.paragraph.id + '?asIframe');
      var redirectToUrl = location.protocol + '//' + location.host + '/#/notebook/' + $scope.paragraph.noteId + '/paragraph/' + $scope.paragraph.id + '?asIframe';
      $window.open(redirectToUrl);
    };
  }
]);
//})()
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
 * @ngdoc directive
 * @name zeppelinWebApp.directive:ngEnter
 * @description
 * # ngEnter
 * Bind the <enter> event
 *
 * @author anthonycorbacho
 */
angular.module('notebookWebApp').directive('ngEnter', function () {
  return function (scope, element, attrs) {
    element.bind('keydown keypress', function (event) {
      if (event.which === 13) {
        scope.$apply(function () {
          scope.$eval(attrs.ngEnter);
        });
        event.preventDefault();
      }
    });
  };
});
'use strict';
angular.module('notebookWebApp').directive('dropdownInput', function () {
  return {
    restrict: 'A',
    link: function (scope, element) {
      element.bind('click', function (event) {
        event.stopPropagation();
      });
    }
  };
});
'use strict';
angular.module('notebookWebApp').directive('resizable', function () {
  var resizableConfig = {
      autoHide: true,
      handles: 'se',
      minHeight: 100,
      grid: [
        10000,
        10
      ]
    };
  return {
    restrict: 'A',
    scope: { callback: '&onResize' },
    link: function postLink(scope, elem) {
      elem.resizable(resizableConfig);
      elem.on('resizestop', function () {
        if (scope.callback) {
          scope.callback();
        }
      });
    }
  };
});
'use strict';
/**
 * @ngdoc directive
 * @name zeppelinWebApp.directive:delete
 * @description
 * # ngDelete
 */
angular.module('notebookWebApp').directive('ngDelete', function () {
  return function (scope, element, attrs) {
    element.bind('keydown keypress', function (event) {
      if (event.which === 27 || event.which === 46) {
        scope.$apply(function () {
          scope.$eval(attrs.ngEnter);
        });
        event.preventDefault();
      }
    });
  };
});
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
'use strict';
angular.module('notebookWebApp').service('baseUrlSrv', function () {
  this.getPort = function () {
    var port = Number(location.port);
    if (!port) {
      port = 80;
      if (location.protocol === 'https:') {
        port = 443;
      }
    }
    //Exception for when running locally via grunt
    if (port === 3333 || port === 9000) {
      port = 8084;
    }
    return port + 1;
  };
  this.getWebsocketUrl = function () {
    var wsProtocol = location.protocol === 'https:' ? 'wss:' : 'ws:';
    return wsProtocol + '//' + location.hostname + ':' + this.getPort() + '/ws';
  };
  this.getRestApiBase = function () {
    var port = Number(location.port);
    if (port === 'undefined' || port === 0) {
      port = 80;
      if (location.protocol === 'https:') {
        port = 443;
      }
    }
    if (port === 3333 || port === 9000) {
      port = 8084;
    }
    return location.protocol + '//' + location.hostname + ':' + port + '/api';
  };
  var skipTrailingSlash = function (path) {
    return path.replace(/\/$/, '');
  };
});
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
'use strict';
angular.module('notebookWebApp').factory('notebookListDataFactory', function () {
  var notes = {};
  notes.list = [];
  notes.setNotes = function (notesList) {
    notes.list = angular.copy(notesList);
  };
  return notes;
});
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
angular.module('notebookWebApp').factory('websocketEvents', [
  '$rootScope',
  '$websocket',
  'baseUrlSrv',
  '$window',
  '$routeParams',
  function ($rootScope, $websocket, baseUrlSrv, $window, $routeParams) {
    //    websocketEvents.$inject = ['$rootScope', '$websocket' ,'baseUrlSrv', '$window', '$routeParams'];
    //    function websocketEvents($rootScope, $websocket, baseUrlSrv, $window, $routeParams) {
    var asIframe = $window.location.href.indexOf('asIframe') > -1 ? true : false;
    var websocketCalls = {};
    websocketCalls.ws = $websocket(baseUrlSrv.getWebsocketUrl());
    websocketCalls.ws.reconnectIfNotNormalClose = true;
    websocketCalls.ws.onOpen(function () {
      console.log('Websocket created');
      $rootScope.$broadcast('setConnectedStatus', true);
      setInterval(function () {
        websocketCalls.sendNewEvent({ op: 'PING' });
      }, 60000);
    });
    websocketCalls.sendNewEvent = function (data) {
      console.log('Send >> %o, %o', data.op, data);
      websocketCalls.ws.send(JSON.stringify(data));
    };
    websocketCalls.isConnected = function () {
      return websocketCalls.ws.socket.readyState === 1;
    };
    websocketCalls.ws.onMessage(function (event) {
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
        if (asIframe) {
          websocketCalls.sendNewEvent({
            op: 'GET_NOTE',
            data: { id: $routeParams.noteId }
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
    websocketCalls.ws.onError(function (event) {
      console.log('error message: ', event);
      $rootScope.$broadcast('setConnectedStatus', false);
    });
    websocketCalls.ws.onClose(function (event) {
      console.log('close message: ', event);
      $rootScope.$broadcast('setConnectedStatus', false);
    });
    return websocketCalls;
  }
]);
//})();
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
angular.module('notebookWebApp').service('websocketMsgSrv', [
  '$rootScope',
  'websocketEvents',
  function ($rootScope, websocketEvents) {
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
      websocketEvents.sendNewEvent({ op: 'NEW_NOTE' });
    }
    function deleteNotebook(noteId) {
      websocketEvents.sendNewEvent({
        op: 'DEL_NOTE',
        data: { id: noteId }
      });
    }
    function getNotebookList() {
      websocketEvents.sendNewEvent({ op: 'LIST_NOTES' });
    }
    function getNotebook(noteId) {
      websocketEvents.sendNewEvent({
        op: 'GET_NOTE',
        data: { id: noteId }
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
        data: { index: newIndex }
      });
    }
    function cancelParagraphRun(paragraphId) {
      websocketEvents.sendNewEvent({
        op: 'CANCEL_PARAGRAPH',
        data: { id: paragraphId }
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
        data: { path: importPath }
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
        data: { id: paragraphId }
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
        data: { paragraphsText: paragraphMap }
      });
    }
    function resetResults(paragraphMap) {
      websocketEvents.sendNewEvent({ op: 'RESET_RESULTS' });
    }
  }
]);
//})();
/**
 * Created by idiaz on 9/03/15.
 */
'use strict';
angular.module('Authentication').factory('AuthenticationService', [
  'Base64',
  '$http',
  '$cookieStore',
  '$rootScope',
  '$timeout',
  function (Base64, $http, $cookieStore, $rootScope, $timeout) {
    var service = {};
    service.Login = function (username, password, callback) {
      /* Dummy authentication for testing, uses $timeout to simulate api call
         ----------------------------------------------*/
      $timeout(function () {
        var response = { success: username === 'Stratio' && password === 'Stratio' };
        if (!response.success) {
          response.message = 'Username or password is incorrect';
        }
        callback(response);
      }, 1000);  /* Use this for real authentication
         ----------------------------------------------*/
                 //$http.post('/api/authenticate', { username: username, password: password })
                 //    .success(function (response) {
                 //        callback(response);
                 //    });
    };
    service.SetCredentials = function (username, password) {
      var authdata = Base64.encode(username + ':' + password);
      $rootScope.globals = {
        currentUser: {
          username: username,
          authdata: authdata
        }
      };
      $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata;
      // jshint ignore:line
      $cookieStore.put('globals', $rootScope.globals);
    };
    service.ClearCredentials = function () {
      $rootScope.globals = {};
      $cookieStore.remove('globals');
      $http.defaults.headers.common.Authorization = 'Basic ';
    };
    return service;
  }
]).factory('Base64', function () {
  /* jshint ignore:start */
  var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
  return {
    encode: function (input) {
      var output = '';
      var chr1, chr2, chr3 = '';
      var enc1, enc2, enc3, enc4 = '';
      var i = 0;
      do {
        chr1 = input.charCodeAt(i++);
        chr2 = input.charCodeAt(i++);
        chr3 = input.charCodeAt(i++);
        enc1 = chr1 >> 2;
        enc2 = (chr1 & 3) << 4 | chr2 >> 4;
        enc3 = (chr2 & 15) << 2 | chr3 >> 6;
        enc4 = chr3 & 63;
        if (isNaN(chr2)) {
          enc3 = enc4 = 64;
        } else if (isNaN(chr3)) {
          enc4 = 64;
        }
        output = output + keyStr.charAt(enc1) + keyStr.charAt(enc2) + keyStr.charAt(enc3) + keyStr.charAt(enc4);
        chr1 = chr2 = chr3 = '';
        enc1 = enc2 = enc3 = enc4 = '';
      } while (i < input.length);
      return output;
    },
    decode: function (input) {
      var output = '';
      var chr1, chr2, chr3 = '';
      var enc1, enc2, enc3, enc4 = '';
      var i = 0;
      // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
      var base64test = /[^A-Za-z0-9\+\/\=]/g;
      if (base64test.exec(input)) {
        window.alert('There were invalid base64 characters in the input text.\n' + 'Valid base64 characters are A-Z, a-z, 0-9, \'+\', \'/\',and \'=\'\n' + 'Expect errors in decoding.');
      }
      input = input.replace(/[^A-Za-z0-9\+\/\=]/g, '');
      do {
        enc1 = keyStr.indexOf(input.charAt(i++));
        enc2 = keyStr.indexOf(input.charAt(i++));
        enc3 = keyStr.indexOf(input.charAt(i++));
        enc4 = keyStr.indexOf(input.charAt(i++));
        chr1 = enc1 << 2 | enc2 >> 4;
        chr2 = (enc2 & 15) << 4 | enc3 >> 2;
        chr3 = (enc3 & 3) << 6 | enc4;
        output = output + String.fromCharCode(chr1);
        if (enc3 != 64) {
          output = output + String.fromCharCode(chr2);
        }
        if (enc4 != 64) {
          output = output + String.fromCharCode(chr3);
        }
        chr1 = chr2 = chr3 = '';
        enc1 = enc2 = enc3 = enc4 = '';
      } while (i < input.length);
      return output;
    }
  };  /* jshint ignore:end */
});