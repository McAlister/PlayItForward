package playitforward

@SuppressWarnings("GroovyUnusedDeclaration")
class BootStrap
{
    def springSecurityService;
    String swVersionString = "3.0.0";
    String dbVersionString = "5.0.0";

    def init = { servletContext ->

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        Sysconfig dbVersion = Sysconfig.findByKey("DB Version");
        if ( dbVersion == null) {

            new Sysconfig(key: 'DB Version', value: "1.0.0").save();
        }
        else {
            dbVersion.setValue(dbVersionString);
            dbVersion.save();
        }

        Sysconfig swVersion = Sysconfig.findByKey("DB Version");
        if ( swVersion == null ) {

            new Sysconfig(key: 'SW Version', value: swVersionString).save();
        }
        else {

            swVersion.setValue(swVersionString);
            swVersion.save();
        }

        Role role = Role.findByAuthority('ROLE_ADMIN');
        if (role == null) {

            role = new Role('ROLE_ADMIN').save();
            User user = new User('play_it_forward@outlook.com', 'Bring1tLadies!').save();
            UserRole.create(user, role);
            UserRole.withSession {

                it.flush()
                it.clear()
            }
        }

        role = Role.findByAuthority('ROLE_GROUP_LEADER');
        if (role == null) {

            role = new Role( authority: 'ROLE_GROUP_LEADER').save();
            User user = new User( 'saiken@ulfheim.net',  'Bring1tLadies!').save();
            UserRole.create(user, role);
            UserRole.withSession {

                it.flush();
                it.clear();
            }
        }

        role = Role.findByAuthority('ROLE_EVENT_ORGANIZER');
        if (role == null) {

            role = new Role( authority: 'ROLE_EVENT_ORGANIZER').save();
            User user = new User( 'bluesaddict@gmail.com',  'Set4Spell').save();
            UserRole.create(user, role);
            UserRole.withSession {

                it.flush();
                it.clear();
            }
        }
    }

    def destroy = {

    }
}
