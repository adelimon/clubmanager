 'use strict';

angular.module('clubmanagerApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-clubmanagerApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-clubmanagerApp-params')});
                }
                return response;
            }
        };
    });
