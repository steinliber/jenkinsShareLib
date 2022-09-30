def configImageRepo(Map imageRepoConfig=[:]) {
    Config.imageRepoSettings = defaultImageRepoSettings() + imageRepoConfig
}

def configGeneral(Map config=[:]) {
    Config.generalSettings = defaultSettings() + config
}

def configNotifyDingDing(String robotID, String atUser) {
    Config.notifySettings = [
        robotID: robotID,
        atUser: atUser,
        notifyType: "dingding",
    ]
}

// config default settings
def defaultSettings() {
    return [
        repo_type: "",
        skip_test: false,
    ]
}

def defaultImageRepoSettings() {
    return [
        image_name: "",
        auth_secret_name: "",
        image_repository: "",
        defaultTag: "latest",
        versionMethod: "commitID",
        maven_image_repo: "maven:3.8.1-jdk-8",
        maven_container_name: "maven",
        buildkit_image_repo: "moby/buildkit:master",
        buildkit_container_name: "buildkit",
    ]
}

return this
