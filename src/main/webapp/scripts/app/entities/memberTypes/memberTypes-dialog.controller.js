'use strict';

angular.module('clubmanagerApp').controller('MemberTypesDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'MemberTypes',
        function($scope, $stateParams, $uibModalInstance, entity, MemberTypes) {

        $scope.memberTypes = entity;
        $scope.load = function(id) {
            MemberTypes.get({id : id}, function(result) {
                $scope.memberTypes = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:memberTypesUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.memberTypes.id != null) {
                MemberTypes.update($scope.memberTypes, onSaveSuccess, onSaveError);
            } else {
                MemberTypes.save($scope.memberTypes, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
