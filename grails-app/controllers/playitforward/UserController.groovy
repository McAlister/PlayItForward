package playitforward


import grails.converters.*
import org.mindrot.jbcrypt.BCrypt

class UserController
{
    static responseFormats = ['json', 'xml'];

    def bcrypt = new BCrypt();

    def login()
    {
        if (request.get)
        {
            redirect(uri: "login");
        }

        def u = User.findByEmail(request.JSON.email);
        def p = request.JSON.password;
        if (u && p)
        {
            if (bcrypt.checkpw(p, u.hash))
            {
                session.user = u;
                redirect(uri: "success");
                def answer = [status: 'ok'];
                render answer as JSON;
            }
            else
            {
                redirect(uri: "login?err=badpass");
            }
        }
        else
        {
            redirect(uri: "login?err=badpass");
        }
    }
}
