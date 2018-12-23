//= wrapped

angular
    .module("playitforward.index")
    .controller("AdminController", AdminController);

function AdminController(contextPath, userPersistenceService, $scope, $http, $location, $filter, fileReader) {

    var vm = this;
    vm.contextPath = contextPath;

    $scope.sessionData = userPersistenceService.getCookieData();
    $scope.authenticated = $scope.sessionData.authenticated;
    $scope.accessToken = $scope.sessionData.accessToken;
    $scope.role = $scope.sessionData.role;
    
    $scope.currentEvent = null;

    $scope.eventData = {

        eventDataError: '',
        currentOrganizer: null,
        organizers: [],
        currentType: null,
        types: []
    };

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
        
        if (tabName) {

            $scope.activeTab = tabName;
            $location.search('tab', tabName);
        }
        else {
            $scope.activeTab = 'mail';
            $location.search('tab', 'mail');
        }
    };

    $scope.activate($location.search().tab);

    // ///////// //
    // Prize Tab //
    // ///////// //

    $scope.prizesLoaded = false;
    $scope.prizeData = {

        prizeError: '',
        prizeHash: {},
        newDonor: '',
        newPrize: '',
        allYears: [2017,2018],
        currentYear: 2018
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

        if ($scope.currentEvent) {

            return $scope.prizeData.prizeHash[$scope.currentEvent.id];
        }

        return [];
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

    $scope.selectYear = function() {

        for (var i = 0 ; i < $scope.winnerData.eventList.length ; ++i) {

            var event = $scope.winnerData.eventList[i];
            if (event.startDate.getFullYear() === $scope.prizeData.currentYear) {

                $scope.currentEvent = event;
                $scope.selectEvent();
                break;
            }
        }

        // $scope.bountyArray.bounties = $scope.bountyArray.bountiesByYear[$scope.GPs.currentYear];
    };

    // //////////////// //
    // Event Winner Tab //
    // //////////////// //

    $scope.eventsLoaded = false;
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

                var date = new Date();
                var localOffset = date.getTimezoneOffset() * 60000;
                for (var i = 0 ; i < $scope.winnerData.eventList.length ; ++i ) {
                    $scope.winnerData.eventList[i].startDate = new Date($scope.winnerData.eventList[i].startDate.getTime() + localOffset);
                    $scope.winnerData.eventList[i].endDate = new Date($scope.winnerData.eventList[i].endDate.getTime() + localOffset);
                }

                var eventId = $location.search().event;
                if(eventId) {

                    var event = $filter('filter')($scope.winnerData.eventList, {id: eventId});
                    if (event.length > 0) {
                        $scope.currentEvent = event[0];
                    }
                }

                $scope.prizeData.currentYear = $scope.currentEvent.startDate.getFullYear();
                $scope.eventsLoaded = true;
                $scope.selectEvent();

            }, function errorCallback(response) {

                $scope.winnerData.winnerError = 'Error: Could not contact database: ' + response.data.message;

            }
        );
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
        $scope.prizeData.currentYear = $scope.currentEvent.startDate.getFullYear();

        var typeId = $scope.currentEvent.type.id;
        for ( var i = 0 ; i < $scope.eventData.types.length ; i++ ) {
            if ( typeId === $scope.eventData.types[i].id ) {
                $scope.eventData.currentType = $scope.eventData.types[i];
                break;
            }
        }

        var orgId = $scope.currentEvent.organizer.id;
        for ( var j = 0 ; j < $scope.eventData.organizers.length ; j++ ) {
            if ( orgId === $scope.eventData.organizers[j].id ) {
                $scope.eventData.currentOrganizer = $scope.eventData.organizers[j];
                break;
            }
        }
    };

    $scope.upsertWinner = function() {

        var winner = $scope.winnerData.currentWinner;
        
        var data = {
            
            name: winner.name,
            ranking: winner.ranking,
            record: winner.record,
            blurb: winner.blurb,
            imageName: winner.imageName,
            event: {
                id: $scope.currentEvent.id
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

    $scope.artistArray = {

        currentArtist: null,
        artistList: [],
        artistError: ''
    };

    $scope.populateArtist = function() {

        $http.get('/api/Artist').then(


            function successCallback(response) {

                $scope.artistArray.artistList = response.data;
                $scope.artistArray.currentArtist = $scope.artistArray.artistList[0];

            }, function errorCallback(response) {

                $scope.artistArray.artistError = response.data;
            }
        );
    };

    $scope.populateArtist();

    $scope.upsertArtist = function() {

        var data = {

            name: $scope.artistArray.currentArtist.name,
            blurb: $scope.artistArray.currentArtist.blurb,
            webSiteUrl: $scope.artistArray.currentArtist.webSiteUrl,
            deviantArtUrl: $scope.artistArray.currentArtist.deviantArtUrl,
            facebookUrl: $scope.artistArray.currentArtist.facebookUrl,
            patreonUrl: $scope.artistArray.currentArtist.patreonUrl
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        if('id' in $scope.artistArray.currentArtist && $scope.artistArray.currentArtist.id > 0) {

            data.id = $scope.artistArray.currentArtist.id;

            $http.put('/api/Artist/' + data.id, data, config).then(
                function(){
                    $scope.populateArtist();
                    window.alert('Artist updated successfully.');
                },
                function(response){
                    window.alert('Error: Failed to edit Artist! ' + response.data.message);
                }
            );
        }
        else {

            $http.post('/api/Artist', data, config).then(
                function(){
                    $scope.populateArtist();
                    window.alert('Artist added successfully.');
                },
                function(response){
                    window.alert('Error: Failed to add Artist! ' + response.data.message);
                }
            );
        }

    };

    // ///////// //
    // Event Tab //
    // ///////// //

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

    $scope.upsertEvent = function() {

        var data = {

            name: $scope.currentEvent.name,
            eventCode: $scope.currentEvent.eventCode,
            startDate: $filter('date')($scope.currentEvent.startDate, "yyyy-MM-dd"),
            endDate: $filter('date')($scope.currentEvent.endDate, "yyyy-MM-dd"),
            eventFormat: $scope.currentEvent.eventFormat,
            organizer: {
                id: $scope.eventData.currentOrganizer.id
            },
            type: {
                id: $scope.eventData.currentType.id
            }
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        if('id' in $scope.currentEvent && $scope.currentEvent.id > 0) {

            data.id = $scope.currentEvent.id;

            $http.put('/api/Event/' + data.id, data, config).then(
                function(){
                    $scope.getEvents();
                    window.alert('Event updated successfully.');
                },
                function(response){
                    $scope.getEvents();
                    window.alert('Error: Failed to edit Event! ' + response.data.message);
                }
            );
        }
        else {

            $http.post('/api/Event', data, config).then(
                function(){
                    $scope.getEvents();
                    window.alert('Event added successfully.');
                },
                function(response){
                    $scope.getEvents();
                    window.alert('Error: Failed to add Event! ' + response.data.message);
                }
            );
        }

    };

    $scope.addNewEventSetup = function() {

        $scope.currentEvent = null;
    };

}