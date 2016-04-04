'use strict';

angular.module('clubmanagerApp')
    .factory('Signup', function ($resource, DateUtils) {
        return $resource('api/signups/:id', {}, {
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
