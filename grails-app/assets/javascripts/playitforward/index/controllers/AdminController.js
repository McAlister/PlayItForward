//= wrapped

angular
    .module("playitforward.index")
    .controller("AdminController", AdminController);

function AdminController(contextPath, userPersistenceService, $scope, $http, $filter) {

    var vm = this;
    vm.contextPath = contextPath;

    $scope.sessionData = userPersistenceService.getCookieData();
    $scope.authenticated = $scope.sessionData.authenticated;
    $scope.accessToken = $scope.sessionData.accessToken;
    $scope.role = $scope.sessionData.role;

    $scope.header = 

    // /////////////// //
    // Active Tab Code //
    // /////////////// //

    $scope.isActiveTab = function (tabName) {

        return (tabName === $scope.activeTab);
    };

    $scope.tabClass = function (tabName) {

        if (tabName === $scope.activeTab) {
            return 'active';
        }

        return '';
    };

    $scope.activate = function (tabName) {
        
        $scope.activeTab = tabName;
    };

    $scope.activate('mail');


    // ///////////// //
    // Mail List Tab //
    // ///////////// //

    $scope.personData = {

        searchString: '',
        sortType: 'name',
        sortReverse: false,
        personError: '',
        personList: []
    };

    $scope.mailData = {
        currentPerson: {
            id: 0,
            fullName: 'Add Person'
        },
        types: [],
        chosenType: null
    };

    $scope.getPeople = function() {
        
        $http.get('/api/Person').then(
            function successCallback(response) {

                $scope.personData.personList = response.data;
                $scope.personData.personList.unshift($scope.mailData.currentPerson);

            }, function errorCallback(response) {

                $scope.personData.personError = 'Error: Could not contact database: ' + response.data.message;

            });
        
        $scope.mailData.currentPerson = {
            id: 0,
            fullName: 'Add Person'
        };
    };
    
    $scope.getPeople();

    $http.get('/api/PersonType').then(

        function successCallback(response) {

            $scope.mailData.types = response.data;

        }, function errorCallback(response) {

            alert ('Error: Could not contact database: ' + response.data.message);
            
        });

    $scope.upsertPerson = function() {

        var data = {
            firstName: $scope.mailData.currentPerson.firstName,
            lastName: $scope.mailData.currentPerson.lastName,
            email: $scope.mailData.currentPerson.email,
            phone: $scope.mailData.currentPerson.phone,
            personType: $scope.mailData.currentPerson.personType,
            sendPushNotifications: $scope.mailData.currentPerson.sendPushNotifications
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        if($scope.mailData.currentPerson.id != 0) {

            data['id'] = $scope.mailData.currentPerson.id;

            $http.put('/api/Person/' + data['id'], data, config).then(
                function(){
                    $scope.getPeople();
                    alert ('They were updated successfully.');
                },
                function(response){
                    alert ('Error: Failed to add you to mailling list! ' + response.data.message);
                }
            );
        }
        else {
            
            $http.post('/api/Person', data, config).then(
                function(){
                    $scope.getPeople();
                    alert ('They were added successfully.');
                },
                function(response){
                    alert ('Error: Failed to add you to mailling list! ' + response.data.message);
                }
            );
        }

    };
    
}