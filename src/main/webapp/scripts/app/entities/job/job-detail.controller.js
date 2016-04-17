'use strict';

angular.module('clubmanagerApp')
    .controller('JobDetailController', function ($scope, $rootScope, $stateParams, entity, Job, EventType, Member) {
        $scope.job = entity;
        $scope.load = function (id) {
            Job.get({id: id}, function(result) {
                $scope.job = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:jobUpdate', function(event, result) {
            $scope.job = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
