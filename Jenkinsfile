
def branchName
def targetBranch

pipeline {
  agent any 

  environment {
    DOCKERHUB_USERNAME = "nesrinedh"
    DEV_TAG = "${DOCKERHUB_USERNAME}/backends:v1.0.0-dev"
      PROD_TAG = "${DOCKERHUB_USERNAME}/Fronts:v1.0.0-dev"
  
  }

  parameters {
    string(name: 'BRANCH_NAME', defaultValue: 'backend', description: 'Git branch to build')
    string(name: 'CHANGE_TARGET', defaultValue: '', description: 'Git change ID for the target merge requests')
  }

  stages {
    stage('Github') { 
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

    stage('MVN BUILD') {
      steps {
        sh 'mvn clean install'
        echo '✅ Build stage completed.'
      }
    }

    stage('MVN COMPILE') {
      steps {
        sh 'mvn compile'
        echo '✅ Compile stage completed.'
      }
    }

    stage('Build Docker') {
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
        withCredentials([usernamePassword(credentialsId: 'CredentialDocker', usernameVariable: 'DOCKERHUB_USERNAME', passwordVariable: 'DOCKERHUB_PASSWORD')]) {
          sh "docker login -u ${DOCKERHUB_USERNAME} -p ${DOCKERHUB_PASSWORD}"
        }
      }
    }

    stage('Docker Push') {
      steps {
        sh "docker push ${DEV_TAG}"
      }
    }

 


      




    
  }
}
