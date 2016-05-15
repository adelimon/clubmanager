'use strict';

angular.module('clubmanagerApp')
	.controller('PaidSignupDeleteController', function($scope, $uibModalInstance, entity, PaidSignup) {

        $scope.paidSignup = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            PaidSignup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
