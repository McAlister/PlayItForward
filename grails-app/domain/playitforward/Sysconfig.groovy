package playitforward

class Sysconfig {

    String key;
    String value;

    static constraints = {

        key nullable: false, unique: true;
        value nullable: false;
    }

    static mapping = {
        table 'sysconfig';
        key index: 'sysconfig_idx';
        id generator: 'native', params: [sequence: 'sysconfig_seq']
    }
}
