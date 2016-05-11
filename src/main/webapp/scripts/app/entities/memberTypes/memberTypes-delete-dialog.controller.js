'use strict';

angular.module('clubmanagerApp')
	.controller('MemberTypesDeleteController', function($scope, $uibModalInstance, entity, MemberTypes) {

        $scope.memberTypes = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MemberTypes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
