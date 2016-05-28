'use strict';

angular.module('clubmanagerApp')
    .controller('SignupReportDetailController', function ($scope, $rootScope, $stateParams, entity, SignupReport) {
        $scope.signupReport = entity;
        $scope.load = function (id) {
            SignupReport.get({id: id}, function(result) {
                $scope.signupReport = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:signupReportUpdate', function(event, result) {
            $scope.signupReport = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
