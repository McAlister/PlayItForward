//= wrapped

angular
    .module("playitforward.index")
    .factory("eventPersistenceService", eventPersistenceService);

// This service stores event related data in local storage
// to minimize scraping of URLs.
function eventPersistenceService($http) {

    var eventData = null;


    /* ***************** *
     * Persistence Layer *
     * ***************** */

    var persistEventData = function () {

        localStorage.setItem('eventDataObj', JSON.stringify(eventData));
    };

    var loadEventData = function() {

        var jsonString = localStorage.getItem('eventDataObj');
        try {
            eventData = JSON.parse(jsonString);
        } catch (ex) {
            eventData = null;
            console.log("Failure to load eventData from local storage");
        }

        if (eventData == null) {
            eventData = {

                currentPlayerName: null,
                currentImage: null,
                watchHash: {}               // EventId -> [players to watch]
            };
        }
    };

    loadEventData();

    /* ******************************** *
     * Event Standing Scraper Functions *
     * ******************************** */

    var loadNextRound = function(eventId, round) {

        if (round > 15) {
            eventData.watchHash[eventId].loading = false;
            persistEventData();
            return;
        }

        $http.get('/api/EventStanding/loadEvent/' + eventId + '/round/' + round).then(

            function successCallback(response) {

                var loaded = response.data.loaded;
                if (loaded) {

                    eventData.watchHash[eventId].lastRound = round;
                    loadNextRound(eventId, round + 1);

                } else {
                    eventData.watchHash[eventId].loading = false;
                    persistEventData();
                }

            }, function errorCallback(response) {

                alert('Unable to load rounds: ' + response.message);
                eventData.watchHash[eventId].loading = false;
            }
        );
    };

    var getLatestRoundForEvent = function(eventId) {

        $http.get('/api/EventStanding/event/' + eventId).then(

            function successCallback(response) {

                var lastRound = response.data.lastRound;
                eventData.watchHash[eventId].lastRound = lastRound;
                persistEventData();

                if (lastRound < 15) {

                    eventData.watchHash[eventId].loading = true;
                    loadNextRound(eventId, lastRound + 1);
                } else {
                    eventData.watchHash[eventId].loading = false;
                }

            }, function errorCallback(response) {

                alert('Unable to load rounds: ' + response.message);
            }
        );
    };


    /* ***************** *
     * Exposed Functions *
     * ***************** */

    var loadLatestRoundForEvent = function(eventId) {

        var lastRound = 0;
        if (eventData.watchHash.hasOwnProperty(eventId)) {
            lastRound = eventData.watchHash[eventId].lastRound;
        } else {
            eventData.watchHash[eventId] = {
                lastRound: 0,
                loading: false,
                watchList: []
            }
        }

        if (lastRound < 15) {
            getLatestRoundForEvent(eventId);
        }
    };

    var addPlayerToEventList = function(eventId) {

        var playerHash = {
            name: eventData.currentPlayerName,
            art: eventData.currentImage + '.png'
        };

        if (! eventData.watchHash.hasOwnProperty(eventId)) {

            eventData.watchHash[eventId] = {
                watchList: [],
                lastRound: 0,
                loading: false
            }
        }

        eventData.watchHash[eventId].watchList.unshift(playerHash);
        persistEventData();
    };

    var removePlayerFromEventList = function(eventId, name) {

        var eventInfo = eventData.watchHash[eventId];
        if (eventInfo != null && eventInfo.hasOwnProperty("watchList")) {

            for (var i = eventInfo.watchList.length - 1 ; i >= 0 ; i--) {
                if (eventInfo.watchList[i].name === name) {
                    eventInfo.watchList.splice(i, 1);
                }
            }

            persistEventData();
        }
    };

    //noinspection JSUnusedGlobalSymbols
    return {

        eventData: eventData,

        artList: ["Arlinn", "Avacyn", "Braids", "Chandra", "Elspeth", "Kaya",
            "Kemba", "Kiora", "Liliana", "Mirri", "Nahiri",
            "Narset", "Nissa", "Olivia", "Oona", "Pia", "Saheeli",
            "SliverQueen", "Tamiyo", "Vraska", "Wort"],

        loadLatestRoundForEvent: loadLatestRoundForEvent,
        addPlayerToEventList: addPlayerToEventList,
        removePlayerFromEventList: removePlayerFromEventList
    };
}