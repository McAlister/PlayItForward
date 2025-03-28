package playitforward

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@SuppressWarnings("GroovyUnusedDeclaration")
@EqualsAndHashCode(includes='authority')
@ToString(includes='authority', includeNames=true, includePackage=false)
class Role implements Serializable {

    private static final long serialVersionUID = 1

    String authority

    Role(String authority) {
        this()
        this.authority = authority
    }

    static constraints = {
        authority blank: false, unique: true
    }

    static mapping = {

        id generator: 'native', params: [sequence: 'role_seq'];
        cache true
    }
}
