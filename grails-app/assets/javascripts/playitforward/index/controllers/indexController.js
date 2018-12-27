//= wrapped

angular
    .module("playitforward.index")
    .controller("IndexController", IndexController);

function IndexController(userPersistenceService, contextPath, $scope, $http, $location) {
    
    var vm = this;
    vm.contextPath = contextPath;

    // /////////////////// //
    // User Authentication //
    // /////////////////// //

    $scope.isAuthenticated = function() {
        return userPersistenceService.isAuthenticated();
    };

    $scope.getRole = function() {
        return userPersistenceService.getCookieData().role;
    };

    // /////////////// //
    // Active Tab Code //
    // /////////////// //

    $scope.activeTab = $location.search().tab || 'art';

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
    // Art Tab Code //
    // //////////// //

    $scope.artData = {

        pathError: '',
        artistError: '',
        artError: '',
        artLoaded: false,
        artistLoaded: false,
        urlLoaded: false,
        baseUrl: '',
        artists: [],
        currentArtist: {
            artSet: []
        },
        currIndex: 0,
        artHash: {}
    };

    $scope.selectArtist = function() {

        for (var i = 0 ; i < $scope.artData.currentArtist.artSet.length ; ++i) {

            $scope.artData.currentArtist.artSet[i].active = (i === 0);
        }

        // Object.keys($scope.artArray.artistSlides).forEach(function (key) {
        //
        //     var slides = $scope.artArray.artistSlides[key];
        //     for ( var i = 0 ; i < slides.length ; ++i ) {
        //
        //         slides[i].active = i === 0;
        //     }
        // });

        $scope.artData.currIndex = 0;
    };

    $scope.getBaseUrl = function() {

        $http.get('/api/Image/getImageBaseURL').then(

            function successCallback(response) {

                $scope.artData.baseUrl = response.data.url;

                for (var i = 0 ; i < $scope.artData.artists.length ; ++i) {

                    var artist = $scope.artData.artists[i];
                    for (var j = 0 ; j < artist.artSet.length ; ++j) {
                        var art = $scope.artData.artHash[artist.artSet[j].id];
                        artist.artSet[j].title = art.title;
                        artist.artSet[j].url = $scope.artData.baseUrl + 'playmatArt/' + art.fileName;
                        artist.artSet[j].purchaseUrl = art.purchaseUrl;
                        artist.artSet[j].active = 0;
                        artist.artSet[j].index = j;
                    }
                }

                $scope.selectArtist();

                $scope.artData.urlLoaded = true;

            }, function errorCallback(response) {

                $scope.artData.pathError = 'Failed to base URL: ' + response.data;
            }
        );
    };

    $scope.populateArt = function() {

        $http.get('/api/Art').then(

            function successCallback(response) {

                for (var i = 0 ; i < response.data.length ; ++i) {

                    var art = response.data[i];
                    $scope.artData.artHash[art.id] = art;
                }

                $scope.artData.artLoaded = true;

                $scope.getBaseUrl();

            }, function errorCallback(response) {

                $scope.artData.artError = response.data;
            }
        );
    };

    $scope.populateArtist = function() {

        $http.get('/api/Artist').then(

            function successCallback(response) {

                $scope.artData.artists = response.data;
                $scope.artData.currentArtist = $scope.artData.artists[0];
                $scope.artData.artistLoaded = true;

                $scope.populateArt();

            }, function errorCallback(response) {

                $scope.artData.artistError = response.data;
            }
        );
    };

    $scope.populateArtist();


    /* ********* */
    /* Slideshow */
    /* ********* */

    $scope.getSlides = function() {

        return $scope.artData.currentArtist.artSet;
    };

    $scope.forwardImage = function() {

        var slides = $scope.getSlides();
        slides[$scope.artData.currIndex].active = false;
        $scope.artData.currIndex++;

        if ($scope.artData.currIndex >= slides.length) {
            $scope.artData.currIndex = 0;
        }

        slides[$scope.artData.currIndex].active = true;
    };

    $scope.backImage = function() {

        var slides = $scope.getSlides();
        slides[$scope.artData.currIndex].active = false;
        $scope.artData.currIndex--;

        if ($scope.artData.currIndex < 0) {
            $scope.artData.currIndex = slides.length - 1;
        }

        slides[$scope.artData.currIndex].active = true;
    };

    $scope.goToSlide = function(index) {

        var slides = $scope.getSlides();
        if (index >= slides.length) {
            index = 0;
        }

        slides[$scope.artData.currIndex].active = false;
        $scope.artData.currIndex = index;
        slides[$scope.artData.currIndex].active = true;
    };
}
