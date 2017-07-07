'use strict';

angular.module('clubmanagerApp')
    .controller('SignupReportController', function ($scope, $state, $http, SignupReport, ParseLinks) {

        $scope.signupReports = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            SignupReport.query({page: $scope.page, size: 500}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    result[i].isTaken = !(result[i].name === null);
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

        $scope.signUpFromLink = function(signupInfo) {
            var userSignup = new Object();
            userSignup.job = new Object();
            userSignup.job.id = signupInfo.jobId;
            userSignup.scheduleDate = new Object();
            userSignup.scheduleDate.id = signupInfo.scheduleDateId;

            $http.post('/api/signups/me', userSignup).then(
                function() {
                    $scope.refresh();
                },
                function() {
                    alert("Your signup had an error.");
                }
            );
        };

        $scope.deleteSignup = function(signupInfo) {
            $http.delete('/api/signups/' + signupInfo.signupId).then(
                function() {
                    $scope.refresh();
                },
                function() {
                    alert("couldn't delete the signup ");
                }
            )
        }
        $scope.clearFilter = function() {
            $scope.searchText = "";
        };

        $scope.setFilter = function(filterString) {
            $scope.searchText = filterString;
        }
    });
