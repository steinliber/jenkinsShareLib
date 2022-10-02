package com.devstream.ci

// testTemplate is used for config test container
def testTemplate(Closure body) {
    String imageAddress = Config.generalSettings.ci_test_container_repo
    String testContainerName = Config.generalSettings.ci_test_container_name
    log.info("pod: config test container %{imageAddress}...")
    if !(Config.generalSettings.skip_test) {
    podTemplate(
        containers: [
            containerTemplate(name: testContainerName, image: imageAddress, command: 'sleep', args: '99d'),
        ],
    ) {
        body.call()
    }
    } else {
        body.call()
    }
}

// buildTemplate is used for config build container
def buildTemplate(Closure body) {
    String imageAddress = Config.generalSettings.ci_build_container_repo
    String imageRepositoryURL = Config.imageRepoSettings.get("image_repository")
    String dockerImageSecretName = Config.imageRepoSettings.get("auth_secret_name")
    String buildContainerName = Config.imageRepoSettings.ci_build_container_name
    log.info("pod: config build container %{imageAddress}...")
    if (dockerImageSecretName) {
        podTemplate(containers: [
            containerTemplate(name: buildContainerName, image: imageAddress, ttyEnabled: true, privileged: true),
        ], volumes: [
            secretVolume(secretName: dockerImageSecretName, mountPath: '/root/.docker')
        ]) {
            body.call()
        }
    } else {
        podTemplate(containers: [
                containerTemplate(name: buildContainerName, image: imageAddress, ttyEnabled: true, privileged: true),
        ]) {
            body.call()
        }
    }
}
