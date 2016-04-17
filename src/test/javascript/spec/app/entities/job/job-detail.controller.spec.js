'use strict';

describe('Controller Tests', function() {

    describe('Job Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockJob, MockEventType, MockMember;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockJob = jasmine.createSpy('MockJob');
            MockEventType = jasmine.createSpy('MockEventType');
            MockMember = jasmine.createSpy('MockMember');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Job': MockJob,
                'EventType': MockEventType,
                'Member': MockMember
            };
            createController = function() {
                $injector.get('$controller')("JobDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:jobUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
