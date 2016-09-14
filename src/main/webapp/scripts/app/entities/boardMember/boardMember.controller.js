'use strict';

angular.module('clubmanagerApp')
    .controller('BoardMemberController', function ($scope, $state, BoardMember) {

        $scope.boardMembers = [];
        $scope.loadAll = function() {
            BoardMember.query(function(result) {
               $scope.boardMembers = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.boardMember = {
                title: null,
                year: null,
                id: null
            };
        };
    });
