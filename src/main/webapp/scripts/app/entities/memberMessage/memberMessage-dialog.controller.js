'use strict';

angular.module('clubmanagerApp').controller('MemberMessageDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'MemberMessage',
        function($scope, $stateParams, $uibModalInstance, entity, MemberMessage) {

        $scope.memberMessage = entity;
        $scope.load = function(id) {
            MemberMessage.get({id : id}, function(result) {
                $scope.memberMessage = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:memberMessageUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.memberMessage.id != null) {
                MemberMessage.update($scope.memberMessage, onSaveSuccess, onSaveError);
            } else {
                MemberMessage.save($scope.memberMessage, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
