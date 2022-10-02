def call() {
    pipeline {
      agent any
      stages {
        stage("Run Pipeline") {
          steps {
            script {
              echo "Start Run Pipeline..."
              controller.entry()
            }
          }
        }
      }
      post {
        failure {
          script {
            post.failure()
          }
        }
        success {
          script {
            post.success()
          }
        }
        aborted {
          script {
            post.aborted()
          }
        }
      }
    }
}
