package playitforward

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

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

    @Secured('ROLE_ADMIN')
    @Transactional
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

    @Secured('ROLE_ADMIN')
    @Transactional
    def save(Player player) {
        if (player == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (player.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond player.errors, view:'create'
            return
        }

        player.save flush:true

        respond player, [status: CREATED, view:"show"]
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def update(Player player) {
        if (player == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (player.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond player.errors, view:'edit'
            return
        }

        player.save flush:true

        respond player, [status: OK, view:"show"]
    }
}
