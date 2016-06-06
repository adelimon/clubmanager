'use strict';

angular.module('clubmanagerApp')
    .controller('RidingAreaStatusController', function ($scope, $state, RidingAreaStatus, ParseLinks) {

        $scope.ridingAreaStatuss = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            RidingAreaStatus.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.ridingAreaStatuss.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.ridingAreaStatuss = [];
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
            $scope.ridingAreaStatus = {
                areaName: null,
                status: false,
                id: null
            };
        };

        $scope.convertStatus = function(status) {
            if (status) {
                return "Open";
            } else {
                return "Closed";
            }

        }
    });
