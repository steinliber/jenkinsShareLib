#!/usr/bin/env groovy

/* notifyDingDing.groovy
This package is used for dingding notification in jenkins pipeline
input params is
robotID: the rebot id created by jenkins config
folder: jenkins folder name
jobName: jenkins job name
branch: repo branch
jenkinsURL: jenkins address in repo
notifyUser: notify users in dingding
statusMessage: dingding status message
headMessage: dingding head message
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

def dingding(String robotID, String folder, String jobName, String branch, String jenkinsURL, String notifyUser, String statusMessage, String headMessage){
    String changeString = getChangeString()
    echo "${changeString}"
    List<String> atUsers = [] as String[]
    if (notifyUser != null && notifyUser != "") {
        atUsers = notifyUser.split(",") as String[]
    }
    wrap([$class: 'BuildUser']){
         dingtalk (
          robot: "${robotID}",
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
