#! /usr/bin/perl -w

################################################################################
#
#	Simone Aiken												Feb 23rd, 2017
#	Parse standings out of wizards page and load into database.
#
################################################################################

	use strict;
    use DBI;

	if (@ARGV != 3)
	{
		print "\n\tUsage: <gptag> <round> <pass>\n\n";
		exit 1;
	}

    my $pass = $ARGV[2];
    my $dbh = DBI->connect("DBI:Pg:dbname=magicmom;host=localhost", "magic", $pass);
    #my $dbh = DBI->connect("DBI:Pg:dbname=magicmom;host=magicmom.c00icap1mfll.us-west-2.rds.amazonaws.com", "magic", $pass);
	my $abbr = $ARGV[0];
	my $round = $ARGV[1];

    my $prefix = "";
    if ($abbr eq "gpvan17") {

          $prefix = "0";
    }

    my ($eventID, $start, $end) = &getEventID($abbr);

    my $url = "https://magic.wizards.com/en/events/coverage/$abbr/round-$prefix" . $round . "-standings-$start";
    my $file = "round-" . $round . "-standings-2017-$start";
    if ( $round > 9 ) {       

            $url = "https://magic.wizards.com/en/events/coverage/$abbr/round-" . $round . "-standings-$end";
            $file = "round-" . $round . "-standings-$end";
    }

	unless ( -e $file ) {

        print "Curling: curl $url > $file\n\n";
		`curl $url > $file`;
	}

	open( INPUT, "<$file" ) or die "\nCannot open $file.\n\n";

    my $count = 0;
	while ( my $rankLine = <INPUT> )
	{
		if ( $rankLine =~ /<\/tr><tr><td>(\d+)<\/td>/ )
		{
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
                . "(nextval('raw_standings_seq'), 0, ?, ?, ?, ?, ?, ?, false);";

            #print "SQL:\n\n$sql\n\n $eventID, $name, $omw, $points, $rank, $round";

            my $qh = $dbh->prepare($sql) or die "Cannot Prepare Statement.";
            $qh->bind_param(1, $eventID);
            $qh->bind_param(2, $name);
            $qh->bind_param(3, $omw);
            $qh->bind_param(4, $points);
            $qh->bind_param(5, $rank);
            $qh->bind_param(6, $round);

            $qh->execute() or die "Cannot execute Statement: $!";

            $count++;
		}
	}

    print "Inserted $count rows!\n";
	close( INPUT );

	# //////// #
	# Clean up #
	# //////// #

    unlink ($file);
    $dbh->disconnect();

	exit 0;

################################################################################

sub getValue() {

	my $line = $_[0];

	if ( $line =~ /<td>(.*?)<\/td>/ )
	{
		return $1;
	}

	return "";
}

sub getEventID() {

        my $eventCode = $_[0];

        my $qh = $dbh->prepare("Select id, start_date, end_date from event where event_code = ?") or die "Cannot Prepare Statement.";
        $qh->bind_param(1, $abbr);

        $qh->execute() or die "Cannot get Event ID.";

        return $qh->fetchrow_array();
}
