//= wrapped

angular
    .module("playitforward.index")
    .factory("httpInterceptor", myHttpInterceptor)
    .config(['$httpProvider', function($httpProvider) {
        $httpProvider.interceptors.push('httpInterceptor');
    }]);

function myHttpInterceptor(userPersistenceService, $q) {

    var sessionRecover = {
        
        responseError: function(response) {
            
            // Session has expired
            if (response.status == 401){

                userPersistenceService.clearCookieData();

                window.location = '/#/login';
            }
            
            return $q.reject(response);
        }
    };
    
    return sessionRecover;
}