'use strict';

angular.module('clubmanagerApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('member', {
                parent: 'entity',
                url: '/members',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Members'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/member/members.html',
                        controller: 'MemberController'
                    }
                },
                resolve: {
                }
            })
            .state('member.signin', {
                parent: 'entity',
                url: '/members/signin/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Members'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/member/members-signin.html',
                        controller: 'MemberController'
                    }
                },
                resolve: {
                }
            })
            .state('member.detail', {
                parent: 'entity',
                url: '/member/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Member'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/member/member-detail.html',
                        controller: 'MemberDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Member', function($stateParams, Member) {
                        return Member.get({id : $stateParams.id});
                    }]
                }
            })
            .state('member.new', {
                parent: 'member',
                url: '/new',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/member/member-dialog.html',
                        controller: 'MemberDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    firstName: null,
                                    lastName: null,
                                    address: null,
                                    city: null,
                                    state: null,
                                    zip: null,
                                    occupation: null,
                                    phone: null,
                                    viewOnline: null,
                                    email: null,
                                    birthday: null,
                                    dateJoined: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('member', null, { reload: true });
                    }, function() {
                        $state.go('member');
                    })
                }]
            })
            .state('member.edit', {
                parent: 'member',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/member/member-dialog.html',
                        controller: 'MemberDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Member', function(Member) {
                                return Member.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('member', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('member.delete', {
                parent: 'member',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_ADMIN'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/member/member-delete-dialog.html',
                        controller: 'MemberDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Member', function(Member) {
                                return Member.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('member', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
