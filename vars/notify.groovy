#!/usr/bin/env groovy

/* notify.groovy
This package is used for dingding notification in jenkins pipeline
*/

def getChangeString() {
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
def dingding(String statusMessage, String headMessage, Integer _timeout=60){
    String changeString = getChangeString()
    String buildUser = variable.buildUserName()
    String notifyUser = Config.notifySettings.atUser
    String robotID = Config.notifySettings.robotID
    List<String> atUsers = [] as String[]
    if (notifyUser != null && notifyUser != "") {
        atUsers = notifyUser.split(",") as String[]
    }
    timeout(time: _timeout, unit: 'SECONDS') {
             dingtalk (
              robot: '${robotID}',
              type: 'MARKDOWN',
              title: "${env.JOB_NAME}[${env.BRANCH_NAME}]构建通知",
              text: [
                  "# $headMessage",
                  "# 构建详情",
                  "- 构建变更: ${changeString}",
                  "- 构建结果: ${statusMessage}",
                  "- 构建人: **${buildUser}**",
                  "- 持续时间: ${currentBuild.durationString}",
                  "- 构建日志: [日志](${env.BUILD_URL}console)",
                  "# Jenkins链接",
                  "[应用Jenkins地址](${env.JOB_URL})"
              ],
              at: atUsers
            )
    }
}
