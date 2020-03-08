//= wrapped

angular
    .module("playitforward.index")
    .controller("IndexController", IndexController);

function IndexController(userPersistenceService, contextPath, $scope, artService, tabService) {
    
    var vm = this;
    vm.contextPath = contextPath;

    $scope.tabService = tabService;
    tabService.registerTabList( "Resources", "community", ["community", "strategy", "events"] );
    tabService.registerTabList( "About", "inspire", ["achieve", "inspire", "program", "faq"] );

    $scope.artService = artService;
    artService.loadArt();

    // /////////////////// //
    // User Authentication //
    // /////////////////// //

    $scope.isAuthenticated = function() {
        return userPersistenceService.isAuthenticated();
    };

    $scope.getRole = function() {
        return userPersistenceService.getCookieData().role;
    };

}
