'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('memberStatus', {
                parent: 'entity',
                url: '/memberStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MemberStatuss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/memberStatus/memberStatuss.html',
                        controller: 'MemberStatusController'
                    }
                },
                resolve: {
                }
            })
            .state('memberStatus.detail', {
                parent: 'entity',
                url: '/memberStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MemberStatus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/memberStatus/memberStatus-detail.html',
                        controller: 'MemberStatusDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MemberStatus', function($stateParams, MemberStatus) {
                        return MemberStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('memberStatus.new', {
                parent: 'memberStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberStatus/memberStatus-dialog.html',
                        controller: 'MemberStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    year: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('memberStatus', null, { reload: true });
                    }, function() {
                        $state.go('memberStatus');
                    })
                }]
            })
            .state('memberStatus.edit', {
                parent: 'memberStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberStatus/memberStatus-dialog.html',
                        controller: 'MemberStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MemberStatus', function(MemberStatus) {
                                return MemberStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('memberStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('memberStatus.delete', {
                parent: 'memberStatus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberStatus/memberStatus-delete-dialog.html',
                        controller: 'MemberStatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MemberStatus', function(MemberStatus) {
                                return MemberStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('memberStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
