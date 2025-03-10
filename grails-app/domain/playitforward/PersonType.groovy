package playitforward

class PersonType
{
    String type;
    String description;

    static constraints = {

        type blank: false, unique: true;
        description blank: false;
    }

    static mapping = {

        table 'person_type';
        id generator: 'native', params: [sequence: 'person_type_seq'];
    }
}
