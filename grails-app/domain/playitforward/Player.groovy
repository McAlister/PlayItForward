package playitforward

class Player {

    String name;
    boolean isWoman;
    String alias;
    String imgUrl;

    static constraints = {

        name blank: false, unique: true;
        isWoman nullable: false;
        alias nullable: true;
        imgUrl nullable: true;
    }

    static mapping = {

        table 'player';
        name index: 'player_idx';
    }
}
