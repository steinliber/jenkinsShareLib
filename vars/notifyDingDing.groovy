#!/usr/bin/env groovy

/* notifyDingDing.groovy
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

def call(String robotID, String notifyUser, String jenkinsURL, String branch, String jobName, String folder, String statusMessage, String headMessage){
    String changeString = getChangeString()
    echo "${changeString}"
    String[] atUsers = [] as List
    if (notifyUser != null && notifyUser != "") {
        atUsers = notifyUser.split(",") as List
    }
    wrap([$class: 'BuildUser']){
         dingtalk (
          robot: '${robotID}',
          type: 'MARKDOWN',
          title: "${jobName}[${branch}]构建通知",
          text: [
              "# $headMessage",
              "# 构建详情",
              "- 构建变更: ${changeString}",
              "- 构建结果: ${statusMessage}",
              "- 构建人: **${env.BUILD_USER}**",
              "- 持续时间: ${currentBuild.durationString}",
              "- 构建日志: [日志](${env.BUILD_URL}console)",
              "# Jenkins链接",
              "[应用Jenkins地址](${jenkinsURL}/${folder}/job/${jobName}/)"

          ],
          at: atUsers
        )
    }
}
