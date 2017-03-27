'use strict';

angular.module('clubmanagerApp').controller('IntegrationDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Integration',
        function($scope, $stateParams, $uibModalInstance, entity, Integration) {

        $scope.integration = entity;
        $scope.load = function(id) {
            Integration.get({id : id}, function(result) {
                $scope.integration = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:integrationUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.integration.id != null) {
                Integration.update($scope.integration, onSaveSuccess, onSaveError);
            } else {
                Integration.save($scope.integration, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
