'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('boardMember', {
                parent: 'entity',
                url: '/boardMembers',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BoardMembers'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/boardMember/boardMembers.html',
                        controller: 'BoardMemberController'
                    }
                },
                resolve: {
                }
            })
            .state('boardMember.detail', {
                parent: 'entity',
                url: '/boardMember/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'BoardMember'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/boardMember/boardMember-detail.html',
                        controller: 'BoardMemberDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'BoardMember', function($stateParams, BoardMember) {
                        return BoardMember.get({id : $stateParams.id});
                    }]
                }
            })
            .state('boardMember.new', {
                parent: 'boardMember',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/boardMember/boardMember-dialog.html',
                        controller: 'BoardMemberDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    year: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('boardMember', null, { reload: true });
                    }, function() {
                        $state.go('boardMember');
                    })
                }]
            })
            .state('boardMember.edit', {
                parent: 'boardMember',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/boardMember/boardMember-dialog.html',
                        controller: 'BoardMemberDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['BoardMember', function(BoardMember) {
                                return BoardMember.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('boardMember', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('boardMember.delete', {
                parent: 'boardMember',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/boardMember/boardMember-delete-dialog.html',
                        controller: 'BoardMemberDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['BoardMember', function(BoardMember) {
                                return BoardMember.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('boardMember', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
