def DOCKERHUB_USERNAME = "nesrinedh"

node() {
  stage('Checkout SCM') {
    checkout scm
  }

  stage('Docker sanity') {
    sh '''
      set -e
      docker version
      docker info | head -n 20
    '''
  }

  withEnv(["IMAGE=${DOCKERHUB_USERNAME}/frontend"]) {

    stage('Install & Build (Node in container)') {
      sh '''
        set -e
        docker run --rm \
          -v "$PWD":/app \
          -w /app \
          node:20 bash -lc "
            set -e
            npm ci || npm install
            npm run build -- --prod
          "
      '''
    }

    stage('Docker Build & Push') {
      withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
        sh '''
          set -e
          echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
          docker build -t "$IMAGE:${GIT_COMMIT}" -t "$IMAGE:latest" .
          docker push "$IMAGE:${GIT_COMMIT}"
          docker push "$IMAGE:latest"
          docker logout || true
        '''
      }
    }
  }
}
