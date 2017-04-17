package playitforward

class GuildLeader
{

    String name;
    GuildLeaderType type;
    Guild guild;
    User user;
    String imageName;

    static constraints = {

        name nullable: false;
        type nullable: false;
        guild nullable: false;
        user nullable: false;
        imageName nullable: true;
    }

    static mapping = {

        table 'guild_leader';
        id generator: 'native', params: [sequence: 'guild_leader_seq'];
        imageName column: 'image_name';
        type index: 'guild_leader_by_type_idx', fetch: 'join', lazy: false;
        guild index: 'guild_leader_group_idx', fetch: 'join', lazy: false;
        user index: 'guild_leader_user_idx', fetch: 'join', lazy: false;
    }
}
