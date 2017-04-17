package playitforward

class GuildLeaderType
{

    String type;
    String description;

    static constraints = {

        type blank: false, unique: true;
        description blank:false;
    }

    static mapping = {

        table 'guild_leader_type';
        id generator: 'native', params: [sequence: 'guild_leader_type_seq'];
        type index: 'guild_leader_type_idx', unique: true;
    }
}
