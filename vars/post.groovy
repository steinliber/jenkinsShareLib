#!/usr/bin/env groovy

def success() {
    if(Config.notifySettings) {
        String successHeadMsg = "✅✅✅✅✅✅✅✅✅"
        String successStatusMsg = "构建成功✅"
        notify.send(successHeadMsg, successStatusMsg)
    }
}

def failure() {
    if(Config.notifySettings) {
        String failureHeadMsg = "❌❌❌❌❌❌❌❌❌"
        String failureStatusMsg = "构建失败❌"
        notify.send(failureHeadMsg, failureStatusMsg)
    }
}

def aborted() {
    if(Config.notifySettings) {
        String failureHeadMsg = "🟠🟠🟠🟠🟠🟠🟠"
        String failureStatusMsg = "构建中断🟠"
        notify.send(failureHeadMsg, failureStatusMsg)
    }
}
