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
            templateUrl: '/playitforward/index/index.html'
        })
        .state('contests', {
            url: '/contests',
            templateUrl: '/playitforward/index/contests.html'
        })
        .state('contribute', {
            url: '/contribute',
            templateUrl: '/playitforward/index/contribute.html'
            })
        .state('resources', {
            url: '/resources',
            templateUrl: '/playitforward/index/resources.html'
        })
        .state('contact', {
            url: '/contact',
            templateUrl: '/playitforward/index/press.html'
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
        .state('currentGP', {
            url: '/currentGP',
            controller: 'GPController',
            templateUrl: '/playitforward/index/gp.html',
            reloadOnSearch: false
        })
        .state('mailingList', {
            url: '/mailingList',
            templateUrl: '/playitforward/index/mailList.html'
        })
        .state('admin', {
            url: '/admin',
            controller: 'AdminController',
            templateUrl: '/playitforward/index/admin.html'
    });

    $urlRouterProvider.otherwise('/');
}
