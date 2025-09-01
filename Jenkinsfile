def DOCKERHUB_USERNAME = "nesrinedh"

node('master') {
    // This part runs on the master Jenkins node
    // It checks out the code here
    try {
        stage('Checkout SCM') {
            checkout scm
        }

        // Now, we run all subsequent commands inside a single Docker container.
        stage('Full Pipeline') {
            // Use the same volume mount for Docker access from the host.
            def dockerArgs = "-v //./pipe/dockerDesktopEngine://./pipe/dockerDesktopEngine"
            
            // This single command runs all your build steps inside the container
            sh """
                docker run --rm ${dockerArgs} \\
                -v ${pwd()}:/app \\
                -w /app \\
                node:20 /bin/bash -c "
                    npm install && \\
                    npm run build -- --prod && \\
                    
                    # You will need to re-log in to docker inside the container
                    # You can pass the credentials as environment variables or a file
                    echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin && \\
                    
                    docker build -t ${DOCKERHUB_USERNAME}/frontend:latest . && \\
                    docker push ${DOCKERHUB_USERNAME}/frontend:latest
                "
            """
        }
    } catch (e) {
        // You can add a post-build action here to handle failures if you want
        throw e
    }
}
