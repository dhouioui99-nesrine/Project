pipeline {
    agent any

    environment {
        DOCKERHUB_USER = credentials('dockerhub-user')  // Credentials DockerHub
        DOCKERHUB_PASS = credentials('dockerhub-pass')
        SONARQUBE     = 'SonarQubeServer'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/USERNAME/PROJECT.git', credentialsId: 'github-cred'
            }
        }

        stage('Backend - Build & Test') {
            steps {
                dir('backend') {
                    sh './mvnw clean test'
                }
            }
        }

        stage('backend - SonarQube Analysis') {
            steps {
                dir('backend') {
                    withSonarQubeEnv('SonarQubeServer') {
                        sh './mvnw sonar:sonar'
                    }
                }
            }
        }

        stage('Frontend - Build') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build --prod'
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                sh "docker build -t ${DOCKERHUB_USER}/backend:latest ./backend"
                sh "docker build -t ${DOCKERHUB_USER}/frontend:latest ./frontend"
            }
        }

        stage('Push Docker Images') {
            steps {
                sh "echo ${DOCKERHUB_PASS} | docker login -u ${DOCKERHUB_USER} --password-stdin"
                sh "docker push ${DOCKERHUB_USER}/backend:latest"
                sh "docker push ${DOCKERHUB_USER}/frontend:latest"
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker compose up -d'
            }
        }
    }
}
