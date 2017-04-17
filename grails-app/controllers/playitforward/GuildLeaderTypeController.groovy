package playitforward

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Transactional(readOnly = true)
class GuildLeaderTypeController
{

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [];

    @Secured('permitAll')
    def index(Integer max) {
        respond GuildLeaderType.listOrderByType(), model:[groupLeaderTypeCount: GuildLeaderType.count()]
    }

    @Secured('permitAll')
    def show(GuildLeaderType groupLeaderType) {
        respond groupLeaderType
    }

}
