package playitforward

class Guild
{

    // Public

    String name;
    String description;
    String url;
    String twitter;

    // Members Only
    String address;
    String email;
    String phone;

    static constraints = {

        name nullable: false;
        description nullable: true;
        address nullable: true;
        phone nullable: true, phone: true;
        email nullable: false, email: true;
        url nullable: true;
        twitter nullable: true;
    }

    static mapping = {

        table 'guild';
        id generator: 'native', params: [sequence: 'guild_seq'];
        description type: 'text';
        name index: 'guild_name_idx';
    }
}
