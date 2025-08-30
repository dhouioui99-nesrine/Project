def branchName
def targetBranch

pipeline {
  agent any

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
          echo "✅ Current branch: ${branchName}"
        }
      }
    }

    stage('Maven Build') {
      agent {
        docker {
          image 'maven:3.9.9-eclipse-temurin-21'
          reuseNode true
          args '-v /root/.m2:/root/.m2'
        }
      }
      steps {
        sh 'java -version'
        sh 'mvn -v'
        sh 'mvn clean install -Dmaven.test.skip=true'
        echo '✅ Build completed.'
      }
    }

    stage('Maven Compile') {
      agent {
        docker {
          image 'maven:3.9.9-eclipse-temurin-21'
          reuseNode true
          args '-v /root/.m2:/root/.m2'
        }
      }
      steps {
        sh 'java -version'
        sh 'mvn -v'
        sh 'mvn compile -Dmaven.test.skip=true'
        echo '✅ Compile completed.'
      }
    }

    // 🔹 Stage d'analyse SonarQube
    stage('SonarQube Analysis') {
      steps {
        withSonarQubeEnv('SonarQube') { // "SonarQube" = nom que tu as configuré dans Jenkins (screenshot)
          sh 'mvn sonar:sonar -Dsonar.projectKey=IntegrationAPI -Dsonar.projectName=IntegrationAPI'
        }
      }
    }

    // 🔹 Contrôle du Quality Gate
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
          } else if (targetBranch == 'front') {
            sh "docker build -t ${PROD_TAG} ."
          }
        }
      }
    }

    stage('Docker Login') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'CredentialDocker', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh "echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin"
        }
      }
    }

    stage('Docker Push') {
      steps {
        script {
          if (targetBranch == 'backend') {
            sh "docker push ${DEV_TAG}"
          } else if (targetBranch == 'front') {
            sh "docker push ${PROD_TAG}"
          }
        }
      }
    }
  }
}
