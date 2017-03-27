'use strict';

describe('Controller Tests', function() {

    describe('Integration Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockIntegration;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockIntegration = jasmine.createSpy('MockIntegration');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Integration': MockIntegration
            };
            createController = function() {
                $injector.get('$controller')("IntegrationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:integrationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
