pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr:'50'))
    disableConcurrentBuilds()
    timeout(time: 30, unit: 'MINUTES')
  }
  stages {
    stage ('Initialize') {
      steps {
        script {
          checkout scm
        }
      }
    }
    stage ('Compile') {
      steps {
        sh 'mvn clean compile'
      }
    }
    stage ('Test') {
      steps {
        sh 'mvn test'
      }
    }
    stage ('Package') {
      steps {
        sh 'mvn package -DskipTests'
        junit testResults: '**/target/surefire-reports/*.xml', skipPublishingChecks: true
      }
    }
  }
  post {
    success {
      archiveArtifacts artifacts: '*/target/*.jar,', fingerprint: true
    }
  }
}
