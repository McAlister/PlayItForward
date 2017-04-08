//= wrapped

angular
    .module("playitforward.index")
    .controller("SetPassController", SetPassController);

function SetPassController($scope, $http, $location) {

    /// password changing functions

    $scope.error = '';
    $scope.info = '';
    $scope.data = {};
    var params = $location.search();
    $scope.user = params.user;
    //noinspection JSUnresolvedVariable
    $scope.key = params.resetKey;

    $scope.submitPassword = function() {
        if ($scope.data.password !== $scope.data.check) {
            $scope.error = "Passwords do not match!";
            return;
        }
        $http.post('/api/User/'+$scope.user+'/setNewPassword', {
            password: $scope.data.password, key: $scope.key
        }).then(
            function success() {
                $scope.info = "Password updated, you may now log in.";
                $scope.error = '';
            },
            function error(response) {
                $scope.info = '';
                $scope.error = response.data.message;
            });
    };

}
