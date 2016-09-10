'use strict';

angular.module('clubmanagerApp')
    .factory('Member', function ($resource, DateUtils) {
        return $resource('api/members/:id', {size: 300}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.birthday = DateUtils.convertLocaleDateFromServer(data.birthday);
                    data.dateJoined = DateUtils.convertLocaleDateFromServer(data.dateJoined);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.birthday = DateUtils.convertLocaleDateToServer(data.birthday);
                    data.dateJoined = DateUtils.convertLocaleDateToServer(data.dateJoined);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.birthday = DateUtils.convertLocaleDateToServer(data.birthday);
                    data.dateJoined = DateUtils.convertLocaleDateToServer(data.dateJoined);
                    return angular.toJson(data);
                }
            }
        });
    });
