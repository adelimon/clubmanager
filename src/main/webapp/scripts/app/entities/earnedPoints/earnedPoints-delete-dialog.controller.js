'use strict';

angular.module('clubmanagerApp')
	.controller('EarnedPointsDeleteController', function($scope, $uibModalInstance, entity, EarnedPoints) {

        $scope.earnedPoints = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            EarnedPoints.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
