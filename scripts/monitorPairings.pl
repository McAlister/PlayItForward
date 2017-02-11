#! /usr/bin/perl -w

################################################################################
#
#	Simone Aiken												Jan 7th, 2016
#	Parse standings out of wizards page.
#
################################################################################

	use strict;
	use LWP::Simple;

	if (@ARGV != 3)
	{
		print "\n\tUsage: <gpcode> <round> <matchWith filename>\n\n";
		exit 1;
	}

    my $abbr = $ARGV[0];
	my $round = $ARGV[1];
	my $url = "http://magic.wizards.com/en/events/coverage/$abbr/round-" . $round . "-pairings-2017-02-11";
	my $file = "round-" . $round . "-pairings-2017-02-11";
	if ( $round > 9 )
	{
		$url = "http://magic.wizards.com/en/events/coverage/$abbr/round-" . $round . "-pairings-2017-02-12";
		$file = "round-" . $round . "-pairings-2017-02-12";
	}

    my $curl = "http://localhost:8080/PushNotification/gp/gppit17/$round";
	
	my $doesExist = head($url);

	while (! $doesExist)
	{
		if ($doesExist) 
		{
  			print "Does exist\n";
		} 
		else 
		{
  			print "Does not exist\n";;
		}

		sleep(20);
		$doesExist = head($url);
	}

    # ############ #
	# Round Exists #
    # ############ #

	unless ( -e $file )
	{
		`wget $url`;
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
    #    </tr><tr><td>271</td>
    #       <td>Panek, Thomas</td>
    #       <td>[US]</td>
    #       <td>vs.</td>
    #       <td>Hubish, Rebecca</td>
    #       <td>[US]</td>
    #   </tr><tr><td>272</td>

	open( INPUT, "<$file" ) or die "\nCannot open $file.\n\n";
	while ( my $rankLine = <INPUT> )
	{
		if ( $rankLine =~ /<\/tr><tr><td>(\d+)<\/td>/ 
            || $rankLine =~ /<\/tr><tr><td>(-)<\/td>/)
		{
            my $name1 = '-';
            my $name2 = '-';

            my $seat = $1;
			my $nameLine = <INPUT>;
			my $countryLine = <INPUT>;
			my $vsLine = <INPUT>;

            $name1 = &getValue($nameLine);

			$nameLine = <INPUT>;
            $name2 = &getValue($nameLine);

			if ( $matches{$name1} )
			{
				print "$seat\t$name1\n";
			}

			if ( $matches{$name2} )
			{
				print "$seat\t$name2\n";
			}
		}
	}

	close( INPUT );

    print "\nShall I curl? $curl\n";
    chomp (my $doCurl = <STDIN>);
    if ($doCurl eq 'y')
    {
        `curl $curl`;
    }    

	# sleep (3600); 

	exit 0;

################################################################################

sub getValue()
{
	my $line = $_[0];

	if ( $line =~ /<td>(.*?)<\/td>/ )
	{
		return $1;
	}

	return "";
}

# vim:sw=4:ts=4:et: 
