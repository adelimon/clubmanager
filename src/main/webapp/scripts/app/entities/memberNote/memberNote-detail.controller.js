'use strict';

angular.module('clubmanagerApp')
    .controller('MemberNoteDetailController', function ($scope, $rootScope, $stateParams, entity, MemberNote, Member) {
        $scope.memberNote = entity;
        $scope.load = function (id) {
            MemberNote.get({id: id}, function(result) {
                $scope.memberNote = result;
            });
        };
        var unsubscribe = $rootScope.$on('clubmanagerApp:memberNoteUpdate', function(event, result) {
            $scope.memberNote = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
