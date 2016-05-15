'use strict';

angular.module('clubmanagerApp').controller('SignupDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Signup', 'Member', 'ScheduleDate', 'Job',
        function($scope, $stateParams, $uibModalInstance, entity, Signup, Member, ScheduleDate, Job) {

        $scope.signup = entity;
        $scope.members = Member.query({id: 'visible'});
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
            var allSignups = $scope.allSignups;
            $scope.jobs = [];

            // first load job types for the user selected date.  We do this by getting the event type and matching
            // it against the job type.
            var eventTypeId = scheduleDate.eventType.id;
            var eventId = scheduleDate.id;

            // This is kind of a hack to get this working without mucking with the back end too much because I
            // can't figure out how get all this angular sh1t to work with the Java back end in time so I did this.
            // Basically we grab ALL the signups, then filter on the ones for the given date.  Then we go through
            // all the jobs and only add the ones that are for the event type and not already taken.  Eventually
            // I will move this to the back end, since the performance of this isn't going to be wonderful.  But it
            // did only take an hour to write. :)
            var dateSignups = new Array();
            for (var index = 0; index < allSignups.length; index++) {
                var signup = allSignups[index];
                if (signup.scheduleDate.id == eventId) {
                    dateSignups.push(signup.job.id);
                }
            }

            var jobIdsForDate = dateSignups.join("#");
            for (var index = 0; index < allJobs.length; index++) {
                //console.log("a job is " + JSON.stringify(allJobs[index]));
                var job = allJobs[index];
                var isEventTypeJob = (eventTypeId === job.eventType.id);
                var isTaken = (jobIdsForDate.indexOf(job.id) >= 0);
                // if the job's event type matches mine, then add the job to the list
                if (isEventTypeJob && !isTaken) {
                    $scope.jobs.push(job);
                }
            }

        }
}]);
