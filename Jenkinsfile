def DOCKERHUB_USERNAME = "nesrinedh"

pipeline {
    agent {
        docker {
            image 'node:20'
            // Arguments to pass to the docker run command
            args '-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine'
        }
    }

    stages {
        stage('Frontend - Build & Test') {
            steps {
                // The npm commands now run inside the 'node:20' container
                sh 'npm install'
                sh 'npm run build -- --prod'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                // The docker commands run inside the 'node:20' container
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
