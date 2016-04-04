'use strict';

describe('Controller Tests', function() {

    describe('ScheduleDate Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockScheduleDate, MockEventType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockScheduleDate = jasmine.createSpy('MockScheduleDate');
            MockEventType = jasmine.createSpy('MockEventType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'ScheduleDate': MockScheduleDate,
                'EventType': MockEventType
            };
            createController = function() {
                $injector.get('$controller')("ScheduleDateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:scheduleDateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
