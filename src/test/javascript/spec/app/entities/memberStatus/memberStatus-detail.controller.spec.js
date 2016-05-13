'use strict';

describe('Controller Tests', function() {

    describe('MemberStatus Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMemberStatus, MockMember, MockMemberTypes;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMemberStatus = jasmine.createSpy('MockMemberStatus');
            MockMember = jasmine.createSpy('MockMember');
            MockMemberTypes = jasmine.createSpy('MockMemberTypes');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MemberStatus': MockMemberStatus,
                'Member': MockMember,
                'MemberTypes': MockMemberTypes
            };
            createController = function() {
                $injector.get('$controller')("MemberStatusDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:memberStatusUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
