//= wrapped

angular
    .module("playitforward.core")
    .factory("eventService", eventService);

function eventService($http, $location, $filter) {

    var service = {

        eventsLoaded: false,
        error: '',
        currentEvent: null,
        events: [],
        currentYear: null,
        years: [],
        baseUrl: null
    };

    var populateCurrentEvent = function(event) {

        var data = {

            id: event.id,
            name: event.name,
            eventCode: event.eventCode,
            coordinator: event.coordinator,
            startDate: event.startDate,
            endDate: event.endDate,
            eventFormat: event.eventFormat,
            playmatFileName: event.playmatFileName,
            matUrl: event.matUrl,

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

        service.currentEvent = data;
    };

    var getBaseUrl = function() {

        $http.get('/api/Image/getImageBaseURL').then(

            function successCallback(response) {

                service.baseUrl = response.data.url;
                service.reloadEvents();

            }, function errorCallback(response) {

                service.error += '\nFailed to get Art URL: ' + response.data;
            }
        );
    };

    service.loadEvents = function() {

        if ( service.events.length === 0 ) {

            if ( service.baseUrl ) {
                service.reloadEvents();
            }
            else {
                getBaseUrl();
            }
        }
    };

    service.reloadEvents = function() {

        $http.get('/api/Event').then(
            function successCallback(response) {

                service.events = response.data;

                var date = new Date();
                var localOffset = date.getTimezoneOffset() * 60000;
                var lastYear =  null;
                for ( var i = 0; i < service.events.length; ++i ) {

                    var event = service.events[i];

                    event.startDate = new Date(event.startDate.getTime() + localOffset);
                    event.endDate = new Date(event.endDate.getTime() + localOffset);

                    var year = event.startDate.getFullYear();
                    if ( year !== lastYear ) {
                        service.years.push( year );
                        lastYear = year;
                    }

                    if (event.playmatFileName && service.baseUrl) {

                        event.matUrl = service.baseUrl + "playmats/" + event.playmatFileName;
                    }
                    else {
                        event.matUrl = "assets/ComingSoon.jpg";
                    }
                }

                var eventId = $location.search().event;
                if (eventId) {

                    var eventList = $filter('filter')(service.events, {id: eventId});
                    if (eventList.length > 0) {
                        populateCurrentEvent(eventList[0]);
                    }
                }

                if ( service.currentEvent === null ) {

                    for ( var j = 1 ; j < service.events.length ; ++j ) {

                        if ( service.events[j].startDate > date ) {

                            populateCurrentEvent( service.events[j-1] );
                            break;
                        }
                    }
                }

                if ( service.currentEvent === null ) {

                    populateCurrentEvent( service.events[0] );
                }

                service.selectedEvent();
                service.eventsLoaded = true;

            }, function errorCallback(response) {

                service.error = 'Error: Could not load events: ' + response.data.message;
            }
        );
    };

    service.selectedEvent = function () {

        var id = service.currentEvent.id;
        if (id && $location.search().id !== id) {

            $location.search("event", id);
        }

        service.currentYear = service.currentEvent.startDate.getFullYear();
    };

    service.selectedYear = function() {

        for ( var i = 0 ; i < service.events.length ; ++i ) {

            var event = service.events[i];
            if (event.startDate.getFullYear() === service.currentYear) {

                populateCurrentEvent(event);
                service.selectedEvent();
                break;
            }
        }
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