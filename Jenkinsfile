pipeline {
    agent any

    stages {
        stage('Build with Maven inside Docker') {
            steps {
                script {
                    docker.image('maven:3.9.4-eclipse-temurin-17').inside {
                        sh 'mvn clean package -DskipTests'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    dockerImage = docker.build("ecommerce-app")
                }
            }
        }

        stage('Run Docker Container') {
            steps {
                sh 'docker run -d -p 8080:8080 ecommerce-app'
            }
        }
    }
}