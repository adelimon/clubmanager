'use strict';

angular.module('clubmanagerApp')
    .controller('MemberDetailController', function ($scope, $rootScope, $stateParams, $http, entity, Member) {
        $scope.member = entity;
        $scope.load = function (id) {
            Member.get({id: id}, function(result) {
                $scope.member = result;
            });
        };

        $scope.resetPassword = function(id) {
            $http.get('/api/users/reset/'+id).then(
                function() {
                    alert("Password successfully reset to the default.");
                },
                function() {
                    alert("Unable to reset, an error occurred.");
                }
            );
        };

        var unsubscribe = $rootScope.$on('clubmanagerApp:memberUpdate', function(event, result) {
            $scope.member = result;
        });
        $scope.$on('$destroy', unsubscribe);


    });
