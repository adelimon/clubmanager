'use strict';

angular.module('clubmanagerApp')
	.controller('ScheduleDateDeleteController', function($scope, $uibModalInstance, entity, ScheduleDate) {

        $scope.scheduleDate = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            ScheduleDate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
