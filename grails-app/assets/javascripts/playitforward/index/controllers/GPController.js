//= wrapped

angular
    .module("playitforward.index")
    .controller("GPController", GPController);

function GPController(contextPath, $scope, $http, $interval,
                      tabService, eventService, artService) {

    var vm = this;
    vm.contextPath = contextPath;

    $scope.eventService = eventService;
    eventService.loadEvents();

    $scope.tabService = tabService;
    tabService.registerTabList( "GPs", "config", ["standings", "config", "watch"] );

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

    $scope.stats = {
        show: false,
        name: "",
        points: "",
        rank: "",
        art: "",
        top: 50,
        left: 50
    };

    var positionToolTip = function(horse) {

        var left = parseInt(horse.style.left.slice(0, -2));
        var top = parseInt(horse.parentElement.style.top.slice(0, -2)) - 20;

        if ( left < 200 ) {
            $scope.stats.left = left + horse.width + 15;
        } else {
            $scope.stats.left = 15;
        }

        if (top > 490) {
            $scope.stats.top = 490;
        } else {
            $scope.stats.top = top;
        }
    };

    $scope.unselectHorse = function () {

        $scope.stats.show = false;
        var horseList = document.getElementsByClassName("horse");
        for (var i = 0 ; i < horseList.length ; i++) {
            horseList[i].classList.remove("selected");
        }
    };

    $scope.selectHorse = function(id, name) {

        $scope.unselectHorse();
        var horse = document.getElementById(id);
        horse.classList.add("selected");
        positionToolTip(horse);

        var trackIndex = eventService.currentRace.playerTrackHash[name];
        var track = eventService.currentRace.tracks[trackIndex];

        $scope.stats.name = track.name;
        $scope.stats.points = track.points;
        $scope.stats.rank = track.rank;
        $scope.stats.art = track.img;

        $scope.stats.show = true;
        event.stopPropagation();
    };

    $scope.showMask = function() {

        return ! eventService.currentRace.loaded;
    };

    $scope.startRace = function(eventId) {

        var config = {
            transformRequest: angular.identity,
            headers : {
                'Content-Type': undefined
            }
        };

        var nameList = [];
        var artList = [];
        for (var i = 0 ; i < eventService.eventData[eventId].watchList.length ; i++ ) {

            var watch = eventService.eventData[eventId].watchList[i];
            nameList.push(watch.name);
            artList.push(watch.art);
        }

        var formData = new FormData();
        formData.append("nameList", JSON.stringify(nameList));
        formData.append("artList", JSON.stringify(artList));

        $http.post('/api/EventStanding/startRace/' + eventId, formData, config).then(

            function successCallback(response) {

                var raceData = eventService.raceHash[eventId];
                var $result = response.data;

                for (var i = 0 ; i < $result.length ; i++) {

                    var data = $result[i];
                    var round = data.round;
                    var index = raceData.playerTrackHash[data.name];

                    if (!raceData.standingsHash.hasOwnProperty(round)) {
                        raceData.standingsHash[round] = [];
                        while (raceData.standingsHash[round].length < raceData.tracks.length) {
                            raceData.standingsHash[round].push({});
                        }
                    }

                    raceData.standingsHash[round][index] = data;
                }

                raceData.loaded = true;
                raceData.playing = true;

            }, function errorCallback(response) {

                alert('Unable to load rounds: ' + response.data.message);
            }
        );
    };

    var scrollToWinner = function() {

        var winner = null;
        var race = eventService.currentRace;
        for (var i = 0 ; i < race.tracks.length ; i++) {

            var track = race.tracks[i];
            if (winner == null || winner.rank > track.rank) {
                winner = track;
            }
        }

        if (winner == null) {
            return;
        }

        var viewPort = document.getElementById("horseRace");
        var left = parseInt(winner.left + (race.avatarHeight/2));
        var rangeStart = viewPort.scrollLeft;
        var rangeEnd = rangeStart + viewPort.clientWidth;

        if (left < rangeStart || left > rangeEnd) {
            var newLeft = left - (viewPort.clientWidth / 2);
            if (newLeft < 0) {
                newLeft = 0;
            }

            viewPort.scroll({
               left: newLeft,
               behavior: "smooth"
            });
            // viewPort.scrollLeft = newLeft;
        }
    };

    var updateToRound = function(round, scroll) {

        var raceData = eventService.currentRace;
        var standingsList = raceData.standingsHash[round];
        for ( var i = 0 ; i < standingsList.length ; i++ ) {

            var standing = standingsList[i];
            var index = raceData.playerTrackHash[standing.name];
            var track = raceData.tracks[index];
            var startLeft = Math.round(35 - (raceData.avatarHeight / 2));

            // 35 px to 0 then 90 to the rest so each point is 30!
            track.points = standing.score;
            track.rank = standing.rank;
            track.left = Math.round(startLeft + (30 * track.points));
            track.stickLeft = Math.round(34 + (30 * track.points));
        }

        eventService.currentRace.currentRound = round;
        if (scroll) {
            scrollToWinner();
        }
    };

    $scope.goBack = function(scroll) {

        if (eventService.currentRace.currentRound > 1) {
            updateToRound( eventService.currentRace.currentRound - 1, scroll);
        }
    };

    $scope.goForward = function(scroll) {

        if (eventService.currentRace.currentRound < eventService.currentRace.maxRound) {
            updateToRound( eventService.currentRace.currentRound + 1, scroll);
        }
    };

    $scope.play = function() {

        eventService.currentRace.playing = true;
    };

    $scope.pause = function() {

        eventService.currentRace.playing = false;
    };

    var playRace = function() {

        var race = eventService.currentRace;
        if (race.playing) {

            if (race.currentRound < race.maxRound) {
                $scope.goForward(true);
            } else {
                $scope.pause();
            }
        }
    };

    $interval(playRace, 1000);

}