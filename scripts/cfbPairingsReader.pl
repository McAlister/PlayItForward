#! /usr/bin/perl -w

    use strict;
    use Data::Dumper;

    my %eventInformation;
    my $url = "http://pairings.channelfireball.com/pairings/129";

    &updateRound( 129, $url );

    exit 0;

#############################################################################################

sub updateRound () {

    my $eventID = $_[0];
    my $baseUrl = $_[1];
    my $baseFile = "cfb-standings";


    print "\n\nScanning Players\n\n";

    for ( my $round = 1 ; $round < 15 ; $round++ ) {

        my $url = "$baseUrl/$round";
        my $file = "$baseFile-$round";
    
        print "Loading $eventID from $baseUrl to $file.\n";
        unlink ($file);
        print "Curling: curl -sL $url > $file\n";
        `curl -sL $url > $file`;

        open( INPUT, "<$file" ) or return 1;
        local $/;
        my $content = <INPUT>;
        close( INPUT );

        #Strip away everything before and after the tbody.
        $content =~ s/.*<tbody>//;
        $content =~ s/<\/tbody>.*//;

        while ( $content =~ /<tr>\s*<td>(\d*)<\/td>.*?<a href="(.*?)">(.*?)<\/a>.*?<td>(\d+)<\/td>.*?<\/tr>/g ) {

            my $tableNum = $1;
            my $personalUrl = $2;
            my $name = $3;
            my $points = $4;

            print "$tableNum, $name, $personalUrl, $points\n";
            &parsePlayerPage( $name, $personalUrl );
        }
    }

    print "\n\nCalculating Tiebreakers\n\n";

    # First pass, calculate player match win and player game win for each round.
    # A player’s match-win percentage is that player’s accumulated match points divided by 3
    # times the number of rounds in which he or she competed, or 0.33, whichever is greater.
    foreach my $name ( keys %eventInformation ) {
    
        my $totalWins = 0;
        my $totalGames = 0;
        my $totalPoints = 0;
        for ( my $round = 1 ; $round <= 15 ; $round++ ) {

            my $player = &getPlayerRoundData( $name, $round );
            $totalWins += $player->{gamesWon};
            $totalGames += $player->{gamesPlayed};
            $totalPoints += $player->{points};

            my $gamePoints = $player->{gamesWon} * 3;
            $gamePoints += $player->{gamesDrawn};

            $player->{totalPoints} = $totalPoints;
            $player->{pmw} = $totalPoints / ( 3 * $round );
            $player->{pgw} = $gamePoints / ( 3 * $totalGames );
        }
    }

    print "\n\nCalculating Opponent Match Win\n\n";

    foreach my $name ( keys %eventInformation ) {

        my $totalOpponentMatchWin = 0;
        my $roundsWithAnOpponent = 0;
        for ( my $round = 1 ; $round <= 15 ; $round++ ) {
    
            my $player = &getPlayerRoundData( $name, $round );
            my $opponentName = $player->{opponent};

            if ( $opponentName ne '' ) {

                $roundsWithAnOpponent++;
                my $opponent = &getPlayerRoundData( $opponentName, $round );
                my $omw = $opponent->{pmw};
                if ( $omw < 0.33 ) {
                    $omw = 0.33;
                }

                $totalOpponentMatchWin += $omw;
            }

            $player->{omw} = $totalOpponentMatchWin / $roundsWithAnOpponent;
        }
    }

    
    for ( my $round = 1 ; $round <= 15 ; $round++ ) {
    
        print "\n\nROUND $round!!!!\n\n";

        my @contenders;
        foreach my $name ( keys %eventInformation ) {

            my $player = &getPlayerRoundData( $name, $round );
            push( @contenders, $player );
        }

        my @sorted = sort { &comparePlayers( $a, $b ) } @contenders;
        my $rank = 1;
        for my $player ( @sorted ) {

            $player->{rank} = $rank;
            $rank++;

            print "$player->{rank}\t$player->{name}\t$player->{pmw}\t$player->{omw}\t$player->{pgw}\n";
        }
    }
}


#If a is before b return -1.  If b is before a return 1.  If equal return 0
sub comparePlayers() {

    my $first = $_[0];
    my $second = $_[1];

    if ( $first->{points} > $second->{points} ) {
        return 1;
    } elsif ( $first->{points} < $second->{points} ) {
        return -1;
    }

    if ( $first->{pmw} > $second->{pmw} ) {
        return 1;
    } elsif ( $a->{pmw} < $second->{pmw} ) {
        return -1;
    }

    if ( $first->{omw} > $second->{omw} ) {
        return 1;
    } elsif ( $first->{omw} < $second->{omw} ) {
        return -1;
    }

    return ( $first->{pgw} <=> $second->{pgw} );
}

sub parsePlayerPage() {

    my $name = $_[0];
    my $url = $_[1];

    if ( ! exists $eventInformation{ $name } ) {

        my $content = `curl -sL $url`;
        $content =~ s/.*<h2>Match History<\/h2>//;
        $content =~ s/<footer.*//;

        while ( $content =~ /<div class="info-block h4">.*?Round (\d+).*?(\d)-(\d).*?href=".*?">(.*?)<\/a>.*?<\/div>/g ) {

            my $round = $1;
            my $playerWins = $2;
            my $opponentWins = $3;
            my $opponent = $4;

            my $points = 0;
            if ( $playerWins > $opponentWins ) {
                $points = 3;
            }
            elsif ( $playerWins == $opponentWins ) {
                $points = 1;
            }

            #print "$name vs $opponent Round $round Record $playerWins - $opponentWins\n";
            my $player = &getPlayerRoundData( $name, $round );
            $player->{opponent} = $opponent;
            $player->{points} = $points;
            $player->{gamesWon} = $playerWins;
            $player->{gamesPlayed} = $playerWins + $opponentWins;
        }
    }
}

sub getPlayerRoundData() {

    my $name = $_[0];
    my $round = $_[1];

    if ( ! exists $eventInformation{ $name } ) {

        my @rounds;
        for ( my $i = 1 ; $i <= 15 ; $i++ ) {
            $rounds[$i] = {
                name => $name,
                opponent => '',
                points => 0,
                gamesWon => 0,
                gamesDrawn => 0,
                gamesPlayed => 0,
                omw => 0,
                pmw => 0,
                pgw => 0,
                totalPoints => 0,
                rank => -1
            }
        }

        $eventInformation{$name} = \@rounds;
    }

    my @rounds = @{ $eventInformation{$name} };
    return $rounds[$round];
}
