def DOCKERHUB_USERNAME = "nesrinedh"

pipeline {
    agent any

    stages {
        stage('Backend - Build & Test') {
            steps {
                // Since this Jenkinsfile is in the backend branch's root,
                // you don't need the 'dir('backend')' command.
                sh './mvnw clean install'
            }
        }

        stage('Backend - SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube Server') {
                    sh './mvnw sonar:sonar'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKERHUB_USERNAME}/backend:latest ."
            }
        }
        
        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    sh "docker push ${DOCKERHUB_USERNAME}/backend:latest"
                }
            }
        }
    }
}
