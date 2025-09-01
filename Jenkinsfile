def DOCKERHUB_USERNAME = "nesrinedh"

pipeline {
    agent none 

    stages {
        stage('Frontend - Build & Test') {
            agent {
                docker {
                    image 'node:20'
                }
            }
            steps {
                sh 'npm install'
                sh 'npm run build -- --prod'
            }
        }
        
        stage('Build & Push Docker Image') {
            agent {
                docker {
                    image 'docker:dind'
                    args '-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine'
                }
            }
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    sh "docker build -t ${DOCKERHUB_USERNAME}/frontend:latest ."
                    sh "docker push ${DOCKERHUB_USERNAME}/frontend:latest"
                }
            }
        }
    }
}
