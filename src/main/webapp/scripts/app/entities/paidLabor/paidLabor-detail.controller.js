'use strict';

angular.module('clubmanagerApp')
    .controller('PaidLaborDetailController', function ($scope, $rootScope, $stateParams, entity, PaidLabor, Member) {
        $scope.paidLabor = entity;
        $scope.load = function (id) {
            PaidLabor.get({id: id}, function(result) {
                $scope.paidLabor = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:paidLaborUpdate', function(event, result) {
            $scope.paidLabor = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
