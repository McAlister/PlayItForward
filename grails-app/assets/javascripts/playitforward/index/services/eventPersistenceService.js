//= wrapped

angular
    .module("playitforward.index")
    .factory("eventPersistenceService", eventPersistenceService);

// This service stores event related data in local storage
// to minimize scraping of URLs.
function eventPersistenceService($cookies, $http) {

    var eventData = $cookies.getObject("eventDataObj");
    if (eventData == null) {
        eventData = {

            currentPlayerName: null,
            currentImage: null
        };
    }

    var loadNextRound = function(eventId, round) {

        if (round > 15) {
            eventData[eventId].loading = false;
            $cookies.putObject("eventDataObj", eventData);
            return;
        }

        $http.get('/api/EventStanding/loadEvent/' + eventId + '/round/' + round).then(

            function successCallback(response) {

                var loaded = response.data.loaded;
                if (loaded) {

                    eventData[eventId].lastRound = round;
                    loadNextRound(eventId, round + 1);

                } else {
                    eventData[eventId].loading = false;
                    $cookies.putObject("eventDataObj", eventData);
                }

            }, function errorCallback(response) {

                alert('Unable to load rounds: ' + response.message);
                eventData[eventId].loading = false;
            }
        );
    };

    var getLatestRoundForEvent = function(eventId) {

        $http.get('/api/EventStanding/event/' + eventId).then(

            function successCallback(response) {

                var lastRound = response.data.lastRound;
                eventData[eventId].lastRound = lastRound;
                $cookies.putObject("eventDataObj", eventData);

                if (lastRound < 15) {

                    eventData[eventId].loading = true;
                    loadNextRound(eventId, lastRound + 1);
                }

            }, function errorCallback(response) {

                alert('Unable to load rounds: ' + response.message);
            }
        );
    };

    //noinspection JSUnusedGlobalSymbols
    return {

        eventData: eventData,

        artList: ["Arlinn", "Avacyn", "Braids", "Chandra", "Elspeth", "Kaya",
            "Kemba", "Kiora", "Liliana", "Mirri", "Nahiri",
            "Narset", "Nissa", "Olivia", "Oona", "Pia", "Saheeli",
            "SliverQueen", "Tamiyo", "Vraska", "Wort"],

        loadLatestRoundForEvent: function(eventId) {

            var lastRound = 0;
            if (eventData.hasOwnProperty(eventId)) {
                lastRound = eventData[eventId].lastRound;
            } else {
                eventData[eventId] = {
                    lastRound: 0,
                    loading: false,
                    watchList: []
                }
            }

            if (lastRound < 15) {
                getLatestRoundForEvent(eventId);
            }
        },

        addPlayerToEventList: function(eventId) {

            var playerHash = {
                name: eventData.currentPlayerName,
                art: eventData.currentImage + '.png'
            };

            if (! eventData.hasOwnProperty(eventId)) {
                eventData[eventId] = {
                    watchList: [],
                    lastRound: 0,
                    loading: false
                }
            }

            if (! eventData[eventId].hasOwnProperty("watchList")) {
                eventData[eventId].watchList = [];
            }

            eventData[eventId].watchList.push(playerHash);
            $cookies.putObject("eventDataObj", eventData);
        },

        removePlayerFromEventList: function(eventId, name) {

            var eventInfo = eventData[eventId];
            if (eventInfo != null && eventInfo.hasOwnProperty("watchList")) {

                for (var i = eventInfo.watchList.length - 1 ; i >= 0 ; i--) {
                    if (eventInfo.watchList[i].name === name) {
                        eventInfo.watchList.splice(i, 1);
                    }
                }
            }

            $cookies.putObject("eventDataObj", eventData);
        }

    };
}