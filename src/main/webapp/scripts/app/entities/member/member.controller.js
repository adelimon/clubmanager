'use strict';

angular.module('clubmanagerApp')
    .controller('MemberController', function ($scope, $state, Member, ParseLinks) {

        $scope.members = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Member.query({page: $scope.page, size: 200, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.members.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.members = [];
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
            $scope.member = {
                firstName: null,
                lastName: null,
                address: null,
                city: null,
                state: null,
                zip: null,
                occupation: null,
                phone: null,
                viewOnline: null,
                email: null,
                birthday: null,
                dateJoined: null,
                id: null
            };
        };
        $scope.clearFilter = function() {
            $scope.searchText = "";
        };

        $scope.setFilter = function(filterString) {
            $scope.searchText = filterString;
        }
    });
