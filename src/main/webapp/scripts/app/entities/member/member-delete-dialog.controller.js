'use strict';

angular.module('clubmanagerApp')
	.controller('MemberDeleteController', function($scope, $uibModalInstance, entity, Member) {

        $scope.member = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Member.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
