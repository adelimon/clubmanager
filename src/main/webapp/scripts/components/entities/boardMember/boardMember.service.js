'use strict';

angular.module('clubmanagerApp')
    .factory('BoardMember', function ($resource, DateUtils) {
        return $resource('api/boardMembers/:id', {}, {
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
