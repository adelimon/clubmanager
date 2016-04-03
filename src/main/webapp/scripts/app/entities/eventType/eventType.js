'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('eventType', {
                parent: 'entity',
                url: '/eventTypes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'EventTypes'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/eventType/eventTypes.html',
                        controller: 'EventTypeController'
                    }
                },
                resolve: {
                }
            })
            .state('eventType.detail', {
                parent: 'entity',
                url: '/eventType/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'EventType'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/eventType/eventType-detail.html',
                        controller: 'EventTypeDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'EventType', function($stateParams, EventType) {
                        return EventType.get({id : $stateParams.id});
                    }]
                }
            })
            .state('eventType.new', {
                parent: 'eventType',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/eventType/eventType-dialog.html',
                        controller: 'EventTypeDialogController',
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
                        $state.go('eventType', null, { reload: true });
                    }, function() {
                        $state.go('eventType');
                    })
                }]
            })
            .state('eventType.edit', {
                parent: 'eventType',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/eventType/eventType-dialog.html',
                        controller: 'EventTypeDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['EventType', function(EventType) {
                                return EventType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('eventType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('eventType.delete', {
                parent: 'eventType',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/eventType/eventType-delete-dialog.html',
                        controller: 'EventTypeDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['EventType', function(EventType) {
                                return EventType.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('eventType', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
