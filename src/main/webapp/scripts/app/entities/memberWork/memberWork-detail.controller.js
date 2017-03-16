'use strict';

angular.module('clubmanagerApp')
    .controller('MemberWorkDetailController', function ($scope, $rootScope, $stateParams, entity, MemberWork, Member) {
        $scope.memberWork = entity;
        $scope.load = function (id) {
            MemberWork.get({id: id}, function(result) {
                $scope.memberWork = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:memberWorkUpdate', function(event, result) {
            $scope.memberWork = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
