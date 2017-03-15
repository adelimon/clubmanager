'use strict';

angular.module('clubmanagerApp')
    .controller('MemberWorkController', function ($scope, $state, MemberWork) {

        $scope.memberWorks = [];
        $scope.loadAll = function() {
            MemberWork.query(function(result) {
               $scope.memberWorks = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.memberWork = {
                start: null,
                end: null,
                description: null,
                id: null
            };
        };
    });
