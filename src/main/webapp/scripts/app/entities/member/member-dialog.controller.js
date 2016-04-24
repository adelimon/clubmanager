'use strict';

angular.module('clubmanagerApp').controller('MemberDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Member',
        function($scope, $stateParams, $uibModalInstance, entity, Member) {

        $scope.member = entity;
        $scope.load = function(id) {
            Member.get({id : id}, function(result) {
                $scope.member = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:memberUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.member.id != null) {
                Member.update($scope.member, onSaveSuccess, onSaveError);
            } else {
                Member.save($scope.member, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForBirthday = {};

        $scope.datePickerForBirthday.status = {
            opened: false
        };

        $scope.datePickerForBirthdayOpen = function($event) {
            $scope.datePickerForBirthday.status.opened = true;
        };
        $scope.datePickerForDateJoined = {};

        $scope.datePickerForDateJoined.status = {
            opened: false
        };

        $scope.datePickerForDateJoinedOpen = function($event) {
            $scope.datePickerForDateJoined.status.opened = true;
        };
}]);
