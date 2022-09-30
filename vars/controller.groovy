def pushDockerImage() {
    String imageRepositoryURL = Config.imageRepoSettings.get("image_repository")
    String imageName = Config.imageRepoSettings.get("image_name")
    String defaultTag = Config.imageRepoSettings.get("defaultTag")
    String versionMethod = Config.imageRepoSettings.get("versionMethod")
    String version = "default_version"
    String imageRepository = "${imageRepositoryURL}/${imageName}"
    switch (versionMethod) {
        case "commitID":
            if (env.GIT_COMMIT) {
                version = env.GIT_COMMIT.substring(0, 8)
            }
    }
    container('buildkit') {
        stage('Build Docker Image') {
            sh """
              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepository}:${defaultTag},push=true,registry.insecure=true
              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepository}:${version},push=true,registry.insecure=true
            """
        }
    }
}


def testInMaven() {
    if (!Config.generalSettings.skip_test) {
        container('maven') {
            stage('Run Test') {
                    sh 'mvn -B test'
            }
        }
    }
}

def cloneCode() {
    stage("Get Project") {
        checkout scm
    }
}
