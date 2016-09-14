'use strict';

angular.module('clubmanagerApp')
    .controller('BoardMemberDetailController', function ($scope, $rootScope, $stateParams, entity, BoardMember, Member) {
        $scope.boardMember = entity;
        $scope.load = function (id) {
            BoardMember.get({id: id}, function(result) {
                $scope.boardMember = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:boardMemberUpdate', function(event, result) {
            $scope.boardMember = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
