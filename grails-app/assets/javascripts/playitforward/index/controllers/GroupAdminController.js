//= wrapped

angular
    .module("playitforward.index")
    .controller("GroupAdminController", GroupAdminController);

function GroupAdminController(contextPath, userPersistenceService, $scope, $http, $location, $filter) {

    var vm = this;
    vm.contextPath = contextPath;

    $scope.sessionData = userPersistenceService.getCookieData();
    $scope.authenticated = $scope.sessionData.authenticated;
    $scope.accessToken = $scope.sessionData.accessToken;
    $scope.role = $scope.sessionData.role;
    
    // /////////////// //
    // Active Tab Code //
    // /////////////// //

    $scope.isActiveTab = function (tabName) {

        return (tabName === $scope.activeTab);
    };

    $scope.tabClass = function (tabName) {

        if (tabName === $scope.activeTab) {
            return 'active';
        }

        return '';
    };

    $scope.activate = function (tabName) {
        
        if (tabName) {

            $scope.activeTab = tabName;
            $location.search('tab', tabName);
        }
        else {
            $scope.activeTab = 'group-profile';
            $location.search('tab', 'group-profile');
        }
    };

    $scope.activate($location.search().tab);


    // ///////////// //
    // Mail List Tab //
    // ///////////// //


    // ///////// //
    // Prize Tab //
    // ///////// //
    

    // //////////////// //
    // Event Winner Tab //
    // //////////////// //


    // ////////// //
    // Player Tab //
    // ////////// //

}