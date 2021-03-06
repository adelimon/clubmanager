'use strict';

angular.module('clubmanagerApp')
    .controller('JobController', function ($scope, $state, $http, Job, ParseLinks) {

        $scope.jobs = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Job.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.jobs.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.jobs = [];
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
            $scope.job = {
                title: null,
                pointValue: null,
                cashValue: null,
                jobDay: null,
                sortOrder: null,
                reserved: null,
                id: null
            };
        };
        $scope.clearFilter = function() {
            $scope.searchText = "";
        };

        $scope.setFilter = function(filterString) {
            $scope.searchText = filterString;
        }

        $scope.cloneJob = function(job) {
            $http.get('/api/jobs/clone/' + job.id + "/1").then(
                function() {
                    $scope.refresh();
                },
                function() {
                    alert("Job clone failed");
                }
            );
        }
    });
