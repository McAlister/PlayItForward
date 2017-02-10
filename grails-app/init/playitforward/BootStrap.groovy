package playitforward

import java.sql.Date

@SuppressWarnings("GroovyUnusedDeclaration")
class BootStrap
{
    def init = { servletContext ->

        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        new User(fullName: "Simone Aiken", email:"play_it_forward@outlook.com",
                hash:"\$2a\$06\$bXZgXYru5o1rDhQXcBpBLeh8yhMzEKUimbFhqEQl8jYZbF/P.ruRm").save();

        new PersonType(type: "WOMAN", description: "A future woman pro").save();
        new PersonType(type: "EVENT_ORGANIZER", description: "An event organizer").save();
        new PersonType(type: "WIZARD", description: "Affiliated with WoTC").save();
        new PersonType(type: "ARTIST", description: "Interested Artist").save();
        new PersonType(type: "OTHER", description: "Supporter").save();

        EventType gp = new EventType(type: "GP", description: "Grand Prix").save();
        new EventType(type: "OTHER", description: "Unknown Type").save();

        EventOrganizer cfb = new EventOrganizer(name: "Channel Fireball", url: "http://store.channelfireball.com/store").save();
        EventOrganizer scg = new EventOrganizer(name: "Star City Games", url: "http://www.starcitygames.com/").save();
        EventOrganizer pt = new EventOrganizer(name: "Pastimes", url: "http://www.pastimes.net/").save();
        EventOrganizer gk = new EventOrganizer(name: "Game Keeper", url: "http://www.gamekeeper.ca/").save();
        EventOrganizer cg = new EventOrganizer(name: "Cascade Games", url: "http://www.cascadegames.com/").save();
        EventOrganizer ftf = new EventOrganizer(name: "Face to Face Games", url: "http://www.facetofacegames.com/").save();

        Event sanJose = new Event(organizer: cfb, name: "GP San Jose", type: gp, eventCode: 'gpsj17',
                  startDate: new Date(117, 00, 06), endDate: new Date(117, 00, 07)).save();

        new Event(organizer: pt, name: "GP Pittsburgh", type: gp, startDate: new Date(117, 1, 11), endDate: new Date(117, 1, 12)).save();
        new Event(organizer: gk, name: "GP Vancouver", type: gp, startDate: new Date(117, 1, 18), endDate: new Date(117, 1, 19)).save();
        new Event(organizer: cfb, name: "GP New Jersey", type: gp, startDate: new Date(117, 2, 11), endDate: new Date(117, 2, 12)).save();
        new Event(organizer: scg, name: "GP Orlando", type: gp, startDate: new Date(117, 2, 25), endDate: new Date(117, 2, 26)).save();
        new Event(organizer: cg, name: "GP San Antonio", type: gp, startDate: new Date(117, 3, 1), endDate: new Date(117, 3, 2)).save();
        new Event(organizer: scg, name: "GP Richmond", type: gp, startDate: new Date(117, 4, 6), endDate: new Date(117, 4, 7)).save();
        new Event(organizer: gk, name: "GP Montreal", type: gp, startDate: new Date(117, 4, 20), endDate: new Date(117, 4, 21)).save();
        new Event(organizer: cg, name: "GP Omaha", type: gp, startDate: new Date(117, 5, 3), endDate: new Date(117, 5, 4)).save();
        new Event(organizer: cfb, name: "GP Las Vegas", type: gp, startDate: new Date(117, 5, 15), endDate: new Date(117, 5, 16)).save();
        new Event(organizer: cfb, name: "GP Las Vegas", type: gp, startDate: new Date(117, 5, 16), endDate: new Date(117, 5, 17)).save();
        new Event(organizer: cfb, name: "GP Las Vegas", type: gp, startDate: new Date(117, 5, 17), endDate: new Date(117, 5, 18)).save();
        new Event(organizer: ftf, name: "GP Toronto", type: gp, startDate: new Date(117, 6, 22), endDate: new Date(117, 6, 23)).save();
        new Event(organizer: pt, name: "GP Minneapolis", type: gp, startDate: new Date(117, 7, 5), endDate: new Date(117, 7, 6)).save();
        new Event(organizer: cg, name: "GP Denver", type: gp, startDate: new Date(117, 7, 19), endDate: new Date(117, 7, 20)).save();
        new Event(organizer: pt, name: "GP Indianapolis", type: gp, startDate: new Date(117, 7, 26), endDate: new Date(117, 7, 27)).save();
        new Event(organizer: scg, name: "GP Washington", type: gp, startDate: new Date(117, 8, 2), endDate: new Date(117, 8, 3)).save();
        new Event(organizer: pt, name: "GP Providence", type: gp, startDate: new Date(117, 8, 30), endDate: new Date(117, 9, 1)).save();
        new Event(organizer: cfb, name: "GP Phoenix", type: gp, startDate: new Date(117, 9, 28), endDate: new Date(117, 9, 29)).save();
        new Event(organizer: scg, name: "GP Atlanta", type: gp, startDate: new Date(117, 10, 11), endDate: new Date(117, 10, 12)).save();
        new Event(organizer: cg, name: "GP Portland", type: gp, startDate: new Date(117, 10, 18), endDate: new Date(117, 10, 19)).save();
        new Event(organizer: cg, name: "GP Oklahoma City", type: gp, startDate: new Date(117, 11, 9), endDate: new Date(117, 11, 10)).save();
        new Event(organizer: cfb, name: "GP New Jersey", type: gp, startDate: new Date(117, 11, 16), endDate: new Date(117, 11, 17)).save();

        new EventBounty(event: sanJose, donor: "Cassidy Melczak - Lask Knight Games", prize: "One box Aether Revolt.").save();
    }

    def destroy = {

    }
}
