#! /usr/bin/perl -w

################################################################################
#
#	Simone Aiken												Apr 7th, 2019
#	Parse standings out of wizards page and load into database.
#
################################################################################

	use strict;
    use DBI;

    my $prodUrl = '';
    my $prodUser = 'magic';
    my $prodPass = 'grinder1!';

    my $targetUrl = '';
    my $targetUser = 'magic';
    my $targetPass = 'grinder1!';

    my $dbhProd = DBI->connect("DBI:Pg:dbname=magicmom;host=magicmom.c00icap1mfll.us-west-2.rds.amazonaws.com", $prodUser, $prodPass) or die "Could not connect to Prod $_\n";
    my $dbhTarget = DBI->connect("DBI:Pg:dbname=magicmom;host=162.249.2.156", $prodUser, $prodPass) or die "Could not connect to Target $_\n";

    my %artistIdHash;       # name -> id
    my %artIdHash;          # title -> id
    my %eventOrgIdHash;     # name -> id
    my %eventTypeIdHash;    # type -> id
    my %eventIdHash;        # name-start_date -> id

    print "Copying Data From Prod Comment in the Tables You Want ...\n";

    #&mergeArtists();
    #&mergeArt();
    #&mergeEventOrganizer();
    #&mergeEventType();
    &mergeEvent();
    #&mergeEventBounty();
    #&mergeEventWinner();
    #&mergePlayer();
    &mergeRawStandings();

    $dbhProd->disconnect();
    $dbhTarget->disconnect();

    exit 0;

    
    

################################################################################


# unique name
sub mergeArtists() {

    my $query = 'Select name, blurb, facebook_url, deviant_art_url, patreon_url, website_url, id from Artist;';
    my $qh = $dbhTarget->prepare($query) or die "Cannot Prepare Artist Query $_\n.";
    $qh->execute() or die "Cannot run Artist Query $_\n";

    my %targetNames;
    while (my @row = $qh->fetchrow_array) {

        my $name = $row[0];
        my $id = $row[6];
        $targetNames{$name} = 1;
        $artistIdHash{$name} = $id;
    }
      
    $qh = $dbhProd->prepare($query) or die "Cannot Prepare Artist Query $_\n.";
    $qh->execute() or die "Cannot run Artist Query $_\n";

    my @nameList;
    my (%blurb, %facebook, %deviant, %patreon, %website);
    while ( my @row = $qh->fetchrow_array ) {
    
        my $name = $row[0];
        if ( ! exists ( $targetNames{$name} )) {
            push ( @nameList, $name );
            $blurb{$name} = $row[1];
            $facebook{$name} = $row[2];
            $deviant{$name} = $row[3];
            $patreon{$name} = $row[4];
            $website{$name} = $row[5];
        }
    }
 
    my $sql = "Insert Into Artist \n"
        . "( id, version, name, blurb, facebook_url, deviant_art_url, patreon_url, website_url )"
        . "VALUES \n"
        . "(nextval( 'artist_seq'), 0, ?, ?, ?, ?, ?, ? );";

    $qh = $dbhTarget->prepare($sql);
    my $count = 0;
    foreach ( @nameList ) {

        my $name = $_;

        $qh->bind_param(1, $name);
        $qh->bind_param(2, $blurb{$name});
        $qh->bind_param(3, $facebook{$name});
        $qh->bind_param(4, $deviant{$name});
        $qh->bind_param(5, $patreon{$name});
        $qh->bind_param(6, $website{$name});

        $qh->execute() or die "Cannot insert into Artist: $_\n";
        $count++;
    }

    print "\tInserted $count rows into Artist ...\n";
}

# unique title;
sub mergeArt() {

    my $query = 'Select title, file_name, purchase_url, name, art.id from Art Inner Join Artist On art.artist_id = Artist.id;';
    my $qh = $dbhTarget->prepare($query) or die "Cannot Prepare Art Query $_\n.";
    $qh->execute() or die "Cannot run Art Query $_\n";

    my %targetTitle;
    while ( my @row = $qh->fetchrow_array ) {

        my $title = $row[0];
        my $id = $row[4];
        $targetTitle{$title} = 1;
        $artIdHash{$title} = $id;
    }
      
    $qh = $dbhProd->prepare( $query ) or die "Cannot Prepare Art Query $_\n.";
    $qh->execute() or die "Cannot run Art Query $_\n";

    my @titleList;
    my ( %fileName, %purchaseUrl, %artistName );
    while ( my @row = $qh->fetchrow_array ) {
    
        my $title = $row[0];
        if ( ! exists ( $targetTitle{$title} )) {
            push ( @titleList, $title );
            $fileName{$title} = $row[1];
            $purchaseUrl{$title} = $row[2];
            $artistName{$title} = $row[3];
        }
    }
 
    my $sql = "Insert Into Art \n"
        . "( id, version, artist_id, title, file_name, purchase_url )" 
        . "VALUES \n"
        . "(nextval( 'art_seq'), 0, ?, ?, ?, ? );";

    $qh = $dbhTarget->prepare($sql);
    my $count = 0;
    foreach ( @titleList ) {

        my $title = $_;

        $qh->bind_param(1, $artistIdHash{$artistName{$title}});
        $qh->bind_param(2, $title);
        $qh->bind_param(3, $fileName{$title});
        $qh->bind_param(4, $purchaseUrl{$title});

        $qh->execute() or die "Cannot insert into Art: $_\n";
        $count++;
    }

    print "\tInserted $count rows into Art ...\n";
}

# unique name;
sub mergeEventOrganizer() {

    my $query = 'Select name, url, id from Event_Organizer;';
    my $qh = $dbhTarget->prepare($query) or die "Cannot Prepare Event Organizer Query $_\n.";
    $qh->execute() or die "Cannot run Event Organizer Query $_\n";

    my %targetName;
    while ( my @row = $qh->fetchrow_array ) {

        my $name = $row[0];
        my $id = $row[2];
        $targetName{$name} = 1;
        $eventOrgIdHash{$name} = $id;
    }
      
    $qh = $dbhProd->prepare( $query ) or die "Cannot Prepare Event Organizer Query $_\n.";
    $qh->execute() or die "Cannot run Event Organizer Query $_\n";

    my @nameList;
    my ( %url );
    while ( my @row = $qh->fetchrow_array ) {
    
        my $name = $row[0];
        if ( ! exists ( $targetName{$name} )) {
            push ( @nameList, $name );
            $url{$name} = $row[1];
        }
    }
 
    my $sql = "Insert Into Event_Organizer \n"
        . "( id, version, name, url )" 
        . "VALUES \n"
        . "(nextval( 'event_organizer_seq'), 0, ?, ? );";

    $qh = $dbhTarget->prepare($sql);
    my $count = 0;
    foreach ( @nameList ) {

        my $name = $_;

        $qh->bind_param(1, $name);
        $qh->bind_param(2, $url{$name});

        $qh->execute() or die "Cannot insert into Event Organizer: $_\n";
        $count++;
    }

    print "\tInserted $count rows into Event Organizer ...\n";
}

# unique type;
sub mergeEventType() {

    my $query = 'Select type, description, id from Event_Type;';
    my $qh = $dbhTarget->prepare($query) or die "Cannot Prepare Event Type Query $_\n.";
    $qh->execute() or die "Cannot run Event Type Query $_\n";

    my %targetType;
    while ( my @row = $qh->fetchrow_array ) {

        my $type = $row[0];
        my $id = $row[2];
        $targetType{$type} = 1;
        $eventTypeIdHash{$type} = $id;
    }
      
    $qh = $dbhProd->prepare( $query ) or die "Cannot Prepare Event Organizer Query $_\n.";
    $qh->execute() or die "Cannot run Event Organizer Query $_\n";

    my @typeList;
    my ( %desc );
    while ( my @row = $qh->fetchrow_array ) {
    
        my $type = $row[0];
        if ( ! exists ( $targetType{$type} )) {
            push ( @typeList, $type );
            $desc{$type} = $row[1];
        }
    }
 
    my $sql = "Insert Into Event_Type \n"
        . "( id, version, type, description )" 
        . "VALUES \n"
        . "(nextval( 'event_type_seq'), 0, ?, ? );";

    $qh = $dbhTarget->prepare($sql);
    my $count = 0;
    foreach ( @typeList ) {

        my $type = $_;

        $qh->bind_param(1, $type);
        $qh->bind_param(2, $desc{$type});

        $qh->execute() or die "Cannot insert into Event Type: $_\n";
        $count++;
    }

    print "\tInserted $count rows into Event Type ...\n";
}

# unique name + start date.
sub mergeEvent() {

    my $query = 'Select e.id, e.name, e.start_date, e.end_date, e.event_code, o.name, t.type, ' 
                        . 'a.title, e.coordinator, e.format, e.playmat_file_name, e.cfb_event_key '
                    . 'From event e Inner Join event_type t On e.type_id = t.id '
                        . 'Inner Join event_organizer o On e.organizer_id = o.id '
                        . 'Inner Join art a On e.art_id = a.id;';

    my $qh = $dbhTarget->prepare($query) or die "Cannot Prepare Event Query $_\n.";
    $qh->execute() or die "Cannot run Event  Query $_\n";

    my %targetKey;
    while ( my @row = $qh->fetchrow_array ) {

        my $key = "$row[1]-$row[2]";
        my $id = $row[0];
        $targetKey{$key} = 1;
        $eventIdHash{$key} = $id;
    }
      
    $qh = $dbhProd->prepare( $query ) or die "Cannot Prepare Event Query $_\n.";
    $qh->execute() or die "Cannot run Event Query $_\n";

    my @keyList;
    my ( %name, %start, %end, %code, %organizer, %type, %title, %coordinator, %format, %playmat, %cfb );
    while ( my @row = $qh->fetchrow_array ) {
    
        my $key = "$row[1]-$row[2]";
        if ( ! exists ( $targetKey{$key} )) {
            push ( @keyList, $key );
            $name{$key} = $row[1];
            $start{$key} = $row[2];
            $end{$key} = $row[3];
            $code{$key} = $row[4];
            $organizer{$key} = $row[5];
            $type{$key} = $row[6];
            $title{$key} = $row[7];
            $coordinator{$key} = $row[8];
            $format{$key} = $row[9];
            $playmat{$key} = $row[10];
            $cfb{$key} = $row[11];
        }
    }
 
    my $sql = "Insert Into Event \n"
        . "( id, version, name, start_date, end_date, event_code, organizer_id, type_id, art_id, coordinator, format, playmat_file_name, cfb_event_key )" 
        . "VALUES \n"
        . "(nextval( 'event_seq'), 0, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );";

    $qh = $dbhTarget->prepare($sql);
    my $count = 0;
    foreach ( @keyList ) {

        my $key = $_;

        $qh->bind_param(1, $name{$key});
        $qh->bind_param(2, $start{$key});
        $qh->bind_param(3, $end{$key});
        $qh->bind_param(4, $code{$key});
        $qh->bind_param(5, $eventOrgIdHash{$organizer{$key}});
        $qh->bind_param(6, $eventTypeIdHash{$type{$key}});
        $qh->bind_param(7, $artIdHash{$title{$key}});
        $qh->bind_param(8, $coordinator{$key});
        $qh->bind_param(9, $format{$key});
        $qh->bind_param(10, $playmat{$key});
        $qh->bind_param(11, $cfb{$key});

        $qh->execute() or die "Cannot insert into Event: $_\n";
        $count++;
    }

    print "\tInserted $count rows into Event ...\n";

}

# No unique key
sub mergeEventBounty() {

    my $query = 'Select b.donor, b.prize, e.name, e.start_date From event e Inner Join event_bounty b On e.id = b.event_id;';
    my $qh = $dbhTarget->prepare($query) or die "Cannot Prepare Event Bounty Query $_\n.";
    $qh->execute() or die "Cannot run Event Bounty  Query $_\n";

    my %targetKey;
    while ( my @row = $qh->fetchrow_array ) {

        my $key = "$row[0]-$row[1]-$row[2]-$row[3]";
        $targetKey{$key} = 1;
    }
      
    $qh = $dbhProd->prepare( $query ) or die "Cannot Prepare Event Bounty Query $_\n.";
    $qh->execute() or die "Cannot run Event Bounty Query $_\n";

    my @keyList;
    my ( %donor, %prize, %eventKey );
    while ( my @row = $qh->fetchrow_array ) {
    
        my $key = "$row[0]-$row[1]-$row[2]-$row[3]";
        my $eventKey = "$row[2]-$row[3]";
        if ( ! exists ( $targetKey{$key} )) {
            push ( @keyList, $key );
            $donor{$key} = $row[0];
            $prize{$key} = $row[1];
            $eventKey{$key} = $eventKey;
        }
    }
 
    my $sql = "Insert Into Event_Bounty \n"
        . "( id, version, donor, prize, event_id )" 
        . "VALUES \n"
        . "(nextval( 'event_bounty_seq'), 0, ?, ?, ? );";

    $qh = $dbhTarget->prepare($sql);
    my $count = 0;
    foreach ( @keyList ) {

        my $key = $_;

        $qh->bind_param(1, $donor{$key});
        $qh->bind_param(2, $prize{$key});
        $qh->bind_param(3, $eventIdHash{$eventKey{$key}});

        $qh->execute() or die "Cannot insert into Event Bounty: $?\n";
        $count++;
    }

    print "\tInserted $count rows into Event Bounty ...\n";
}


# unique event ID
sub mergeEventWinner() {

    my $query = 'Select w.name, w.blurb, w.ranking, w.record, w.image_name, e.name, e.start_date from Event e Inner Join Event_Winner w On e.id = w.event_id;';
    my $qh = $dbhTarget->prepare($query) or die "Cannot Prepare Event Winner Query $_\n.";
    $qh->execute() or die "Cannot run Event Winner Query $_\n";

    my %targetKey;
    while ( my @row = $qh->fetchrow_array ) {

        my $key = "$row[5]-$row[6]";
        $targetKey{$key} = 1;
    }
      
    $qh = $dbhProd->prepare( $query ) or die "Cannot Prepare Event Bounty Query $_\n.";
    $qh->execute() or die "Cannot run Event Bounty Query $_\n";

    my @keyList;
    my ( %name, %blurb, %rank, %record, %image );
    while ( my @row = $qh->fetchrow_array ) {
    
        my $key = "$row[5]-$row[6]";
        if ( ! exists ( $targetKey{$key} )) {
            push ( @keyList, $key );
            $name{$key} = $row[0];
            $blurb{$key} = $row[1];
            $rank{$key} = $row[2];
            $record{$key} = $row[3];
            $image{$key} = $row[4];
        }
    }
 
    my $sql = "Insert Into Event_Winner \n"
        . "( id, version, name, blurb, ranking, record, image_name, event_id )" 
        . "VALUES \n"
        . "(nextval( 'event_winner_seq'), 0, ?, ?, ?, ?, ?, ? );";

    $qh = $dbhTarget->prepare($sql);
    my $count = 0;
    foreach ( @keyList ) {

        my $key = $_;

        $qh->bind_param(1, $name{$key});
        $qh->bind_param(2, $blurb{$key});
        $qh->bind_param(3, $rank{$key});
        $qh->bind_param(4, $record{$key});
        $qh->bind_param(5, $image{$key});
        $qh->bind_param(6, $eventIdHash{$key});

        $qh->execute() or die "Cannot insert into Event Winner: $?\n";
        $count++;
    }

    print "\tInserted $count rows into Event Winner ...\n";
}

# unique name
sub mergePlayer() {

    my $query = 'Select name, alias, img_url, is_woman from Player;';
    my $qh = $dbhTarget->prepare($query) or die "Cannot Prepare Player Query $_\n.";
    $qh->execute() or die "Cannot run Player Query $_\n";

    my %targetKey;
    while ( my @row = $qh->fetchrow_array ) {

        my $key = "$row[0]";
        $targetKey{$key} = 1;
    }
      
    $qh = $dbhProd->prepare( $query ) or die "Cannot Prepare Player Query $_\n.";
    $qh->execute() or die "Cannot run Player Query $_\n";

    my @keyList;
    my ( %name, %alias, %img, %woman );
    while ( my @row = $qh->fetchrow_array ) {
    
        my $key = "$row[0]";
        if ( ! exists ( $targetKey{$key} )) {
            push ( @keyList, $key );
            $alias{$key} = $row[1];
            $img{$key} = $row[2];
            $woman{$key} = $row[3];
        }
    }
 
    my $sql = "Insert Into Player \n"
        . "( id, version, name, alias, img_url, is_woman )" 
        . "VALUES \n"
        . "(nextval( 'player_seq'), 0, ?, ?, ?, ? );";

    $qh = $dbhTarget->prepare($sql);
    my $count = 0;
    foreach ( @keyList ) {

        my $key = $_;

        $qh->bind_param(1, $key);
        $qh->bind_param(2, $alias{$key});
        $qh->bind_param(3, $img{$key});
        $qh->bind_param(4, $woman{$key});

        $qh->execute() or die "Cannot insert into Player: $?\n";
        $count++;
    }

    print "\tInserted $count rows into Player ...\n";
}

# unique event, round, rank
sub mergeRawStandings() {   
    
    my $query = 'Select e.name, e.start_date, r.name, r.opponent_match_win, r.points, r.rank, r.round, r.is_woman From Raw_Standings r Inner Join Event e On r.event_id = e.id';
    my $qh = $dbhTarget->prepare($query) or die "Cannot Prepare Raw Standings Query $_\n.";
    $qh->execute() or die "Cannot run Raw Standings Query $_\n";

    my %targetKey;
    while ( my @row = $qh->fetchrow_array ) {

        my $key = "$row[0]-$row[1]-$row[6]-$row[5]";
        $targetKey{$key} = 1;
    }
      
    $qh = $dbhProd->prepare( $query ) or die "Cannot Prepare Raw_Standings Query $_\n.";
    $qh->execute() or die "Cannot run Raw_Standings Query $_\n";

    my @keyList;
    my ( %eventKey, %name, %omw, %points, %rank, %round );
    while ( my @row = $qh->fetchrow_array ) {
    
        my $key = "$row[0]-$row[1]-$row[6]-$row[5]";
        my $eventKey = "$row[0]-$row[1]";
        if ( ! exists ( $targetKey{$key} )) {
            push ( @keyList, $key );
            $eventKey{$key} = $eventKey;
            $name{$key} = $row[2];
            $omw{$key} = $row[3];
            $points{$key} = $row[4];
            $rank{$key} = $row[5];
            $round{$key} = $row[6];
        }
    }
 
    my $sql = "Insert Into Raw_Standings \n"
        . "( id, version, event_id, is_woman, name, opponent_match_win, points, rank, round )" 
        . "VALUES \n"
        . "(nextval( 'raw_standings_seq'), 0, ?, 'f', ?, ?, ?, ?, ? );";

    $qh = $dbhTarget->prepare($sql);
    my $count = 0;
    foreach ( @keyList ) {

        my $key = $_;

        $qh->bind_param(1, $eventIdHash{$eventKey{$key}});
        $qh->bind_param(2, $name{$key});
        $qh->bind_param(3, $omw{$key});
        $qh->bind_param(4, $points{$key});
        $qh->bind_param(5, $rank{$key});
        $qh->bind_param(6, $round{$key});

        $qh->execute() or die "Cannot insert into Raw_Standings: $?\n";
        $count++;
    }

    print "\tInserted $count rows into Raw_Standings ...\n";
}

