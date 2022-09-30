#!/usr/bin/env groovy

def testInMaven(String imageAddress="maven:3.8.1-jdk-8") {
    if (!Config.generalSettings.skip_test) {
        pod(
            container('maven') {
                stage('run mvn test') {
                    checkout scm
                    sh 'mvn -B test'
                }
            }
        )
    }
}
