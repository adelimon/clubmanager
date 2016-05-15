'use strict';

angular.module('clubmanagerApp')
    .factory('PaidSignup', function ($resource, DateUtils) {
        return $resource('api/paidSignups/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
