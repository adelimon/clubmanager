'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('job', {
                parent: 'entity',
                url: '/jobs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Jobs'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/job/jobs.html',
                        controller: 'JobController'
                    }
                },
                resolve: {
                }
            })
            .state('job.detail', {
                parent: 'entity',
                url: '/job/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Job'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/job/job-detail.html',
                        controller: 'JobDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Job', function($stateParams, Job) {
                        return Job.get({id : $stateParams.id});
                    }]
                }
            })
            .state('job.new', {
                parent: 'job',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/job/job-dialog.html',
                        controller: 'JobDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    title: null,
                                    pointValue: null,
                                    cashValue: null,
                                    jobDay: null,
                                    sortOrder: null,
                                    reserved: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('job', null, { reload: true });
                    }, function() {
                        $state.go('job');
                    })
                }]
            })
            .state('job.edit', {
                parent: 'job',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/job/job-dialog.html',
                        controller: 'JobDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Job', function(Job) {
                                return Job.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('job', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('job.delete', {
                parent: 'job',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/job/job-delete-dialog.html',
                        controller: 'JobDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Job', function(Job) {
                                return Job.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('job', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
