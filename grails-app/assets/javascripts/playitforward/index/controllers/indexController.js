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
    tabService.registerTabList( "Claim", "input", ["input", "sent"]);
    
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

    // //////////////// //
    // Prize Claim Form //
    // //////////////// //

    $scope.claimEmail = {

        name: null,
        email: null,
        contest: null,
        text: null,
        error: '' 
    };

    $scope.claimPrize = function() {

        tabService.activateTab('Claim', 'sent')
    };

    $scope.newClaim = function() {

        $scope.claimEmail.name = null;
        $scope.claimEmail.email = null;
        $scope.claimEmail.contrest = null;
        $scope.claimEmail.text = null;
        $scope.claimEmail.error = '';
        tabService.activateTab('Claim', 'input');
    };
}
