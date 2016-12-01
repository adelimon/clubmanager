'use strict';

angular.module('clubmanagerApp')
    .factory('MemberMessage', function ($resource, DateUtils) {
        return $resource('api/memberMessages/:id', {}, {
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
