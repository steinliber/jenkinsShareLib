import com.devstream.ci.Language

def configImageRepo(Map imageRepoConfig=[:]) {
   imageRepoSettings = defaultImageRepoSettings() + imageRepoConfig
   imageRepo = imageRepoSettings.get('image_repo')
   if (!imageRepo) {
       throw new Exception("Please set image_repo in configImageRepo")
   }
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
        ci_test_container_name: "test-container",
        ci_build_container_repo: "moby/buildkit:master",
        ci_build_container_name: "build-container",
    ]
}

def defaultImageRepoSettings() {
    return [
        auth_secret_name: "",
        image_repo: "",
        defaultTag: "latest",
        versionMethod: "commitID",
    ]
}


return this
