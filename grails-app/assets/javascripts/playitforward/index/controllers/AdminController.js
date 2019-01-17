//= wrapped

angular
    .module("playitforward.index")
    .controller("AdminController", AdminController);

function AdminController(contextPath, userPersistenceService, $scope, $http, $filter,
                         fileReader, tabService, eventService, artService) {

    var vm = this;
    vm.contextPath = contextPath;

    $scope.eventService = eventService;
    eventService.loadEvents();

    $scope.tabService = tabService;
    tabService.registerTabList( "Admin", "art", ["art", "events", "winners", "prizes", "players", "files"] );

    $scope.artService = artService;
    artService.loadArt();

    $scope.sessionData = userPersistenceService.getCookieData();
    $scope.authenticated = $scope.sessionData.authenticated;
    $scope.accessToken = $scope.sessionData.accessToken;
    $scope.role = $scope.sessionData.role;
    
    $scope.eventData = {

        eventDataError: '',
        currentOrganizer: null,
        organizers: [],
        currentType: null,
        types: []
    };

    // ///////// //
    // Prize Tab //
    // ///////// //

    $scope.prizesLoaded = false;
    $scope.prizeData = {

        prizeError: '',
        prizeHash: {},
        newDonor: '',
        newPrize: ''
    };

    $scope.populateBounties = function() {

        $http.get('/api/EventBounty').then(

            function successCallback(response) {

                // emptying it is needed after adding a bounty.
                $scope.prizeData.prizeHash = {};

                for (var i = 0 ; i < response.data.length ; ++i) {

                    var prize = response.data[i];
                    if ( ! (prize.eventId in $scope.prizeData.prizeHash)) {
                        $scope.prizeData.prizeHash[prize.eventId] = [];
                    }

                    $scope.prizeData.prizeHash[prize.eventId].push(prize);
                }

                $scope.prizesLoaded = true;

            }, function errorCallback(response) {

                $scope.prizeData.prizeError = 'ERROR: ' + response.data.message;
            }
        );
    };

    $scope.getPrizeList = function() {

        if (eventService.currentEvent) {

            return $scope.prizeData.prizeHash[eventService.currentEvent.id];
        }

        return [];
    };

    $scope.populateBounties();
    
    $scope.createPrize = function() {
        
        var data = {

            donor: $scope.prizeData.newDonor,
            prize: $scope.prizeData.newPrize,
            event: {
                id: eventService.currentEvent.id
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
                window.alert('Error: Failed to add prize: ' + response.data.message);
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
                
                window.alert('Error: Failed to delete bounty: ' + response.data.message);
            }
        );
    };

    // //////////////// //
    // Event Winner Tab //
    // //////////////// //

    $scope.eventsLoaded = true;
    $scope.winnerData = {

        newWinner: {},
        winnerError: '',
        winnerHash: {}
    };

    $scope.getCurrentWinner = function() {

        var winner;
        if( eventService.currentEvent ) {

            winner = $scope.winnerData.winnerHash[eventService.currentEvent.id];
        }

        return winner || $scope.winnerData.newWinner;
    };

    $scope.getWinners = function() {

        $http.get('/api/EventWinner').then(
            
            function successCallback(response) {

                for (var i = 0 ;i < response.data.length ; ++i) {

                    var winner = response.data[i];
                    $scope.winnerData.winnerHash[winner.event.id] = winner;
                }
                
            }, function errorCallback(response) {

                $scope.winnerData.winnerError = 'Error: Could not contact database: ' + response.data.message;

            });
    };
    
    $scope.getWinners();
    
    $scope.upsertWinner = function() {

        var winner = $scope.getCurrentWinner();
        
        var data = {
            
            name: winner.name,
            ranking: winner.ranking,
            record: winner.record,
            blurb: winner.blurb,
            imageName: winner.imageName,
            event: {
                id: eventService.currentEvent.id
            }
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        if('id' in winner && winner.id > 0) {

            data.id = winner.id;

            $http.put('/api/EventWinner/' + data.id, data, config).then(
                function(){
                    $scope.getWinners();
                    window.alert('They were updated successfully.');
                },
                function(response){
                    window.alert('Error: Failed to update winner: ' + response.data.message);
                }
            );
        }
        else {

            $http.post('/api/EventWinner', data, config).then(
                function(){
                    $scope.getWinners();
                    window.alert('They were added successfully.');
                },
                function(response){
                    window.window.alert('Error: Failed to add winner: ' + response.data.message);
                }
            );
        }
    };


    // ////////// //
    // Player Tab //
    // ////////// //

    $scope.playerData = {

        currentPlayer: null,
        playerList: [],
        filteredList: [],
        playerError: '',
        pagedPlayers: [],
        currentPage: 0,
        itemsPerPage: 10,
        sortReverse: true,
        sortType: 'Name',
        searchString: ''
    };

    $scope.populatePlayerPages = function () {

        $scope.playerData.pagedPlayers = [];

        for (var i = 0; i < $scope.playerData.filteredList.length; i++) {

            var index = Math.floor(i / $scope.playerData.itemsPerPage);

            if(! (index in $scope.playerData.pagedPlayers) ) {
                $scope.playerData.pagedPlayers[index] = [];
            }

            $scope.playerData.pagedPlayers[index].push($scope.playerData.filteredList[i]);
        }
    };

    $scope.filterPlayers = function() {

        $scope.playerData.filteredList = $filter('filter')($scope.playerData.playerList, $scope.playerData.searchString);

        $scope.playerData.filteredList = $filter('orderBy')($scope.playerData.filteredList, $scope.playerData.sortType,
            $scope.playerData.sortReverse);
        $scope.populatePlayerPages();
    };

    $scope.sortPlayers = function(column) {

        $scope.playerData.sortType = column;
        $scope.playerData.sortReverse = ! $scope.playerData.sortReverse;

        $scope.playerData.filteredList = $filter('filter')($scope.playerData.playerList, $scope.playerData.searchString);

        $scope.playerData.filteredList = $filter('orderBy')($scope.playerData.filteredList, column,
            $scope.playerData.sortReverse);

        $scope.populatePlayerPages();
    };

    $scope.populatePlayers = function() {

        $http({
            method: 'GET',
            url: '/api/Player/adminList'
        }).then(function successCallback(response) {

            $scope.playerData.playerList = response.data;
            $scope.sortPlayers();

        }, function errorCallback(response) {

            $scope.playerData.playerError = 'ERROR: ' + response.data.message;
        });
    };

    $scope.populatePlayers();

    $scope.playerTableRange = function () {

        var ret = [];
        var begin = $scope.playerData.currentPage;
        var end = begin + 5;

        if (end > $scope.playerData.pagedPlayers.length - 1) {
            end = $scope.playerData.pagedPlayers.length - 1;
            begin = end - 5;
            if (begin < 0 ) {
                begin = 0;
            }
        }

        for (var i = begin ; i <= end ; i++) {
            ret.push(i);
        }

        if ($scope.playerData.currentPage > end && end >= 0) {
            $scope.playerData.currentPage = end;
        }

        return ret;
    };

    $scope.prevPlayerPage = function () {

        if ($scope.playerData.currentPage > 0) {
            $scope.playerData.currentPage--;
        }
    };

    $scope.nextPlayerPage = function () {

        if ($scope.playerData.currentPage < $scope.playerData.pagedPlayers.length - 1) {
            $scope.playerData.currentPage++;
        }
    };

    $scope.setPlayerPage = function (pageNum) {
        $scope.playerData.currentPage = pageNum;
    };

    $scope.addNewPlayerSetup = function() {

        $scope.playerData.currentPlayer = null;
    };

    $scope.upsertPlayer = function() {

        var data = {

            alias: $scope.playerData.currentPlayer.alias,
            imgUrl: $scope.playerData.currentPlayer.imgUrl,
            isWoman: $scope.playerData.currentPlayer.isWoman,
            name: $scope.playerData.currentPlayer.name
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        if('id' in $scope.playerData.currentPlayer && $scope.playerData.currentPlayer.id > 0) {

            data.id = $scope.playerData.currentPlayer.id;

            $http.put('/api/Player/' + data.id, data, config).then(
                function(){
                    $scope.populatePlayers();
                    window.alert('They were updated successfully.');
                },
                function(response){
                    window.alert('Error: Failed to add Player! ' + response.data.message);
                }
            );
        }
        else {

            $http.post('/api/Player', data, config).then(
                function(){
                    $scope.populatePlayers();
                    window.alert('Player added successfully.');
                },
                function(response){
                    window.alert('Error: Failed to add Player! ' + response.data.message);
                }
            );
        }

    };

    // /////////////// //
    // Upload File Tab //
    // /////////////// //

    $scope.getFile = function () {

        $scope.progress = 0;
        $scope.imageSrc = null;
        $scope.showImage = false;

        if ($scope.file) {

            fileReader.readAsDataUrl($scope.file, $scope)
                .then(function (result) {
                    $scope.showImage = true;
                    $scope.imageSrc = result;
                });
        }
    };

    $scope.fileData = {

        name: '',
        path: 'images/winners',
        fileError: ''
    };

    $scope.uploadFile = function () {

        var formData = new FormData();
        //formData.append("file", $scope.file);
        formData.append("file", $scope.imageSrc);
        formData.append("name", $scope.file.name);
        formData.append("path", $scope.fileData.path);

        var config = {

            transformRequest: angular.identity,
            headers : {
                // 'enctype': 'multipart/form-data',
                'Content-Type': undefined
            }
        };

        $http.post('/api/Image', formData, config)
            .success(function(){

                window.alert("done");
                $scope.fileData.fileError = '';

            }).error(function(response) {

                window.alert("failed");
                $scope.fileData.fileError = response.message;
            });
    };

    // /////// //
    // Art Tab //
    // /////// //


    $scope.selectArt = function() {

        artService.setPreview();

        for ( var i = 0 ; i < artService.artistsList.length ; i++ ) {

            if ( artService.artModel.artist.id === artService.artistsList[i].id ) {

                artService.currentArtist = artService.artistsList[i];
                break;
            }
        }
    };

    // ///////// //
    // Event Tab //
    // ///////// //

    $scope.getEventPreview = function() {

        if ( eventService.currentEvent ) {
            return eventService.baseUrl + 'playmats/' + eventService.currentEvent.playmatFileName;
        }
    };

    $scope.populateEventProperties = function() {

        $http.get('/api/EventOrganizer').then(


            function successCallback(response) {

                $scope.eventData.organizers = response.data;

            }, function errorCallback(response) {

                $scope.eventData.eventDataError = response.data;
            }
        );

        $http.get('/api/EventType').then(


            function successCallback(response) {

                $scope.eventData.types = response.data;

            }, function errorCallback(response) {

                $scope.eventData.eventDataError = response.data;
            }
        );
    };

    $scope.populateEventProperties();

    $scope.addNewEventSetup = function() {

        eventService.currentEvent = {};
    };

}