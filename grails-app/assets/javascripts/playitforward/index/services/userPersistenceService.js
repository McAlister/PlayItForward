//= wrapped

angular
    .module("playitforward.index")
    .factory("userPersistenceService", userPersistenceService);

function userPersistenceService($cookies) {

    var data = {};
    
    data.authenticated = false;
    data.accessToken = '';
    data.role = '';
    data.username = '';

    //noinspection JSUnusedGlobalSymbols
    return {
        
        setCookieData: function(token, roleName, username) {

            data.authenticated = true;
            $cookies.put("authenticated", data.authenticated);

            data.accessToken = token;
            $cookies.put("accessToken", data.accessToken);

            data.role = roleName;
            $cookies.put("role", data.role);

            data.username = username;
            $cookies.put("username", data.username);
        },
        
        getCookieData: function() {

            data.authenticated = $cookies.get("authenticated") === "true";
            data.accessToken = $cookies.get("accessToken");
            data.role = $cookies.get("role");
            data.username = $cookies.get("username");
            
            return data;
        },
        
        clearCookieData: function() {

            data.authenticated = false;
            data.accessToken = '';
            data.role = '';

            $cookies.remove("authenticated");
            $cookies.remove("accessToken");
            $cookies.remove("role");
            $cookies.remove("username");
        },

        isAuthenticated: function() {
            return $cookies.get("authenticated") === "true";
        }
    };
    
}