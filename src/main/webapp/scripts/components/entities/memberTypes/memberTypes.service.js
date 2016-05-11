'use strict';

angular.module('clubmanagerApp')
    .factory('MemberTypes', function ($resource, DateUtils) {
        return $resource('api/memberTypess/:id', {}, {
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
