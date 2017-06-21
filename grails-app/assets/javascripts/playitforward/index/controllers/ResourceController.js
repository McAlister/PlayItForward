//= wrapped

angular
    .module("playitforward.index")
    .controller("ResourceController", ResourceController);

function ResourceController( $scope, $location) {
    $scope.activeTab = $location.search().tab || 'podcasts';

    $scope.activate = function(tabName) {
        $scope.activeTab = tabName;
        $location.search("tab", tabName);
    };

    $scope.isActiveTab = function(tabName) {

        return (tabName === $scope.activeTab);
    };
}
