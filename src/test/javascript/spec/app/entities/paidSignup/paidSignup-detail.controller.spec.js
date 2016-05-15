'use strict';

describe('Controller Tests', function() {

    describe('PaidSignup Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPaidSignup, MockPaidLabor, MockScheduleDate, MockJob;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPaidSignup = jasmine.createSpy('MockPaidSignup');
            MockPaidLabor = jasmine.createSpy('MockPaidLabor');
            MockScheduleDate = jasmine.createSpy('MockScheduleDate');
            MockJob = jasmine.createSpy('MockJob');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PaidSignup': MockPaidSignup,
                'PaidLabor': MockPaidLabor,
                'ScheduleDate': MockScheduleDate,
                'Job': MockJob
            };
            createController = function() {
                $injector.get('$controller')("PaidSignupDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:paidSignupUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
