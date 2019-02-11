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

        $scope.rebill = function(id) {
            var year = new Date().getFullYear();
            $http.get('/api/billing/send/'+id+'/'+ year).then(
                function() {
                    $http.get('/api/billing/'+year+'/false/30');
                    alert("resent dues renewal");
                },
                function() {
                    alert("Unable to resend, an error occurred.");
                }
            );
        }
        var unsubscribe = $rootScope.$on('clubmanagerApp:memberUpdate', function(event, result) {
            $scope.member = result;
        });
        $scope.$on('$destroy', unsubscribe);


    });
