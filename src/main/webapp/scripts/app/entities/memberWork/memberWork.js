'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('memberWork', {
                parent: 'entity',
                url: '/memberWorks',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MemberWorks'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/memberWork/memberWorks.html',
                        controller: 'MemberWorkController'
                    }
                },
                resolve: {
                }
            })
            .state('memberWork.detail', {
                parent: 'entity',
                url: '/memberWork/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'MemberWork'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/memberWork/memberWork-detail.html',
                        controller: 'MemberWorkDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'MemberWork', function($stateParams, MemberWork) {
                        return MemberWork.get({id : $stateParams.id});
                    }]
                }
            })
            .state('memberWork.new', {
                parent: 'memberWork',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberWork/memberWork-dialog.html',
                        controller: 'MemberWorkDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    start: null,
                                    end: null,
                                    description: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('memberWork', null, { reload: true });
                    }, function() {
                        $state.go('memberWork');
                    })
                }]
            })
            .state('memberWork.edit', {
                parent: 'memberWork',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberWork/memberWork-dialog.html',
                        controller: 'MemberWorkDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['MemberWork', function(MemberWork) {
                                return MemberWork.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('memberWork', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('memberWork.delete', {
                parent: 'memberWork',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/memberWork/memberWork-delete-dialog.html',
                        controller: 'MemberWorkDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['MemberWork', function(MemberWork) {
                                return MemberWork.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('memberWork', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
