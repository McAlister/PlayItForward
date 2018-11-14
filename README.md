# PlayItForward
Code for tracking Magic the Gathering Player Demographics.

Steps to get here:

    download from https://grails.org/download.html
    $ sudo mkdir -p /opt
    $ sudo mv grails-3.2.4/ /opt
    $ vi ~/.profile
    $ grep PATH ~/.profile
    export PATH=/usr/local/bin:/usr/local/sbin:/usr/bin:$PATH:/opt/grails-3.2.4/bin
    $ . ~/.profile
    $ cd ~/src/grails-sandbox/

    ### i needed this due to a bug in my current installed JVM
    $ export GRAILS_OPTS="-XX:-UseSplitVerifier -Xverify:none"

    $ grails create-app --inplace --profile angular
    | Application created at /Users/mdriscoll/src/grails-sandbox
    $ grails run-app
    Grails application running at http://localhost:8080 in environment: development

Then to switch out for postgres: [changeset](https://github.com/syncsynchalt/grails-sandbox/commit/0fa590fdae54056ea98316f17d92ad1e650a328d)

    $ brew install postgres
    $ createuser grails -d
    $ createdb -U grails grails
    (made the changes in this changeset)
    $ grails compile
    $ grails run-app

Then to add a basic domain object and a REST API for manipulating it: [changeset](https://github.com/syncsynchalt/grails-sandbox/commit/409b703506f653fe54ae557c029dde7e1f58db09)

    $ grails create-domain-class frob
    [... edit the resulting class to add String name, Integer value ...]
    ### generate the CRUD scaffolding for a Frob
    $ grails generate-all frob
    $ alias jsonpp='python -m json.tool'
    $ curl -s -X POST http://localhost:8080/frob \
      -H "Content-Type: application/json" \
      --data '{"name":"Foo","value":100}' | jsonpp
        {
            "id": 1,
            "name": "Foo",
            "value": 100
        }
    $ curl -s -X POST http://localhost:8080/frob \
      -H "Content-Type: application/json" \
      --data '{"name":"Bar","value":200}' | jsonpp
        {
            "id": 2,
            "name": "Bar",
            "value": 200
        }
    $ curl -s http://localhost:8080/frob | jsonpp
        [
            {
                "id": 1,
                "name": "Foo",
                "value": 100
            },
            {
                "id": 2,
                "name": "Bar",
                "value": 200
            }
        ]
    $ curl -XDELETE -s http://localhost:8080/frob/2
    $ curl -s http://localhost:8080/frob | jsonpp
        [
            {
                "id": 1,
                "name": "Foo",
                "value": 100
            }
        ]

To add some basic authentication: [changeset](https://github.com/syncsynchalt/grails-sandbox/commit/c0b67e7f570dc27dd9c4e540f52bcc4ca5740995)

    $ grails create-interceptor security
    [... edit according to changeset ...]
    $ grails create-controller user
    [... edit according to changeset ...]
    $ create-domain-class User
    [... edit according to changeset ...]
    [... add a UrlMapping for /login ...]
    [... edit BootStrap and add a sample user ...]

    To log in:

    $ curl -XPOST -si http://localhost:8080/login?foo=bar \
        -H Content-Type:application/json \
        --data '{"email":"test@test.com","password":"password"}' | grep Set-Cookie
    Set-Cookie: JSESSIONID=2398F3C4712C97773B9D9490EC3AD67A;path=/;HttpOnly

    To use the authenticated login:

    $ curl -s http://localhost:8080/frob \
      --cookie JSESSIONID=265B97152BF47793ED9A00223DD12072 | jsonpp
        [
            {
                "id": 2,
                "name": "Foo",
                "value": 100
            }
        ]
