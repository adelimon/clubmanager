'use strict';

angular.module('clubmanagerApp').controller('BoardMemberDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'BoardMember', 'Member',
        function($scope, $stateParams, $uibModalInstance, $q, entity, BoardMember, Member) {

        $scope.boardMember = entity;
        $scope.members = Member.query({filter: 'boardmember-is-null'});
        $q.all([$scope.boardMember.$promise, $scope.members.$promise]).then(function() {
            if (!$scope.boardMember.member || !$scope.boardMember.member.id) {
                return $q.reject();
            }
            return Member.get({id : $scope.boardMember.member.id}).$promise;
        }).then(function(member) {
            $scope.members.push(member);
        });
        $scope.load = function(id) {
            BoardMember.get({id : id}, function(result) {
                $scope.boardMember = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('clubmanagerApp:boardMemberUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.boardMember.id != null) {
                BoardMember.update($scope.boardMember, onSaveSuccess, onSaveError);
            } else {
                BoardMember.save($scope.boardMember, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
