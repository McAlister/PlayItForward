package playitforward

@SuppressWarnings("GroovyUnusedDeclaration")
class UrlMappings {

    static mappings = {

        post "/api/PushNotification/gp/$gpName/$roundNum" (
                controller: 'pushNotification', action: 'index'
        );

        post "/api/PushNotification/pasttimes/gp/$gpName/$roundNum" (
                controller: 'pushNotification', action: 'pushPastTimeGames'
        );

        post "/api/PushNotification/gamekeeper/gp/$gpName/$roundNum" (
                controller: 'pushNotification', action: 'pushGameKeeper'
        );

        get "/api/PushNotification/ping/$name" (
                controller: 'pushNotification', action: 'ping'
        );

        get "/api/EventStanding/event/$eventId/round/$roundNum" (
                controller: 'eventStanding', action: 'index'
        );

        get "/api/EventStanding/event/$eventId" (
                controller: 'eventStanding', action: 'latestRound'
        );

        post "/api/User/$username/sendResetEmail" (
                controller: 'user', action: 'sendResetEmail'
        )
        post "/api/User/$username/setNewPassword" (
                controller: 'user', action: 'setNewPassword'
        )

        delete "/api/$controller/$id(.$format)?"(action:"delete");
        get "/api/$controller(.$format)?"(action:"index");
        get "/api/$controller/$id(.$format)?"(action:"show");
        post "/api/$controller(.$format)?"(action:"save");
        put "/api/$controller/$id(.$format)?"(action:"update");
        patch "/api/$controller/$id(.$format)?"(action:"patch");

        "/"(view: '/index');
        "500"(view: '/error');
        "404"(view: '/notFound');
    }
}
