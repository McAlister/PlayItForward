#! /usr/bin/perl -w

################################################################################
#
#   Simone Aiken                                                  Dec 29, 2018
#   Parse standings out of wizards page and load into database.
#
################################################################################

	use strict;
	use DBI;
	use Data::Dumper;

	my $dbHost = $ENV{'PIF_DB_HOST'};
	my $dbUser = $ENV{'PIF_DB_USER'};
	my $dbPass = $ENV{'PIF_DB_PASS'};
	my $rounds = 15;
	my $lastDay1Round = 8;
	my $lastDay1RoundLimited = 9;
    my $logFile = '/tmp/standings.out';

	my $dbh = DBI->connect($dbHost, $dbUser, $dbPass) or &exitError( "Can't connect to DB $!\n", 1 );

	# Get all Incomplete Events prior to Tomorrow.
	my @incompleteEvents;
	&populateIncompleteEvents();

	while ( 1 ) {

		my $inProgress = 0;
		foreach my $event (@incompleteEvents) {

			my $eventID = $event->{id};
			my $eventCode = $event->{event_code};
			my $startDate = $event->{start_date};
			my $endDate = $event->{end_date};
			my $maxRound = $event->{max};
			my $format = $event->{format};

			my $round = 1;
			if ( defined $maxRound ) {
				$round = $maxRound + 1;
			}

			my $rc = 0;
			while ($round <= $rounds && !$rc) {

				my $day = $startDate;
				if ( $round > $lastDay1RoundLimited || ($format ne 'Limited' && $round > $lastDay1Round )) {
					$day = $endDate;
				}

				#my $url = "http://magic.wizards.com/en/events/coverage/$eventCode/round-$round-standings-$day";
				my $url = "http://magic.wizards.com/en/events/coverage/$eventCode/round-$round-standings";
				my $file = "round-$round-standings-$day";

				$rc = &updateRound($eventID, $round, $url, $file);
				if ( $rc == 2 ) {

					$inProgress = 1;
				}
				else {
					$round++;
				}
			}
		}

		$dbh->disconnect();

		if ( $inProgress ) {
			sleep (600);  	# 5 min
		}
		else {
			&log ( "No events in progress.  Checking in tomorrow." );
			sleep ( 86400 ); 	# 1 day
		}

		$dbh = DBI->connect($dbHost, $dbUser, $dbPass) or &exitError( "Can't connect to DB $!\n", 1 );
		&populateIncompleteEvents();
	}

	$dbh->disconnect();
	exit 0;

################################################################################

sub populateIncompleteEvents () {

	&log( "Querying for incomplete events ... \n" );
	@incompleteEvents = ();

	my $query = "Select e.id, e.name, e.format, e.event_code, e.start_date, e.end_date, max(r.round) \n"
			. "From event e left join raw_standings r On e.id = r.event_id \n"
			. "Where e.start_date < now() + interval '1 day'  \n"
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

sub updateRound () {

	my $eventID = $_[0];
	my $round = $_[1];
	my $url = $_[2];
	my $file = $_[3];

	&log ( "Loading $eventID round $round from $url to $file.\n" );
	unlink ($file);
	&log ( "Curling: curl -sL $url > $file\n" );
	`curl -sL $url > $file`;

	open( INPUT, "<$file" ) or return 1;

	my $count = 0;
	while ( my $rankLine = <INPUT> ) {

		if ( $rankLine =~ /<\/tr><tr><td>(\d+)<\/td>/ ) {

			my $rank = $1;
			my $nameLine = <INPUT>;
			my $pointLine = <INPUT>;
			my $omwLine = <INPUT>;

			my $points = &getValue($pointLine);
			my $name = &getValue($nameLine);
			my $omw = &getValue($omwLine);

			my $sql = "Insert Into raw_standings \n"
				. "(id, version, event_id, name, opponent_match_win, points, rank, round, is_woman)"
				. "VALUES \n"
				. "(nextval('hibernate_sequence'), 0, ?, ?, ?, ?, ?, ?, false);";

			my $qh = $dbh->prepare($sql) or return 1;
			$qh->bind_param(1, $eventID);
			$qh->bind_param(2, $name);
			$qh->bind_param(3, $omw);
			$qh->bind_param(4, $points);
			$qh->bind_param(5, $rank);
			$qh->bind_param(6, $round);

			#print "EventID: $eventID, Name: $name, OMW: $omw, points: $points, Rank: $rank, round: $round\n";

			$qh->execute() or return 1;
			$count++;
		}
	}

	close( INPUT );

	if ($count > 0) {

		&log ( "Inserted $count rows!\n" );
		unlink ($file);
	} 
	else {

		&log ( "Bad file, No rows inserted.\n" );
		return 2;
	}

	return 0;
}

sub getValue() {

	my $line = $_[0];

	if ( $line =~ /<td>(.*?)<\/td>/ )
	{
		return $1;
	}

	return "";
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

