'use strict';

angular.module('clubmanagerApp').controller('SignupReportDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'SignupReport',
        function($scope, $stateParams, $uibModalInstance, entity, SignupReport) {

        $scope.signupReport = entity;
        $scope.load = function(id) {
            SignupReport.get({id : id}, function(result) {
                $scope.signupReport = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:signupReportUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.signupReport.id != null) {
                SignupReport.update($scope.signupReport, onSaveSuccess, onSaveError);
            } else {
                SignupReport.save($scope.signupReport, onSaveSuccess, onSaveError);
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
