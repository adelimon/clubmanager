'use strict';

describe('Controller Tests', function() {

    describe('PaidLabor Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPaidLabor, MockMember;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPaidLabor = jasmine.createSpy('MockPaidLabor');
            MockMember = jasmine.createSpy('MockMember');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PaidLabor': MockPaidLabor,
                'Member': MockMember
            };
            createController = function() {
                $injector.get('$controller')("PaidLaborDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:paidLaborUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
