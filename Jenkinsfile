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
                    -v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine \\
                    -v ${pwd()}:/app \\
                    -w /app \\
                    node:20 /bin/bash -c "
                        # Set DOCKER_HOST to force Docker to use the named pipe
                        export DOCKER_HOST='//./pipe/dockerDesktopEngine'
                        
                        echo 'Starting npm install...'
                        npm install
                        
                        echo 'Starting npm build...'
                        npm run build -- --prod
                        
                        echo 'Logging into Docker Hub...'
                        echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin
                        
                        echo 'Building Docker image...'
                        docker build -t ${DOCKERHUB_USERNAME}/frontend:latest .
                        
                        echo 'Pushing Docker image...'
                        docker push ${DOCKERHUB_USERNAME}/frontend:latest
                    "
                """
            }
        }
    } catch (e) {
        throw e
    }
}
