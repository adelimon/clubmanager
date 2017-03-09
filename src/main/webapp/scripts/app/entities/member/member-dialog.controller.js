'use strict';

angular.module('clubmanagerApp').controller('MemberDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Member', 'MemberTypes',
        function($scope, $stateParams, $uibModalInstance, entity, Member, MemberTypes) {

        $scope.member = entity;
        $scope.memberTypes = MemberTypes.query();
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

        $scope.preFillFields = function(status) {
            var isPaidLabor = (status.type === "Paid Labor");
            // this is a paid labor entry, then set the default fields, since we don't care about them anyway.
            if (isPaidLabor) {
                $scope.member.address = "4343 Hogback Hill Road";
                $scope.member.city = "Palmyra";
                $scope.member.state = "NY";
                $scope.member.zip = "14522";
                $scope.member.phone = "585-555-1234";
                $scope.member.email = "n/a"
            }
            $scope.member.state = "NY";
            $scope.member.viewOnline = true;
        };
}]);
