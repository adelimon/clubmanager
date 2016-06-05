'use strict';

angular.module('clubmanagerApp').controller('RidingAreaStatusDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'RidingAreaStatus',
        function($scope, $stateParams, $uibModalInstance, entity, RidingAreaStatus) {

        $scope.ridingAreaStatus = entity;
        $scope.load = function(id) {
            RidingAreaStatus.get({id : id}, function(result) {
                $scope.ridingAreaStatus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:ridingAreaStatusUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.ridingAreaStatus.id != null) {
                RidingAreaStatus.update($scope.ridingAreaStatus, onSaveSuccess, onSaveError);
            } else {
                RidingAreaStatus.save($scope.ridingAreaStatus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
