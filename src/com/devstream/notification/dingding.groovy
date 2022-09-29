#!/usr/bin/env groovy
package com.devstream.notification

/* dingding.groovy
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

def notice(String statusMessage, String headMessage){
    String changeString = getChangeString()
    wrap([$class: 'BuildUser']){
         dingtalk (
          robot: '${Config.notifySettings.robotID}',
          type: 'MARKDOWN',
          title: "${env.JOB_NAME}[${env.BRANCH_NAME}]构建通知",
          text: [
              "# $headMessage",
              "# 构建详情",
              "- 构建变更: ${changeString}"
              "- 构建结果:${statusMessage}",
              "- 构建人: **${env.BUILD_USER}**",
              "- 持续时间: ${currentBuild.durationString}",
              "- 构建日志: [日志](${env.BUILD_URL}console)",
              "# Jenkins链接",
              "[应用Jenkins地址](${env.JOB_URL})"

          ],
          at: ["${Config.notifySettings.atUser}"]
        )
    }
}
