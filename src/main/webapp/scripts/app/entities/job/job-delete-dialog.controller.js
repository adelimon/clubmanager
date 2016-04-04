'use strict';

angular.module('clubmanagerApp')
	.controller('JobDeleteController', function($scope, $uibModalInstance, entity, Job) {

        $scope.job = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Job.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
