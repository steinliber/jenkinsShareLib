def buildUserName(){
    /*
       Requeire 'build user vars' plugin, See https://plugins.jenkins.io/build-user-vars-plugin for more information
    */
    wrap([$class: 'BuildUser']) {
        return BUILD_USER
    }
}
