'use strict';

angular.module('clubmanagerApp').controller('SignupDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Signup', 'Member', 'ScheduleDate', 'Job',
        function($scope, $stateParams, $uibModalInstance, entity, Signup, Member, ScheduleDate, Job) {

        $scope.signup = entity;
        $scope.members = Member.query();
        $scope.scheduledates = ScheduleDate.query();
        $scope.jobs = Job.query();
        $scope.load = function(id) {
            Signup.get({id : id}, function(result) {
                $scope.signup = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:signupUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.signup.id != null) {
                Signup.update($scope.signup, onSaveSuccess, onSaveError);
            } else {
                Signup.save($scope.signup, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        $scope.dateHandler = function (scheduleDate) {
            console.log("changed da date to " + JSON.stringify(scheduleDate) );
            var dateSelected = scheduleDate.date;
            $scope.jobs = Job.query();
        }
}]);
