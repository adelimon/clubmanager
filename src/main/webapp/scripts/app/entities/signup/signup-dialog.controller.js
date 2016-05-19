'use strict';

angular.module('clubmanagerApp').controller('SignupDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Signup', 'Member', 'ScheduleDate', 'Job', 'PaidSignup',
        function($scope, $stateParams, $uibModalInstance, entity, Signup, Member, ScheduleDate, Job, PaidSignup) {

        $scope.signup = entity;
        $scope.members = Member.query({id: 'visible'});
        $scope.scheduledates = ScheduleDate.query();
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
            // tell the back end what date ID we want and let him figure out the rest for us.
            // this gets a consistent experience and doesn't rely on the angular events in the
            // DOM happening in the correct order while you hop on one foot while dancing a jig
            // because the hipsters like it webscale.  This is the opposite of "bad ass rock star tech", which is
            // called "shit that works".
            $scope.jobs = Job.query({id: 'availOn'+scheduleDate.id});
        }
}]);
