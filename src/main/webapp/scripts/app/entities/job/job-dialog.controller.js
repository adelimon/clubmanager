'use strict';

angular.module('clubmanagerApp').controller('JobDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Job', 'EventType', 'Member',
        function($scope, $stateParams, $uibModalInstance, entity, Job, EventType, Member) {

        $scope.job = entity;
            $scope.job.online = true;
        $scope.eventtypes = EventType.query();
        $scope.members = Member.query();
        $scope.load = function(id) {
            Job.get({id : id}, function(result) {
                $scope.job = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:jobUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.job.id != null) {
                Job.update($scope.job, onSaveSuccess, onSaveError);
            } else {
                Job.save($scope.job, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
