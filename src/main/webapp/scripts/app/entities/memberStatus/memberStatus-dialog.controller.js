'use strict';

angular.module('clubmanagerApp').controller('MemberStatusDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'MemberStatus', 'Member', 'MemberTypes',
        function($scope, $stateParams, $uibModalInstance, entity, MemberStatus, Member, MemberTypes) {

        $scope.memberStatus = entity;
        $scope.members = Member.query();
        $scope.membertypess = MemberTypes.query();
        $scope.load = function(id) {
            MemberStatus.get({id : id}, function(result) {
                $scope.memberStatus = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:memberStatusUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.memberStatus.id != null) {
                MemberStatus.update($scope.memberStatus, onSaveSuccess, onSaveError);
            } else {
                MemberStatus.save($scope.memberStatus, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
