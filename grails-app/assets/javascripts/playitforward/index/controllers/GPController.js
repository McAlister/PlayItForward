//= wrapped

angular
    .module("playitforward.index")
    .controller("GPController", GPController);

function GPController(contextPath, $scope, $http, tabService, eventService, artService) {

    var vm = this;
    vm.contextPath = contextPath;

    $scope.eventService = eventService;
    eventService.loadEvents();

    $scope.tabService = tabService;
    tabService.registerTabList( "GPs", "playmat", ["winner", "playmat", "race", "org"] );

    $scope.artService = artService;
    artService.loadArt();

    // ///////////////// //
    // Data Loading Code //
    // ///////////////// //

    $scope.winnerError = '';
    $scope.artError = '';
    $scope.artistsError = '';
    $scope.artLoaded = false;
    $scope.artistsLoaded = false;
    $scope.winnersLoaded = false;

    $scope.eventData = {

        winners: {}            // Event_ID -> Winner Object
    };

    $scope.loadWinners = function() {

        $http.get('/api/EventWinner').then(

            function successCallback(response) {

                for (var i = 0 ; i < response.data.length ; ++i ) {

                    var winner = response.data[i];
                    $scope.eventData.winners[winner.event.id] = winner;
                }

                $scope.winnersLoaded = true;

            }, function errorCallback(response) {

                $scope.winnerError = response.data;
            }
        );
    };

    $scope.loadWinners();

    $scope.getCurrentArt = function() {

        if (eventService.currentEvent && eventService.currentEvent.art) {

            return artService.getArt(eventService.currentEvent.art.id);
        }
    };

    $scope.getCurrentArtist = function() {

        if (eventService.currentEvent && eventService.currentEvent.art) {

            return artService.getArtist(eventService.currentEvent.art.id);
        }
    };

    // /////////////// //
    // Winner Tab Code //
    // /////////////// //

    $scope.isFutureGP = function() {

        var now = new Date();
        return !eventService.currentEvent || eventService.currentEvent.startDate > now;
    };

    $scope.gpStartDateString = function() {

        if (eventService.currentEvent) {

            var startDate = new Date(eventService.currentEvent.startDate.getTime());
            var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };

            return startDate.toLocaleDateString("en-US", options);
        }

        return "[Error:  No Event Selected.]";
    };

    $scope.getCurrentWinner = function() {

        if (eventService.currentEvent) {

            return $scope.eventData.winners[eventService.currentEvent.id];
        }
    };

    $scope.getWinnerImageName = function() {

        var winner = $scope.getCurrentWinner();
        if (winner && winner.imageName) {

            return eventService.baseUrl + "winners/" + winner.imageName;
        }

        return "/assets/winners/Unknown.jpg";
    };


    // ////////// //
    // Bounty Tab //
    // ////////// //

    $scope.bountyArray = {

        bountyHash: {},
        error: '',
        sortType: 'event',
        sortReverse: false
    };

    $scope.populateBounties = function() {

        $http.get('/api/EventBounty').then(

            function successCallback(response) {

                for(var i = 0 ; i < response.data.length ; ++i) {

                    var prize = response.data[i];
                    if ( ! (prize.eventId in $scope.bountyArray.bountyHash)) {
                        $scope.bountyArray.bountyHash[prize.eventId] = [];
                    }

                    $scope.bountyArray.bountyHash[prize.eventId].push(prize);
                }

            }, function errorCallback(response) {

                $scope.bountyArray.error = 'ERROR: ' + response.data.message;
            }
        );
    };

    $scope.populateBounties();

    $scope.getPrizeList = function() {

        if ( eventService.currentEvent ) {

            return $scope.bountyArray.bountyHash[eventService.currentEvent.id];
        }

        return [];
    };

    $scope.bountiesExist = function() {

        if ( eventService.currentEvent ) {
            var currentBounties = $scope.getPrizeList();
            return currentBounties && currentBounties.length > 0;
        }

        return false;
    };


    // ////////////// //
    // Horse race Tab //
    // ////////////// //

    $scope.getHorseRaceSrc = function() {

        if (eventService.currentEvent) {

            return '/assets/horserace/index.html?race=' + eventService.currentEvent.id;
        }
        else {

            return '/assets/horserace/notfound.html';
        }
    };

}