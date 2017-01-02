package playitforward

class BootStrap
{
    def init = { servletContext ->

        new User(fullName: "Simone Aiken", email:"play_it_forward@outlook.com",
                hash:"\$2a\$06\$bXZgXYru5o1rDhQXcBpBLeh8yhMzEKUimbFhqEQl8jYZbF/P.ruRm").save();
    }

    def destroy = {

    }
}
