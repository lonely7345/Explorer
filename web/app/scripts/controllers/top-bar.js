 * based on NFlabs Zeppelin originaly forked on Nov'14
 */

    'use strict';
    /**
     * @ngdoc function
     * @name explorerWebApp.controller:TopBarCtrl
     * @description
     * # TopBarCtrl
     * Controller of the top navigation, mainly use for the dropdown menu
     *
     * @author anthonycorbacho
     * @author ivdiaz
     *
     */

    angular.module('explorerWebApp').controller('TopBarCtrl', function($scope, $rootScope, AuthenticationService,
    $modal, $http, baseUrlSrv){


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
                templateUrl: '/views/modal-settings.html',
                windowClass: 'center-modal'
            });
        };
        $rootScope.$on('openPropertiesEditor', function(event, path) {
                    $scope.openPropertiesEditor(path);
        });
        $scope.openPropertiesEditor = function(path) {
            console.log(path);
            var modalInstance = $modal.open({
                animation: true,
                controller: 'ModalEditCtrl',
                templateUrl: '/views/modal-editor.html',
                windowClass: 'center-modal',
                resolve: {
                    properties: function() {
                        return $http.get(baseUrlSrv.getRestApiBase() + '/interpreter/settings/editor?path=' + path).
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
    });
