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

    $scope.artArray = {

        artError: '',
        artistError: '',
        artLoaded: false,
        artistLoaded: false,
        artists: [],
        currentArtist: null,
        currIndex: 0,
        artistSlides: {}  // artistID -> [eventArt]
    };

    $scope.getCurrentArtPath = function(title) {

        var baseURL = 'https://s3-us-west-2.amazonaws.com/playitforward-magic/images/playmatArt/';
        var name = title.replace(/\s/g, '');
        return baseURL + name + '.jpg';
    };

    $scope.populateArt = function() {

        $http.get('/api/EventArt').then(

            function successCallback(response) {

                for (var i = 0 ; i < response.data.length ; ++i) {

                    var art = response.data[i];
                    art.image = $scope.getCurrentArtPath(art.title);
                    if ( !(art.artistId in $scope.artArray.artistSlides) ) {
                        art.active = true;
                        $scope.artArray.artistSlides[art.artistId] = [art];
                        art.id = 0;
                    }
                    else {

                        art.active = false;
                        var stuff = $scope.artArray.artistSlides[art.artistId];
                        var exists = false;
                        for (var j = 0; j < stuff.length; ++j) {

                            if (art.title === stuff[j].title) {
                                exists = true;
                                break;
                            }
                        }

                        if ( ! exists ) {
                            art.id = stuff.length;
                            stuff.push(art);
                        }
                    }
                }

                $scope.artArray.artLoaded = true;

            }, function errorCallback(response) {

                $scope.artArray.artError = response.data;
            }
        );

        $http.get('/api/Artist').then(


            function successCallback(response) {

                $scope.artArray.artists = response.data;
                $scope.artArray.currentArtist = $scope.artArray.artists[0];
                $scope.artArray.artistLoaded = true;

            }, function errorCallback(response) {

                $scope.artArray.artError = response.data;
            }
        );
    };

    $scope.populateArt();

    $scope.selectArtist = function() {

        Object.keys($scope.artArray.artistSlides).forEach(function (key) {

            var slides = $scope.artArray.artistSlides[key];
            for ( var i = 0 ; i < slides.length ; ++i ) {

                slides[i].active = i === 0;
            }

        });

        $scope.artArray.currIndex = 0;
    };

    /* Slideshow */

    $scope.getSlides = function() {

        return $scope.artArray.artistSlides[$scope.artArray.currentArtist.id];
    };

    $scope.forwardImage = function() {

        var slides = $scope.getSlides();
        slides[$scope.artArray.currIndex].active = false;
        $scope.artArray.currIndex++;

        if ($scope.artArray.currIndex >= slides.length) {
            $scope.artArray.currIndex = 0;
        }

        slides[$scope.artArray.currIndex].active = true;
    };

    $scope.backImage = function() {

        var slides = $scope.getSlides();
        slides[$scope.artArray.currIndex].active = false;
        $scope.artArray.currIndex--;

        if ($scope.artArray.currIndex < 0) {
            $scope.artArray.currIndex = slides.length - 1;
        }

        slides[$scope.artArray.currIndex].active = true;
    };

    $scope.goToSlide = function(index) {

        var slides = $scope.getSlides();
        if (index >= slides.length) {
            index = 0;
        }

        slides[$scope.artArray.currIndex].active = false;
        $scope.artArray.currIndex = index;
        slides[$scope.artArray.currIndex].active = true;
    };

}
