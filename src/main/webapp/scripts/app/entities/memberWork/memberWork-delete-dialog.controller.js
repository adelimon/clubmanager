'use strict';

angular.module('clubmanagerApp')
	.controller('MemberWorkDeleteController', function($scope, $uibModalInstance, entity, MemberWork) {

        $scope.memberWork = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MemberWork.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
