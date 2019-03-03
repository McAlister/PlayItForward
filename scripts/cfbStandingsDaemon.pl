#! /usr/bin/perl -w

################################################################################
#
#   Simone Aiken                                                  Feb 1, 2018
#   Parse standings out of cfb page and load into database.
#
################################################################################

	use strict;
	use DBI;
	use Data::Dumper;

	my $dbHost = $ENV{'PIF_DB_HOST'};
	my $dbUser = $ENV{'PIF_DB_USER'};
	my $dbPass = $ENV{'PIF_DB_PASS'};
	my $rounds = 15;
    my $logFile = '/tmp/standings.out';

	my $dbh = DBI->connect($dbHost, $dbUser, $dbPass) or &exitError( "Can't connect to DB $!\n", 1 );

	# Get all Incomplete Events prior to Tomorrow.
	my @incompleteEvents;
	&populateIncompleteEvents();

    while ( 1 ) {

        my $masterPageContent;
        my $inProgress = 0;
        foreach my $event ( @incompleteEvents ) {

            my $eventID = $event->{id};
            my $eventCode = $event->{event_code};
            my $startDate = $event->{start_date};
            my $endDate = $event->{end_date};
            my $maxRound = $event->{max};
            my $format = $event->{format};
            my $cfbKey = $event->{cfb_event_key};
            my $name = $event->{name};

            &log( "Processing $name ..." );

    		my $round = 1;
		    if ( defined $maxRound ) {
	    		$round = $maxRound + 1;
    		}

            $masterPageContent = `curl http://coverage.channelfireball.com/event/$cfbKey`;
            $masterPageContent =~ s/.*Standings//;
            $masterPageContent =~ s/<\/tbody>.*//;

            my $rc = 0;
    		while ( $round <= $rounds && !$rc ) {
    
                my $url = '';
                my $count = 0;
                while ( $masterPageContent =~ /<a href="(.*?)" target="_blank">$round<\/a>/g ) {
    
                    $url = $1;  #Should be 3 hits and the last is Standings.
                    $count++;
                }

                if ( $count == 3 &&  $url ne '' ) {        

                    &log( "\tRound $round $url" ); 
	    			$rc = &updateRound($eventID, $round, $url );
                    if ( $rc == 2 ) {
                        $inProgress = 1;
                    }
                    else {
                        $round++;
                    }
                }
                else {
                    $inProgress = 1;
                    $rc = 1;
                }
            }
        }

        $dbh->disconnect();

        if ( $inProgress ) {
            sleep ( 600 );    # 5 min
        }
        else {
            &log ( "No events in progress.  Checking in tomorrow." );
            sleep ( 86400 );    # 1 day
        }

        $dbh = DBI->connect($dbHost, $dbUser, $dbPass) or &exitError( "Can't connect to DB $!\n", 1 );
        &populateIncompleteEvents();
    }

	$dbh->disconnect();

	exit 0;

################################################################################

sub populateIncompleteEvents() {

	&log( "Querying for incomplete events ... \n" );
	@incompleteEvents = ();

	my $query = "Select e.id, e.name, e.format, e.event_code, e.start_date, e.end_date, cfb_event_key, max(r.round) \n"
			. "From event e left join raw_standings r On e.id = r.event_id \n"
			. "Where e.start_date < now() + interval '1 day' And cfb_event_key is not null  \n"
			. "Group By e.id, e.format, e.event_code, e.start_date, e.end_date \n"
    			. "Having max(r.round) is null Or max(r.round) < 15;";

	my $qh = $dbh->prepare($query);
	$qh->execute or &exitError( "Cannot get Incomplete Events.", 2 );

	while( my $element = $qh->fetchrow_hashref ) {

		&log( "\t$element->{name}\n" );
		push(@incompleteEvents, $element);

	}

	$qh->finish();
}

sub updateRound() {

	my $eventID = $_[0];
	my $round = $_[1];
	my $url = $_[2];

	&log ( "Curling: curl -sL $url" );
	my $content = `curl -sL $url`;
    $content =~ s/.*<tbody>//;
    $content =~ s/<\/tbody>.*//;

	my $count = 0;
	my @nameList;
	my %pointHash; 	#name -> points
    my %omwHash;    #name -> OMW
    my %rankHash;   #name -> rank

    # <tr><td>1</td><td>Elenbogen, Andrew [US] </td><td>15</td><td>0.8000</td></tr>
	while ( $content =~ /<tr>.*?(\d+).*?<td>(.*?)<\/td>.*?(\d+).*?(\d\.\d+).*?<\/tr>/g ) {

        my $rank = $1;
		my $name = $2;
		my $points = $3;
        my $omw = $4;

        $name =~ s/^\s+|\s+$//g;
		#print "$rank, $name, $points, $omw\n";

		push( @nameList, $name );
		$pointHash{$name} = $points;
        $omwHash{$name} = $omw;
        $rankHash{$name} = $rank;
	}

	my $sql = "Insert Into raw_standings \n"
		. "(id, version, event_id, name, opponent_match_win, points, rank, round, is_woman)"
		. "VALUES \n"
		. "(nextval('hibernate_sequence'), 0, ?, ?, ?, ?, ?, ?, false);";
	my $qh = $dbh->prepare($sql) or return 1;

	foreach my $name ( @nameList ) {

		my $points = $pointHash{$name};
        my $rank = $rankHash{$name};
        my $omw = $omwHash{$name};

		#print "EventID: $eventID, Name: $name, points: $points, round: $round\n";
		$qh->execute($eventID, $name, $omw,  $points, $rank, $round) or return 1;
		$count++;
	}

	if ($count > 0) {

		&log ( "Inserted $count rows!\n" );
	} 
	else {

		&log ( "Bad file, No rows inserted.\n" );
		return 2;
	}

	return 0;
}

sub exitError() {

	my $error = $_[0];
	my $code = $_[1];

	&log( "$code: $error" );
	exit $code;
}

sub log() {

    my $message = localtime . ": $_[0]";
    print "$message";
    `echo "$message" >> $logFile`;
}

