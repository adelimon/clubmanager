'use strict';

angular.module('clubmanagerApp')
    .controller('ScheduleDateDetailController', function ($scope, $rootScope, $stateParams, entity, ScheduleDate, EventType) {
        $scope.scheduleDate = entity;
        $scope.load = function (id) {
            ScheduleDate.get({id: id}, function(result) {
                $scope.scheduleDate = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:scheduleDateUpdate', function(event, result) {
            $scope.scheduleDate = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
