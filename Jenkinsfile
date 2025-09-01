def DOCKERHUB_USERNAME = "nesrinedh"

pipeline {
    // We'll use a single 'agent any' and then specify a Docker agent for each stage that needs it.
    // This provides more control.
    agent any

    stages {
        stage('Frontend - Build & Test') {
            agent {
                docker {
                    // Use a node image for the npm commands
                    image 'node:20'
                }
            }
            steps {
                sh 'npm install'
                sh 'npm run build -- --prod'
            }
        }
        
        stage('Build Docker Image') {
            agent {
                docker {
                    image 'docker:dind'
                    // This is the crucial part that passes the host's Docker socket to the container
                    args '-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine'
                }
            }
            steps {
                sh "docker build -t ${DOCKERHUB_USERNAME}/frontend:latest ."
            }
        }
        
        stage('Push Docker Image') {
            agent {
                docker {
                    image 'docker:dind'
                    // The same argument is needed for the push command
                    args '-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine'
                }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    sh "docker push ${DOCKERHUB_USERNAME}/frontend:latest"
                }
            }
        }
    }
}
