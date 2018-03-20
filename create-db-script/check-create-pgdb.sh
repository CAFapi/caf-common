#!/bin/bash
# ----------Variable Section-----------#
#Dummy values (come from environment vars)
#CAF_DATABASE=
#CAF_DATABASE_HOST=
#CAF_DATABASE_PASSWORD=
#CAF_DATABASE_PORT=
#CAF_DATABASE_USERNAME=
#ENV_PREFIX="CAF_"

tmpDir="/tmp"
scriptName=$(basename "$0")
extension=$([[ "$scriptName" = *.* ]] && echo ".${scriptName##*.}" || echo '')
baseName="${scriptName%.*}"
tmpErr=$tmpDir/$baseName"-stderr"

# Should arrive from environment definition.
# All database related variables will begin with it
#if [ -z $ENV_PREFIX ] ; then
#  ENV_PREFIX="CAF_"
#fi

# Need to convert prefixed variables to known values:
varName="$ENV_PREFIX"DATABASE
database=$(echo ${!varName})

# Or like this:
#database=$(eval echo \$$(echo $env_prefix"_DATABASE"))

varName="$ENV_PREFIX"DATABASE_HOST
datasource_host=$(echo ${!varName})

varName="$ENV_PREFIX"DATABASE_PORT
datasource_port=$(echo ${!varName})

varName="$ENV_PREFIX"DATABASE_USERNAME
datasource_user=$(echo ${!varName})

varName="$ENV_PREFIX"DATABASE_PASSWORD
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
    echo "Missing "$(echo $ENV_PREFIX"DATABASE")
    missingVar+=1
  fi

  if [ -z $datasource_host ] ; then
    echo "Missing "$(echo $ENV_PREFIX"DATABASE_HOST")
    missingVar+=1
  fi

  if [ -z $datasource_port ] ; then
    echo "Missing "$(echo $ENV_PREFIX"DATABASE_PORT")
    missingVar+=1
  fi

  if [ -z $datasource_user ] ; then
    echo "Missing "$(echo $ENV_PREFIX"DATABASE_USERNAME")
    missingVar+=1
  fi

  if [ -z $datasource_password ] ; then
    echo "Missing "$(echo $ENV_PREFIX"DATABASE_PASSWORD")
    missingVar+=1
  fi

  if [ $missingVar -gt 0 ] ; then
    echo "Not all required variables defined, exiting."
    echo "HINT: If the ENV_PREFIX variable is provided, expected database parameters will be constructed with it."
    exit 1
  fi
}

function check_db_exist {
  echo "Checking database existence..."

# Need to set password for run
# Sending psql errors to file, using quiet grep to search for valid result
 if  PGPASSWORD="$datasource_password" \
   psql --username="$datasource_user" \
   --host="$datasource_host" \
   --port="$datasource_port" \
   --tuples-only \
   --command="SELECT 1 FROM pg_database WHERE datname = '$database'" \
   2>$tmpErr | grep -q 1 \
 ; then
   echo "Database [$database] exists."
   exit 0
 else
   if [ -f "$tmpErr" ] && [ -s "$tmpErr" ] ; then
     echo "Database connection error, exiting."
     cat "$tmpErr"
     exit 1
   else
     echo "Database [$database] does not exist, creating..."
     create_db
   fi
 fi
}

function create_db {
# Need to set password for run
# Sending psql errors to file, stdout to NULL
# postgres will auto-lowercase database names unless they are quoted
  if  PGPASSWORD="$datasource_password" \
   psql --username="$datasource_user" \
   --host="$datasource_host" \
   --port="$datasource_port" \
   --command="CREATE DATABASE \"$database\"" \
   >/dev/null 2>$tmpErr \
  ; then
    echo "Database [$database] created."
  else
     echo "Database creation error, exiting."
     cat "$tmpErr"
     exit 1
  fi
}

# -------Main Execution Section--------#

check_variables
check_psql
check_db_exist