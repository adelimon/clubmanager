'use strict';

describe('Controller Tests', function() {

    describe('RidingAreaStatus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockRidingAreaStatus;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockRidingAreaStatus = jasmine.createSpy('MockRidingAreaStatus');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'RidingAreaStatus': MockRidingAreaStatus
            };
            createController = function() {
                $injector.get('$controller')("RidingAreaStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:ridingAreaStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
