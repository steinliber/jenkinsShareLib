// def configGeneral(Map args=[:]){
    // defaultSettings()
// }

def configNotify(String robotID, String atUser) {
    Config.notifySettings = [
        robotID: "${robotID}",
        atUser: "${atUser}",
    ]
}

// config default settings
// def defaultSettings() {
    // Config.settings = [
        // image_maven_version: "maven:3.8.6-openjdk-18",
        // image_buildkit_version: "moby/buildkit:master",
        // k8s_docker_config_mount_path: "/root/.docker",
        // k8s_docker_config_mount_name: "docker-config",
    // ]
// }

// return this
