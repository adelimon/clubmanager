'use strict';

angular.module('clubmanagerApp')
    .controller('EarnedPointsDetailController', function ($scope, $rootScope, $stateParams, entity, EarnedPoints, Member, EventType) {
        $scope.earnedPoints = entity;
        $scope.load = function (id) {
            EarnedPoints.get({id: id}, function(result) {
                $scope.earnedPoints = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:earnedPointsUpdate', function(event, result) {
            $scope.earnedPoints = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
