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

        get "/api/EventLinks/event/$eventId" (
                controller: 'eventLinks', action: 'getLinks'
        );

        get "/api/EventStanding/event/$eventId/round/$roundNum" (
                controller: 'eventStanding', action: 'index'
        );

        get "/api/RawStandings/year/$year/type/$type" (
                controller: 'rawStandings', action: 'scrapeEvents'
        );

        get "/api/RawStandings/getPlayers/event/$eventId" (
                controller: 'rawStandings', action: 'getPlayers'
        );

        get "/api/RawStandings/event/$eventId/type/$type" (
                controller: 'rawStandings', action: 'loadEvent'
        );

        post "/api/EventStanding/startRace/$eventId" (
                controller: 'eventStanding', action: 'startRace'
        )

        get "/api/EventStanding/event/$eventId" (
                controller: 'eventStanding', action: 'latestRound'
        );

        get "/api/EventStanding/loadEvent/$eventId/round/$round" (
                controller: 'eventStanding', action: 'loadEvent'
        )

        post "/api/User/$username/sendResetEmail" (
                controller: 'user', action: 'sendResetEmail'
        );

        post "/api/User/$username/setNewPassword" (
                controller: 'user', action: 'setNewPassword'
        );

        get "/api/Player/adminList" (
                controller: 'player', action: 'trueList'
        );

        put "/api/Image/generateCameo" (
                controller: 'image', action: 'generateCameo'
        )

        get "/api/Image/getImageBaseURL" (
                controller: 'image', action: 'getImageBaseURL'
        );

        post "/api/Claim/submit" (
                controller: 'claim', action: 'claimPrize'
        );

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
