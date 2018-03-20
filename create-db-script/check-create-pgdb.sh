#!/bin/bash
# ----------Variable Section-----------#
#Dummy values (come from environment vars)
CAF_ANALYTICS_DATABASE=aspen
#CAF_ANALYTICS_DATASOURCE_HOST=moe-pdb01.hpeswlab.net
CAF_ANALYTICS_DATASOURCE_PASSWORD=postgres
CAF_ANALYTICS_DATASOURCE_PORT=5432
#CAF_ANALYTICS_DATASOURCE_SCHEMA=aspen_analytics
#CAF_ANALYTICS_DATASOURCE_USERNAME=postgres

# Should arrive from environment definition.
# All database related variables will begin with it
if [ -z $env_prefix ] ; then
  env_prefix="CAF_ANALYTICS"
fi

# Need to convert prefixed variables to known values:
varName="$env_prefix"_DATABASE
database=$(echo ${!varName})

# Or like this:
#database=$(eval echo \$$(echo $env_prefix"_DATABASE"))

varName="$env_prefix"_DATASOURCE_HOST
datasource_host=$(echo ${!varName})

varName="$env_prefix"_DATASOURCE_PORT
datasource_port=$(echo ${!varName})

varName="$env_prefix"_DATASOURCE_USERNAME
datasource_user=$(echo ${!varName})

varName="$env_prefix"_DATASOURCE_PASSWORD
datasource_password=$(echo ${!varName})

# ----------Function Section-----------#
function check_psql {
  if [ $(type -p psql) ]; then
      _psql=$(type -p psql)
  else
      echo "Install psql (to the system path) before this script can be used."
      exit 1
  fi

  if [[ "$_psql" ]]; then
    version=$("$_psql" --version 2>&1 | awk '{print $3}')
    echo "psql $version found, OK to continue"
  fi
}

function check_variables {
  local -i missingVar=0

  if [ -z $database ] ; then
    echo "Missing "$(echo $env_prefix"_DATABASE")
    missingVar+=1
  fi

  if [ -z $datasource_host ] ; then
    echo "Missing "$(echo $env_prefix"_DATASOURCE_HOST")
    missingVar+=1
  fi

  if [ -z $datasource_port ] ; then
    echo "Missing "$(echo $env_prefix"_DATASOURCE_PORT")
    missingVar+=1
  fi

  if [ -z $datasource_user ] ; then
    echo "Missing "$(echo $env_prefix"_DATASOURCE_USERNAME")
    missingVar+=1
  fi

  if [ -z $datasource_password ] ; then
    echo "Missing "$(echo $env_prefix"_DATASOURCE_PASSWORD")
    missingVar+=1
  fi

  if [ $missingVar -gt 0 ] ; then
    echo "Not all required variables defined, exiting."
  exit 1
  fi
}

# -------Main Execution Section--------#

check_variables
check_psql
