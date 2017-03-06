package playitforward

@SuppressWarnings("GroovyUnusedDeclaration")
class BootStrap
{
    String swVersionString = "2.0.0";

    def init = { servletContext ->

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        new User(fullName: "Simone Aiken", email:"play_it_forward@outlook.com",
                hash:"\$2a\$06\$bXZgXYru5o1rDhQXcBpBLeh8yhMzEKUimbFhqEQl8jYZbF/P.ruRm").save();

        Sysconfig dbVersion = Sysconfig.findByKey("DB Version");
        if ( dbVersion == null) {

            new Sysconfig(key: 'DB Version', value: "1.0.0").save();
        }

        Sysconfig swVersion = Sysconfig.findByKey("DB Version");
        if ( swVersion == null ) {

            new Sysconfig(key: 'SW Version', value: swVersionString).save();
        }
        else {

            swVersion.setValue(swVersionString);
            swVersion.save();
        }
    }

    def destroy = {

    }
}
