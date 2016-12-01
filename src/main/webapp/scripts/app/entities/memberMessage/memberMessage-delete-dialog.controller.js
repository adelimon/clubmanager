'use strict';

angular.module('clubmanagerApp')
	.controller('MemberMessageDeleteController', function($scope, $uibModalInstance, entity, MemberMessage) {

        $scope.memberMessage = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MemberMessage.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
