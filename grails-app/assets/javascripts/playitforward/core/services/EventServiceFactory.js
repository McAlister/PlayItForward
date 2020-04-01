//= wrapped

angular
    .module("playitforward.core")
    .factory("eventService", eventService);

function eventService($http, $location, $filter, eventPersistenceService, raceService) {

    var data = {

        eventsByYearAndType: {}, // {year:{typeId:[events]}}
        eventLinks: {},          // EventId -> [round indexed array of standings urls]
        playerHash: {}           // EventId -> [player names]
    };

    var service = {

        error: '',
        eventsLoaded: false,
        loading: false,

        eventData: eventPersistenceService.eventData,
        artList: eventPersistenceService.artList,

        currentEvent: null,
        eventToEdit: null,
        activeEvents: [],

        years: [],
        currentYear: 2017,

        types: [],
        currentType: null,

        organizers: [],
        currentOrganizer: null,

        currentLinks: [],
        playerList: []
    };


    /* ********************************************* *
     * Populate years, types, organizers, and events *
     * ********************************************* */

    var year = service.currentYear;
    var thisYear = new Date().getFullYear();
    while (year <= thisYear) {

        service.years.push(year);
        data.eventsByYearAndType[year] = {};
        service.currentYear = year;

        year++;
    }

    var loadEvents = function() {

        $http.get('/api/Event').then(

            function successCallback(response) {

                var events = response.data;
                service.currentEvent = events[0];

                for (var yearKey in data.eventsByYearAndType) {
                    for (var typeKey in data.eventsByYearAndType[yearKey]) {
                        data.eventsByYearAndType[yearKey][typeKey] = [];
                    }
                }

                for ( var i = 0; i < events.length; ++i ) {

                    var event = events[i];
                    var year = event.startDate.getFullYear();
                    var typeId = event.type.id;

                    event.startDate = new Date(event.startDate.toUTCString().substr(0, 16));
                    event.endDate = new Date(event.endDate.toUTCString().substr(0, 16));

                    data.eventsByYearAndType[year][typeId].push(event);
                }

                /* If event ID is specified on the url honor it */
                var eventId = $location.search().event;
                if (eventId) {

                    var eventList = $filter('filter')(events, {id: eventId});
                    if (eventList.length > 0) {

                        service.currentEvent = eventList[0];
                        service.currentYear = service.currentEvent.startDate.getFullYear();
                        for( i = 0 ; i < service.types.length ; ++i) {
                            if (service.types[i].id === service.currentEvent.type.id) {
                                service.currentType = service.types[i];
                                break;
                            }
                        }
                    }
                }

                service.selectedYearType();

                service.eventsLoaded = true;
                service.loading = false;
                service.error = '';

            }, function errorCallback(response) {

                service.error = 'Error: Could not load events: ' + response.data.message;
                service.eventsLoaded = false;
                service.loading = false
            }
        );
    };

    var loadOrganizations = function() {

        $http.get('/api/EventOrganizer').then(

            function successCallback(response) {

                service.organizers = response.data;
                service.currentOrganizer = service.organizers[0];
                loadEvents();

            }, function errorCallback() {
                service.error = 'Unable to load events, failure on org load.';
            }
        );
    };

    service.loadEvents = function() {

        if (Object.keys(data.eventsByYearAndType[2017]).length > 0 || service.loading) {
            return;
        }

        service.loading = true;

        $http.get('/api/EventType').then(

            function successCallback(response) {

                service.types = response.data;
                service.currentType = service.types[0];

                for (var i = 0 ; i < service.years.length ; i++) {
                    for (var j = 0 ; j < service.types.length ; j++) {
                        var year = service.years[i];
                        var typeId = service.types[j].id;
                        data.eventsByYearAndType[year][typeId] = [];
                    }
                }

                loadOrganizations();

            }, function errorCallback() {
                service.error = 'Unable to load events, failure on type load.';
            }
        );
    };


    /* ********************************************************** *
     * Functions for building a watchlist of players for an event *
     * ********************************************************** */

    service.getPlayersForEvent = function(eventId) {

        $http.get('/api/RawStandings/getPlayers/event/' + eventId).then(

            function successCallback(response) {

                if (response.data.hasOwnProperty("players")) {
                    data.playerHash[eventId] = response.data.players;
                    service.playerList = response.data.players;
                } else {
                    service.playerList = [];
                }

            }, function errorCallback() {

                alert ('Player load failed: first round standings aren\'t loaded yet.');
                service.playerList = [];
            }
        );
    };

    service.addPlayerToEvent = function(eventId) {

        eventPersistenceService.addPlayerToEventList(eventId);
        raceService.resetRace(eventId);
    };

    service.delPlayerFromEvent = function(eventId, name) {

        eventPersistenceService.removePlayerFromEventList(eventId, name);
        raceService.resetRace(eventId);
    };


    /* ********************************************************************* *
     * Functions that search for new events and round URLs of current events *
     * ********************************************************************* */

    service.scrapeNewEvents = function() {

        var data = {};
        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        $http.get('/api/RawStandings/year/' + service.currentYear + '/type/' + service.currentType.type, data, config).then(
            function(){
                loadEvents();
                window.alert('Scraped ' + service.currentType.description + ' web site for more events.');
            },
            function(response){
                window.alert('Error: Failed to load events! ' + response.data.message);
            }
        );
    };

    var scrapeStandings = function() {

        var data = {};
        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        $http.get('/api/RawStandings/event/' + service.currentEvent.id + '/type/' + service.currentType.type, data, config).then(

            function() {
                populateCurrentLinks(false);
            },
            function(response){
                window.alert('Error: Failed to load details! ' + response.data.message);
            }
        );
    };

    var populateCurrentLinks = function(scrapeOnFail) {

        var eventId = service.currentEvent.id;
        service.currentLinks = [];

        if (eventId in data.eventLinks && data.eventLinks[eventId].length > 0) {

            service.currentLinks = data.eventLinks[eventId];

        } else {

            $http.get('/api/EventLinks/event/' + eventId).then(

                function successCallback(response) {

                    data.eventLinks[eventId] = response.data;
                    service.currentLinks = data.eventLinks[eventId];

                    if (service.currentLinks.length < 15 && scrapeOnFail) {
                        scrapeStandings();
                    }

                }, function errorCallback() {

                    scrapeStandings();
                }
            );
        }
    };


    /* ************************ *
     * Select a Year/Type/Event *
     * ************************ */

    service.selectedEvent = function () {

        var id = service.currentEvent.id;
        if (id && $location.search().event !== id) {
            $location.search("event", id);
        }

        if (data.playerHash.hasOwnProperty(id)) {
            service.playerList = data.playerHash[id];
        } else {
            service.playerList = [];
        }

        populateEventToEdit();
        populateCurrentLinks(true);
        eventPersistenceService.loadLatestRoundForEvent(id);
        raceService.updateCurrentRace(id);
    };

    service.selectedYearType = function() {

        var year = service.currentYear;
        var typeId = service.currentType.id;

        service.activeEvents = data.eventsByYearAndType[year][typeId];

        var activeYear = service.currentEvent.startDate.getFullYear();
        var activeTypeId = service.currentEvent.type.id;
        if (activeYear !== year || activeTypeId !== typeId) {
            service.currentEvent = service.activeEvents[0];
        }

        service.selectedEvent();
    };


    /* ************************************* *
     * Admin functions to edit/create events *
     * ************************************* */

    var populateEventToEdit = function() {

        var event = service.currentEvent;
        service.eventToEdit = {

            id: event.id,
            name: event.name,
            startDate: event.startDate,
            endDate: event.endDate,
            eventFormat: event.eventFormat,
            eventUrl: event.eventUrl,

            organizer: {
                id: event.organizer.id
            },

            type: {
                id: event.type.id
            }
        };
    };

    service.upsertEvent = function() {

        var event = {

            name: service.eventToEdit.name,
            startDate: $filter('date')(service.eventToEdit.startDate, "yyyy-MM-dd"),
            endDate: $filter('date')(service.eventToEdit.endDate, "yyyy-MM-dd"),
            eventFormat: service.eventToEdit.eventFormat,
            eventUrl: service.eventToEdit.eventUrl,

            organizer: {
                id: service.eventToEdit.organizer.id
            },

            type: {
                id: service.eventToEdit.type.id
            }
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        if('id' in service.eventToEdit && service.eventToEdit.id > 0) {

            event.id = service.eventToEdit.id;

            $http.put('/api/Event/' + event.id, event, config).then(
                function(){
                    loadEvents();
                    window.alert('Event updated successfully.');
                },
                function(response){
                    window.alert('Error: Failed to edit Event! ' + response.data.message);
                }
            );
        }
        else {

            $http.post('/api/Event', event, config).then(
                function(){
                    loadEvents();
                    window.alert('Event added successfully.');
                },
                function(response){
                    window.alert('Error: Failed to add Event! ' + response.data.message);
                }
            );
        }

    };

    return service;
}