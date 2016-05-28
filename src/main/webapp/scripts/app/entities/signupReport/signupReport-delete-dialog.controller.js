'use strict';

angular.module('clubmanagerApp')
	.controller('SignupReportDeleteController', function($scope, $uibModalInstance, entity, SignupReport) {

        $scope.signupReport = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            SignupReport.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
