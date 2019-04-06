package playitforward

class Art {

    String title;
    Artist artist;
    String fileName;
    String purchaseUrl


    static constraints = {

        title nullable: false, unique: true;
        artist nullable: false;
        fileName nullable: false;
        purchaseUrl nullable: true;
    }

    static mapping = {

        id generator: 'native', params: [sequence: 'art_seq'];
        artist index: 'art_artist_idx', fetch: 'join', lazy: true;
        title index: 'art_artist_idx';
        fileName column: 'file_name';
        purchaseUrl column: 'purchase_url';
    }

    static belongsTo = [Artist];
}
