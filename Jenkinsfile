def DOCKERHUB_USERNAME = "nesrinedh"

node() {
    // This part runs on the main Jenkins node
    try {
        stage('Checkout SCM') {
            checkout scm
        }

        stage('Full Pipeline') {
            def dockerArgs = "-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine"
            
            sh """
                docker run --rm ${dockerArgs} \\
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
    } catch (e) {
        throw e
    }
}
