pipeline {
    agent any
    tools {
        jdk 'graalvm'
    }
    stages {
        stage('Build') {
            steps {
                sh './mvnw -V clean install -DskipTests -DskipITs -DskipDocs'
            }
        }
    }
    post {
        always {
            cleanWs()
        }
    }
}