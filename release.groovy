@Library('Shared')
import com.library.Shared

def shared = new Shared(this)
def repoNames = ["boxfuse-sample-java-war-hello"]
def repoOwner = "edbighead"
 
pipeline {
    agent any

    parameters { 
        choice(name: 'REPO', choices: repoNames, description: 'repository to release')
        text(name: 'BRANCH', defaultValue: 'master', description: 'branch to build')
        booleanParam(name: 'MAJOR', defaultValue: false, description: 'major version x.0.0')
        booleanParam(name: 'MINOR', defaultValue: false, description: 'minor version 0.x.0')
        booleanParam(name: 'INCREMENTAL', defaultValue: true, description: 'inremental version 0.0.x')
    }

    
    stages {

        stage("checkout scm") {
            steps {
                git branch: params.BRANCH, credentialsId: 'github-key', url: "git@github.com:${repoOwner}/${params.REPO}.git"
            }
        }

        stage('set version') {
            steps {
                echo "setting artefact version"
                script {
                    shared.setVersion(params.MAJOR, params.MINOR, params.INCREMENTAL)
                }
            }
        }

    }
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
    }

}