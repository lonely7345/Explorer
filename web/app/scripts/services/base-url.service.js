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
