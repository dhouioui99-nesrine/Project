def DOCKERHUB_USERNAME = "nesrinedh"

pipeline {
    // The agent for the entire pipeline is the default one, which is OK
    agent any

    stages {
        stage('Frontend - Build & Test') {
            steps {
                // Now, we will run all commands INSIDE a Docker container.
                docker.image('node:20').inside("-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine") {
                    sh 'npm install'
                    sh 'npm run build -- --prod'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                // Same for this stage, we must run the commands inside a container.
                docker.image('docker:dind').inside("-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine") {
                    sh "docker build -t ${DOCKERHUB_USERNAME}/frontend:latest ."
                }
            }
        }
        
        stage('Push Docker Image') {
            steps {
                docker.image('docker:dind').inside("-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine") {
                    withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                        sh "docker push ${DOCKERHUB_USERNAME}/frontend:latest"
                    }
                }
            }
        }
    }
}
