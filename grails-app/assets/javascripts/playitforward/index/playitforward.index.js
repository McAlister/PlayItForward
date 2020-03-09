//= wrapped
//= require /angular/angular
//= require /angular/angular-cookies
//= require /angular/ui-bootstrap-tpls
//= require /angular/angular-ui-router
//= require /angular/angular-animate
//= require_self
//= require_tree services
//= require_tree controllers
//= require_tree directives
//= require_tree templates

angular.module('playitforward.index', [
    'playitforward.core',
    'ui.bootstrap.dropdown',
    'ui.bootstrap.collapse',
    'ui.router',
    'ngCookies'
])
.config(config);

function config($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('index', {
            url: '/',
            controller: 'GPController',
            templateUrl: '/playitforward/index/gp.html',
            reloadOnSearch: false
        })
        .state('claim', {
            url: '/claim',
            templateUrl: '/playitforward/index/claim.html'
        })
        .state('about', {
            url: '/about',
            templateUrl: '/playitforward/index/index.html'
        })
        .state('supporters', {
            url: '/supporters',
            templateUrl: '/playitforward/index/supporters.html'
        })
        .state('resources', {
            url: '/resources',
            templateUrl: '/playitforward/index/resources.html'
        })
        .state('login', {
            url: '/login',
            controller: 'LoginController',
            templateUrl: '/playitforward/index/login.html'
        })
        .state('logoff', {
            url: '/logoff',
            controller: 'LoginController',
            templateUrl: '/playitforward/index/login.html'
        })
        .state('setPass', {
            url: '/setPass',
            controller: 'SetPassController',
            templateUrl: '/playitforward/index/setPass.html'
        })
        .state('currentGP', {
            url: '/currentGP',
            controller: 'GPController',
            templateUrl: '/playitforward/index/gp.html',
            reloadOnSearch: false
        })
        .state('admin', {
            url: '/admin',
            controller: 'AdminController',
            templateUrl: '/playitforward/index/admin.html'
        })
        .state('event', {
            url: '/event',
            controller: 'EventController',
            templateUrl: '/playitforward/index/event.html'
        })
        .state('guildAdmin', {
            url: '/guildAdmin',
            controller: 'GuildAdminController',
            templateUrl: '/playitforward/index/guildAdmin.html'
    });

    $urlRouterProvider.otherwise('/');
}
