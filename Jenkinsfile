
pipeline {
  agent any
   tools {
          maven 'Maven 3.0.5'
      }
  stages {
    stage('Build'){
      steps {
        sh 'mvn package -Dmaven.test.skip=true'
      }
    }
     stage('Copy'){
          steps {
         //   sh 'scp -i /var/lib/jenkins/users/sms/ssh/NotificationServerKey.pem -o StrictHostKeyChecking=no target/notificationWrapper-1.0-SNAPSHOT.jar ec2-user@34.207.175.228:/home/ec2-user/notificationService'
          sh 'cp target/notificationMapper-1.0-SNAPSHOT.jar /home/ec2-user/notificationMapper/src/'

          }
        }
    stage('Deploy'){
      steps {
     //sh 'sudo ssh -i /var/lib/jenkins/users/sms/ssh/NotificationServerKey.pem -o StrictHostKeyChecking=no ec2-user@34.207.175.228 "/home/ec2-user/notificationService/notification.sh"'
     //   sh 'java -jar target/notificationWrapper-1.0-SNAPSHOT.jar'

     sh '/home/ec2-user/notificationMapper/src/notification.sh'
      }
    }
  }
}

