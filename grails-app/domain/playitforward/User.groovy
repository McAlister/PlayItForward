package playitforward

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@SuppressWarnings("GroovyUnusedDeclaration")
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    transient springSecurityService

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    String resetKey

    User(String username, String password) {
        this()
        this.username = username
        this.password = password
    }

    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this)*.role
    }

    def beforeInsert() {
        encodePassword()
    }

    def beforeUpdate() {
        if (isDirty('password')) {
            encodePassword()
        }
    }

    protected void encodePassword() {
        password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
    }

    def makeResetKey() {
        def random = new Random()
        String charset = (('A'..'F') + ('0'..'9')).join('')
        resetKey = (1..20).collect {
            charset[random.nextInt(charset.length())]
        }.join('')
    }

    static transients = ['springSecurityService']

    static constraints = {
        username blank: false, unique: true
        password blank: false
        resetKey nullable: true
    }

    static mapping = {
        password column: '`password`'
        table 'app_user'
    }
}
