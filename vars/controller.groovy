import com.devstream.scanner.SonarQube
import com.devstream.ci.Git


def testCode() {
    def s = Config.generalSettings
    private testCommand = s.ci_test_command + ' ' + s.ci_test_options
    if (s.test_enable) {
        container(s.ci_test_container_name) {
            stage('Run Test') {
                timeout(time: s.ci_test_timeout_minutes, unit: 'MINUTES') {
                    sh testCommand
                }
            }
        }
    }
}


def pushCodeImage() {
    def s = Config.generalSettings
    String imageRepo = "${Config.imageRepoSettings.image_repo}/${s.name}"
    String defaultTag = Config.imageRepoSettings.get("defaultTag")
    String versionMethod = Config.imageRepoSettings.get("versionMethod")
    String version = "default_version"
    String buildContainerName = s.ci_build_container_name
    switch (versionMethod) {
        case "commitID":
            gitUtil = new Git()
            version = gitUtil.getCommitIDHead()
    }
    container(buildContainerName) {
        stage('Build Docker Image') {
            echo "controller: build and push to ${imageRepo}:${defaultTag} and ${imageRepo}:${version}"
            timeout(time: s.ci_build_timeout_minutes, unit: 'MINUTES') {
                sh """
                  buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepo}:${defaultTag},push=true,registry.insecure=true
                  buildctl build --frontend dockerfile.v0 --local context=. --local dockerfile=. --output type=image,name=${imageRepo}:${version},push=true,registry.insecure=true
                """
            }
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
    if (s.sonarqube_enable) {
        container(s.sonarqube_cli_container_name) {
            stage('Sonar Scan Code') {
                def sonar = new SonarQube()
                sonar.scanner(
                    s.name,
                    s.language,
                    s.sonarqube_options,
                )
                /*
                This config has to config sonarqube, refer to https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-jenkins/
                */
                // if (s.sonarqube_qualitygate_enable) {
                    // sonar.qualityGateStatus()
                // }
            }
        }
    }
}
