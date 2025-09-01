def DOCKERHUB_USERNAME = "nesrinedh"

pipeline {
    agent any

    stages {
        stage('Frontend - Build & Test') {
            steps {
                // Since this Jenkinsfile is in the frontend branch's root,
                // you don't need the 'dir('frontend')' command.
                sh 'npm install'
                sh 'npm run test-headless -- --no-watch --code-coverage'
                sh 'npm run build -- --prod'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKERHUB_USERNAME}/frontend:latest ."
            }
        }
        
        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    sh "docker push ${DOCKERHUB_USERNAME}/frontend:latest"
                }
            }
        }
    }
}
