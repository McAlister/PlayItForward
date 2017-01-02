package playitforward

class User
{
    String fullName;
    String email;
    String hash;

    String toString()
    {
        return "$email";
    }

    static constraints = {
        email email: true, blank: false, unique: true;
        fullName blank: false;
        hash blank: false;
    }

    static mapping = {
        table 'app_user';
        fullName column: "full_name";
    }
}
