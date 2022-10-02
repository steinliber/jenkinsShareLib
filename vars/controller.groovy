import com.devstream.ci.Pod

def entry() {
    Pod.templates {
      node(POD_LABEL) {
        cloneCode()
        testCode()
        pushCodeImage()
      }
    }
}

def testCode() {
    private testCommand = Config.generalSettings.ci_test_command + ' ' + Config.generalSettings.ci_test_options
    if (!Config.generalSettings.skip_test) {
        container(Config.generalSettings.ci_test_container_name) {
            stage('Run Test') {
                sh testCommand
            }
        }
    }
}


def pushCodeImage() {
    String imageRepositoryURL = Config.imageRepoSettings.get("image_repository")
    String imageName = Config.imageRepoSettings.get("image_name")
    String defaultTag = Config.imageRepoSettings.get("defaultTag")
    String versionMethod = Config.imageRepoSettings.get("versionMethod")
    String version = "default_version"
    String imageRepository = "${imageRepositoryURL}/${imageName}"
    String buildContainerName = Config.imageRepoSettings.ci_build_container_name
    switch (versionMethod) {
        case "commitID":
            if (env.GIT_COMMIT) {
                version = env.GIT_COMMIT.substring(0, 8)
            }
    }
    container(buildContainerName) {
        stage('Build Docker Image') {
            echo "controller: build and push to ${imageRepository}:${defaultTag} and ${imageRepository}:${version}"
            sh """
              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepository}:${defaultTag},push=true,registry.insecure=true
              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepository}:${version},push=true,registry.insecure=true
            """
        }
    }
}


def cloneCode() {
    stage("Get Project") {
        checkout scm
    }
}
