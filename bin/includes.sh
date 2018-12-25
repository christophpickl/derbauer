#!/usr/bin/env bash

LAST_RESULT=""
safeEval() {
    COMMAND=$1
    echo ""
    echo ">> $COMMAND"
    LAST_RESULT=`eval ${COMMAND}`
    if [[ $? -ne 0 ]] ; then
        echo "Last command did not end successful!"
        osascript -e 'display notification "Build failed 😢" with title "DerBauer2 Build"'
        exit 1
    fi
}
