//= wrapped

angular
    .module("playitforward.index")
    .factory("eventPersistenceService", eventPersistenceService);

// This service stores event related data in local storage
// to minimize scraping of URLs.
function eventPersistenceService($cookies, $http) {

    var eventData = $cookies.getObject("eventDataObj");
    if (eventData == null) {
        eventData = {};
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

        loadLatestRoundForEvent: function(eventId) {

            var lastRound = 0;
            if (eventData.hasOwnProperty(eventId)) {
                lastRound = eventData[eventId].lastRound;
            } else {
                eventData[eventId] = {
                    lastRound: 0,
                    loading: false
                }
            }

            if (lastRound < 15) {
                getLatestRoundForEvent(eventId);
            }
        }
    };
}