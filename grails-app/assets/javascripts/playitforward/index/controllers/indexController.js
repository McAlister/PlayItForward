//= wrapped

angular
    .module("playitforward.index")
    .controller("IndexController", IndexController);

function IndexController(applicationDataFactory, contextPath, $state, $scope, $http) {
    var vm = this;

    vm.contextPath = contextPath;

    applicationDataFactory.get().then(function(response) {
        vm.applicationData = response.data;
    });

    vm.stateExists = function(name) {
        return $state.get(name) != null;
    };

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
}
