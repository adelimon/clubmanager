'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('integration', {
                parent: 'entity',
                url: '/integrations',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Integrations'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/integration/integrations.html',
                        controller: 'IntegrationController'
                    }
                },
                resolve: {
                }
            })
            .state('integration.detail', {
                parent: 'entity',
                url: '/integration/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Integration'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/integration/integration-detail.html',
                        controller: 'IntegrationDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Integration', function($stateParams, Integration) {
                        return Integration.get({id : $stateParams.id});
                    }]
                }
            })
            .state('integration.new', {
                parent: 'integration',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/integration/integration-dialog.html',
                        controller: 'IntegrationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    platform: null,
                                    apikey: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('integration', null, { reload: true });
                    }, function() {
                        $state.go('integration');
                    })
                }]
            })
            .state('integration.edit', {
                parent: 'integration',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/integration/integration-dialog.html',
                        controller: 'IntegrationDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Integration', function(Integration) {
                                return Integration.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('integration', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('integration.delete', {
                parent: 'integration',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/integration/integration-delete-dialog.html',
                        controller: 'IntegrationDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Integration', function(Integration) {
                                return Integration.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('integration', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
