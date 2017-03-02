//= wrapped

angular
    .module("playitforward.index")
    .controller("GPController", GPController);

function GPController(contextPath, $scope, $http, $filter) {

    var vm = this;

    vm.contextPath = contextPath;

    $scope.error = '';
    $scope.GPs = {
        currentEvent: null,
        allEvents: [],
        eventWinners: {}
    };

    $http.get('/Event').then(
        
        function successCallback(response) {
      
            $scope.GPs.allEvents = response.data;

            var now = new Date();
            for (var i = 1 ; i < response.data.length ; ++i) {

                var event = response.data[i];
                if (event.startDate > now ) {

                    $scope.GPs.currentEvent = response.data[i-1];
                    break;
                }
            }
            
        }, function errorCallback(response) {
            
            $scope.error = response.data;
        }
    );

    $scope.getImageName = function() {

        if ($scope.GPs.currentEvent != null) {
            
            var name = $scope.GPs.currentEvent.name;
            name = name.replace(/\s/g, "-").substring(3);
            return "/assets/gps/" + name + ".jpg";
        }
        
        return '';
    };

   
    // /////////////// //
    // Active Tab Code //
    // /////////////// //

    $scope.activeTab = 'playmat';

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
    };


    // //////////// //
    // Overview Tab //
    // //////////// //

    $scope.isFutureGP = function() {
        
        if ($scope.GPs.currentEvent != null) {
            return $scope.GPs.eventWinners[$scope.GPs.currentEvent.id] == null;
        }
        
        return true;
    };
    
    $scope.monthList = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September',
                        'October', 'November', 'December'];
    $scope.gpStartDateString = function() {

        if ($scope.GPs.currentEvent != null) {
            var startDate = new Date($scope.GPs.currentEvent.startDate);
            return $scope.monthList[startDate.getMonth()] + ' ' + ( startDate.getDate() + 1) + ', ' + startDate.getFullYear();
        }
        
        return "";
    };

    $http.get('/EventWinner').then(

        function successCallback(response) {

            for (var i = 0 ;i < response.data.length ; ++i) {

                var winner = response.data[i];
                $scope.GPs.eventWinners[winner.event.id] = winner;
            }

        }, function errorCallback(response) {

            $scope.error = response.data;
        }
    );

    $scope.getWinnerImageName = function() {

        if ($scope.GPs.currentEvent != null) {
            
            var name = $scope.GPs.currentEvent.name;
            name = name.replace(/\s/g, "-").substring(3);
            return "/assets/winners/" + name + ".jpg";
        }
        
        return '';
    };

    // ////////// //
    // Bounty Tab //
    // ////////// //

    $scope.bountyArray = {
        bounties: [],
        errorMessage: '',
        sortType: 'event',
        sortReverse: false
    };

    $scope.populateBounties = function() {

        $http({
            method: 'GET',
            url: '/EventBounty'
        }).then(function successCallback(response) {
            $scope.bountyArray.bounties = response.data;
        }, function errorCallback(response) {
            $scope.bountyArray.errorMessage = 'ERROR: ' + response.data.message;
        });
    };

    $scope.populateBounties();

    $scope.bountiesExist = function() {

        if ($scope.GPs.currentEvent != null) {
            var currentBounties = $scope.bountyArray.bounties.filter(function (bounty) {
                return bounty.event == $scope.GPs.currentEvent.name;
            });

            return currentBounties.length > 0;
        }
        
        return false;
    };
}