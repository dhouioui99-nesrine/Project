def DOCKERHUB_USERNAME = "nesrinedh"

node() {
    try {
        stage('Checkout SCM') {
            checkout scm
        }

        stage('Full Pipeline') {
            // Set DOCKER_HOST as an environment variable for the entire stage
            withEnv(["DOCKER_HOST=//./pipe/dockerDesktopEngine"]) {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh """
                        docker run --rm \\
                        -v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine \\
                        -v ${pwd()}:/app \\
                        -w /app \\
                        node:20 /bin/bash -c "
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
        }
    } catch (e) {
        throw e
    }
}
