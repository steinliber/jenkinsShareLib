package com.devstream.ci

def seletor(String language){
    // config default options for different language
    switch(language.toLowerCase()){
        case "java":
            setJavaDefault()
            break
        default:
            if (!Config.generalSettings.ci_test_command) {
                throw new Exception("Language %s language should set ci_test_command and ci_test_options in generalSettings")
            }
    }
}


def setJavaDefault(String b_file=null){
    log.info 'Preparing to build ' + Config.generalSettings.language.toUpperCase() + ' project.'
    if (!Config.generalSettings.ci_test_command) {
        Config.generalSettings.ci_test_command = "mvn"
    }
    if (!Config.generalSettings.ci_test_options) {
        Config.generalSettings.ci_test_options = "-B test"
    }
    if (!Config.generalSettings.ci_test_container_repo) {
        Config.generalSettings.ci_test_container_repo = "maven:3.8.1-jdk-8"
    }
    // set java container resource
    Config.generalSettings.container_requests_cpu = "1"
    Config.generalSettings.container_requests_memory = "2Gi"
    Config.generalSettings.container_limit_cpu = "1"
    Config.generalSettings.container_limit_memory = "2Gi"
}
