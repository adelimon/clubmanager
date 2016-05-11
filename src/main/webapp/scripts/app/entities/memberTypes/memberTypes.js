'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('memberTypes', {
                parent: 'entity',
                url: '/memberTypess',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MemberTypess'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/memberTypes/memberTypess.html',
                        controller: 'MemberTypesController'
                    }
                },
                resolve: {
                }
            })
            .state('memberTypes.detail', {
                parent: 'entity',
                url: '/memberTypes/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MemberTypes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/memberTypes/memberTypes-detail.html',
                        controller: 'MemberTypesDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MemberTypes', function($stateParams, MemberTypes) {
                        return MemberTypes.get({id : $stateParams.id});
                    }]
                }
            })
            .state('memberTypes.new', {
                parent: 'memberTypes',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberTypes/memberTypes-dialog.html',
                        controller: 'MemberTypesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    type: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('memberTypes', null, { reload: true });
                    }, function() {
                        $state.go('memberTypes');
                    })
                }]
            })
            .state('memberTypes.edit', {
                parent: 'memberTypes',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberTypes/memberTypes-dialog.html',
                        controller: 'MemberTypesDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MemberTypes', function(MemberTypes) {
                                return MemberTypes.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('memberTypes', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('memberTypes.delete', {
                parent: 'memberTypes',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberTypes/memberTypes-delete-dialog.html',
                        controller: 'MemberTypesDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MemberTypes', function(MemberTypes) {
                                return MemberTypes.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('memberTypes', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
