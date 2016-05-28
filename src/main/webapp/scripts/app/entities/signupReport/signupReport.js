'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('signupReport', {
                parent: 'entity',
                url: '/signupReports',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'SignupReports'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/signupReport/signupReports.html',
                        controller: 'SignupReportController'
                    }
                },
                resolve: {
                }
            })
            .state('signupReport.detail', {
                parent: 'entity',
                url: '/signupReport/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'SignupReport'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/signupReport/signupReport-detail.html',
                        controller: 'SignupReportDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'SignupReport', function($stateParams, SignupReport) {
                        return SignupReport.get({id : $stateParams.id});
                    }]
                }
            })
            .state('signupReport.new', {
                parent: 'signupReport',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/signupReport/signupReport-dialog.html',
                        controller: 'SignupReportDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    title: null,
                                    pointValue: null,
                                    cashValue: null,
                                    reserved: null,
                                    jobDay: null,
                                    leader: null,
                                    date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('signupReport', null, { reload: true });
                    }, function() {
                        $state.go('signupReport');
                    })
                }]
            })
            .state('signupReport.edit', {
                parent: 'signupReport',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/signupReport/signupReport-dialog.html',
                        controller: 'SignupReportDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['SignupReport', function(SignupReport) {
                                return SignupReport.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('signupReport', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('signupReport.delete', {
                parent: 'signupReport',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/signupReport/signupReport-delete-dialog.html',
                        controller: 'SignupReportDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['SignupReport', function(SignupReport) {
                                return SignupReport.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('signupReport', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
