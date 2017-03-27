'use strict';

angular.module('clubmanagerApp')
	.controller('IntegrationDeleteController', function($scope, $uibModalInstance, entity, Integration) {

        $scope.integration = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Integration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
