'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('paidLabor', {
                parent: 'entity',
                url: '/paidLabors',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PaidLabors'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paidLabor/paidLabors.html',
                        controller: 'PaidLaborController'
                    }
                },
                resolve: {
                }
            })
            .state('paidLabor.detail', {
                parent: 'entity',
                url: '/paidLabor/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PaidLabor'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paidLabor/paidLabor-detail.html',
                        controller: 'PaidLaborDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PaidLabor', function($stateParams, PaidLabor) {
                        return PaidLabor.get({id : $stateParams.id});
                    }]
                }
            })
            .state('paidLabor.new', {
                parent: 'paidLabor',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/paidLabor/paidLabor-dialog.html',
                        controller: 'PaidLaborDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('paidLabor', null, { reload: true });
                    }, function() {
                        $state.go('paidLabor');
                    })
                }]
            })
            .state('paidLabor.edit', {
                parent: 'paidLabor',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/paidLabor/paidLabor-dialog.html',
                        controller: 'PaidLaborDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PaidLabor', function(PaidLabor) {
                                return PaidLabor.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('paidLabor', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('paidLabor.delete', {
                parent: 'paidLabor',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/paidLabor/paidLabor-delete-dialog.html',
                        controller: 'PaidLaborDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['PaidLabor', function(PaidLabor) {
                                return PaidLabor.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('paidLabor', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
