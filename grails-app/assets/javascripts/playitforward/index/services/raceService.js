//= wrapped

angular
    .module("playitforward.index")
    .factory("raceService", raceService);

// This service stores event related data in local storage
// to minimize scraping of URLs.
function raceService($http, $interval) {

    var data = {

        ovalUrlBase: 'https://playitforward-magic.s3-us-west-2.amazonaws.com/images/race/ovals/',
        squareUrlBase: 'https://playitforward-magic.s3-us-west-2.amazonaws.com/images/race/',
        active: null,               // The Race object currently being animated.
        raceHash: {},               // EventId -> Race object.
        toolTip: {                  // If a user is selected their data shows here.
            show: false,
            name: "",
            points: "",
            rank: "",
            img: "",
            style: "top: 50px; left: -200px;"
        }
    };


    // ///////////////// //
    // Private Functions //
    // ///////////////// //

    var createNewRace = function(eventId) {

        var raceData = {

            eventId: eventId,
            maxRound: 0,
            round: 0,
            empty: false,
            loaded: false,              // if Standings data are populated.
            playing: false,             // Update to next round every second.
            tracks: [{},{},{},{},{}],   // This drives the horserace animation.
            playerTrackHash: {},        // player name -> tracks array index.
            standingsHash: {},          // round -> [standings] for populating tracks
            step: 104,                  // spacing between tracks.
            avatarHeight: 121           // how high the ovals are.
        };

        // We are positioning each horse out of bounds to the left
        // so they won't be seen.  On load they will enter the tracks.
        for (var i = 0 ; i < 5 ; i++) {

            var track = raceData.tracks[i];
            track.id = 'horse_' + i;
            track.eventId = eventId;
            track.left = -300;
            track.stickLeft = "-25px";
            track.top = ((i * raceData.step) + 80) + "px";
            track.offset = (-40 * i) + "px center";
            track.rank = -1;
            track.points = 0;
            track.name = "";
            track.img = "Arlinn.png";
        }

        return raceData;
    };

    var recalculatePositioning = function(raceData) {

        var trackCount = raceData.tracks.length;
        raceData.step = Math.round(520 / trackCount);
        raceData.avatarHeight = Math.round(raceData.step * 1.2);

        var startLeft = Math.round(35 - (raceData.avatarHeight / 2));
        for (var i = 0 ; i < trackCount ; i++) {

            if (raceData.tracks[i].name) {
                raceData.tracks[i].left = startLeft;
                raceData.tracks[i].top = ((i * raceData.step) + 80) + "px";
            }
        }
    };

    // Because we have a minimum of 5 tracks we need to
    // map the first 4 to populate from the center out.
    var mapIndexToTrack = function(i) {

        if (i === 0) { return 2; }
        if (i === 2) { return 3; }
        if (i === 3) { return 0; }
        return i;
    };

    var getWatchListFromServer = function(eventId, watchList) {

        var raceData = data.raceHash[eventId];

        var config = {
            transformRequest: angular.identity,
            headers: {
                'Content-Type': undefined
            }
        };

        var nameList = [];
        var artList = [];
        for (var i = 0; i < watchList.length; i++) {

            var watch = watchList[i];
            nameList.push(watch.name);
            artList.push(watch.art);
        }

        var formData = new FormData();
        formData.append("nameList", JSON.stringify(nameList));
        formData.append("artList", JSON.stringify(artList));

        $http.post('/api/EventStanding/startRace/' + eventId, formData, config).then(

            function successCallback(response) {

                var $result = response.data;
                var maxRound = 0;

                for (var i = 0; i < $result.length; i++) {

                    var data = $result[i];
                    var round = data.round;
                    var index = raceData.playerTrackHash[data.name];

                    if (!raceData.standingsHash.hasOwnProperty(round)) {
                        raceData.standingsHash[round] = [];
                    }

                    raceData.standingsHash[round][index] = data;

                    if (round > maxRound) {
                        maxRound = round;
                    }
                }

                raceData.maxRound = maxRound;
                raceData.loaded = true;
                raceData.playing = true;

            }, function errorCallback(response) {

                alert('Unable to load rounds: ' + response.data.message);
                raceData.loaded = false;
                raceData.playing = false;
            }
        );
    };

    var initTrack = function(trackIndex, raceData, data) {

        if (!raceData.tracks.hasOwnProperty(trackIndex)) {
            raceData.tracks[trackIndex] = {};
        }

        var track = raceData.tracks[trackIndex];

        track.id = 'horse_' + trackIndex;
        track.eventId = raceData.eventId;
        track.left = 34;
        track.stickLeft = "34px";
        track.top = ((trackIndex * raceData.step) + 80) + "px";
        track.rank = -1;
        track.points = 0;
        track.offset = (-40 * trackIndex) + "px center";
        track.name = data.name;
        track.img = data.img;
    };

    var positionToolTip = function(horse) {

        var top = parseInt(horse.parentElement.style.top.slice(0, -2)) - 20;
        if (top > 490) {
            top = 490;
        }

        var viewPort = document.getElementById("horseRace");

        var left = parseInt(horse.style.left.slice(0, -2));
        var rangeLeft = viewPort.scrollLeft;
        var absoluteLeft = left - rangeLeft;

        if (absoluteLeft > 290) {
            left = absoluteLeft - 275;
        } else if (viewPort.clientWidth - absoluteLeft > 275 + data.active.avatarHeight ) {
            left = absoluteLeft + data.active.avatarHeight;
        } else {
            left = (viewPort.clientWidth / 2) - 138;
        }

        data.toolTip.style = "top: " + top + "px; left: " + left + "px;";
    };


    // ////////////// //
    // Animation Loop //
    // ////////////// //

    var clearAnimationClass = function() {

        var horses = document.getElementsByClassName("horse");
        for (var i = 0 ; i < horses.length ; i++) {
            horses[i].classList.remove("sliding");
        }

        void horses[0].offsetWidth;
    };

    var scrollToWinner = function() {

        var winner = null;
        var raceData = data.active;
        for (var i = 0 ; i < raceData.tracks.length ; i++) {

            var track = raceData.tracks[i];
            if (track.rank > 0 && (winner == null || (track.rank > 0 && winner.rank > track.rank))) {
                winner = track;
            }
        }

        if (winner == null) {
            return;
        }

        var viewPort = document.getElementById("horseRace");
        var left = parseInt(winner.left + (raceData.avatarHeight/2));
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
        }
    };

    var updateToRound = function(round, scroll) {

        clearAnimationClass();

        var raceData = data.active;
        var standingsList = raceData.standingsHash[round];
        for ( var i = 0 ; i < standingsList.length ; i++ ) {

            var standing = standingsList[i];
            if (standing) {
                var trackIndex = raceData.playerTrackHash[standing.name];
                var track = raceData.tracks[trackIndex];
                var startLeft = Math.round(35 - (raceData.avatarHeight / 2));

                // 35 px to 0 then 90 to the rest of the markers so each point is 30!
                track.points = standing.score;
                track.rank = standing.rank;
                track.left = Math.round(startLeft + (30 * track.points));
                track.stickLeft = Math.round(34 + (30 * track.points)) + "px";

                var horse = document.getElementById(track.id);
                horse.classList.add("sliding");
            }
        }

        raceData.round = round;
        if (scroll) {
            scrollToWinner();
        }
    };

    var playRace = function() {

        var raceData = data.active;
        if (raceData && raceData.playing) {

            if (raceData.round < raceData.maxRound) {
                goForward(true);
            } else {
                pause();
            }
        }
    };

    $interval(playRace, 1000);


    // ///////////////// //
    // Exposed Functions //
    // ///////////////// //

    var updateCurrentRace = function (eventId) {

        if (data.raceHash.hasOwnProperty(eventId)) {
            data.active = data.raceHash[eventId];
        } else {
            data.active = createNewRace(eventId);
            data.raceHash[eventId] = data.active;
        }
    };

    var loadRaceFromWatchList = function(eventId, watchList) {

        var raceData = data.raceHash[eventId];
        if (raceData == null) {
            updateCurrentRace(eventId);
            raceData = data.raceHash[eventId];
        }

        if (watchList.length > 5) {
            raceData.step = Math.round(520 / watchList.length);
            raceData.avatarHeight = Math.round(raceData.step * 1.2);
        }

        var startLeft = Math.round(35 - (raceData.avatarHeight / 2));

        for (var i = 0; i < watchList.length; i++) {

            if (i > 4) {
                raceData.tracks.push({});
            }

            var trackIndex = mapIndexToTrack(i);
            raceData.playerTrackHash[watchList[i].name] = trackIndex;
            var track = raceData.tracks[trackIndex];

            track.id = 'horse_' + trackIndex;
            track.eventId = eventId;
            track.left = startLeft;
            track.stickLeft = "34px";
            track.top = ((trackIndex * raceData.step) + 80) + "px";
            track.rank = -1;
            track.points = 0;
            track.offset = (-40 * trackIndex) + "px center";
            track.name = watchList[i].name;
            track.img = watchList[i].art;
        }

        getWatchListFromServer(eventId, watchList);
    };

    var loadRaceFromServer = function(eventId) {

        var raceData = data.raceHash[eventId];
        if (raceData == null) {
            updateCurrentRace(eventId);
            raceData = data.raceHash[eventId];
        }

        $http.get('/api/EventStanding/loadDefaultStandings/' + eventId).then(

            function successCallback(response) {

                var $result = response.data;
                var maxRound = 0;

                raceData.empty = $result.length === 0;
                for (var i = 0; i < $result.length; i++) {

                    var data = $result[i];
                    var round = parseInt(data.round);
                    var trackIndex = mapIndexToTrack(i);
                    if (round === 1) {
                        initTrack(trackIndex, raceData, data);
                        raceData.playerTrackHash[data.name] = trackIndex;
                    } else {
                        trackIndex = raceData.playerTrackHash[data.name];
                    }

                    if (!raceData.standingsHash.hasOwnProperty(round)) {
                        raceData.standingsHash[round] = [];
                    }

                    raceData.standingsHash[round][trackIndex] = data;

                    if (round > maxRound) {
                        maxRound = round;
                    }
                }

                recalculatePositioning(raceData);

                raceData.maxRound = maxRound;
                raceData.loaded = true;
                raceData.playing = true;

            }, function errorCallback() {

                alert('Unable to load rounds: ' + response.data.message);
                raceData.loaded = false;
                raceData.playing = false;
            }
        );
    };

    var play = function() {
        data.active.playing = true;
    };

    var pause = function() {
        data.active.playing = false;
    };

    var goForward = function(scroll) {

        if (data.active && data.active.round < data.active.maxRound) {
            updateToRound( data.active.round + 1, scroll);
        }
    };

    var goBack = function(scroll) {

        if (data.active && data.active.round > 1) {
            updateToRound( data.active.round - 1, scroll);
        }
    };

    var unselectHorse = function () {

        data.toolTip.show = false;
        var horseList = document.getElementsByClassName("horse");
        for (var i = 0 ; i < horseList.length ; i++) {
            horseList[i].classList.remove("selected");
        }
    };

    var selectHorse = function(id, name) {

        unselectHorse();
        var horse = document.getElementById(id);
        horse.classList.add("selected");
        positionToolTip(horse);

        var trackIndex = data.active.playerTrackHash[name];
        var track = data.active.tracks[trackIndex];

        data.toolTip.name = track.name;
        data.toolTip.points = track.points;
        data.toolTip.rank = track.rank;
        data.toolTip.img = track.img;

        data.toolTip.show = true;
        event.stopPropagation();
    };

    var resetRace = function(eventId) {

        delete data.raceHash[eventId];
        updateCurrentRace(eventId);
    };

    //noinspection JSUnusedGlobalSymbols
    return {

        raceData: data,
        toolTip: data.toolTip,
        updateCurrentRace: updateCurrentRace,
        loadRaceFromWatchList: loadRaceFromWatchList,
        loadRaceFromServer: loadRaceFromServer,
        play: play,
        pause: pause,
        goForward: goForward,
        goBack: goBack,
        unselectHorse: unselectHorse,
        selectHorse: selectHorse,
        resetRace: resetRace
    };
}