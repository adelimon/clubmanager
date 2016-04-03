'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('earnedPoints', {
                parent: 'entity',
                url: '/earnedPointss',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'EarnedPointss'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/earnedPoints/earnedPointss.html',
                        controller: 'EarnedPointsController'
                    }
                },
                resolve: {
                }
            })
            .state('earnedPoints.detail', {
                parent: 'entity',
                url: '/earnedPoints/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'EarnedPoints'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/earnedPoints/earnedPoints-detail.html',
                        controller: 'EarnedPointsDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'EarnedPoints', function($stateParams, EarnedPoints) {
                        return EarnedPoints.get({id : $stateParams.id});
                    }]
                }
            })
            .state('earnedPoints.new', {
                parent: 'earnedPoints',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/earnedPoints/earnedPoints-dialog.html',
                        controller: 'EarnedPointsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    date: null,
                                    description: null,
                                    pointValue: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('earnedPoints', null, { reload: true });
                    }, function() {
                        $state.go('earnedPoints');
                    })
                }]
            })
            .state('earnedPoints.edit', {
                parent: 'earnedPoints',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/earnedPoints/earnedPoints-dialog.html',
                        controller: 'EarnedPointsDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['EarnedPoints', function(EarnedPoints) {
                                return EarnedPoints.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('earnedPoints', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('earnedPoints.delete', {
                parent: 'earnedPoints',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/earnedPoints/earnedPoints-delete-dialog.html',
                        controller: 'EarnedPointsDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['EarnedPoints', function(EarnedPoints) {
                                return EarnedPoints.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('earnedPoints', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
