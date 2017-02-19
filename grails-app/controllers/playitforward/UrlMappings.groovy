package playitforward

@SuppressWarnings("GroovyUnusedDeclaration")
class UrlMappings {

    static mappings = {

        post "/PushNotification/gp/$gpName/$roundNum" (
                controller: 'pushNotification', action: 'index'
        );

        post "/PushNotification/pasttimes/gp/$gpName/$roundNum" (
                controller: 'pushNotification', action: 'pushPastTimeGames'
        );

        post "/PushNotification/gamekeeper/gp/$gpName/$roundNum" (
                controller: 'pushNotification', action: 'pushGameKeeper'
        );

        get "/PushNotification/ping/$name" (
                controller: 'pushNotification', action: 'ping'
        );

        delete "/$controller/$id(.$format)?"(action:"delete");
        get "/$controller(.$format)?"(action:"index");
        get "/$controller/$id(.$format)?"(action:"show");
        post "/$controller(.$format)?"(action:"save");
        put "/$controller/$id(.$format)?"(action:"update");
        patch "/$controller/$id(.$format)?"(action:"patch");

        post "/login" (controller: 'user', action: 'login');
        "/"(view: '/index');
        "500"(view: '/error');
        "404"(view: '/notFound');
    }
}
