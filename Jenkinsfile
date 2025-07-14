pipeline {
    agent any

    stages {
        stage('Clone repo') {
            steps {
                git branch: 'main', url: 'https://github.com/streda/E-Commerce-Project.git'
            }
        }

        stage('Build with Maven') {
            steps {
                script {
                    docker.image('maven:3.9.6-openjdk-17').inside {
                        sh 'mvn clean package -DskipTests'
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ecommerce-app .'
            }
        }

        stage('Run Docker Container') {
            steps {
                sh 'docker rm -f ecommerce-app || true'
                sh 'docker run -d --name ecommerce-app -p 8080:8080 ecommerce-app'
            }
        }
    }
}