pipeline {
    agent any

    environment {
        FRONTEND_DIR = 'front'   // Chemin vers votre projet Angular
        DOCKER_IMAGE = 'nesrinedh/angular16-app:latest' // Nom de l'image Docker
    }

    stages {

         stage('Checkout from GitHub') {
      steps {
        script {
          branchName = params.BRANCH_NAME
          if (!branchName?.trim()) {
            error("❌ BRANCH_NAME is empty. Please provide a valid branch.")
          }
          targetBranch = branchName
          git branch: branchName,
              url: 'https://github.com/dhouioui99-nesrine/Project.git',
              credentialsId: 'gitcredential'
        }
      }
    }

        stage('Frontend: Install Dependencies') {
            steps {
                dir("${env.FRONTEND_DIR}") {
                    sh 'npm install'
                }
            }
        }

        stage('Frontend: Build Angular') {
            steps {
                dir("${env.FRONTEND_DIR}") {
                    sh 'npm run build -- --prod'
                }
            }
        }

        stage('Frontend: Build Docker Image') {
            steps {
                dir("${env.FRONTEND_DIR}") {
                    // Construire l'image Docker à partir du Dockerfile
                    sh "docker build -t ${env.DOCKER_IMAGE} ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    // Si vous utilisez un registry Docker (ex: Docker Hub)
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-cred', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
                        sh "docker push ${env.DOCKER_IMAGE}"
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build Angular 16 et Docker réussis !'
        }
        failure {
            echo 'Le build a échoué.'
        }
    }
}
