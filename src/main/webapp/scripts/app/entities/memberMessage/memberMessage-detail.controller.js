'use strict';

angular.module('clubmanagerApp')
    .controller('MemberMessageDetailController', function ($scope, $rootScope, $stateParams, entity, MemberMessage) {
        $scope.memberMessage = entity;
        $scope.load = function (id) {
            MemberMessage.get({id: id}, function(result) {
                $scope.memberMessage = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:memberMessageUpdate', function(event, result) {
            $scope.memberMessage = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
