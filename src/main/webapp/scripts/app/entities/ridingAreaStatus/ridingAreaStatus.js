'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('ridingAreaStatus', {
                parent: 'entity',
                url: '/ridingAreaStatuss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'RidingAreaStatuss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ridingAreaStatus/ridingAreaStatuss.html',
                        controller: 'RidingAreaStatusController'
                    }
                },
                resolve: {
                }
            })
            .state('ridingAreaStatus.detail', {
                parent: 'entity',
                url: '/ridingAreaStatus/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'RidingAreaStatus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/ridingAreaStatus/ridingAreaStatus-detail.html',
                        controller: 'RidingAreaStatusDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'RidingAreaStatus', function($stateParams, RidingAreaStatus) {
                        return RidingAreaStatus.get({id : $stateParams.id});
                    }]
                }
            })
            .state('ridingAreaStatus.new', {
                parent: 'ridingAreaStatus',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/ridingAreaStatus/ridingAreaStatus-dialog.html',
                        controller: 'RidingAreaStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    areaName: null,
                                    status: false,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('ridingAreaStatus', null, { reload: true });
                    }, function() {
                        $state.go('ridingAreaStatus');
                    })
                }]
            })
            .state('ridingAreaStatus.edit', {
                parent: 'ridingAreaStatus',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/ridingAreaStatus/ridingAreaStatus-dialog.html',
                        controller: 'RidingAreaStatusDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['RidingAreaStatus', function(RidingAreaStatus) {
                                return RidingAreaStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ridingAreaStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('ridingAreaStatus.delete', {
                parent: 'ridingAreaStatus',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/ridingAreaStatus/ridingAreaStatus-delete-dialog.html',
                        controller: 'RidingAreaStatusDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['RidingAreaStatus', function(RidingAreaStatus) {
                                return RidingAreaStatus.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('ridingAreaStatus', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
