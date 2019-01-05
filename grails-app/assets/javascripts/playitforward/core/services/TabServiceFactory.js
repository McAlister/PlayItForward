//= wrapped

angular
    .module("playitforward.core")
    .factory("tabService", tabService);

function tabService($location) {

    var service = {};
    var tabLists = {};

    service.registerTabList = function(name, defaultTab, tabs) {

        if ( ! tabLists.hasOwnProperty( name ) ) {

            var activeTab = $location.search().tab || defaultTab;
            if (tabs.indexOf(activeTab) === -1) {

                activeTab = defaultTab;
            }

            tabLists[name] = {
                default: defaultTab,
                active: activeTab,
                tabs: tabs
            };
        }
    };

    service.isActiveTab = function( name, tab ) {

        if ( tabLists.hasOwnProperty( name ) ) {

            return tabLists[name].active === tab;
        }

        return false;
    };

    service.tabClass = function( name, tab ) {

        if ( name in tabLists && tabLists[name].active === tab ) {

            return 'active';
        }

        return '';
    };

    service.activateTab = function( name, tab ) {

        if ( tabLists.hasOwnProperty( name ) ) {

            if( tabLists[name].tabs.indexOf( tab ) !== -1 ) {

                tabLists[name].active = tab;
                $location.search("tab", tab);
            }
        }
    };

    return service;
}

