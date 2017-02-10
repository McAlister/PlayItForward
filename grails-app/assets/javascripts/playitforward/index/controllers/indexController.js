//= wrapped

angular
    .module("playitforward.index")
    .controller("IndexController", IndexController);

function IndexController(applicationDataFactory, contextPath, $state, $scope, $http, $interval, $sce) {
    var vm = this;

    vm.contextPath = contextPath;

    applicationDataFactory.get().then(function(response) {
        vm.applicationData = response.data;
    });

    vm.stateExists = function(name) {
        return $state.get(name) != null;
    };


    /*
        Email List Stuff
     */

    $scope.mailData = {
        fName: '',
        lName: '',
        email: '',
        types: [],
        chosenType: null
    };

    $http.get('/PersonType').then(

        function successCallback(response) {
            $scope.mailData.types = response.data;
        }, function errorCallback(response) {
            alert ('Error: Could not contact database.');
    });

    $scope.signUpForMailList = function() {

        var data = {
            firstName: $scope.mailData.fName,
            lastName: $scope.mailData.lName,
            email: $scope.mailData.email,
            personType: $scope.mailData.chosenType
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        $http.post('/Person', data, config).then(
            function(response){
                alert ('You were added successfully.');
            },
            function(response){
                alert ('Error: Failed to add you to mailling list! ' + response.data.message);
            }
        );

    };

    /*
        Bounty Carousel Stuff
     */

    $scope.myInterval = 5000;
    $scope.noWrapSlides = false;
    $scope.active = 0;
    var slides = $scope.slides = [];
    var currIndex = 0;

    // I should really clean this up before letting other people see it.  To late!

    slides.push({
        image: '/assets/playmats/CatRabbit.jpg',
        text: 'Max Collins',
        id: currIndex++,
        active: false,
        promote: '<b>Max Collins</b> is an artist and animator at Cartoon Network and Studio Yotta. You can check ' +
        'out his work on his <a href="http://maxjcollins.tumblr.com/">tumblr</a>.  Max\'s art will be used for mats ' +
        'in January and July.'
    });

    slides.push({
        image: '/assets/playmats/Witch.jpg',
        text: 'Malwina Kwiatkowska',
        id: currIndex++,
        active: true,
        promote: '<b>Malwina Kwiatkowska</b> is a freelance 3D Concept Artist from Poland.  Check her out ' +
                 'on <a href="https://www.artstation.com/artist/merkerinn">Art Station</a> or subscribe ' +
                 'to her <a href="https://www.patreon.com/merkerinn/posts">patreon</a> to support her work. ' +
                 'Witch will be the playmat for February and October.'
    });

    slides.push({
        image: '/assets/playmats/Snow.jpg',
        text: 'R.K. Post',
        id: currIndex++,
        active: false,
        promote: "<b>R.K. Post</b> is a long time Magic artist and regular on the GP circuit.  You can probably" +
                 "find him on Artists Row if you want to get your mat signed.  You can purchase his mats " +
                 "<a href='http://www.rkpost.net/_p/prd1/690387591/product/pul117-snowblind'>here.</a> " +
                 "Snowblind will be the mat for March and September (Except for San Antonio which will " +
                 "get a seasonal mat.)"
    });

    slides.push({
        image: '/assets/playmats/StoneWoman.jpg',
        text: 'Malwina Kwiatkowska',
        id: currIndex++,
        active: false,
        promote: '<b>Malwina Kwiatkowska</b> is a freelance 3D Concept Artist from Poland.  Check her out ' +
        'on <a href="https://www.artstation.com/artist/merkerinn">Art Station</a> or subscribe ' +
        'to her <a href="https://www.patreon.com/merkerinn/posts">patreon</a> to support her work. ' +
        'This art will be used for GPs in May'
    });

    slides.push({
        image: '/assets/playmats/Alice.jpg',
        text: 'Malwina Kwiatkowska',
        id: currIndex++,
        active: false,
        promote: '<b>Malwina Kwiatkowska</b> is a freelance 3D Concept Artist from Poland.  Check her out ' +
        'on <a href="https://www.artstation.com/artist/merkerinn">Art Station</a> or subscribe ' +
        'to her <a href="https://www.patreon.com/merkerinn/posts">patreon</a> to support her work. ' +
        'Alice will be bashing heads in Vegas and Omaha for the month of June.'
    });

    slides.push({
        image: '/assets/playmats/Angel.jpg',
        text: 'Malwina Kwiatkowska',
        id: currIndex++,
        active: false,
        promote: '<b>Malwina Kwiatkowska</b> is a freelance 3D Concept Artist from Poland.  Check her out ' +
        'on <a href="https://www.artstation.com/artist/merkerinn">Art Station</a> or subscribe ' +
        'to her <a href="https://www.patreon.com/merkerinn/posts">patreon</a> to support her work. ' +
        'Angel will be flying for GPs in the month of August.'
    });

    slides.push({
        image: '/assets/playmats/Bunny.jpg',
        text: 'R.K. Post',
        id: currIndex++,
        active: false,
        promote: "<b>R.K. Post</b> is a long time Magic artist and regular on the GP circuit.  You can probably" +
        "find him on Artists Row if you want to get your mat signed.  You can purchase his mats " +
        "<a href='http://www.rkpost.net/_p/prd1/690392571/product/pul116-the-easter-bunny'>here.</a> " +
        "Easter Bunny is our seasonal mat for GP San Antonio."
    });

    slides.push({
        image: '/assets/playmats/Aisling.jpg',
        text: 'The Secret Of Kells',
        id: currIndex++,
        active: false,
        promote:  'An iconic image of Aisling from <a href="http://www.gkids.com/films/the-secret-of-kells/">The Secret Of Kells</a>' +
                  ' used with permission of <a href="http://www.gkids.com">GKIDS</a>. Pangur Ban will ' +
                  ' <a target="_blank" href="https://www.youtube.com/watch?v=tTiSak8r9P8">go where I cannot</a> in November.'
    });

    slides.push({
        image: '/assets/playmats/Elf.jpg',
        text: 'Malwina Kwiatkowska',
        id: currIndex++,
        active: false,
        promote: '<b>Malwina Kwiatkowska</b> is a freelance 3D Concept Artist from Poland.  Check her out ' +
        'on <a href="https://www.artstation.com/artist/merkerinn">Art Station</a> or subscribe ' +
        'to her <a href="https://www.patreon.com/merkerinn/posts">patreon</a> to support her work. ' +
        'Elf is our mat for December.'
    });

    currIndex = 1;
    $scope.manual = false;

    $scope.autoIncrement = function() {

        if ($scope.manual)
        {
            return;
        }
        
        $scope.forwardImage();
    };

    
    $scope.forwardImage = function() {
        
        slides[currIndex].active = false;
        currIndex++;
        if (currIndex > 8)
        {
            currIndex = 0;
        }
        slides[currIndex].active = true;
    };

    $scope.backImage = function() {
        
        $scope.manual = true;
        slides[currIndex].active = false;
        currIndex--;
        if (currIndex < 0)
        {
            currIndex = 8;
        }
        slides[currIndex].active = true;
    };
    
    $scope.goToSlide = function(index) {

        $scope.manual = true;
        slides[currIndex].active = false;
        currIndex = index;
        slides[currIndex].active = true;
    };

    $scope.getPromotionString = function() {

        return $sce.trustAsHtml(slides[currIndex].promote);
    };

    $interval($scope.autoIncrement, 3000);
}
