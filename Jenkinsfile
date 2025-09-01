def DOCKERHUB_USERNAME = "nesrinedh"

node() {
    try {
        stage('Checkout SCM') {
            checkout scm
        }

        stage('Full Pipeline') {
            withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                sh """
                    docker run --rm \\
                    -v //./pipe/dockerDesktopEngine://./pipe/docker.sock \\
                    -v ${pwd()}:/app \\
                    -w /app \\
                    node:20 /bin/bash -c "
                        npm install && \\
                        npm run build -- --prod && \\
                        
                        echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin && \\
                        
                        docker build -t ${DOCKERHUB_USERNAME}/frontend:latest . && \\
                        docker push ${DOCKERHUB_USERNAME}/frontend:latest
                    "
                """
            }
        }
    } catch (e) {
        throw e
    }
}
