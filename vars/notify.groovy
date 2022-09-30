#!/usr/bin/env groovy

/* notify.groovy
This package is used for dingding notification in jenkins pipeline
*/
import com.devstream.notification.Dingtalk

/*
send notifucation to dingding
input params =>
statusMessage: dingding status message
headMessage: dingding head message
*/
def send(String headMessage, String statusMessage) {
    switch (Config.notifySettings.notifyType) {
        case "dingding":
            dingtalk = new Dingtalk()
            dingtalk.send(headMessage, statusMessage)
            break
        default:
            throw new Exception("jenkins notify type ${Config.notifySettings.notifyType} doesn't support")
    }
}
