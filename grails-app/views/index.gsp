<!doctype html>
<html lang="en" class="no-js">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>Play It Forward</title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />

    <style type="text/css">
        [ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak, .x-ng-cloak {
            display: none !important;
        }
    </style>

    <link href="https://fonts.googleapis.com/css?family=Oswald" rel="stylesheet">
    <asset:stylesheet src="application.css"/>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico" />

    <script type="text/javascript">
        window.contextPath = "${request.contextPath}";
    </script>

</head>

<body ng-app="playitforward" ng-controller="IndexController as indexCtrl">

    <div class="body-wrapper">

        <div class="sidebar">

            <a ui-sref="index">
                <header class="header">
                    <asset:image src="/banner.png" class="main-logo-image"/>
                </header>
            </a>

            <div class="navigation">

                <ul class="navigation-list">
                    <li class="navigation-list-item">
                        <a ui-sref="currentGP" class="navigation-link">
                            GP Coverage
                        </a>
                    </li>
                    <li class="navigation-list-item">
                        <a ui-sref="supporters" class="navigation-link">
                            Artists
                        </a>
                    </li>
                    <li class="navigation-list-item">
                        <a ui-sref="resources" class="navigation-link">
                            Resources
                        </a>
                    </li>
                    <li class="navigation-list-item">
                        <a ui-sref="about" class="navigation-link">
                            About Us
                        </a>
                    </li>
                    <li class="navigation-list-item" ng-hide="isAuthenticated()">
                        <a ui-sref="login" class="navigation-link">
                            Login
                        </a>
                    </li>
                    <li class="navigation-list-item" ng-show="isAuthenticated()" ng-cloak>
                        <a ui-sref="logoff" class="navigation-link">
                            Logoff
                        </a>
                    </li>
                    <li class="navigation-list-item" ng-if="getRole() === 'ROLE_ADMIN'" ng-cloak>
                        <a ui-sref="admin" class="navigation-link">
                            Administration
                        </a>
                    </li>
                    <li class="navigation-list-item" ng-if="['ROLE_ADMIN', 'ROLE_EVENT_ORGANIZER'].indexOf(getRole()) >= 0" ng-cloak>
                        <a ui-sref="event" class="navigation-link">
                            Event Tools
                        </a>
                    </li>
                    <li class="navigation-list-item" ng-if="getRole() === 'ROLE_GROUP_LEADER'" ng-cloak>
                        <a ui-sref="guildAdmin" class="navigation-link">
                            Group Admin
                        </a>
                    </li>
                </ul>

                <div class="navIconsDiv">

                    <a href="mailto:play_it_forward@outlook.com">
                        <img src="/assets/socialmedia/mailMe.png" class="navLinkImage"/>
                    </a>

                </div>

            </div>

        </div>

        <div ui-view class="page"></div>

    </div>

    <asset:javascript src="/playitforward/playitforward.js" />

</body>
</html>
