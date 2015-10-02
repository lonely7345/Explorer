'use strict';

angular.module('explorerWebApp').directive('resizable', function () {
    var resizableConfig = {
        autoHide: true,
        handles: 'se',
        minHeight:100,
        grid: [10000, 10]  // allow only vertical
    };

    return {
        restrict: 'A',
        scope: {
            callback: '&onResize'
        },
        link: function postLink(scope, elem) {
            elem.resizable(resizableConfig);
            elem.on('resizestop', function () {
                if (scope.callback) { scope.callback(); }
            });
        }
    };
});
