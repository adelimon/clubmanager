'use strict';

describe('Controller Tests', function() {

    describe('EarnedPoints Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEarnedPoints, MockMember, MockEventType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEarnedPoints = jasmine.createSpy('MockEarnedPoints');
            MockMember = jasmine.createSpy('MockMember');
            MockEventType = jasmine.createSpy('MockEventType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'EarnedPoints': MockEarnedPoints,
                'Member': MockMember,
                'EventType': MockEventType
            };
            createController = function() {
                $injector.get('$controller')("EarnedPointsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:earnedPointsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
