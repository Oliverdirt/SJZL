node  {
        
  stage ('Checkout') {
    git branch: '${gitBranch}', credentialsId: 'git-ssdc-kube', url: '${gitUrl}'
  }

  stage('SonarQube analysis') {
    // requires SonarQube Scanner 2.8+
    def scannerHome = tool 'sonar';
    withSonarQubeEnv('sonar') {
      sh "${scannerHome}/bin/sonar-scanner -Dsonar.sourceEncoding=UTF-8 -Dsonar.projectKey=${sonarProjectKey} -Dsonar.projectName=${sonarProjectName} -Dsonar.sources=src"
    }
  }

  stage('Build') {
    nodejs(nodeJSInstallationName: 'node8', configId: '757b5783-ec96-4607-acf1-00b215f0fc43') {
      sh 'npm install'
      sh 'npm run build'
    }
  }

  stage('Docker build') {
      
    docker.withRegistry('https://image.docker.ctbiyi.com', 'docker-admin') {

        def customImage = docker.build("${imageName}:${imageVersion}")

        customImage.push()
    }
  }  
}