'use strict';

angular.module('clubmanagerApp')
	.controller('MemberNoteDeleteController', function($scope, $uibModalInstance, entity, MemberNote) {

        $scope.memberNote = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            MemberNote.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
