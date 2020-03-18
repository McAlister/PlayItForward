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
    tabService.registerTabList( "GPs", "standings", ["standings", "config", "race"] );

    $scope.artService = artService;
    artService.loadArt();

    $scope.monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

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


    $scope.gpRangeString = function() {

        if (eventService.currentEvent) {

            var startDate = new Date(eventService.currentEvent.startDate.getTime());
            var endDate = new Date(eventService.currentEvent.endDate.getTime());

            var startDay = startDate.getUTCDate();
            var startMonth = $scope.monthNames[startDate.getUTCMonth()];
            var endDay = endDate.getUTCDate();
            var endMonth = $scope.monthNames[endDate.getUTCMonth()];

            return startMonth + " " + startDay + " - " + endMonth + " " + endDay;
        }

        return "[Error:  No Event Selected.]";
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