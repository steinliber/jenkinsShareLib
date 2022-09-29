def configImageRepo(Map imageRepoConfig=[:]) {
    Config.imageRepoConfig = defaultImageRepoSettings() + imageRepoConfig
}

def configGeneral(Map congfig=[:]) {
    defaultImageRepoSettings()
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
    Config.generalSettings  = [
        repo_type: "",
    ]
    Config.stepSettings = [
        skip_test: false,
    ]
}

def defaultImageRepoSettings() {
    return [
        auth_secret_name: "",
        image_repository: "",
        defaultTag: "latest",
        versionMethod: "commitID"
    ]
}

return this
