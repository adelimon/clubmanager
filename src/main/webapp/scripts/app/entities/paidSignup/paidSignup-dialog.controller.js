'use strict';

angular.module('clubmanagerApp').controller('PaidSignupDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaidSignup', 'PaidLabor', 'ScheduleDate', 'Job',
        function($scope, $stateParams, $uibModalInstance, entity, PaidSignup, PaidLabor, ScheduleDate, Job) {

        $scope.paidSignup = entity;
        $scope.paidlabors = PaidLabor.query();
        $scope.scheduledates = ScheduleDate.query();
        $scope.jobs = Job.query({id:'paid'});
        $scope.load = function(id) {
            PaidSignup.get({id : id}, function(result) {
                $scope.paidSignup = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:paidSignupUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.paidSignup.id != null) {
                PaidSignup.update($scope.paidSignup, onSaveSuccess, onSaveError);
            } else {
                PaidSignup.save($scope.paidSignup, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
