package playitforward

class Person {

    String firstName;
    String lastName;
    String email;
    PersonType personType;
    String phone;
    Boolean sendPushNotifications = false;

    static constraints = {

        email email: true, blank: false, unique: true;
        phone nullable: true;
        sendPushNotifications nullable: true;
    }

    static mapping = {

        id generator: 'native', params: [sequence: 'person_seq'];
        firstName column: "first_name", index: 'person_name_idx';
        lastName column: "last_name", index: 'person_name_idx';
        personType column: "person_type";
        sendPushNotifications column: "send_push_notifications";
        personType index: 'person_type_idx', fetch: 'join', lazy: false;
    }

    static belongsTo = PersonType;
}
