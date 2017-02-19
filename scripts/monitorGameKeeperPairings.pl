#! /usr/bin/perl -w

################################################################################
#
#	Simone Aiken												Feb 11th, 2016
#	Parse standings out of gameKeeper
#
################################################################################

	use strict;

	if (@ARGV != 4)
	{
		print "\n\tUsage: <gp abbreviation> <round> <matchWith filename> <test flag>\n\n";
		exit 1;
	}

    my $abbr = $ARGV[0];
	my $round = $ARGV[1];
    my $test = $ARGV[3];
    my $url = "http://pairings.gamekeeper.ca/pairings/14";

    while ($round < 16)
    {	
        my $file = "gamekeeper-$abbr-round-$round-pairings";
        my $curl = "http://www.playitforward-magic.org/PushNotification/gamekeeper/gp/$abbr/$round";
        if ( $test )
        {
            $curl = "http://localhost:8080/PushNotification/gamekeeper/gp/$abbr/$round";
        }
        
        my $postedRound = &getRoundFromFile($url, $file);
	    while ( $postedRound ne $round)
	    {
  		    print "Round is not up.  At $postedRound/$round\n";
            unlink($file);

		    sleep(30);
            $postedRound = &getRoundFromFile($url, $file);
	    }

        # ############################################## #
        # Read matchfile to get names we are looking for #
        # ############################################## #

    	my %matches;
    	my $matchFile = $ARGV[2];
	
    	open( INPUT, "<$matchFile" ) or die "\nCannot open $matchFile.\n\n";
    	while ( <INPUT> )
    	{
    		chomp();
    		$matches{$_} = 1;
    	}

    	close( INPUT );

        # ####################################### #
        # Parse pairings file looking for matches #
        # ####################################### #
        #   <tr>
        #                   <td>249</td>
        #                   <td>Pho, Teresa</td>
        #                   <td>3</td>
        #                   <td>McIntyre, James</td>
        #           </tr>
    
    	open( INPUT, "<$file" ) or die "\nCannot open $file.\n\n";
    	while ( my $line = <INPUT> )
    	{
    		if ( $line =~ /^\s*<tr>\s*$/ )
    		{
                my $seatLine = <INPUT>;
                my $nameLine = <INPUT>;

                my $seat = &getValue($seatLine);
                my $name = &getValue($nameLine);

			    if ( $matches{$name} )
			    {
				    print "$seat\t$name\n";
			    }
		    }
	    }

	    close( INPUT );

        print "curling $curl\n\n";
        `curl -X POST $curl`;

        $round++;
	    sleep (3600); 
    }

	exit 0;

################################################################################

sub getRoundFromFile()
{
    my $url = $_[0];
    my $pairingsFile = $_[1];

	if ( -e $pairingsFile )
	{
		unlink($pairingsFile);
	}
    `wget -O $pairingsFile $url`;

	open( PAIRS, "<$pairingsFile" ) or die "\nCannot open $pairingsFile.\n\n";
	while ( my $line = <PAIRS> )
	{
        if ( $line =~ /<p>Round (\d+)<\/p>/ )
        {
            my $currentRound = $1;
            close ( PAIRS );
            return $currentRound;
        }      
    }

    close( PAIRS );
	unlink($pairingsFile);
    return -1;
}

sub getValue()
{
	my $htmlLine = $_[0];

	if ( $htmlLine =~ /<td>\s*(.*?)\s*<\/td>/ )
	{
		return $1;
	}

	return "";
}

# vim:sw=4:ts=4:et: 
