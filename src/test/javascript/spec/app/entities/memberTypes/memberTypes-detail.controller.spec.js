'use strict';

describe('Controller Tests', function() {

    describe('MemberTypes Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMemberTypes;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMemberTypes = jasmine.createSpy('MockMemberTypes');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MemberTypes': MockMemberTypes
            };
            createController = function() {
                $injector.get('$controller')("MemberTypesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:memberTypesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
