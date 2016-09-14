'use strict';

angular.module('clubmanagerApp')
	.controller('BoardMemberDeleteController', function($scope, $uibModalInstance, entity, BoardMember) {

        $scope.boardMember = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            BoardMember.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
