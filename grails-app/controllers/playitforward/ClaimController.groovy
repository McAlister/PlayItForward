package playitforward


import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ClaimController {

	static responseFormats = ['json', 'xml'];
    def emailService;
	
    def index() {

        // Empty Block
    }

    @Secured(['permitAll'])
    def claimPrize() {

        String name = params.name ?: request.JSON?.name;
        String email = params.email ?: request.JSON?.email;
        String contest = params.contest ?: request.JSON?.contest;
        String text = params.text ?: request.JSON?.text;

        emailService.sendClaimEmail(name, email, contest, text);

        respond([message: 'success']);
    }

}
