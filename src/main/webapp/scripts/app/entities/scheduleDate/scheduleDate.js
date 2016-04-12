'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('scheduleDate', {
                parent: 'entity',
                url: '/scheduleDates',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ScheduleDates'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/scheduleDate/scheduleDates.html',
                        controller: 'ScheduleDateController'
                    }
                },
                resolve: {
                }
            })
            .state('scheduleDate.detail', {
                parent: 'entity',
                url: '/scheduleDate/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'ScheduleDate'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/scheduleDate/scheduleDate-detail.html',
                        controller: 'ScheduleDateDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ScheduleDate', function($stateParams, ScheduleDate) {
                        return ScheduleDate.get({id : $stateParams.id});
                    }]
                }
            })
            .state('scheduleDate.new', {
                parent: 'scheduleDate',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scheduleDate/scheduleDate-dialog.html',
                        controller: 'ScheduleDateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    date: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('scheduleDate', null, { reload: true });
                    }, function() {
                        $state.go('scheduleDate');
                    })
                }]
            })
            .state('scheduleDate.edit', {
                parent: 'scheduleDate',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scheduleDate/scheduleDate-dialog.html',
                        controller: 'ScheduleDateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ScheduleDate', function(ScheduleDate) {
                                return ScheduleDate.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('scheduleDate', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('scheduleDate.delete', {
                parent: 'scheduleDate',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/scheduleDate/scheduleDate-delete-dialog.html',
                        controller: 'ScheduleDateDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['ScheduleDate', function(ScheduleDate) {
                                return ScheduleDate.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('scheduleDate', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
