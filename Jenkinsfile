def DOCKERHUB_USERNAME = "nesrinedh"

pipeline {
    // Force the entire pipeline to run inside a single container that has Docker and Node
    agent {
        docker {
            // This image is based on alpine and has both docker and node installed
            image 'docker:20.10-dind-rootless'
            // Mount the Docker socket from the host to the container for access
            args "-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine"
        }
    }

    stages {
        stage('Frontend - Build & Test') {
            steps {
                // Since npm is installed in the docker agent, it will work
                sh 'npm install'
                sh 'npm run build -- --prod'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                // Use the docker client inside the container to build the image on the host's daemon
                sh "docker build -t ${DOCKERHUB_USERNAME}/frontend:latest ."
            }
        }
        
        stage('Push Docker Image') {
            steps {
                // Use the docker client inside the container to push the image
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    sh "docker push ${DOCKERHUB_USERNAME}/frontend:latest"
                }
            }
        }
    }
}
