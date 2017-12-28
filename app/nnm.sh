#!/bin/bash

while true
do
    dnsmasq_pid="$(pidof dnsmasq)"
    dnsc_pid="$(pidof dnscrypt-proxy)"
    echo "Dnsmasq pid: $dnsmasq_pid; Dnscrypt pid: $dnsc_pid"

    response="$(dig +short dobrochan.org)"
    echo "Dig response: $response"

    if [[ -z $response ]]
    then
            key="Your api key here"
            data='{
                "to":"Device token here",
                "priority":"high",
                "notification": {
                    "body":"Dns failure!",
                    "title":"Restart of Dnsmasq is required.",
                    "sound":"default"
                }
    }'
            curl -H "Authorization:key=$key" -H "Content-Type: application/json" --data "$data" https://fcm.googleapis.com/fcm/send --silent
    fi
    sleep $((10 * 60))
done