'use strict';

angular.module('clubmanagerApp')
	.controller('SignupDeleteController', function($scope, $uibModalInstance, entity, Signup) {

        $scope.signup = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Signup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
