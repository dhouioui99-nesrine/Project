def DOCKERHUB_USERNAME = "nesrinedh"

node() {
    stage('Checkout SCM') {
        checkout scm
    }

    stage('Install & Build (Node in container)') {
        sh '''
          docker run --rm \
            -v "$PWD":/app \
            -w /app \
            node:20 bash -lc "
              npm ci || npm install
              npm run build -- --prod
            "
        '''
    }

    stage('Docker Build & Push') {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
            sh '''
              echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
              docker build -t ''' + DOCKERHUB_USERNAME + '''/frontend:${GIT_COMMIT} -t ''' + DOCKERHUB_USERNAME + '''/frontend:latest .
              docker push ''' + DOCKERHUB_USERNAME + '''/frontend:${GIT_COMMIT}
              docker push ''' + DOCKERHUB_USERNAME + '''/frontend:latest
            '''
        }
    }
}
