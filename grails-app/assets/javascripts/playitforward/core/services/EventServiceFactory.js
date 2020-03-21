//= wrapped

angular
    .module("playitforward.core")
    .factory("eventService", eventService);

function eventService($http, $location, $filter) {

    var service = {

        eventsLoaded: false,
        error: '',
        events: [],
        currentEvent: null,
        eventToEdit: null,
        activeEvents: [],
        years: [],
        currentYear: 2017,
        types: [],
        currentType: null,
        eventLinks: {},
        currentLinks: []
    };

    var year = service.currentYear;
    var thisYear = new Date().getFullYear();
    while (year <= thisYear) {
        service.years.push(year);
        year++;
    }

    var populateCurrentLinks = function(scrapeOnFail) {

        var eventId = service.currentEvent.id;
        service.currentLinks = [];

        if (eventId in service.eventLinks && service.eventLinks[eventId].length > 0) {

            service.currentLinks = service.eventLinks[eventId];

        } else {

            $http.get('/api/EventLinks/event/' + eventId).then(

                function successCallback(response) {

                    service.eventLinks[eventId] = response.data;
                    service.currentLinks = service.eventLinks[eventId];

                    if (service.currentLinks.length < 15 && scrapeOnFail) {
                        service.scrapeStandings();
                    }

                }, function errorCallback() {

                    service.scrapeStandings();
                }
            );
        }
    };

    var populateEventToEdit = function() {

        var event = service.currentEvent;

        var data = {

            id: event.id,
            name: event.name,
            eventCode: event.eventCode,
            coordinator: event.coordinator,
            startDate: event.startDate,
            endDate: event.endDate,
            eventFormat: event.eventFormat,
            playmatFileName: event.playmatFileName,
            cfbEventNum: event.cfbEventNum,
            eventUrl: event.eventUrl,

            organizer: {
                id: event.organizer.id
            },

            type: {
                id: event.type.id
            }

        };

        if (event.art) {

            data.art = {
                id: event.art.id
            };
        }

        service.eventToEdit = data;
    };

    var getTypes = function() {

        $http.get('/api/EventType').then(

            function successCallback(response) {

                service.types = response.data;
                service.currentType = service.types[0];
                service.reloadEvents();

            }, function errorCallback(response) {

                service.types = [{"id":1,"description":"Grand Prix","type":"GP"},
                    {"id":3,"description":"Star City Open","type":"SCO"}];
                service.currentType = service.types[0];
                console.log(response.message);
                service.reloadEvents();
            }
        );
    };

    service.loadEvents = function() {

        if ( service.events.length === 0 ) {

            if ( service.types.length > 0 ) {
                service.reloadEvents();
            }
            else {
                getTypes();
            }
        }
    };

    service.reloadEvents = function() {

        $http.get('/api/Event').then(

            function successCallback(response) {

                service.events = response.data;

                var date = new Date();
                var localOffset = date.getTimezoneOffset() * 60000;
                for ( var i = 0; i < service.events.length; ++i ) {

                    var event = service.events[i];

                    event.startDate = new Date(event.startDate.getTime() + localOffset);
                    event.endDate = new Date(event.endDate.getTime() + localOffset);
                }

                var eventId = $location.search().event;
                if (eventId) {

                    var eventList = $filter('filter')(service.events, {id: eventId});
                    if (eventList.length > 0) {
                        service.currentEvent = eventList[0];
                    }
                }

                if ( service.currentEvent === null ) {

                    for ( var j = 1 ; j < service.events.length ; ++j ) {

                        if ( service.events[j].startDate > date ) {

                            service.currentEvent = service.events[j-1];
                            break;
                        }
                    }
                }

                if ( service.currentEvent === null ) {

                    service.currentEvent = service.events[0];
                }

                service.selectedEvent();
                service.eventsLoaded = true;

            }, function errorCallback(response) {

                service.error = 'Error: Could not load events: ' + response.data.message;
                service.eventsLoaded = false;
            }
        );
    };

    service.selectedEvent = function () {

        var id = service.currentEvent.id;
        if (id && $location.search().event !== id) {

            $location.search("event", id);
        }

        service.currentYear = service.currentEvent.startDate.getFullYear();
        for(var i = 0 ; i < service.types.length ; ++i) {

            if (service.types[i].id === service.currentEvent.type.id) {
                service.currentType = service.types[i];
                break;
            }
        }

        service.activeEvents = [];
        for ( i = 0 ; i < service.events.length ; ++i ) {

            var event = service.events[i];
            if (event.startDate.getFullYear() === service.currentYear
                && event.type.id === service.currentType.id ) {

                service.activeEvents.push(event);
            }
        }

        populateCurrentLinks(true);
        populateEventToEdit();
    };

    service.selectedYearType = function() {

        service.currentEvent = null;

        for ( var i = 0 ; i < service.events.length ; ++i ) {

            var event = service.events[i];
            if (event.startDate.getFullYear() === service.currentYear
                && event.type.id === service.currentType.id ) {

                service.currentEvent = event;
                break;
            }
        }

        service.selectedEvent();
    };

    service.scrapeNewEvents = function() {

        var data = {};
        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        $http.get('/api/RawStandings/year/' + service.currentYear + '/type/' + service.currentType.type, data, config).then(
            function(){
                service.reloadEvents();
                window.alert('Scraped ' + service.currentType.description + ' web site for more events.');
            },
            function(response){
                window.alert('Error: Failed to load events! ' + response.data.message);
            }
        );
    };

    service.scrapeStandings = function() {

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

    service.upsertEvent = function() {

        var data = {

            name: service.currentEvent.name,
            eventCode: service.currentEvent.eventCode,
            coordinator: service.currentEvent.coordinator,
            startDate: $filter('date')(service.currentEvent.startDate, "yyyy-MM-dd"),
            endDate: $filter('date')(service.currentEvent.endDate, "yyyy-MM-dd"),
            eventFormat: service.currentEvent.eventFormat,
            playmatFileName: service.currentEvent.playmatFileName,
            cfbEventNum: service.currentEvent.cfbEventNum,

            organizer: {
                id: service.currentEvent.organizer.id
            },
            type: {
                id: service.currentEvent.type.id
            },
            art: {
                id: service.currentEvent.art.id
            }
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        if('id' in service.currentEvent && service.currentEvent.id > 0) {

            data.id = service.currentEvent.id;

            $http.put('/api/Event/' + data.id, data, config).then(
                function(){
                    service.reloadEvents();
                    window.alert('Event updated successfully.');
                },
                function(response){
                    service.reloadEvents();
                    window.alert('Error: Failed to edit Event! ' + response.data.message);
                }
            );
        }
        else {

            $http.post('/api/Event', data, config).then(
                function(){
                    service.reloadEvents();
                    window.alert('Event added successfully.');
                },
                function(response){
                    service.reloadEvents();
                    window.alert('Error: Failed to add Event! ' + response.data.message);
                }
            );
        }

    };

    return service;
}