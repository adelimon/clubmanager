'use strict';

angular.module('clubmanagerApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


