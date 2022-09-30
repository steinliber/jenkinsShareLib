#!/usr/bin/env groovy

/* notify.groovy
This package is used for dingding notification in jenkins pipeline
*/
import com.devstream.notification.Dingtalk

def _getChangeString() {
    def changeString = ""
    def MAX_MSG_LEN = 10
    def changeLogSets = currentBuild.changeSets
    for (int i = 0; i < changeLogSets.size(); i++) {
        def entries = changeLogSets[i].items
        for (int j = 0; j < entries.length; j++) {
            def entry = entries[j]
            truncatedMsg = entry.msg.take(MAX_MSG_LEN)
            commitTime = new Date(entry.timestamp).format("yyyy-MM-dd HH:mm:ss")
            changeString += " - ${truncatedMsg} [${entry.author} ${commitTime}]\n"
        }
    }
    if (!changeString) {
        changeString = " - No new changes"
    }
    return (changeString)
}
/*
send notifucation to dingding
input params =>
statusMessage: dingding status message
headMessage: dingding head message
*/
def send(String headMessage, String statusMessage) {
    String changeString = _getChangeString()
    switch (Config.notifySettings.notifyType) {
        case "dingding":
            dingtalk = new Dingtalk()
            dingtalk.send(changeString, headMessage, statusMessage)
            break
        default:
            throw new Exception("jenkins notify type ${Config.notifySettings.notifyType} doesn't support")
    }
}
