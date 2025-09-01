def DOCKERHUB_USERNAME = "nesrinedh"

pipeline {
    agent any

    environment {
        // We will define credentials directly within the steps that need them for better security.
        // We do not define them here.
    }

    stages {
        stage('Checkout') {
            steps {
                // Jenkins handles this automatically if you configured it in the job settings.
                echo 'Source code checked out.'
            }
        }

        stage('Backend - Build & Test') {
            steps {
                dir('backend') {
                    // This will run the Maven build and all your JUnit tests.
                    sh './mvnw clean install'
                }
            }
        }

        stage('Backend - SonarQube Analysis') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQube Server') {
                        // Your `pom.xml` should have the SonarQube plugin configured
                        sh './mvnw sonar:sonar'
                    }
                }
            }
        }

        stage('Frontend - Build & Test') {
            steps {
                dir('frontend') {
                    // Install Node.js dependencies
                    sh 'npm install'
                    // Run unit tests and generate code coverage reports
                    sh 'npm run test-headless -- --no-watch --code-coverage'
                    // Build the application for production
                    sh 'npm run build -- --prod'
                }
            }
        }
        
        stage('Build Docker Images') {
            steps {
                dir('backend') {
                    sh "docker build -t ${DOCKERHUB_USERNAME}/backend:latest ."
                }
                dir('frontend') {
                    sh "docker build -t ${DOCKERHUB_USERNAME}/frontend:latest ."
                }
            }
        }
        
        stage('Push Docker Images') {
            steps {
                // Use a proper withCredentials block for secure Docker login
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                    
                    sh "docker push ${DOCKERHUB_USERNAME}/backend:latest"
                    sh "docker push ${DOCKERHUB_USERNAME}/frontend:latest"
                }
            }
        }
        
        stage('Deploy') {
            steps {
                // Assuming you have a docker-compose.yml file in the root
                sh 'docker-compose up -d'
            }
        }
    }
}
