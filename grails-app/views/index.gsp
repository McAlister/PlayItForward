<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Play It Forward</title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <style type="text/css">
        [ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak, .x-ng-cloak {
            display: none !important;
        }
    </style>

    <asset:stylesheet src="application.css"/>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />

    <script type="text/javascript">
        window.contextPath = "${request.contextPath}";
    </script>
</head>

<body ng-app="playitforward" ng-controller="IndexController as indexCtrl">

    <div class="grails-logo-container">
        <image src="/assets/logo.png" class="grails-logo"/>
    </div>

    <div id="navigation">

        <ul>
            <li><a ui-sref="index">Home</a></li>
            <li><a ui-sref="contests">Bounties</a></li>
            <li><a ui-sref="contribute">Contribute</a></li>
            <li><a ui-sref="resources">Resources</a></li>
            <li><a ui-sref="login">Login</a></li>
        </ul>

    </div>

    <div ui-view></div>

    <div class="footer" role="contentinfo">
        <a href="mailto:play_it_forward@outlook.com">Contact Us</a>
    </div>

    <asset:javascript src="/playitforward/playitforward.js" />
</body>
</html>
