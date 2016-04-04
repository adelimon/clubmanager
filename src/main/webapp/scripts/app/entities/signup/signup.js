'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('signup', {
                parent: 'entity',
                url: '/signups',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Signups'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/signup/signups.html',
                        controller: 'SignupController'
                    }
                },
                resolve: {
                }
            })
            .state('signup.detail', {
                parent: 'entity',
                url: '/signup/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Signup'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/signup/signup-detail.html',
                        controller: 'SignupDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Signup', function($stateParams, Signup) {
                        return Signup.get({id : $stateParams.id});
                    }]
                }
            })
            .state('signup.new', {
                parent: 'signup',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/signup/signup-dialog.html',
                        controller: 'SignupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('signup', null, { reload: true });
                    }, function() {
                        $state.go('signup');
                    })
                }]
            })
            .state('signup.edit', {
                parent: 'signup',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/signup/signup-dialog.html',
                        controller: 'SignupDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Signup', function(Signup) {
                                return Signup.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('signup', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('signup.delete', {
                parent: 'signup',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/signup/signup-delete-dialog.html',
                        controller: 'SignupDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Signup', function(Signup) {
                                return Signup.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('signup', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
