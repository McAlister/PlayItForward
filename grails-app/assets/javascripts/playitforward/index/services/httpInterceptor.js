//= wrapped

angular // jshint ignore:line
    .module("playitforward.index")
    .factory("httpInterceptor", myHttpInterceptor)
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('httpInterceptor');
    }]);

function myHttpInterceptor(userPersistenceService, $q) {

    return {

        request: function (config) {

            // Add Auth token if logged in.
            var data = userPersistenceService.getCookieData();
            if (data.authenticated) {

                config.headers.Authorization = 'Bearer ' + data.accessToken;
            }

            return config || $q.when(config);
        },

        responseError: function (response) {

            // Session has expired
            if (response.status === 401) {

                userPersistenceService.clearCookieData();

                window.location = '/#/login';
            }

            return $q.reject(response);
        }
    };
}