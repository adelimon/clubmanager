'use strict';

angular.module('clubmanagerApp')
	.controller('EventTypeDeleteController', function($scope, $uibModalInstance, entity, EventType) {

        $scope.eventType = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            EventType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
