 * based on NFlabs Zeppelin originaly forked on Nov'14
 */


//(function() {
    'use strict';
//    angular.module('explorerWebApp').controller('MainCtrl', MainCtrl);
    /**
     * @ngdoc function
     * @name explorerWebApp.controller:MainCtrl
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

    angular.module('explorerWebApp').controller('MainCtrl', function($scope, $rootScope, $window){
        $rootScope.connected = true;
        $scope.looknfeel = 'simple';
        $scope.lastChangedNotebookId = ''
        $scope.lastChangedNotebookDate = '';
        //    $scope.lastParagraphRunId = "";
        var init = function() {
                $scope.asIframe = (($window.location.href.indexOf('asIframe') > -1) ? true : false);
            };
        init();

        $rootScope.$on('setIframe', function(event, data) {
            if (!event.defaultPrevented) {
                $scope.asIframe = data;
                event.preventDefault();
            }
        });
        $rootScope.$on('setLookAndFeel', function(event, data) {
            if (!event.defaultPrevented && data && data !== '') {
                //            console.log("MAIN.JS -setLookAndFeel - "+ data);
                $scope.looknfeel = data;
                event.preventDefault();
            }
        });
        $rootScope.$on('changeActiveNotebook', function(event, data) {
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
    });
//})()
