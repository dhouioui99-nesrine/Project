def branchName
def targetBranch

pipeline {
  agent {
    docker {
      image 'maven:3.9.9-eclipse-temurin-21'
      args '-v /root/.m2:/root/.m2'
    }
  }

  environment {
    DOCKERHUB_USERNAME = "nesrinedh"
    DEV_TAG = "${DOCKERHUB_USERNAME}/backends:v1.0.0-dev"
    PROD_TAG = "${DOCKERHUB_USERNAME}/fronts:v1.0.0-dev"
  }

  parameters {
    string(name: 'BRANCH_NAME', defaultValue: 'backend', description: 'Git branch to build')
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

    stage('Maven Build') {
      steps {
        sh 'mvn clean install -Dmaven.test.skip=true'
      }
    }

   stage('SonarQube Analysis') {
  steps {
    withSonarQubeEnv('SonarQube') {
      sh '''
        mvn org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121:sonar \
          -Dsonar.projectKey=IntegrationAPI \
          -Dsonar.projectName=IntegrationAPI
      '''
    }
  }
}

    stage("Quality Gate") {
      steps {
        timeout(time: 2, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          if (targetBranch == 'backend') {
            sh "docker build -t ${DEV_TAG} ."
          } else {
            sh "docker build -t ${PROD_TAG} ."
          }
        }
      }
    }

    stage('Docker Login & Push') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'CredentialDocker', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
          script {
            if (targetBranch == 'backend') {
              sh "docker push ${DEV_TAG}"
            } else {
              sh "docker push ${PROD_TAG}"
            }
          }
        }
      }
    }
  }
}
