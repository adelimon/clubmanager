'use strict';

angular.module('clubmanagerApp').controller('ScheduleDateDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'ScheduleDate', 'EventType',
        function($scope, $stateParams, $uibModalInstance, entity, ScheduleDate, EventType) {

        $scope.scheduleDate = entity;
        $scope.eventtypes = EventType.query();
        $scope.load = function(id) {
            ScheduleDate.get({id : id}, function(result) {
                $scope.scheduleDate = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:scheduleDateUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.scheduleDate.id != null) {
                ScheduleDate.update($scope.scheduleDate, onSaveSuccess, onSaveError);
            } else {
                ScheduleDate.save($scope.scheduleDate, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForDate = {};

        $scope.datePickerForDate.status = {
            opened: false
        };

        $scope.datePickerForDateOpen = function($event) {
            $scope.datePickerForDate.status.opened = true;
        };
}]);
