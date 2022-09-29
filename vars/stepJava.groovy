#!/usr/bin/env groovy

def testInMaven(String imageAddress="maven:3.8.1-jdk-8") {
    if (!Config.stepSettings.skip_test) {
        stage('Run Maven Test') {
            agent {
                kubernetes {
                  containerTemplate {
                    name 'maven'
                    image imageAddress
                    command 'sleep'
                    args '99d'
                  }
                }
            }
            sh 'mvn -B test'
        }
    }
}
