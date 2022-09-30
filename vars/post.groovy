#!/usr/bin/env groovy

def success() {
    // config notify
    if(Config.notifySettings) {
        String successHeadMsg = "✅✅✅✅✅✅✅✅✅"
        String successStatusMsg = "构建成功✅"
        notify.send(successHeadMsg, successStatusMsg)
    }
    // config gitlab
    def repoType = Config.generalSettings.get("repo_type")
    if (repoType && repoType == "gitlab") {
        updateGitlabCommitStatus name: 'build', state: 'success'
    }
}

def failure() {
    // config notify
    if(Config.notifySettings) {
        String failureHeadMsg = "❌❌❌❌❌❌❌❌❌"
        String failureStatusMsg = "构建失败❌"
        notify.send(failureHeadMsg, failureStatusMsg)
    }
    // config gitlab
    def repoType = Config.generalSettings.get("repo_type")
    if (repoType && repoType == "gitlab") {
        updateGitlabCommitStatus name: 'build', state: 'failed'
    }
}

def aborted() {
    // config notify
    if(Config.notifySettings) {
        String failureHeadMsg = "🟠🟠🟠🟠🟠🟠🟠"
        String failureStatusMsg = "构建中断🟠"
        notify.send(failureHeadMsg, failureStatusMsg)
    }
    // config gitlab
    def repoType = Config.generalSettings.get("repo_type")
    if (repoType && repoType == "gitlab") {
        updateGitlabCommitStatus name: 'build', state: 'canceled'
    }
}
