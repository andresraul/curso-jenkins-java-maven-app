job('Java Maven App DSL') {
    description('Java Maven App con DSL para el curso de Jenkins')
    scm {
        git('https://github.com/andresraul/curso-jenkins-java-maven-app.git', 'main') { node ->
            node / gitConfigName('Andres Mateo')
            node / gitConfigEmail('andresraul@gmail.com')
        }
    }
    steps {
        maven {//utilizamos maven
          mavenInstallation('mavenjenkins') //le indicamos la versión de maven que debe utilizar y que hemos configurado anteriormente en jenkins
          goals('-B -DskipTests clean package') //utilizamos los comandos maven. En este caso contruimos la aplicación
        }
        maven {//volvemos a utilizar maven
          mavenInstallation('mavenjenkins')//le indicamos la versión de maven que debe utilizar y que hemos configurado anteriormente en jenkins
          goals('test')//ejecutamos los test 
        }
        shell('''
          echo "Entrega: Desplegando la aplicación" 
          java -jar "/var/jenkins_home/workspace/Java Maven App DSL/target/my-app-1.0-SNAPSHOT.jar"
        ''')  // Utilizamos shell para imprimir un mensaje y ejecutar el archivo jar
    }
    publishers {
        archiveArtifacts('target/*.jar') //Archivamos los artifacts
        archiveJunit('target/surefire-reports/*.xml') //archivamos los archivos de test xml
	slackNotifier {//Configuramos slack
            notifyAborted(true)
            notifyEveryFailure(true)
            notifyNotBuilt(false)
            notifyUnstable(false)
            notifyBackToNormal(true)
            notifySuccess(true)
            notifyRepeatedFailure(false)
            startNotification(false)
            includeTestSummary(false)
            includeCustomMessage(false)
            customMessage(null)
            sendAs(null)
            commitInfoChoice('NONE')
            teamDomain(null)
            authToken(null)
       }
    }
}
