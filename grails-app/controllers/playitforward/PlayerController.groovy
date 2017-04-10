package playitforward

import grails.transaction.Transactional

@Transactional(readOnly = true)
class PlayerController {

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [];

    static defaultNames = [ "Arlinn", "Avacyn", "Chandra", "Elspeth", "Kaya",
                            "Kemba", "Kiora", "Liliana", "Mirri", "Nahiri",
                            "Narset", "Nissa", "Olivia", "Oona", "Pia", "Saheeli",
                            "SliverQueen", "Tamiyo", "Vraska", "Wort"
    ];

    def index(Integer max) {

        List<Player> playerList = Player.list();
        for( Player player : playerList ) {

            player.name = player.alias;
        }

        respond playerList, model:[playerCount: Player.count()];
    }

    def trueList() {

        List<Player> playerList = Player.list();
        for(Player player : playerList) {

            int index = player.id % 20;

            if (player.alias == null) {

                player.alias = defaultNames[index];
            }

            if (player.imgUrl == null) {

                player.imgUrl = defaultNames[index] + ".png";
            }
        }

        respond playerList, model:[playerCount: Player.count()];
    }

    def show(Player player) {
        respond player;
    }
}
