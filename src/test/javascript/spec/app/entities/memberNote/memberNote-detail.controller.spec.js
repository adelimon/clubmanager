'use strict';

describe('Controller Tests', function() {

    describe('MemberNote Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockMemberNote, MockMember;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockMemberNote = jasmine.createSpy('MockMemberNote');
            MockMember = jasmine.createSpy('MockMember');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'MemberNote': MockMemberNote,
                'Member': MockMember
            };
            createController = function() {
                $injector.get('$controller')("MemberNoteDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'clubmanagerApp:memberNoteUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
