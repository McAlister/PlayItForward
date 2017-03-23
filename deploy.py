#!/usr/local/bin/python -u
import os, sys, time, datetime, glob
import argparse
from collections import namedtuple
from pprint import pprint
import boto3

### Deployment script for elasticbeanstalk
#
# Prerequisites (on OSX):
#  $ brew install python
#  $ brew install awscli
#  $ aws configure
#  $ pip install boto3
#

### set some defaults
s3_bucket='playitforward-versions'
nowstr=str(int(time.time()))
defaults = {
    'application': ['PlayItForward'],
    'version': [datetime.datetime.today().strftime('%Y%m%d%H%M%S')],
    'environment': ['PIF' + nowstr],
    'cname': ['playitforwardred.hbrthk4xwr.us-west-2.elasticbeanstalk.com'],
    'warfile': glob.glob('dist/*.war'),
}

def parse_args():
    d = defaults
    parser = argparse.ArgumentParser(description='Deploy WAR file to production.')
    parser.add_argument('-w', '--warfile', nargs=1, help='Path to .war file', default=d['warfile'])
    parser.add_argument('-a', '--application', nargs=1, help='Application name', default=d['application'])
    parser.add_argument('-v', '--version', nargs=1, help='Application version', default=d['version'])
    parser.add_argument('-c', '--cname', nargs=1, help='Application cname', default=d['cname'])
    parser.add_argument('-e', '--environment', nargs=1, help='The environment name', default=d['environment'])
    parser.add_argument('-s', '--swap', action='store_true', help='Perform cname swap when done')
    opts = parser.parse_args()

    if len(opts.environment[0]) > 15:
        sys.exit('Environment name is too long, 15 characters max')

    if opts.swap and not opts.swap[0].endswith('.elasticbeanstalk.com'):
        opts.swap[0] = opts.swap[0] + '.elasticbeanstalk.com'

    if not opts.warfile:
        sys.exit('No .war found in dist/, use --warfile to override')

    Opts = namedtuple('Opts', 'warfile application version cname environment swap client')
    return Opts(warfile=opts.warfile[0], application=opts.application[0],
                environment=opts.environment[0], version=opts.version[0],
                cname=opts.cname[0], swap=opts.swap, client=None)

def connect_client(opts):
    client = boto3.client('elasticbeanstalk')
    if not client:
        sys.exit('Unable to connect to elasticbeanstalk, check "aws configure"')
    return client

def get_existing_env_id(opts):
    print 'Looking for environment id for %s' % opts.cname

    environments = lookup_environments(opts)
    for e in environments:
        env_id = e['EnvironmentId']
        if e['Health'] == 'Green' and e['CNAME'] and e['CNAME'].lower() == opts.cname.lower():
            print 'Found environment id: %s' % env_id
            return env_id
    sys.exit('Unable to find environment id with Green status and cname of %s' % opts.cname)

def create_template(opts, template_name, environment_id):
    print 'Creating ConfigurationTemplate named %s' % (template_name)
    opts.client.create_configuration_template(ApplicationName=opts.application,
                                              TemplateName=template_name,
                                              EnvironmentId=environment_id)

def delete_template(opts, template_name):
    print 'Deleting ConfigurationTemplate named %s' % (template_name)
    opts.client.delete_configuration_template(ApplicationName=opts.application, TemplateName=template_name)

def launch_environment(opts, template_name):
    print 'Creating new environment named %s' % (template_name)
    env = opts.client.create_environment(ApplicationName=opts.application,
                                         EnvironmentName=template_name,
                                         VersionLabel=opts.version,
                                         TemplateName=template_name)
    env_id = env['EnvironmentId']
    print 'New Environment Id: %s' % env_id
    return env_id

def lookup_environment(opts, env_id):
    e = opts.client.describe_environments(EnvironmentIds=[env_id])
    return e['Environments'] and e['Environments'][0]

def lookup_environments(opts):
    e = opts.client.describe_environments(ApplicationName=opts.application)
    return e['Environments']

def test_for_environment_ready(opts, env_id, template_name):
    wait_seconds = 1200
    while wait_seconds > 0:
        env = lookup_environment(opts, env_id)
        if env['Health'] == 'Green':
            print 'Current Health: %s Status: %s' % (env['Health'], env['Status'])
            print 'New environment %s launched' % (template_name)
            return True
        print 'Environment not ready (%s/%s). %d seconds remain' % (env['Status'], env['Health'], wait_seconds)
        time.sleep(30)
        wait_seconds -= 30

    sys.exit('Environment does not seem to be launching in a reasonable time. Deployment failed. id: %s name: %s'
                % (env_id, template_name))

def cname_swap(opts, old_env_id, new_env_id):
    print 'Executing CNAME Swap between old environment %s and new environment %s' % (old_env_id, new_env_id)
    opts.client.swap_environment_cnames(SourceEnvironmentId=old_env_id, DestinationEnvironmentId=new_env_id)
    time.sleep(30)
    env = lookup_environment(opts, new_env_id)
    if (env['CNAME'] != opts.cname):
        sys.exit('Environment has unexpected cname of %s after swap' % env['CNAME'])

def terminate_environment(opts, old_env_id):
    print 'Terminating environment %s' % old_env_id
    opts.client.terminate_environment(EnvironmentId=old_env_id)

def upload_new_version(opts, filename):
    print 'Updating application version'
    opts.client.create_application_version(ApplicationName=opts.application,
                                           VersionLabel=opts.version,
                                           SourceBundle={'S3Bucket': s3_bucket, 'S3Key': filename})

def push_war_to_s3(opts):
    original_file = opts.warfile
    new_file = 'app_%s.war' % opts.version

    print 'Pushing war to S3 as %s' % new_file
    s3 = boto3.resource('s3')
    f = open(original_file, 'rb')
    s3.Bucket(s3_bucket).put_object(Key=new_file, Body=f)
    return new_file

def delete_war_from_s3(opts, filename):
    print 'Deleting %s from S3' % filename
    s3 = boto3.client('s3')
    s3.delete_object(Bucket=s3_bucket, Key=filename)

if __name__ == '__main__':

    opts = parse_args()
    opts = opts._replace(client=connect_client(opts))

    filename = push_war_to_s3(opts)
    upload_new_version(opts, filename)
    time.sleep(10)
    old_env_id = get_existing_env_id(opts)

    template_name = opts.environment
    create_template(opts, template_name, old_env_id)
    new_env_id = launch_environment(opts, template_name)
    time.sleep(5)
    test_for_environment_ready(opts, new_env_id, template_name)

    if opts.swap:
        cname_swap(opts, old_env_id, new_env_id)
        terminate_environment(old_env_id)
    else:
        print 'Skipping cname swap. This step must be accomplished manually.'
        print 'New environment has cname http://%s' % lookup_environment(opts, new_env_id)['CNAME']

    delete_war_from_s3(opts, filename)
    delete_template(opts, template_name)
    print 'Deployment complete'
