//= wrapped

angular
    .module("playitforward.index")
    .controller("AdminController", AdminController);

function AdminController(contextPath, $scope, $http, $filter) {

    var vm = this;
    vm.contextPath = contextPath;


    // /////////////// //
    // Active Tab Code //
    // /////////////// //

    $scope.activeTab = 'mail';

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
    
    
    
}