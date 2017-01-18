package playitforward

class BootStrap
{
    def init = { servletContext ->

        new User(fullName: "Simone Aiken", email:"play_it_forward@outlook.com",
                hash:"\$2a\$06\$bXZgXYru5o1rDhQXcBpBLeh8yhMzEKUimbFhqEQl8jYZbF/P.ruRm").save();

        new PersonType(type: "WOMAN", description: "A future woman pro").save();
        new PersonType(type: "EVENT_ORGANIZER", description: "An event organizer").save();
        new PersonType(type: "WIZARD", description: "Affiliated with WoTC").save();
        new PersonType(type: "OTHER", description: "Other").save();
    }

    def destroy = {

    }
}
