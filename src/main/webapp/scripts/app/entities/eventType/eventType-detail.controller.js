'use strict';

angular.module('clubmanagerApp')
    .controller('EventTypeDetailController', function ($scope, $rootScope, $stateParams, entity, EventType) {
        $scope.eventType = entity;
        $scope.load = function (id) {
            EventType.get({id: id}, function(result) {
                $scope.eventType = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:eventTypeUpdate', function(event, result) {
            $scope.eventType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
