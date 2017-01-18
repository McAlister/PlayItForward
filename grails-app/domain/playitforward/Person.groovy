package playitforward

class Person {

    String firstName;
    String lastName;
    String email;
    PersonType personType;

    static constraints = {
        email email: true, blank: false, unique: true;
    }

    static mapping = {
        firstName column: "first_name";
        lastName column: "last_name";
        personType column: "person_type";
    }

    static belongsTo = PersonType;
}
