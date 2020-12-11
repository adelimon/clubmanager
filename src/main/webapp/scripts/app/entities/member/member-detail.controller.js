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
            let rightNow = new Date();
            let year = rightNow.getFullYear();
            // if we are doing this in November or December then send the bill for the next season.
            if (rightNow.getMonth() > 10) {
                year += 1;
            }
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
