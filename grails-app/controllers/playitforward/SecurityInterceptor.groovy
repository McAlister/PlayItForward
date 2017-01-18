package playitforward


class SecurityInterceptor
{
    SecurityInterceptor()
    {
        // TODO Match the secure pages/services when they exist.
        //matchAll().except(controller:'user', action:'login');
    }

    boolean before()
    {
        if (!session.user && actionName != "login")
        {
            redirect(uri: "/login");
            return false;
        }

        return true;
    }
}
