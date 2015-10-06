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
angular.module('explorerWebApp').directive('ngEnter', function() {
  return function(scope, element, attrs) {
    element.bind('keydown keypress', function(event) {
      if (event.which === 13) {
        scope.$apply(function() {
          scope.$eval(attrs.ngEnter);
        });
        event.preventDefault();
      }
    });
  };
});
