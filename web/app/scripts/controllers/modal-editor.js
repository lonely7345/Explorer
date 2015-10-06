 * based on NFlabs Zeppelin originaly forked on Nov'14
 */
//(function() {
    'use strict';
    angular
        .module('ExplorerWebApp')
        .controller('ModalEditorCtrl', function( $modalInstance, properties, resolvePath, $http) {
    /**
     * @ngdoc function
     * @name explorerWebApp.controller:ModalEditorCtrl
     * @description
     * # ModalEditorCtrl
     * Controller of modal window for file edition
     *
     * @author ivdiaz
     *
     */
        $scope.file = {};
        $scope.file.text = properties.data.body;
        $scope.saveEditorSettings = function() {
            var message = resolvePath + "~" + $scope.file.text;
            console.log("saveEditorSettings");
            $http.put(getRestApiBase() + '/interpreter/settings/editor', message).
            success(function(data, status, headers, config) {
                alert('Editor settings saved');
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
