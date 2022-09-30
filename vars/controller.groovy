def entry() {
	pod.withContainers({
		stage('Get Project') {
            checkout scm
        }
        stage('Run Maven test') {
			container('maven') {
				stage('run mvn test') {
					sh 'mvn -B test'
				}
			}
        }
	})
}
