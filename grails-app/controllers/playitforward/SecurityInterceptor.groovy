package playitforward


class SecurityInterceptor
{
    SecurityInterceptor()
    {
        matchAll().except(controller:'user', action:'login');
    }

    boolean before()
    {
        if (!session.user && actionName != "login")
        {
            redirect(uri: "login");
            return false;
        }

        return true;
    }
}
