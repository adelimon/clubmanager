'use strict';

describe('Controller Tests', function() {

    describe('BoardMember Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBoardMember, MockMember;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBoardMember = jasmine.createSpy('MockBoardMember');
            MockMember = jasmine.createSpy('MockMember');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'BoardMember': MockBoardMember,
                'Member': MockMember
            };
            createController = function() {
                $injector.get('$controller')("BoardMemberDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:boardMemberUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
