//= wrapped

angular
    .module("playitforward.index")
    .controller("LoginController", LoginController);

function LoginController(userPersistenceService, $scope, $http) {

    // /////////////// //
    // Login Functions //
    // /////////////// //

    $scope.sessionData = userPersistenceService.getCookieData();

    $scope.authenticated = $scope.sessionData.authenticated;
    $scope.loginError = '';
    $scope.credentials = {};
    $scope.accessToken = $scope.sessionData.accessToken;
    $scope.role = $scope.sessionData.role;
    $scope.forgotPassword = false;

    var setInfoError = function(info, error) {
        $scope.infoBox = info || '';
        $scope.loginError = error || '';
    };

    $scope.login = function() {

        $http.post('/api/login', {
            username: $scope.credentials.username,
            password: $scope.credentials.password
        }).success(function(data) {

            if (data.access_token) {
                setInfoError('Successful login.', '');
                $scope.authenticated = true;
                $scope.accessToken = data.access_token;
                $scope.role = data.roles[0];
                userPersistenceService.setCookieData($scope.accessToken, $scope.role, $scope.credentials.username);
            }
            else {

                $scope.authenticated = false;
                setInfoError('', 'Bad User Name Or Password.');
                $scope.accessToken = '';
            }

        }).error(function() {

            $scope.authenticated = false;
            setInfoError('', 'Bad Username or Password.');
            $scope.accessToken = '';
        });
    };

    $scope.logOff = function() {

        userPersistenceService.clearCookieData();

        $scope.sessionData = userPersistenceService.getCookieData();
        $scope.authenticated = $scope.sessionData.authenticated;
        $scope.accessToken = $scope.sessionData.accessToken;
        $scope.role = $scope.sessionData.role;
    };

    $scope.sendResetPassword = function() {
        $http.post('/api/User/'+$scope.credentials.username+'/sendResetEmail', {}).then(
            function success () {
                setInfoError('Instructions sent to user', '');
                $scope.forgotPassword = false;
            },
            function error (response) {
                setInfoError('', response.data.message);
            });
    };

    $scope.isAuthenticated = function() {
        return userPersistenceService.isAuthenticated();
    };
}
