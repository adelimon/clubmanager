'use strict';

angular.module('clubmanagerApp')
    .controller('EventTypeController', function ($scope, $state, EventType) {

        $scope.eventTypes = [];
        $scope.loadAll = function() {
            EventType.query(function(result) {
               $scope.eventTypes = result;
            });
        };
        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.eventType = {
                type: null,
                id: null
            };
        };
    });
