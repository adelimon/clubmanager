'use strict';

angular.module('clubmanagerApp')
    .controller('IntegrationDetailController', function ($scope, $rootScope, $stateParams, entity, Integration) {
        $scope.integration = entity;
        $scope.load = function (id) {
            Integration.get({id: id}, function(result) {
                $scope.integration = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:integrationUpdate', function(event, result) {
            $scope.integration = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
