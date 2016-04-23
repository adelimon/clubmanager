'use strict';

angular.module('clubmanagerApp').controller('MemberNoteDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'MemberNote', 'Member',
        function($scope, $stateParams, $uibModalInstance, entity, MemberNote, Member) {

        $scope.memberNote = entity;
        $scope.members = Member.query();
        $scope.load = function(id) {
            MemberNote.get({id : id}, function(result) {
                $scope.memberNote = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:memberNoteUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.memberNote.id != null) {
                MemberNote.update($scope.memberNote, onSaveSuccess, onSaveError);
            } else {
                MemberNote.save($scope.memberNote, onSaveSuccess, onSaveError);
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
