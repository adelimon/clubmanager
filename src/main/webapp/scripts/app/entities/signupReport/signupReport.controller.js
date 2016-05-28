'use strict';

angular.module('clubmanagerApp')
    .controller('SignupReportController', function ($scope, $state, SignupReport, ParseLinks) {

        $scope.signupReports = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            SignupReport.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.signupReports.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.signupReports = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.signupReport = {
                name: null,
                title: null,
                pointValue: null,
                cashValue: null,
                reserved: null,
                jobDay: null,
                leader: null,
                date: null,
                id: null
            };
        };
    });
