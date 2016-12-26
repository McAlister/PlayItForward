#! /usr/bin/perl -w

################################################################################
#
#   Simone Aiken                                              Dec 25, 2016
#
#   Script to run database alters for MagicMom.
#
################################################################################

    use DBI;
    use DBD::Pg;
    use Getopt::Long;
    use Cwd 'abs_path';
    use File::chdir;
    use File::Basename;

    my $showUsage = 0;

    # Connection information in development.
    my $dbName = "magicmom";
    my $dbUser = "magic";
    my $dbPass = "grinder";
    my $dbHost = "localhost";

    print "\n";
    &runCommandLineParser();

    print "Connecting to $dbHost as $dbUser ... \n";
    my $dbh = DBI->connect("dbi:Pg:dbname=$dbName;host=$dbHost", $dbUser, $dbPass,
            { AutoCommit=>1, RaiseError=>1, PrintError=>0 } );

    my $scriptPath = abs_path($0);
    my $folderPath = dirname($scriptPath) . "/alters";
    print "Changing dir to alter directory $folderPath ... \n";
    chdir($folderPath) or &cleanExit("Could not change working directory to $folderPath", 4);

    my $alterNumber = &getFirstAlterNumber();
    my $fileName = sprintf('%06d.sql', $alterNumber);
    print "Running alters on $dbName starting with $fileName ... \n";

    $ENV{PGUSER} = $dbUser;
    $ENV{PGPASSWORD} = $dbPass;
    $ENV{PGHOST} = $dbHost;
    $ENV{PGDATABASE} = $dbName;
    while ( -e $fileName && -f $fileName )
    {
        print "\tProcessing $fileName ...\n";

        my $cmd = "psql -f $fileName";
        if (system($cmd))
        {
            &cleanExit("$cmd failed: $?", 10);
        }

        &updateLastAlterNumber($alterNumber);

        $alterNumber++;
        $fileName = sprintf('%06d.sql', $alterNumber);
    }

    &cleanExit("Database Updated!", 0);

################################### functions ##################################

sub getFirstAlterNumber()
{
    my $sysconfigExists = 0;
    my $sql = "Select count(*) from information_schema.tables\n"
            . "Where table_catalog = ?\n"
            . "\tAnd table_schema = 'public'\n"
            . "\tAnd table_name = 'sysconfig';\n";
    my $qh = $dbh->prepare($sql) or &cleanExit("Cannot prepare sql:\n\n$sql", 5);

    $qh->execute($dbName);
    while (my @row = $qh->fetchrow_array)
    {
        $sysconfigExists = $row[0];
    }

    if ( $sysconfigExists )
    {
        $sql = "Select value from sysconfig\n"
             . "Where name = 'last alter'\n";

        $qh = $dbh->prepare($sql) or &cleanExit("Cannot prepare sql:\n\n$sql", 6);
        $qh->execute();
        while (my @row = $qh->fetchrow_array)
        {
            return ($row[0] + 1);
        }
    }

    return 1;
}

sub updateLastAlterNumber()
{
    my $newAlterNumber = $_[0];

    my $sql = "Update sysconfig set value = ? Where name = 'last alter';";
    $dbh->do($sql, undef, $newAlterNumber) or &cleanExit("Unable to update last alter number sql:\n\n$sql", 6);
}

sub runCommandLineParser()
{
    GetOptions ("db=s"          => \$dbName,
                "user=s"        => \$dbUser,
                "pass=s"        => \$dbPass,
                "host=s"        => \$dbHost,
                "help"          => \$showUsage) or &cleanExit("Error in command line arguments", 2);

    if ($showUsage)
    {
        &printUsage();
        print "\n";
        exit 3;
    }
}

sub printUsage()
{
    print "\nUsage:\n\n";
    print "\t--db\t\tThe database to connect to.\n";
    print "\t--host\t\tThe host running the database.\n";
    print "\t--user\t\tThe user to log in as.\n";
    print "\t--pass\t\tThe password for the user.\n";
}

sub cleanExit()
{
    my $msg = $_[0];
    my $code = $_[1];

    if ($code == 2)
    {
        &printUsage();
    }

    print "\n\nExiting[$code] ... $msg\n\n";

    $dbh->disconnect();
    exit $code;
}