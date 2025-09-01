def DOCKERHUB_USERNAME = "nesrinedh"

pipeline {
    agent any

    stages {
        stage('Frontend - Build & Test') {
            steps {
                docker.image('node:20').inside(args: "-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine") {
                    sh 'npm install'
                    sh 'npm run build -- --prod'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                docker.image('docker:dind').inside(args: "-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine") {
                    sh "docker build -t ${DOCKERHUB_USERNAME}/frontend:latest ."
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                docker.image('docker:dind').inside(args: "-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine") {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                        sh "docker push ${DOCKERHUB_USERNAME}/frontend:latest"
                    }
                }
            }
        }
    }
}
