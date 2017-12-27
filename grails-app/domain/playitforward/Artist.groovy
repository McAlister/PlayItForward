package playitforward

class Artist {

    String name;
    String blurb;
    String deviantArtUrl;
    String patreonUrl;
    String facebookUrl;
    String webSiteUrl;

    static constraints = {

        name nullable: false, unique: true;
        blurb nullable: false;
        deviantArtUrl nullable: true, required: false;
        patreonUrl nullable: true, required: false;
        facebookUrl nullable: true, required: false;
        webSiteUrl nullable: true, required: false;
    }

    static mapping = {

        id generator: 'native', params: [sequence: 'artist_seq'];
        name index: 'artist_name_idx';
        deviantArtUrl column: 'deviant_art_url';
        patreonUrl column: 'patreon_url';
        facebookUrl column: 'facebook_url';
        webSiteUrl column: 'website_url';
    }
}
