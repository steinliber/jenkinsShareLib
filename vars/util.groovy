#!/usr/bin/env groovy

/* utils.groovy
This package is used for utils func
*/
import com.devstream.notification.Dingtalk
import com.devstream.ci.Git

/*
send notifucation
input params =>
statusMessage: jenkins status message
headMessage: jenkins head message
*/
def notify(String headMessage, String statusMessage) {
    def gitUtils = new Git()
    String changeString = gitUtils.getChangeString()
    switch (Config.notifySettings.notify_type) {
        case "dingding":
            dingtalk = new Dingtalk()
            dingtalk.send(changeString, headMessage, statusMessage)
            break
        default:
            throw new Exception("jenkins notify type ${Config.notifySettings.notifyType} doesn't support")
    }
}


// testTemplate is used for config test container
def testTemplate(Closure body) {
    String imageAddress = Config.generalSettings.ci_test_container_repo
    String containerName = Config.generalSettings.ci_test_container_name
    podTemplate(
        containers: [
            containerTemplate(
              name: containerName,
              image: imageAddress,
              command: 'sleep',
              args: '99d',
              resourceRequestCpu: Config.generalSettings.container_requests_cpu,
              resourceLimitCpu: Config.generalSettings.container_limit_cpu,
              resourceRequestMemory: Config.generalSettings.container_requests_memory,
              resourceLimitMemory: Config.generalSettings.container_limit_memory,
            ),
        ],
    ) {
        body.call()
    }
}

// buildTemplate is used for config build container
def buildTemplate(Closure body) {
    String dockerImageSecretName = Config.imageRepoSettings.get("auth_secret_name")
    String imageAddress = Config.generalSettings.ci_build_container_repo
    String containerName = Config.generalSettings.ci_build_container_name
    if (dockerImageSecretName) {
        podTemplate(containers: [
            containerTemplate(
            name: containerName,
            image: imageAddress,
            ttyEnabled: true,
            privileged: true,
            resourceRequestCpu: Config.generalSettings.container_requests_cpu,
            resourceLimitCpu: Config.generalSettings.container_limit_cpu,
            resourceRequestMemory: Config.generalSettings.container_requests_memory,
            resourceLimitMemory: Config.generalSettings.container_limit_memory,
          ),
        ], volumes: [
            secretVolume(secretName: dockerImageSecretName, mountPath: '/root/.docker')
        ]) {
            body.call()
        }
    } else {
        podTemplate(containers: [
          containerTemplate(
            name: containerName,
            image: imageAddress,
            ttyEnabled: true,
            privileged: true,
            resourceRequestCpu: Config.generalSettings.container_requests_cpu,
            resourceLimitCpu: Config.generalSettings.container_limit_cpu,
            resourceRequestMemory: Config.generalSettings.container_requests_memory,
            resourceLimitMemory: Config.generalSettings.container_limit_memory,
            ),
        ]) {
            body.call()
        }
    }
}

def templates(Closure body) {
    if (!Config.generalSettings.skip_test) {
        testTemplate {
            buildTemplate {
                body.call()
            }
        }
    } else {
        buildTemplate {
            body.call()
        }
    }
}
