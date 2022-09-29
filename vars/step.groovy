#!/usr/bin/env groovy

def getProject() {
    stage("Get Project") {
        checkout scm
    }
}


def buildDockerImage(String imageAddress="moby/buildkit:master") {
    String imageRepository = Config.imageRepoConfig.get("image_repository")
    String defaultTag = Config.imageRepoConfig.get("defaultTag")
    String versionMethod = Config.imageRepoConfig.get("versionMethod")
    String dockerImageSecretName = Config.imageRepoConfig.get("auth_secret_name")
    String version = "default_version"
    echo "----------> ${dockerImageSecretName}"
    echo "----------> ${versionMethod}"
    echo "----------> ${env.GIT_COMMIT}"
    switch versionMethod {
        case "commitID":
            if env.GIT_COMMIT {
                version = env.GIT_COMMIT.substring(0, 8)
            }
    }
    stage("Build Docker image") {
        if (dockerImageSecretName) {
            podTemplate(containers: [
                    containerTemplate(name: 'buildkit', image: "${imageAddress}", ttyEnabled: true, privileged: true),
            ], volumes: [
                secretVolume(secretName: "${dockerImageSecretName}", mountPath: '/root/.docker')
            ]) {
                node(POD_LABEL) {
                    container('buildkit') {
                        stage('build a Maven project') {
                            sh """
                              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepository}:${defaultTag},push=true,registry.insecure=true
                              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepository}:${version},push=true,registry.insecure=true
                            """
                        }
                    }
                }
            }
        } else {
            podTemplate(containers: [
                    containerTemplate(name: 'buildkit', image: "${imageAddress}", ttyEnabled: true, privileged: true),
            ]) {
                node(POD_LABEL) {
                    container('buildkit') {
                        stage('build a Maven project') {
                            sh """
                              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepository}:${defaultTag},push=true,registry.insecure=true
                              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepository}:${version},push=true,registry.insecure=true
                            """
                        }
                    }
                }
            }

        }
    }
}
