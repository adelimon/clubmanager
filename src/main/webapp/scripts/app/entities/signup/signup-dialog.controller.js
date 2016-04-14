'use strict';

angular.module('clubmanagerApp').controller('SignupDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Signup', 'Member', 'ScheduleDate', 'Job',
        function($scope, $stateParams, $uibModalInstance, entity, Signup, Member, ScheduleDate, Job) {

        $scope.signup = entity;
        $scope.members = Member.query();
        $scope.scheduledates = ScheduleDate.query();
        $scope.allJobs = Job.query();
        $scope.allSignups = Signup.query();
        $scope.jobs = new Array();
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
            console.log("changed date to " + JSON.stringify(scheduleDate) );
            var dateSelected = scheduleDate.date;
            // get all jobs
            var allJobs = $scope.allJobs;

            // first load job types for the user selected date.  We do this by getting the event type and matching
            // it against the job type.
            var eventTypeId = scheduleDate.eventType.id;

            for (var index = 0; index < allJobs.length; index++) {
                console.log("a job is " + JSON.stringify(allJobs[index]));
                var job = allJobs[index];
                var isEventTypeJob = (eventTypeId === job.eventType.id);
                if (isEventTypeJob) {
                    $scope.jobs.push(job);
                }
            }

        }
}]);
