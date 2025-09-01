def DOCKERHUB_USERNAME = "nesrinedh"

pipeline {
    agent {
        docker {
            image 'docker:dind'
            args '-v //./pipe/dockerDesktopEngine://./pipe/docker.sock'
        }
    }

    stages {
        stage('Frontend - Build & Test') {
            steps {
                // The git checkout will happen automatically in this agent
                // We'll run the build inside a separate node container
                sh 'docker run --rm -v ${pwd()}:/app -w /app node:20 /bin/bash -c "npm install && npm run build -- --prod"'
            }
        }
        
        stage('Build & Push Docker Image') {
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
