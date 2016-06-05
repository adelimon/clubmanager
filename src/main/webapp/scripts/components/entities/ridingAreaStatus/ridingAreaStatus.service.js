'use strict';

angular.module('clubmanagerApp')
    .factory('RidingAreaStatus', function ($resource, DateUtils) {
        return $resource('api/ridingAreaStatuss/:id', {}, {
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
