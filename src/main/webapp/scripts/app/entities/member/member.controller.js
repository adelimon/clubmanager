'use strict';

angular.module('clubmanagerApp')
    .controller('MemberController', function ($scope, $state, $location, $http, $rootScope, Member, ParseLinks) {

        $scope.members = [];
        $scope.signedUp = [];
        $scope.predicate = 'lastName';
        $scope.reverse = false;

        $scope.signupInfo = $location.path();

        $scope.page = 0;
        $scope.loadAll = function() {
            Member.query({page: $scope.page, size: 500, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'lastName']}, function(result, headers) {
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
            $scope.searchText = $rootScope.memberSearch;
        };
        $scope.searchFilter = function(item) {
            var userSearchTerm = $scope.searchText;
            var userSearched = ((userSearchTerm !== "") && (userSearchTerm !== undefined));
            var condition = true;
            if (userSearched) {
                userSearchTerm = userSearchTerm.toLowerCase();
                $rootScope.memberSearch = userSearchTerm;
                // if the user searched, then check the object for their search term.
                condition = ((item.firstName+item.lastName).toLowerCase().includes(userSearchTerm));
                if (!condition) {
                    condition = item.status.type.toLowerCase().includes(userSearchTerm);
                }
            } else {
                var status = item.status.type;
                // by default exclude paid labor and sponsored riders, unless they click the buttons asking for them
                // these folks rarely would be looked for.
                condition = (status !== "Paid Labor");
            }

            if (condition) {
                return item;
            }
        }
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

        $scope.signUpFromLink = function(member) {
            //alert(JSON.stringify(member));
            var searchUrl = $location.search();
            var urlSplit = $location.path().split("/");
            var eventId = urlSplit[3];
            var memberId = member.id;



            $http.post('/api/earnedPoints/'+memberId+'/'+eventId).then(
                function() {
                    $scope.signedUp.push(memberId);
                    $scope.refresh();
                },
                function() {
                    alert("Your signup had an error.");
                }
            );
        };

        $scope.checkHide = function(id) {
            return ($scope.signedUp.indexOf(id) > -1);
        };

    });
