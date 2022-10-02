import com.devstream.ci.Language

def configImageRepo(Map imageRepoConfig=[:]) {
    Config.imageRepoSettings = defaultImageRepoSettings() + imageRepoConfig
}

def configGeneral(Map config=[:]) {
    defaultConfig = defaultSettings()
    language = config['language']
    if (!language) {
        language = defaultConfig['language']
    }
    languageConfig = new Language()
    languageDefaultConfig = languageConfig.selector(language)
    Config.generalSettings = defaultConfig + languageDefaultConfig + config
}

def configNotifyDingDing(Map notifyConfig=[:]) {
    notifyConfig['notify_type'] = 'dingding'
    if (notifyConfig.containsKey("at_user")) {
        notifyConfig['at_user'] = ""
    }
    Config.notifySettings = notifyConfig
}

// config default settings
def defaultSettings() {
    return [
        repo_type: "",
        language: "java",
        // container resource for podTemplate
        container_requests_cpu: "0.3",
        container_requests_memory: "512Mi",
        container_limit_cpu: "1",
        container_limit_memory: "2Gi",
        // ci related config
        skip_test: false,
        ci_test_command: "",
        ci_test_options: "",
        ci_test_container_repo: "",
        ci_test_container_name: "testContainer",
        ci_build_container_repo: "moby/buildkit:master",
        ci_build_container_name: "buildContainer",
    ]
}

def defaultImageRepoSettings() {
    return [
        image_name: "",
        auth_secret_name: "",
        image_repository: "",
        defaultTag: "latest",
        versionMethod: "commitID",
    ]
}


return this
