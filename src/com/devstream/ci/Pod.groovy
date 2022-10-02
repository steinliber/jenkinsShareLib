package com.devstream.ci

// testTemplate is used for config test container
def testTemplate(Closure body) {
    String imageAddress = Config.generalSettings.ci_test_container_repo
    String containerName = Config.generalSettings.ci_test_container_name
    println("----->${containerName} ${imageAddress}")
    if (!Config.generalSettings.skip_test) {
podTemplate(
    containers: [
        containerTemplate(name: containerName, image: imageAddress, command: 'sleep', args: '99d'),
    ],
) {
    body.call()
}
    }
}

// buildTemplate is used for config build container
def buildTemplate(Closure body) {
    String imageRepositoryURL = Config.imageRepoSettings.get("image_repository")
    String dockerImageSecretName = Config.imageRepoSettings.get("auth_secret_name")
    String imageAddress = Config.generalSettings.ci_build_container_repo
    String containerName = Config.generalSettings.ci_build_container_name
    if (dockerImageSecretName) {
        podTemplate(containers: [
            containerTemplate(name: containerName, image: imageAddress, ttyEnabled: true, privileged: true),
        ], volumes: [
            secretVolume(secretName: dockerImageSecretName, mountPath: '/root/.docker')
        ]) {
            body.call()
        }
    } else {
        podTemplate(containers: [
                containerTemplate(name: containerName, image: imageAddress, ttyEnabled: true, privileged: true),
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
