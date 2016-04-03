'use strict';

angular.module('clubmanagerApp').controller('EarnedPointsDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'EarnedPoints', 'Member', 'EventType',
        function($scope, $stateParams, $uibModalInstance, entity, EarnedPoints, Member, EventType) {

        $scope.earnedPoints = entity;
        $scope.members = Member.query();
        $scope.eventtypes = EventType.query();
        $scope.load = function(id) {
            EarnedPoints.get({id : id}, function(result) {
                $scope.earnedPoints = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:earnedPointsUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.earnedPoints.id != null) {
                EarnedPoints.update($scope.earnedPoints, onSaveSuccess, onSaveError);
            } else {
                EarnedPoints.save($scope.earnedPoints, onSaveSuccess, onSaveError);
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
