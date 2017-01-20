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

    <div id="body-wrapper">

        <header class="header">
            <div class="header-liliana"></div>
            <image src="/assets/banner.png" class="main-logo-image"/>
        </header>

        <div class="navigation">

            <ul class="navigation-list">
                <li class="navigation-list-item">
                    <a ui-sref="index" class="navigation-link">
                        Home
                    </a>
                </li>
                <li class="navigation-list-item">
                    <a ui-sref="contests" class="navigation-link">
                        Bounties
                    </a>
                </li>
                <li class="navigation-list-item">
                    <a ui-sref="contribute" class="navigation-link">
                        Contribute
                    </a>
                </li>
                <li class="navigation-list-item">
                    <a ui-sref="resources" class="navigation-link">
                        Resources
                    </a>
                </li>
                <li class="navigation-list-item">
                    <a ui-sref="press" class="navigation-link">
                        Press
                    </a>
                </li>
                <li class="navigation-list-item">
                    <a ui-sref="login" class="navigation-link">
                        Login
                    </a>
                </li>
                <li class="navigation-list-item">
                    <a href="mailto:play_it_forward@outlook.com" class="navigation-link">
                        Contact Us
                    </a>
                </li>
            </ul>

        </div>

        <div ui-view id="main-view"></div>

    </div>

    <asset:javascript src="/playitforward/playitforward.js" />

</body>
</html>
