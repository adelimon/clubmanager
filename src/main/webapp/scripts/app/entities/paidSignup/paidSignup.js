'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('paidSignup', {
                parent: 'entity',
                url: '/paidSignups',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PaidSignups'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paidSignup/paidSignups.html',
                        controller: 'PaidSignupController'
                    }
                },
                resolve: {
                }
            })
            .state('paidSignup.detail', {
                parent: 'entity',
                url: '/paidSignup/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'PaidSignup'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/paidSignup/paidSignup-detail.html',
                        controller: 'PaidSignupDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PaidSignup', function($stateParams, PaidSignup) {
                        return PaidSignup.get({id : $stateParams.id});
                    }]
                }
            })
            .state('paidSignup.new', {
                parent: 'paidSignup',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/paidSignup/paidSignup-dialog.html',
                        controller: 'PaidSignupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('paidSignup', null, { reload: true });
                    }, function() {
                        $state.go('paidSignup');
                    })
                }]
            })
            .state('paidSignup.edit', {
                parent: 'paidSignup',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/paidSignup/paidSignup-dialog.html',
                        controller: 'PaidSignupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PaidSignup', function(PaidSignup) {
                                return PaidSignup.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('paidSignup', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('paidSignup.delete', {
                parent: 'paidSignup',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/paidSignup/paidSignup-delete-dialog.html',
                        controller: 'PaidSignupDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['PaidSignup', function(PaidSignup) {
                                return PaidSignup.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('paidSignup', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
