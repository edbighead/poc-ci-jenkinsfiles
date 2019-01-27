@Library('Shared')
import com.library.Shared

def shared = new Shared(this)
def versions = ["2.2.0","2.1.4","1.0.0"]
 
pipeline {
    agent any

    parameters { 
        choice(name: 'APP_VERSION', choices: versions, description: 'app version')
    }

    
    stages {

        stage("cleanup") {
            steps {
                deleteDir()
                sh "mkdir -p deploy"
            }
        }

        // stage('set version') {
        //     steps {
        //         echo "setting artefact version"
        //         script {
        //             shared.setVersion(params.MAJOR, params.MINOR, params.INCREMENTAL)
        //         }
        //     }
        // }

        stage('download') {
            parallel {
                stage('download from nexus') {
                    steps {
                        script {
                            shared.downloadHelloService(params.APP_VERSION)
                        }
                    }
                }
            }
        }



    }
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '3'))
    }

}