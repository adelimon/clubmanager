'use strict';

angular.module('clubmanagerApp')
    .factory('EventType', function ($resource, DateUtils) {
        return $resource('api/eventTypes/:id', {}, {
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
