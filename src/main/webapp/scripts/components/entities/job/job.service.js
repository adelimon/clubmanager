'use strict';

angular.module('clubmanagerApp')
    .factory('Job', function ($resource, DateUtils) {
        return $resource('api/jobs/:id', {}, {
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
