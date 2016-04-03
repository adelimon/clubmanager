'use strict';

angular.module('clubmanagerApp')
    .controller('MemberDetailController', function ($scope, $rootScope, $stateParams, entity, Member) {
        $scope.member = entity;
        $scope.load = function (id) {
            Member.get({id: id}, function(result) {
                $scope.member = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:memberUpdate', function(event, result) {
            $scope.member = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
