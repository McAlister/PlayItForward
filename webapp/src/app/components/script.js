// create the module and name it playItForward
var playItForward = angular.module('playItForward', ['ngRoute']);

// configure our routes
playItForward.config(function($routeProvider) {
  $routeProvider

  // route for the home page
    .when('/', {
      templateUrl : '/app/views/register.html',
      controller  : 'registerController'
    })

    .when('/register', {
      templateUrl : '/app/views/register.html',
      controller  : 'registerController'
    })

    // route for the about page
    .when('/mission', {
      templateUrl : '/app/views/mission.html',
      controller  : 'missionController'
    })

    // route for the contact page
    .when('/resources', {
      templateUrl : '/app/views/resources.html',
      controller  : 'resourcesController'
    })

    // route for the contact page
    .when('/contribute', {
      templateUrl : '/app/views/contribute.html',
      controller  : 'contributeController'
    });
});

// create the controller and inject Angular's $scope
playItForward.controller('registerController', function($scope) {
  $scope.message = 'Register Page Placeholder.';
});

playItForward.controller('missionController', function($scope) {
  $scope.message = 'Mission Page Placeholder.';
});

playItForward.controller('resourcesController', function($scope) {
  $scope.message = 'Resources Page Placeholder.';
});

playItForward.controller('contributeController', function($scope) {
  $scope.message = 'Contribute Page Placeholder';
});
