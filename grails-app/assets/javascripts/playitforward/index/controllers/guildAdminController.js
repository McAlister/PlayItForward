//= wrapped

angular
    .module("playitforward.index")
    .controller("GuildAdminController", GuildAdminController);

function GuildAdminController(contextPath, userPersistenceService, $scope, $http, $location, $filter) {

    var vm = this;
    vm.contextPath = contextPath;

    $scope.sessionData = userPersistenceService.getCookieData();
    $scope.authenticated = $scope.sessionData.authenticated;
    $scope.accessToken = $scope.sessionData.accessToken;
    $scope.role = $scope.sessionData.role;
    $scope.username = $scope.sessionData.username;

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
            $scope.activeTab = 'guild-profile';
            $location.search('tab', 'guild-profile');
        }
    };

    $scope.activate($location.search().tab);


    // /////////// //
    // Profile Tab //
    // /////////// //

    $scope.guildError = '';
    $scope.guild = {};
    $scope.viewGuild = true;
    $scope.pristineGuild = {};

    $scope.loadGroup = function() {

        $http.get('/api/Guild/getMyGuild/' + $scope.username).then(
            function successCallback(response) {

                $scope.guild = response.data;

            }, function errorCallback(response) {

                $scope.guildError = response.data;
            }
        );
    };

    $scope.loadGroup();

    $scope.toggleView = function() {

        if ($scope.viewGuild) {

            $scope.pristineGuild = angular.copy($scope.guild);
            $scope.viewGuild = false;
        }
        else {

            $scope.viewGuild = true;
            $scope.guild = angular.copy($scope.pristineGuild);
        }
    };

    $scope.updateGuild = function() {

        var data = {

            id: $scope.guild.id,
            name: $scope.guild.name,
            description: $scope.guild.description,
            url: $scope.guild.url,
            twitter: $scope.guild.twitter,
            address: $scope.guild.address,
            email: $scope.guild.email,
            phone: $scope.guild.phone
        };

        var config = {
            headers : {
                'Content-Type': 'application/json;charset=utf-8;'
            }
        };

        $http.put('/api/Guild/' + data.id, data, config).then(

            function() {

                $scope.loadGroup();
                $scope.viewGuild = true;
            },
            function(response){

                window.alert('Error: Failed to add Player! ' + response.data.message);
            }
        );
    }

    // /////////// //
    // Members Tab //
    // /////////// //
    

    // //////////// //
    // Stangins Tab //
    // //////////// //


    // ///////// //
    // Store Tab //
    // ///////// //

}