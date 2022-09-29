def configGeneral(Map args=[:]){
    defaultSettings()
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
        docker_image_auth_secret_name: "",
    ]
    Config.stepSettings = [
        skip_test: false,
    ]
}

return this
