'use strict';

angular.module('clubmanagerApp').controller('PaidLaborDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaidLabor', 'Member',
        function($scope, $stateParams, $uibModalInstance, entity, PaidLabor, Member) {

        $scope.paidLabor = entity;
        $scope.members = Member.query();
        $scope.load = function(id) {
            PaidLabor.get({id : id}, function(result) {
                $scope.paidLabor = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:paidLaborUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.paidLabor.id != null) {
                PaidLabor.update($scope.paidLabor, onSaveSuccess, onSaveError);
            } else {
                PaidLabor.save($scope.paidLabor, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
