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
        firstName column: "first_name";
        lastName column: "last_name";
        personType column: "person_type";
        sendPushNotifications column: "send_push_notifications";
    }

    static belongsTo = PersonType;
}
