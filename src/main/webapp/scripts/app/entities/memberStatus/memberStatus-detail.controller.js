'use strict';

angular.module('clubmanagerApp')
    .controller('MemberStatusDetailController', function ($scope, $rootScope, $stateParams, entity, MemberStatus, Member, MemberTypes) {
        $scope.memberStatus = entity;
        $scope.load = function (id) {
            MemberStatus.get({id: id}, function(result) {
                $scope.memberStatus = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:memberStatusUpdate', function(event, result) {
            $scope.memberStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
