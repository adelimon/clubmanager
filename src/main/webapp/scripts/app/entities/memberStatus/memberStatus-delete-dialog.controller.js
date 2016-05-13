'use strict';

angular.module('clubmanagerApp')
	.controller('MemberStatusDeleteController', function($scope, $uibModalInstance, entity, MemberStatus) {

        $scope.memberStatus = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MemberStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
