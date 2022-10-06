#!/usr/bin/env groovy

def postResult(String status) {
    node {
        stage("Post Result") {
            steps {
                switch status {
                    case "success":
                        success()
                        break
                    case "failure":
                        failure()
                        break
                    case "aborted":
                        aborted()
                        break
                }
            }
        }
    }
}

def success() {
    // config notify
    if(Config.notifySettings) {
        String successHeadMsg = "✅✅✅✅✅✅✅✅✅"
        String successStatusMsg = "构建成功✅"
        util.notify(successHeadMsg, successStatusMsg)
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
        util.notify(failureHeadMsg, failureStatusMsg)
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
        util.notify(failureHeadMsg, failureStatusMsg)
    }
    // config gitlab
    def repoType = Config.generalSettings.get("repo_type")
    if (repoType && repoType == "gitlab") {
        updateGitlabCommitStatus name: 'build', state: 'canceled'
    }
}
