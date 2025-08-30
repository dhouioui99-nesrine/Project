pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out Git repository..."
                    // Jenkins handles the checkout from the SCM configuration
                }
            }
        }

        stage('Backend: Test and Build') {
            steps {
                dir('backend') {
                    // This command will execute all the JUnit tests you just wrote.
                    // If any test fails, the pipeline will stop here.
                    sh './mvnw clean verify'
                }
            }
        }

        /*stage('Backend: SonarQube Analysis') {
            steps {
                dir('backend') {
                    // You must have SonarQube server configured in Jenkins
                    withSonarQubeEnv('SonarQube Server') {
                        // This command triggers the static analysis
                        sh './mvnw sonar:sonar -Dsonar.login=squ_e2930270dc13bf009683c70b8a965403cd89f3e0'
                    }
                }
            }
        }*/

        // We will add the Docker build stage next
        stage('Backend: Build Docker Image') {
            steps {
                dir('backend') {
                    // Build the Docker image using the Dockerfile
                    sh 'docker build -t nesrinedh/my-spring-boot-app:latest .'
                }
            }
        }
    }
}

