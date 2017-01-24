//= wrapped
//= require /angular/angular
//= require /angular/ui-bootstrap-tpls
//= require /angular/angular-ui-router
//= require_self
//= require_tree services
//= require_tree controllers
//= require_tree directives
//= require_tree templates

angular.module("playitforward.index", [
    "playitforward.core",
    "ui.bootstrap.dropdown",
    "ui.bootstrap.collapse",
    "ui.router"
])
.config(config);

function config($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('index', {
            url: "/",
            templateUrl: "/playitforward/index/index.html"
        })
        .state('contests', {
            url: "/contests",
            templateUrl: "/playitforward/index/contests.html"
        })
        .state('contribute', {
            url: "/contribute",
            templateUrl: "/playitforward/index/contribute.html"
            })
        .state('resources', {
            url: "/resources",
            templateUrl: "/playitforward/index/resources.html"
        })
        .state('press', {
            url: "/press",
            templateUrl: "/playitforward/index/press.html"
        })
        .state('login', {
            url: "/login",
            templateUrl: "/playitforward/index/login.html"
        })
        .state('currentGP', {
            url: "/currentGP",
            templateUrl: "/playitforward/index/gp.html"
        })
        .state('mailingList', {
            url: "/mailingList",
            templateUrl: "/playitforward/index/mailList.html"
        });

    $urlRouterProvider.otherwise('/');
}
