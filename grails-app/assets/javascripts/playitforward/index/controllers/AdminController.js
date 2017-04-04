//= wrapped

angular
    .module("playitforward.index")
    .controller("AdminController", AdminController);

function AdminController(contextPath, userPersistenceService, $scope, $http, $location, $filter) {

    var vm = this;
    vm.contextPath = contextPath;

    $scope.sessionData = userPersistenceService.getCookieData();
    $scope.authenticated = $scope.sessionData.authenticated;
    $scope.accessToken = $scope.sessionData.accessToken;
    $scope.role = $scope.sessionData.role;
    
    $scope.currentEvent = null;

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
        
        if (tabName != null) {

            $scope.activeTab = tabName;
            $location.search('tab', tabName);
        }
        else {
            $scope.activeTab = 'mail';
            $location.search('tab', 'mail');
        }
    };

    $scope.activate($location.search().tab);


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

    // ///////// //
    // Prize Tab //
    // ///////// //
    
    $scope.prizeData = {

        prizeError: '',
        prizeHash: {},
        prizeList: [],
        newDonor: '',
        newPrize: ''
    };

    $scope.populateBounties = function() {

        $http({
            method: 'GET',
            url: '/api/EventBounty'
        }).then(function successCallback(response) {

            $scope.prizeData.prizeList = response.data;
            $scope.prizeData.prizeHash = {};
            
            for (var i = 1; i < response.data.length; ++i) {

                var prize = response.data[i];
                if ( ! (prize.eventId in $scope.prizeData.prizeHash)) {
                    $scope.prizeData.prizeHash[prize.eventId] = [];
                }

                $scope.prizeData.prizeHash[prize.eventId].push(prize);
            }

        }, function errorCallback(response) {

            $scope.prizeData.prizeError = 'ERROR: ' + response.data.message;
        });
    };

    $scope.populateBounties();
    
    $scope.createPrize = function() {
        
        var data = {

            donor: $scope.prizeData.newDonor,
            prize: $scope.prizeData.newPrize,
            event: {
                id: $scope.currentEvent.id
            }
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        $http.post('/api/EventBounty', data, config).then(
            function(){
                $scope.populateBounties();
            },
            function(response){
                alert ('Error: Failed to add prize: ' + response.data.message);
            }
        );
    };
    
    $scope.deletePrize = function(bounty) {

        var data = bounty;

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        $http.delete('/api/EventBounty/' + bounty.id, data, config).then(
            function(){
                
                $scope.populateBounties();
            },
            function(response){
                
                alert ('Error: Failed to delete bounty: ' + response.data.message);
            }
        );
    };
    
    // //////////////// //
    // Event Winner Tab //
    // //////////////// //

    $scope.winnerData = {

        currentWinner: null,
        winnerError: '',
        winnerHash: {},
        eventList: []
    };

    $scope.getEvents = function() {

        $http.get('/api/Event').then(

            function successCallback(response) {

                $scope.winnerData.eventList = response.data;
                $scope.currentEvent = $scope.winnerData.eventList[0];
                $scope.winnerData.currentWinner = 
                    $scope.winnerData.winnerHash[$scope.currentEvent.id];

                var eventId = $location.search().event;
                if(eventId) {

                    var event = $filter('filter')($scope.winnerData.eventList, {id: eventId});
                    if (event.length > 0) {
                        $scope.currentEvent = event[0];
                    }
                }

                $scope.selectEvent();

            }, function errorCallback(response) {

                $scope.winnerData.winnerError = 'Error: Could not contact database: ' + response.data.message;

            });
    };

    $scope.getWinners = function() {

        $http.get('/api/EventWinner').then(
            
            function successCallback(response) {

                for (var i = 0 ;i < response.data.length ; ++i) {

                    var winner = response.data[i];
                    $scope.winnerData.winnerHash[winner.event.id] = winner;
                }
                
                $scope.getEvents();

            }, function errorCallback(response) {

                $scope.winnerData.winnerError = 'Error: Could not contact database: ' + response.data.message;

            });
    };
    
    $scope.getWinners();
    
    $scope.selectEvent = function() {

        var id = $scope.currentEvent.id;
        
        if (id && $location.search().id !== id) {
            $location.search("event", id);
        }

        $scope.winnerData.currentWinner = $scope.winnerData.winnerHash[id];
    };

    $scope.upsertWinner = function() {

        var winner = $scope.winnerData.currentWinner;
        
        var data = {
            
            name: winner.name,
            ranking: winner.ranking,
            record: winner.record,
            blurb: winner.blurb,
            event: {
                id: $scope.currentEvent.id
            }
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        if(winner.id != null) {

            data['id'] = winner.id;
            data['imageName'] = winner.imageName;

            $http.put('/api/EventWinner/' + data['id'], data, config).then(
                function(){
                    $scope.getPeople();
                    alert ('They were updated successfully.');
                },
                function(response){
                    alert ('Error: Failed to update winner: ' + response.data.message);
                }
            );
        }
        else {

            data['imageName'] = 'Unknown.jpg';
            
            $http.post('/api/EventWinner', data, config).then(
                function(){
                    $scope.getPeople();
                    alert ('They were added successfully.');
                },
                function(response){
                    alert ('Error: Failed to add winner: ' + response.data.message);
                }
            );
        }
    };
}