'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('memberNote', {
                parent: 'entity',
                url: '/memberNotes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MemberNotes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/memberNote/memberNotes.html',
                        controller: 'MemberNoteController'
                    }
                },
                resolve: {
                }
            })
            .state('memberNote.detail', {
                parent: 'entity',
                url: '/memberNote/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MemberNote'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/memberNote/memberNote-detail.html',
                        controller: 'MemberNoteDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MemberNote', function($stateParams, MemberNote) {
                        return MemberNote.get({id : $stateParams.id});
                    }]
                }
            })
            .state('memberNote.new', {
                parent: 'memberNote',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberNote/memberNote-dialog.html',
                        controller: 'MemberNoteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    notes: null,
                                    date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('memberNote', null, { reload: true });
                    }, function() {
                        $state.go('memberNote');
                    })
                }]
            })
            .state('memberNote.edit', {
                parent: 'memberNote',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberNote/memberNote-dialog.html',
                        controller: 'MemberNoteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MemberNote', function(MemberNote) {
                                return MemberNote.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('memberNote', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('memberNote.delete', {
                parent: 'memberNote',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberNote/memberNote-delete-dialog.html',
                        controller: 'MemberNoteDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MemberNote', function(MemberNote) {
                                return MemberNote.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('memberNote', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
