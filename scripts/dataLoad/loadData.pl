#! /usr/bin/perl -w

################################################################################
#
#	Simone Aiken												March 5, 2017
#	Run the alters in order and update the DB Version.
#   Expected that you are in the dataLoad folder when you run it.
#
################################################################################

	use strict;
	use FindBin;
    use DBI;

	if (@ARGV != 3)
	{
		print "\n\tUsage: <db host> <db user> <db password>\n\n";
		print "\n\tExample: localhost magic secretPassword\n\n";
		exit 1;
	}

    #############################
    print "Loading Alters:\n\n";
    #############################

    my $host = $ARGV[0];
    my $user = $ARGV[1];
    my $pass = $ARGV[2];

    #####################################################
    print "\tConnecting to database $user\@$host ...\n";
    #####################################################

    my $dbh = DBI->connect("DBI:Pg:dbname=magicmom;host=$host", $user, $pass);

    my $execPath = $FindBin::Bin;
    print "\tChdir to $execPath ...\n";
    chdir($execPath) or die "Could not change dir to $execPath\n";

    ###########################################
    print "\tFinding Current DB revision ... ";
    ###########################################

    my $dbVersion = &getDBVersion();
    print "$dbVersion\n";

    ############################################
    print "\tIterating over alter folders ...\n";
    ############################################

    opendir(DH, ".") or die "Cannot open current directory.";
    my @files;
    while ( my $file = readdir(DH)) {

        if ( $file =~ /(\d+\.\d+\.\d+)/ ) {
            push (@files, $file);
        }
    }
    closedir(DH);

    my @sorted_files = sort { &sortFiles($a, $b) } @files;

    my $lastFolder;
    my $newAlters = 0;
    while ( my $name = shift @sorted_files ) {

        if ($name eq $dbVersion) {
            $newAlters = 1;
        }

        if ($newAlters) {
            print "\t\tLoading $name ...\n";
            &processAlterFolder($name);
            $lastFolder = $name;
        }
    }

    ########################################################
    print "\tUpdating the DB Version to $lastFolder ... ";
    ########################################################

    if ($newAlters) {

        my $rows = $dbh->do("Update sysconfig set value = '$lastFolder' where key = 'DB Version';")
            or die "Could not update DB Version: $!\n";

        if ($rows) {
            print "Updated\n";
        }
        else {
            print "Failed\n";
        }
    }
    else {
        print "No Alters Were Run\n";
    }

    #############################
    print "\tCleaning up ...\n\n";
    #############################

    print "Success!\n\n";
    $dbh->disconnect();

	exit 0;

#################################################################################

sub sortFiles {

    my $fileA = $_[0];
    my $fileB = $_[1];

    my $a1;
    my $a2;
    my $a3;
    if ($fileA =~ /(\d+)\.(\d+)\.(\d+)/) {

        $a1 = $1;
        $a2 = $2;
        $a3 = $3;
    }
    else {
        print ("File A is $fileA");
        exit 2;
    }

    my $b1;
    my $b2;
    my $b3;
    if ($fileB =~ /(\d+)\.(\d+)\.(\d+)/) {

        $b1 = $1;
        $b2 = $2;
        $b3 = $3;
    }

    if ($a1 != $b1) {

        return $a1 > $b1;
    }
    elsif ($a2 != $b2) {

        return $a2 > $b2;
    }
    else {
        return $a3 > $b3;
    }


}

sub processAlterFolder() {

	my $folder = $_[0];

    opendir(AH, "$folder") or die "Cannot open current directory.";
    my @files = readdir(AH);
    closedir(AH);

    while ( my $name = shift @files ) {

        my $path = "$folder/$name";
        if ( -f $path && $path =~ /\.sql$/ ) {

            open FILE, "$path";
            my @lines = <FILE>;
            my $sql = join '', @lines;
            $dbh->do($sql) or die "Cannot run sql $path: $!\n\n";
        }
    }
}

sub getDBVersion() {

    my $qh = $dbh->prepare("Select value from sysconfig where key = 'DB Version'") or die "Cannot Prepare Statement.";
    $qh->execute() or die "Cannot get DB Version.";
    my $id = $qh->fetchrow_array();
    return $id;
}
