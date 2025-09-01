def DOCKERHUB_USERNAME = "nesrinedh"

pipeline {
    // We'll use the 'node' docker agent, which comes with npm pre-installed
    agent {
        docker {
            image 'node:20'
        }
    }

    stages {
        stage('Frontend - Build & Test') {
            steps {
                // The current working directory will be the repository root
                // since the Jenkinsfile is in the 'front' branch
                sh 'npm install'
                sh 'npm run build -- --prod'
            }
        }
        
        stage('Build Docker Image') {
            // The Docker agent needs the Docker daemon, so we add this configuration.
            agent {
                docker {
                    image 'docker:dind' // Use a Docker in Docker image for building
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
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
                    args '-v /var/run/docker.sock:/var/run/docker.sock'
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
