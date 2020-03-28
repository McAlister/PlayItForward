//= wrapped

angular
    .module("playitforward.index")
    .controller("GPController", GPController);

function GPController(contextPath, $scope, $http, tabService,
                      eventService, raceService) {

    var vm = this;
    vm.contextPath = contextPath;

    $scope.eventService = eventService;
    eventService.loadEvents();

    $scope.tabService = tabService;
    tabService.registerTabList( "GPs", "config", ["standings", "config", "watch"] );

    $scope.raceService = raceService;


    // ///////// //
    // Links tab //
    // ///////// //

    $scope.monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                         'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

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

    $scope.startRace = function(eventId) {

        var watchList = null;
        var eventData = eventService.eventData[eventId];
        if (eventData && eventData.watchList.length > 0) {
            watchList = eventData.watchList;
        }

        if (watchList == null) {
            raceService.loadRaceFromServer(eventId);
        } else {
            raceService.loadRaceFromWatchList(eventId, watchList);
        }
    };
}