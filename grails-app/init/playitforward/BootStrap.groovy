package playitforward

@SuppressWarnings("GroovyUnusedDeclaration")
class BootStrap
{
    def init = { servletContext ->

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        new User(fullName: "Simone Aiken", email:"play_it_forward@outlook.com",
                hash:"\$2a\$06\$bXZgXYru5o1rDhQXcBpBLeh8yhMzEKUimbFhqEQl8jYZbF/P.ruRm").save();

        new Sysconfig(key: 'DB Version', value: "1.0.0").save();
        new Sysconfig(key: 'SW Version', value: "2.0.0").save();
    }

    def destroy = {

    }
}
