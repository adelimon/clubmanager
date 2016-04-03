'use strict';

angular.module('clubmanagerApp').controller('EventTypeDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'EventType',
        function($scope, $stateParams, $uibModalInstance, entity, EventType) {

        $scope.eventType = entity;
        $scope.load = function(id) {
            EventType.get({id : id}, function(result) {
                $scope.eventType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:eventTypeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.eventType.id != null) {
                EventType.update($scope.eventType, onSaveSuccess, onSaveError);
            } else {
                EventType.save($scope.eventType, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
