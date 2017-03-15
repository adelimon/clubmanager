'use strict';

angular.module('clubmanagerApp')
    .factory('MemberWork', function ($resource, DateUtils) {
        return $resource('api/memberWorks/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.start = DateUtils.convertDateTimeFromServer(data.start);
                    data.end = DateUtils.convertDateTimeFromServer(data.end);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
