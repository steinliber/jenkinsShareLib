import com.devstream.scanner.SonarQube
import com.devstream.ci.Git


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
    String imageRepo = "${Config.imageRepoSettings.image_repo}/${Config.generalSettings.name}"
    String defaultTag = Config.imageRepoSettings.get("defaultTag")
    String versionMethod = Config.imageRepoSettings.get("versionMethod")
    String version = "default_version"
    String buildContainerName = Config.generalSettings.ci_build_container_name
    switch (versionMethod) {
        case "commitID":
            gitUtil = new Git()
            version = gitUtil.getCommitIDHead()
    }
    container(buildContainerName) {
        stage('Build Docker Image') {
            echo "controller: build and push to ${imageRepo}:${defaultTag} and ${imageRepo}:${version}"
            sh """
              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepo}:${defaultTag},push=true,registry.insecure=true
              buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepo}:${version},push=true,registry.insecure=true
            """
        }
    }
}


def cloneCode() {
    stage("Get Project") {
        checkout scm
    }
}

def sonarScan() {
    def s = Config.generalSettings
    println("------------> ${s}")
    if (s.sonarqube_enable) {
        def sonar = new SonarQube()
        sonar.scanner(
            s.name,
            s.language,
            s.sonarqube_options,
        )
        if (s.sonarqube_qualitygate_enable) {
            sonar.qualityGateStatus()
        }
    }
}
