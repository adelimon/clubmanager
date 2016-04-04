'use strict';

describe('Controller Tests', function() {

    describe('Signup Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSignup, MockMember, MockScheduleDate, MockJob;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSignup = jasmine.createSpy('MockSignup');
            MockMember = jasmine.createSpy('MockMember');
            MockScheduleDate = jasmine.createSpy('MockScheduleDate');
            MockJob = jasmine.createSpy('MockJob');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Signup': MockSignup,
                'Member': MockMember,
                'ScheduleDate': MockScheduleDate,
                'Job': MockJob
            };
            createController = function() {
                $injector.get('$controller')("SignupDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:signupUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
