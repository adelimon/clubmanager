'use strict';

angular.module('clubmanagerApp')
	.controller('RidingAreaStatusDeleteController', function($scope, $uibModalInstance, entity, RidingAreaStatus) {

        $scope.ridingAreaStatus = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            RidingAreaStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
