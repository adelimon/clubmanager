'use strict';

angular.module('clubmanagerApp')
    .controller('SignupDetailController', function ($scope, $rootScope, $stateParams, entity, Signup, Member, ScheduleDate, Job) {
        $scope.signup = entity;
        $scope.load = function (id) {
            Signup.get({id: id}, function(result) {
                $scope.signup = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:signupUpdate', function(event, result) {
            $scope.signup = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
