//= wrapped

angular
    .module("playitforward.index")
    .factory("userPersistenceService", userPersistenceService);

function userPersistenceService($cookies) {

    var data = {};
    
    data.authenticated = false;
    data.accessToken = '';
    data.role = '';

    //noinspection JSUnusedGlobalSymbols
    return {
        
        setCookieData: function(token, roleName) {
            
            data.authenticated = roleName != 'ROLE_ANYONE';
            $cookies.put("authenticated", data.authenticated);

            data.accessToken = token;
            $cookies.put("accessToken", data.accessToken);

            data.role = roleName;
            $cookies.put("role", data.role);
            
        },
        
        getCookieData: function() {

            data.authenticated = $cookies.get("authenticated") == "true";
            data.accessToken = $cookies.get("accessToken");
            data.role = $cookies.get("role");
            
            return data;
        },
        
        clearCookieData: function() {

            data.authenticated = false;
            data.accessToken = '';
            data.role = '';

            $cookies.remove("authenticated");
            $cookies.remove("accessToken");
            $cookies.remove("role");
        }
    }
    
}