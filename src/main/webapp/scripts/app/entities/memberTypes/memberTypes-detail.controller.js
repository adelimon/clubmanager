'use strict';

angular.module('clubmanagerApp')
    .controller('MemberTypesDetailController', function ($scope, $rootScope, $stateParams, entity, MemberTypes) {
        $scope.memberTypes = entity;
        $scope.load = function (id) {
            MemberTypes.get({id: id}, function(result) {
                $scope.memberTypes = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:memberTypesUpdate', function(event, result) {
            $scope.memberTypes = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
