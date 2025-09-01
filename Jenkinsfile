pipeline {
  agent any

  environment {
    DOCKER_HOST = 'tcp://host.docker.internal:2375'
    IMAGE       = 'nesrinedh/frontend'
  }

  options {
    skipDefaultCheckout(true)
    timestamps()
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Docker sanity') {
      steps {
        sh '''
          set -e
          echo "DOCKER_HOST=${DOCKER_HOST}"
          docker version
          docker info | head -n 20
        '''
      }
    }

    stage('Build image') {
      steps {
        sh '''
          set -e
          docker build -t "$IMAGE:${GIT_COMMIT}" -t "$IMAGE:latest" .
        '''
      }
    }

    stage('Push image') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh '''
            set -e
            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
            docker push "$IMAGE:${GIT_COMMIT}"
            docker push "$IMAGE:latest"
            docker logout || true
          '''
        }
      }
    }
  }
}
