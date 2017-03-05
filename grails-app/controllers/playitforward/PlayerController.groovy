package playitforward

import grails.transaction.Transactional

@Transactional(readOnly = true)
class PlayerController {

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [];

    def index(Integer max) {
        respond Player.list(), model:[playerCount: Player.count()];
    }

    def show(Player player) {
        respond player;
    }
}
