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

    angular.module('Authentication').controller('LoginCtrl', function($scope, $rootScope, $location,
    AuthenticationService){
        // reset login status
        AuthenticationService.ClearCredentials();
        $scope.login = function() {
            $scope.dataLoading = true;
            AuthenticationService.Login($scope.username, $scope.password, function(response) {
                if (response.success) {
                    AuthenticationService.SetCredentials($scope.username, $scope.password);
                    $location.path('/');
                } else {
                    $scope.error = response.message;
                    $scope.dataLoading = false;
                }
            });
        };
    });
//})()