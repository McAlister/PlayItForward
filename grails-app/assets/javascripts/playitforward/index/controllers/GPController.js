//= wrapped

angular
    .module("playitforward.index")
    .controller("GPController", GPController);

function GPController(contextPath, $scope, $http, $location) {

    var vm = this;
    vm.contextPath = contextPath;


    // /////////////// //
    // Active Tab Code //
    // /////////////// //

    $scope.activeTab = $location.search().tab || 'playmat';

    $scope.isActiveTab = function(tabName) {

        return (tabName === $scope.activeTab);
    };

    $scope.tabClass = function(tabName) {

        if (tabName === $scope.activeTab) {
            return 'active';
        }

        return '';
    };

    $scope.activate = function(tabName) {
        $scope.activeTab = tabName;
        $location.search("tab", tabName);
    };


    // ///////////////// //
    // Data Loading Code //
    // ///////////////// //

    $scope.eventError = '';
    $scope.winnerError = '';
    $scope.artError = '';
    $scope.artistsError = '';
    $scope.eventsLoaded = false;
    $scope.artLoaded = false;
    $scope.artistsLoaded = false;
    $scope.winnersLoaded = false;

    $scope.eventData = {

        years: [],              // All the years for which we have events in order
        currentYear: null,
        events: [],             // All the Events, in Start Date order
        currentEvent: null,
        winners: {},            // Event_ID -> Winner Object
        currentWinner: null,
        art: {},                // Art_ID -> Art Object
        currentArt: null,
        artists: {},            // Artist_ID -> Artist Object
        currentArtist: null,
        baseUrl: null
    };

    $scope.loadWinners = function() {

        $http.get('/api/EventWinner').then(

            function successCallback(response) {

                for (var i = 0 ; i < response.data.length ; ++i ) {

                    var winner = response.data[i];
                    $scope.eventData.winners[winner.event.id] = winner;
                }

                $scope.eventData.currentWinner = $scope.eventData.winners[$scope.eventData.currentEvent.id];
                $scope.winnersLoaded = true;

            }, function errorCallback(response) {

                $scope.winnerError = response.data;
            }
        );
    };

    $scope.loadArtists = function() {

        $http.get('/api/Artist').then(

            function successCallback(response) {

                for ( var i = 0 ; i < response.data.length ; ++i ) {

                    var artist = response.data[i];
                    $scope.eventData.artists[artist.id] = artist;
                }

                if( $scope.eventData.currentArt ) {
                    $scope.eventData.currentArtist = $scope.eventData.artists[$scope.eventData.currentArt.artist.id];
                }

                $scope.artistsLoaded = true;

            }, function errorCallback(response) {

                $scope.artistsError = response.data;
            }
        );
    };

    $scope.loadArt = function() {

        $http.get('/api/Art').then(

            function successCallback(response) {

                for ( var i = 0 ; i < response.data.length ; ++i ) {

                    var art = response.data[i];
                    $scope.eventData.art[art.id] = art;
                }

                if ($scope.eventData.currentEvent.art) {

                    $scope.eventData.currentArt = $scope.eventData.art[$scope.eventData.currentEvent.art.id];
                }

                $scope.artLoaded = true;
                $scope.loadArtists();

            }, function errorCallback(response) {

                $scope.artError = response.data;
            }
        );
    };

    $scope.getBaseUrl = function() {

        $http.get('/api/Image/getImageBaseURL').then(

            function successCallback(response) {

                $scope.eventData.baseUrl = response.data.url;

                for ( var i = 0 ; i < $scope.eventData.events.length ; i++ ) {

                    var event = $scope.eventData.events[i];
                    if (event.playmatFileName) {

                        event.matUrl = $scope.eventData.baseUrl + "playmats/" + event.playmatFileName;
                    }
                    else {
                        event.matUrl = "assets/ComingSoon.jpg";
                    }
                }

            }, function errorCallback(response) {

                $scope.artError += 'Failed to get Art URL: ' + response.data;
            }
        );
    };

    // Events come from the server ordered by Start Date
    $scope.loadEvents = function() {

        $http.get('/api/Event').then(

            function successCallback(response) {

                $scope.eventData.events = response.data;

                var selected = Number($location.search().event);
                if ( selected ) {

                    $scope.eventData.currentEvent = response.data.find(function(el) { return el.id === selected });
                }

                if ( $scope.eventData.currentEvent === null ) {

                    var now = new Date();
                    for ( var i = 1 ; i < response.data.length; ++i ) {

                        var event = response.data[i];
                        if (event.startDate > now) {

                            $scope.eventData.currentEvent = response.data[i - 1];
                            break;
                        }
                    }
                }

                if ($scope.eventData.currentEvent === null) {

                    $scope.eventData.currentEvent = response.data[response.data.length - 1];
                }

                var lastYear =  null;
                for ( var j = 0 ; j < $scope.eventData.events.length ; ++j ) {

                    var year = $scope.eventData.events[j].startDate.getFullYear();
                    if ( year !== lastYear ) {
                        $scope.eventData.years.push( year );
                        lastYear = year;
                    }
                }
                $scope.eventData.currentYear = $scope.eventData.currentEvent.startDate.getFullYear();
                $scope.eventsLoaded = true;

                $scope.loadArt();
                $scope.loadWinners();
                $scope.getBaseUrl();

            }, function errorCallback(response) {

                $scope.eventError = response.data;
            }
        );
    };

    $scope.loadEvents();


    // //////////////////// //
    // Event Selection Code //
    // //////////////////// //

    $scope.selectYear = function() {

        for ( var i = 0 ; i < $scope.eventData.events.length ; ++i ) {

            var event = $scope.eventData.events[i];
            if (event.startDate.getFullYear() === $scope.eventData.currentYear) {

                $scope.eventData.currentEvent = event;
                $scope.selectEvent();
                break;
            }
        }
    };

    $scope.selectEvent = function() {

        if ($scope.eventData.currentEvent) {

            $scope.eventData.currentYear = $scope.eventData.currentEvent.startDate.getFullYear();
            $scope.eventData.currentWinner = $scope.eventData.winners[$scope.eventData.currentEvent.id];

            if ( $scope.eventData.currentEvent.art ) {
                $scope.eventData.currentArt = $scope.eventData.art[$scope.eventData.currentEvent.art.id];
            }
            else {
                $scope.eventData.currentArt = null;
            }

            if ( $scope.eventData.currentArt ) {
                $scope.eventData.currentArtist = $scope.eventData.artists[$scope.eventData.currentArt.artist.id];
            }
            else {
                $scope.eventData.currentArtist = null;
            }

            $scope.setEventId();
        }
    };

    $scope.setEventId = function() {

        var id = $scope.eventData.currentEvent.id;
        if (id && $location.search().id !== id) {
            $location.search("event", id);
        }
    };

    $scope.$on('$locationChangeSuccess', function() {

        // if event or tab changed, update
        var searchId = Number($location.search().event);
        var loadedEvent = $scope.eventData.currentEvent;
        var searchTab = $location.search().tab;

        if (searchId && loadedEvent && searchId !== loadedEvent.id) {

            $scope.selectEvent();
        }

        if (searchTab && $scope.activeTab !== searchTab) {

            $scope.activate(searchTab);
        }
    });


    // /////////////// //
    // Winner Tab Code //
    // /////////////// //

    $scope.isFutureGP = function() {

        var now = new Date();
        return !$scope.eventData.currentEvent || $scope.eventData.currentEvent.startDate > now;
    };

    $scope.gpStartDateString = function() {

        if ($scope.eventData.currentEvent) {

            var date = new Date();
            var localOffset = date.getTimezoneOffset() * 60000;
            var startDate = new Date($scope.eventData.currentEvent.startDate.getTime() + localOffset);
            var options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };

            return startDate.toLocaleDateString("en-US", options);
        }

        return "[Error:  No Event Selected.]";
    };

    $scope.getWinnerImageName = function() {

        if ($scope.eventData.currentEvent) {

            var winner = $scope.eventData.winners[$scope.eventData.currentEvent.id];
            if (winner && winner.imageName ) {

                return $scope.eventData.baseUrl + "winners/" + winner.imageName;
            }
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

    $scope.bountiesExist = function() {

        if ( $scope.eventData.currentEvent ) {
            var currentBounties = $scope.getPrizeList();
            return currentBounties && currentBounties.length > 0;
        }

        return false;
    };

    $scope.getPrizeList = function() {

        if ($scope.eventData.currentEvent) {

            return $scope.bountyArray.bountyHash[$scope.eventData.currentEvent.id];
        }

        return [];
    };


    // ////////////// //
    // Horse race Tab //
    // ////////////// //

    $scope.getHorseRaceSrc = function() {

        if ($scope.eventData.currentEvent) {

            return '/assets/horserace/index.html?race=' + $scope.eventData.currentEvent.id;
        }
        else {

            return '/assets/horserace/notfound.html';
        }
    };

}