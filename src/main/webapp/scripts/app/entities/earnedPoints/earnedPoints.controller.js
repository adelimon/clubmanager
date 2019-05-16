'use strict';

angular.module('clubmanagerApp')
    .controller('EarnedPointsController', function ($scope, $state, $location, EarnedPoints, ParseLinks) {

        $scope.earnedPointss = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.pointsTotal = 0;
        $scope.isMemberPoints = ($location.path().includes('member') ||
            $location.path().includes('/me')
        );
        
        // yes, use the equal to true here because this guy can be undefined otherwise and Javascript is
        // stupid in that case.  I hate this and it looks like discount programming, but at least I
        // did it on purpose.
        $scope.viewMyPoints = ($state.$current.data.loggedInUserOnly === true);
        $scope.loadAll = function() {
            var queryProps = {page: $scope.page, size: 200, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']};
            var path = $location.path();

            var isMemberPoints = (path.indexOf('member/') > -1);
            if (isMemberPoints) {
                var pathSplit = path.split('/');
                var id = pathSplit[pathSplit.length-1];
                queryProps.id = "member"+id;
                queryProps.memberLookup = true;
            }
            // if we only want to see verified points, then add that to the query properties.
            if (!isMemberPoints && $scope.viewMyPoints) {
                queryProps.id = "me";
            }
            EarnedPoints.query(queryProps, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    var pointsObject = result[i];
                    $scope.earnedPointss.push(pointsObject);
                    if (pointsObject.verified && !pointsObject.paid) {
                        $scope.pointsTotal += pointsObject.pointValue;
                    }

                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.earnedPointss = [];
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
            $scope.earnedPoints = {
                date: null,
                description: null,
                pointValue: null,
                verified: false,
                id: null
            };
        };

        $scope.verifyPoints = function (earnedPoints) {
            // if it's a points entry, then set verified to true, then do the update.  this is the same
            // as using the dialog just faster.
            earnedPoints.verified = true;
            EarnedPoints.update(earnedPoints,
                function success() {
                    $scope.refresh();
                },
                function failure() {
                    alert("Update failed, please re-try...");
                    $scope.refresh();
                }
            );
        };

        $scope.payWork = function (earnedPoints) {
            earnedPoints.paid = true;
            $scope.verifyPoints(earnedPoints);
        }

        $scope.clearFilter = function() {
            $scope.searchText = "";
        };

        $scope.setFilter = function(filterString) {
            $scope.searchText = filterString;
        }
    });
