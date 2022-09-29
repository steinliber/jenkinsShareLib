#!/usr/bin/env groovy

def getProject() {
    stage("Get Project") {
        checkout scm
    }
}


def buildDockerImage(String imageAddress="moby/buildkit:master") {
    stage("Build Docker image") {
        String dockerImageSecretName = Config.generalSettings.docker_image_auth_secret_name
        if (dockerImageSecretName != "") {
            podTemplate(containers: [
                    containerTemplate(name: 'buildkit', image: "${imageAddress}", ttyEnabled: true, privileged: true),
            ], volumes: [
                secretVolume(secretName: dockerImageSecretName, mountPath: '/root/.docker')
            ]) {
                node(POD_LABEL) {
                    container('buildkit') {
                        stage('build a Maven project') {
                            sh """
                              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${image},push=true,registry.insecure=true
                              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${repository}:${tag},push=true,registry.insecure=true
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
                              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${image},push=true,registry.insecure=true
                              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${repository}:${tag},push=true,registry.insecure=true
                            """
                        }
                    }
                }
            }

        }
    }
}
