package com.devstream.scanner

def scanner(
    String name,
    String version,
    String lang,
    String options='') {
    try {
        log.i 'Preparing SonarQube Scanner'
        withSonarQubeEnv(){
            def private opts

            opts  = ' -Dsonar.projectKey='       + name
            opts += ' -Dsonar.projectName='      + name
            opts += ' -Dsonar.projectVersion='   + version
            opts += ' -Dsonar.language='         + lang
            opts += ' -Dsonar.projectBaseDir=.'
            opts += ' -Dsonar.sources=.'
            opts += ' -Dsonar.java.binaries=.'
            sonar_exec  = cmd + opts + ' ' + options

            sh(sonar_exec)
        }
    }
    catch (e) {
        println('Errror: Failed with SonarQube Scanner')
        throw e
    }
}

def qualityGateStatus(){
    try {
        timeout(time: 20, unit: 'MINUTES') {
            def qg_stats = waitForQualityGate()
            if (qg_stats.status != 'SUCCESS') {
                println('Error: Pipeline aborted due to quality gate failure: ' + qg.stats)
                error "Pipeline aborted due to quality gate failure: ${qg.status}"
            }
        }
    }
    catch (e) {
        throw e
    }
}