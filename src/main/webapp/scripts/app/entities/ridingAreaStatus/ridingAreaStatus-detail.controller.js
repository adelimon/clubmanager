'use strict';

angular.module('clubmanagerApp')
    .controller('RidingAreaStatusDetailController', function ($scope, $rootScope, $stateParams, entity, RidingAreaStatus) {
        $scope.ridingAreaStatus = entity;
        $scope.load = function (id) {
            RidingAreaStatus.get({id: id}, function(result) {
                $scope.ridingAreaStatus = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:ridingAreaStatusUpdate', function(event, result) {
            $scope.ridingAreaStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
