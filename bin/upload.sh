#!/usr/bin/env bash

source bin/includes.sh

if [[ $# -ne 2 ]]; then
    echo "ERROR: Required 2 arguments but passed $#!" >&2
    exit 1
fi

NEXT_vVERSION=$1
UPLOAD_FILE=$2

if ! [[ -f "${UPLOAD_FILE}" ]] ; then
    echo "ERROR: upload file does not exist at '${UPLOAD_FILE}'" >&2
    exit 1
fi

if [[ -z "${GITHUB_TOKEN}" ]]; then
    echo "ERROR: Setting the environment variable GITHUB_TOKEN is mandatory." >&2
    exit 1
fi

if ! [[ -x "$(command -v jq)" ]]; then
  echo "ERROR: jq is not installed." >&2
  exit 1
fi

echo "Next version: [${NEXT_vVERSION}]"
echo "Upload file: [${UPLOAD_FILE}]"
echo
echo "Creating new release in GitHub ..."

safeEval "curl \
    --header \"Authorization: token ${GITHUB_TOKEN}\" \
    --header \"Content-Type: application/json\" \
    --request POST \
    --data '{ \"tag_name\": \"${NEXT_vVERSION}\", \"target_commitish\": \"master\", \"name\": \"${NEXT_vVERSION}\", \"body\": \"DerBauer 2 Release\", \"draft\": false, \"prerelease\": false }' \
    https://api.github.com/repos/christophpickl/derbauer/releases"
UPLOAD_URL=`echo ${LAST_RESULT} | jq -r '.upload_url' | sed -e 's/{?name,label}//g'`

echo ""
echo "Uploading asset to GitHub ..."
safeEval "curl \
    --header \"Authorization: token ${GITHUB_TOKEN}\" \
    --header \"Content-Type: application/jar\" \
    --request POST \
    --data-binary @${UPLOAD_FILE} \
    $UPLOAD_URL?name=DerBauer2-${NEXT_vVERSION}.jar"