def mavenTemplate(Closure body) {
    String imageAddress = Config.imageRepoSettings.get("maven_image_repo")
    String mavenContainerName = Config.imageRepoSettings.get("maven_container_name")
    podTemplate(
        containerTemplate(name: 'maven', image: imageAddress, command: 'sleep', args: '99d'),
    ) {
    body.call()
}
}

def buildkitTemplate(Closure body) {
    String imageAddress = Config.imageRepoSettings.get("buildkit_image_repo")
    String imageRepositoryURL = Config.imageRepoSettings.get("image_repository")
    String dockerImageSecretName = Config.imageRepoSettings.get("auth_secret_name")
    String buildkitContainerName = Config.imageRepoSettings.get("buildkit_container_name")
    if (dockerImageSecretName) {
        podTemplate(containers: [
                containerTemplate(name: 'buildkit', image: "${imageAddress}", ttyEnabled: true, privileged: true),
        ], volumes: [
            secretVolume(secretName: "${dockerImageSecretName}", mountPath: '/root/.docker')
        ]) {
            body.call()
        }
    } else {
        podTemplate(containers: [
                containerTemplate(name: 'buildkit', image: "${imageAddress}", ttyEnabled: true, privileged: true),
        ]) {
            body.call()
        }
    }
}
