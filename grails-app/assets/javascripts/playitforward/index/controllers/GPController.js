//= wrapped

angular
    .module("playitforward.index")
    .controller("GPController", GPController);

function GPController(contextPath, $scope, $http, $location) {

    var vm = this;
    vm.contextPath = contextPath;

    $scope.error = '';
    $scope.artError = '';
    $scope.artLoaded = false;
    $scope.GPs = {
        lastRound: 0,
        currentEvent: null,
        allEvents: [],
        eventWinners: {},
        allYears: [2017, 2018],
        currentYear: 2018,
        eventArt: {}
    };

    $scope.loadEvent = function() {
        $http.get('/api/Event').then(

            function successCallback(response) {

                $scope.GPs.allEvents = response.data;

                var now = new Date();
                var selected = Number($location.search().event);
                if (selected) {
                    $scope.GPs.currentEvent = response.data.find(function(el) { return el.id === selected });
                } else {
                    for (var i = 1; i < response.data.length; ++i) {

                        var event = response.data[i];
                        if (event.startDate > now) {

                            $scope.GPs.currentEvent = response.data[i - 1];
                            break;
                        }
                    }

                    if ($scope.GPs.currentEvent === null) {
                        $scope.GPs.currentEvent = response.data[response.data.length - 1];
                    }
                }

                $scope.GPs.currentYear = $scope.GPs.currentEvent.startDate.getFullYear();

            }, function errorCallback(response) {

                $scope.error = response.data;
            }
        );
    };
    $scope.loadEvent();

    $scope.$on('$locationChangeSuccess', function() {
        // if event or tab changed, update
        var searchId = Number($location.search().event);
        var loadedEvent = $scope.GPs.currentEvent;
        var searchTab = $location.search().tab;
        if (searchId && loadedEvent && searchId !== loadedEvent.id) {
            $scope.loadEvent();
        }
        if (searchTab && $scope.activeTab !== searchTab) {
            $scope.activate(searchTab);
        }
    });

    $scope.getImageName = function() {

        if ($scope.GPs.currentEvent) {
            
            var name = $scope.GPs.currentEvent.name;
            name = name.replace(/\s/g, "-").substring(3);
            var baseString = "https://s3-us-west-2.amazonaws.com/playitforward-magic/images/playmats/";
            var year = $scope.GPs.currentEvent.startDate.getFullYear() + "/";
            return baseString + year + name + ".jpg";
        }
        
        return '';
    };

   
    // /////////////// //
    // Active Tab Code //
    // /////////////// //

    $scope.activeTab = $location.search().tab || 'playmat';

    $scope.isActiveTab = function(tabName) {

        return (tabName === $scope.activeTab);
    };
    
    $scope.tabClass = function(tabName) {
      
        if (tabName === $scope.activeTab) {
            return 'active';
        }
        
        return '';
    };
    
    $scope.activate = function(tabName) {
        $scope.activeTab = tabName;
        $location.search("tab", tabName);
    };


    // //////////// //
    // Overview Tab //
    // //////////// //

    $scope.isFutureGP = function() {

        var now = new Date();
        //noinspection JSUnresolvedVariable
        return !$scope.GPs.currentEvent || $scope.GPs.currentEvent.startDate > now;
    };
    
    $scope.monthList = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September',
                        'October', 'November', 'December'];
    $scope.gpStartDateString = function() {

        if ($scope.GPs.currentEvent) {
            var startDate = new Date($scope.GPs.currentEvent.startDate);
            return $scope.monthList[startDate.getMonth()] + ' ' + ( startDate.getDate() + 1) + ', ' + startDate.getFullYear();
        }
        
        return "";
    };

    $http.get('/api/EventWinner').then(

        function successCallback(response) {

            for (var i = 0 ;i < response.data.length ; ++i) {

                var winner = response.data[i];
                $scope.GPs.eventWinners[winner.event.id] = winner;
            }

        }, function errorCallback(response) {

            $scope.error = response.data;
        }
    );

    $http.get('/api/EventArt').then(

        function successCallback(response) {

            for (var i = 0 ; i < response.data.length ; ++i) {

                var art = response.data[i];
                $scope.GPs.eventArt[art.eventId] = art;
            }

            $scope.artLoaded = true;

        }, function errorCallback(response) {

            $scope.artError = response.data;
        }
    );

    $scope.getActiveArt = function() {

        return $scope.GPs.eventArt[$scope.GPs.currentEvent.id];
    };

    $scope.getWinnerImageName = function() {

        if ($scope.GPs.currentEvent) {

            var winner = $scope.GPs.eventWinners[$scope.GPs.currentEvent.id];
            if (winner != null && winner.imageName != null) {

                return "https://s3-us-west-2.amazonaws.com/playitforward-magic/images/winners/" + winner.imageName;
            }
        }

        return "/assets/winners/Unknown.jpg";
    };

    // ////////// //
    // Bounty Tab //
    // ////////// //

    $scope.bountyArray = {
        bounties: [],
        bountiesByYear: {},
        errorMessage: '',
        sortType: 'event',
        sortReverse: false
    };

    $scope.populateBounties = function() {

        $http.get('/api/EventBounty').then(

            function successCallback(response) {

                for(var i = 0 ; i < response.data.length ; ++i) {

                    var prize = response.data[i];
                    var year = prize.startDate.getFullYear();

                    if (! (year in $scope.bountyArray.bountiesByYear)) {
                        $scope.bountyArray.bountiesByYear[year] = [];
                    }

                    $scope.bountyArray.bountiesByYear[year].push(prize);
                }

                $scope.bountyArray.bounties = $scope.bountyArray.bountiesByYear[$scope.GPs.currentYear];

            }, function errorCallback(response) {

                $scope.bountyArray.errorMessage = 'ERROR: ' + response.data.message;
            }
        );
    };

    $scope.populateBounties();

    $scope.bountiesExist = function() {

        if ($scope.GPs.currentEvent) {
            var currentBounties = $scope.bountyArray.bounties.filter(function (bounty) {
                return bounty.eventId === $scope.GPs.currentEvent.id;
            });

            return currentBounties.length > 0;
        }
        
        return false;
    };

    $scope.getHorseraceSrc = function() {

        if ($scope.GPs.currentEvent) {

            return '/assets/horserace/index.html?race=' + $scope.GPs.currentEvent.id;
        }
        else {
            
            return '/assets/horserace/notfound.html';
        }
    };

    $scope.selectYear = function() {

        for (var i = 0 ; i < $scope.GPs.allEvents.length ; ++i) {

            var event = $scope.GPs.allEvents[i];
            if (event.startDate.getFullYear() === $scope.GPs.currentYear) {

                $scope.GPs.currentEvent = event;
                $scope.setEventId();
                break;
            }
        }

        $scope.bountyArray.bounties = $scope.bountyArray.bountiesByYear[$scope.GPs.currentYear];
    };

    $scope.selectEvent = function() {

        if ($scope.GPs.currentEvent) {

            $scope.GPs.currentYear = $scope.GPs.currentEvent.startDate.getFullYear();
            $scope.setEventId();
        }
    };

    $scope.setEventId = function() {

        var id = $scope.GPs.currentEvent.id;
        if (id && $location.search().id !== id) {
            $location.search("event", id);
        }
    };
}