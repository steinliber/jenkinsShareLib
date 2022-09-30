#!/usr/bin/env groovy

/* notify.groovy
This package is used for dingding notification in jenkins pipeline
*/
import com.devstream.notification.Dingtalk
import com.devstream.git.Utils

/*
send notifucation
input params =>
statusMessage: jenkins status message
headMessage: jenkins head message
*/
def notify(String headMessage, String statusMessage) {
    def gitUtils = new Utils()
    String changeString = gitUtils.getChangeString()
    switch (Config.notifySettings.notifyType) {
        case "dingding":
            dingtalk = new Dingtalk()
            dingtalk.send(changeString, headMessage, statusMessage)
            break
        default:
            throw new Exception("jenkins notify type ${Config.notifySettings.notifyType} doesn't support")
    }
}
