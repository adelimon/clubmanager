'use strict';

angular.module('clubmanagerApp')
    .controller('PaidSignupDetailController', function ($scope, $rootScope, $stateParams, entity, PaidSignup, PaidLabor, ScheduleDate, Job) {
        $scope.paidSignup = entity;
        $scope.load = function (id) {
            PaidSignup.get({id: id}, function(result) {
                $scope.paidSignup = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:paidSignupUpdate', function(event, result) {
            $scope.paidSignup = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
