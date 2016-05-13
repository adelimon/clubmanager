'use strict';

angular.module('clubmanagerApp')
	.controller('PaidLaborDeleteController', function($scope, $uibModalInstance, entity, PaidLabor) {

        $scope.paidLabor = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            PaidLabor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
