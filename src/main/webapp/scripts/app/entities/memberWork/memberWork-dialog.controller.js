'use strict';

angular.module('clubmanagerApp').controller('MemberWorkDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'MemberWork', 'Member',
        function($scope, $stateParams, $uibModalInstance, entity, MemberWork, Member) {

        $scope.memberWork = entity;
        $scope.members = Member.query();
        $scope.load = function(id) {
            MemberWork.get({id : id}, function(result) {
                $scope.memberWork = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:memberWorkUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.memberWork.id != null) {
                MemberWork.update($scope.memberWork, onSaveSuccess, onSaveError);
            } else {
                MemberWork.save($scope.memberWork, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForStart = {};

        $scope.datePickerForStart.status = {
            opened: false
        };

        $scope.datePickerForStartOpen = function($event) {
            $scope.datePickerForStart.status.opened = true;
        };
        $scope.datePickerForEnd = {};

        $scope.datePickerForEnd.status = {
            opened: false
        };

        $scope.datePickerForEndOpen = function($event) {
            $scope.datePickerForEnd.status.opened = true;
        };
}]);
