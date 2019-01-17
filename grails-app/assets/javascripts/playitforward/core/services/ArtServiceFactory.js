//= wrapped

angular
    .module("playitforward.core")
    .factory("artService", artService);

function artService($http) {

    var service = {

        unloaded: true,
        ready: false,
        error: '',
        baseUrl: null,
        currentArt: null,
        artList: [],
        artHash: {},
        currentArtist: {
            artSet: []
        },
        artistsList: [],
        artistHash: {},
        currIndex: 0,

        artistModel: {},
        artModel: {},
        previewUrl: 'assets/ComingSoon.jpg'
    };

    var getBaseUrl = function() {

        $http.get('/api/Image/getImageBaseURL').then(

            function successCallback(response) {

                service.baseUrl = response.data.url;
                service.reloadArt();

            }, function errorCallback(response) {

                service.error += '\nFailed to get Art URL: ' + response.data;
            }
        );
    };

    var getArtDetails = function() {

        $http.get('/api/Art').then(

            function successCallback(response) {

                service.artList = response.data;
                service.currentArt = service.artList[0];

                for ( var i = 0 ; i < service.artList.length ; ++i ) {

                    var art = service.artList[i];
                    service.artHash[art.id] = art;
                }

                for ( var j = 0 ; j < service.artistsList.length ; ++j ) {

                    var artist = service.artistsList[j];

                    if ( artist.hasOwnProperty('artSet') ) {

                        for ( var k = 0 ; k < artist.artSet.length ; ++k ) {

                            var target = artist.artSet[k];
                            var source = service.artHash[target.id];

                            target.title = source.title;
                            target.url = service.baseUrl + 'playmatArt/' + source.fileName;
                            target.purchaseUrl = source.purchaseUrl;
                            target.index = k;
                            target.active = 0;
                        }
                    }
                }

                service.selectArtist();
                service.ready = true;

            }, function errorCallback(response) {

                service.error += '\nFailed to get Art details' + response.data;
            }
        );
    };

    service.reloadArt = function () {

        $http.get('/api/Artist').then(

            function successCallback(response) {

                service.artistsList = response.data;
                service.currentArtist = service.artistsList[0];

                for ( var i = 0 ; i < service.artistsList.length ; ++i ) {

                    var artist = service.artistsList[i];
                    service.artistHash[artist.id] = artist;
                }

                getArtDetails();

            }, function errorCallback(response) {

                service.error += 'Failed to get Artists: ' + response.data;
            }
        );
    };

    service.loadArt = function() {

        if ( service.unloaded ) {

            service.unloaded = false;
            getBaseUrl();
        }
    };

    // ///////// //
    // Get By ID //
    // ///////// //

    service.getArt = function ( artId ) {

        return service.artHash[artId];
    };

    service.getArtist = function ( artId ) {

        var art = service.getArt( artId );
        return service.artistHash[art.artist.id];
    };

    // ////////////// //
    // Slideshow Code //
    // ////////////// //

    service.selectArtist = function() {

        for ( var i = 0 ; i < service.currentArtist.artSet.length ; ++i ) {

            service.currentArtist.artSet[i].active = (i === 0);
        }

        service.currIndex = 0;
        service.currentArt = service.artHash[service.currentArtist.artSet[0].id];
    };

    service.forwardImage = function() {

        var slides = service.currentArtist.artSet;
        slides[service.currIndex].active = false;
        service.currIndex++;

        if (service.currIndex >= slides.length) {
            service.currIndex = 0;
        }

        slides[service.currIndex].active = true;
        service.currentArt = service.artHash[slides[service.currIndex].id];
    };

    service.backImage = function() {

        var slides = service.currentArtist.artSet;
        slides[service.currIndex].active = false;
        service.currIndex--;

        if (service.currIndex < 0) {
            service.currIndex = slides.length - 1;
        }

        slides[service.currIndex].active = true;
        service.currentArt = service.artHash[slides[service.currIndex].id];
    };

    service.goToSlide = function( index ) {

        var slides = service.currentArtist.artSet;

        if ( index >= slides.length ) {
            index = 0;
        }

        slides[service.currIndex].active = false;
        service.currIndex = index;
        slides[service.currIndex].active = true;
        service.currentArt = service.artHash[slides[service.currIndex].id];
    };

    service.setPreview = function () {

        service.previewUrl = service.baseUrl + 'playmatArt/' + service.artModel.fileName;
    };

    // //////// //
    // Art CRUD //
    // //////// //


    service.clearArt = function() {

        service.artModel = {};
        service.previewUrl = 'assets/ComingSoon.jpg';
    };

    service.upsertArt = function() {

        var data = {

            title: service.artModel.title,
            fileName: service.artModel.fileName,
            purchaseUrl: service.artModel.purchaseUrl,
            artist: {
                id: service.currentArtist.id
            }
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        if( 'id' in service.artModel && service.artModel.id > 0 ) {

            data.id = service.artModel.id;

            $http.put('/api/Art/' + data.id, data, config).then(

                function(){
                    service.reloadArt();
                    window.alert('Artist updated successfully.');
                },
                function(response){
                    window.alert('Error: Failed to edit Art! ' + response.data.message);
                }
            );
        }
        else {

            $http.post('/api/Art', data, config).then(
                function(){
                    service.reloadArt();
                    window.alert('Art added successfully.');
                },
                function(response){
                    window.alert('Error: Failed to add Art! ' + response.data.message);
                }
            );
        }
    };

    service.clearArtist = function() {

        service.artistModel = {};
    };

    service.upsertArtist = function() {

        var data = {

            name: service.artistModel.name,
            blurb: service.artistModel.blurb,
            webSiteUrl: service.artistModel.webSiteUrl,
            deviantArtUrl: service.artistModel.deviantArtUrl,
            facebookUrl: service.artistModel.facebookUrl,
            patreonUrl: service.artistModel.patreonUrl
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        if('id' in service.artistModel && service.artistModel.id > 0) {

            data.id = service.artistModel.id;

            $http.put('/api/Artist/' + data.id, data, config).then(
                function(){
                    service.reloadArt();
                    window.alert('Artist updated successfully.');
                },
                function(response){
                    window.alert('Error: Failed to edit Artist! ' + response.data.message);
                }
            );
        }
        else {

            $http.post('/api/Artist', data, config).then(
                function(){
                    service.reloadArt();
                    window.alert('Artist added successfully.');
                },
                function(response){
                    window.alert('Error: Failed to add Artist! ' + response.data.message);
                }
            );
        }

    };

    return service;
}
