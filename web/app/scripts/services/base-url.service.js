/*
 *  Licensed to STRATIO (C) under one or more contributor license agreements.
 *  See the NOTICE file distributed with this work for additional information
 *  regarding copyright ownership.  The STRATIO (C) licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
 * based on NFlabs Zeppelin originaly forked on Nov'14
 */

'use strict';

angular.module('explorerWebApp').service('baseUrlSrv', function() {

  this.getPort = function() {
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
    return port+1;
  };

  this.getWebsocketUrl = function() {
    var wsProtocol = location.protocol === 'https:' ? 'wss:' : 'ws:';
    return wsProtocol + '//' + location.hostname + ':' + this.getPort() + '/ws';
  };

  this.getRestApiBase = function() {
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

  var skipTrailingSlash = function(path) {
    return path.replace(/\/$/, '');
  };

});
