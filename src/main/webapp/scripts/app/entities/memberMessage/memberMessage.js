'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('memberMessage', {
                parent: 'entity',
                url: '/memberMessages',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MemberMessages'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/memberMessage/memberMessages.html',
                        controller: 'MemberMessageController'
                    }
                },
                resolve: {
                }
            })
            .state('memberMessage.detail', {
                parent: 'entity',
                url: '/memberMessage/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MemberMessage'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/memberMessage/memberMessage-detail.html',
                        controller: 'MemberMessageDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MemberMessage', function($stateParams, MemberMessage) {
                        return MemberMessage.get({id : $stateParams.id});
                    }]
                }
            })
            .state('memberMessage.new', {
                parent: 'memberMessage',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberMessage/memberMessage-dialog.html',
                        controller: 'MemberMessageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    subject: null,
                                    messageText: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('memberMessage', null, { reload: true });
                    }, function() {
                        $state.go('memberMessage');
                    })
                }]
            })
            .state('memberMessage.edit', {
                parent: 'memberMessage',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberMessage/memberMessage-dialog.html',
                        controller: 'MemberMessageDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MemberMessage', function(MemberMessage) {
                                return MemberMessage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('memberMessage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('memberMessage.delete', {
                parent: 'memberMessage',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberMessage/memberMessage-delete-dialog.html',
                        controller: 'MemberMessageDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MemberMessage', function(MemberMessage) {
                                return MemberMessage.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('memberMessage', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
