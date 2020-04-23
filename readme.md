eBibli-batch

Application batch du système d'information des bibliothèques de la ville.
Ce batch quotidien (fréquence paramétrable dans le cron du fichier application.properties) envoie des emails aux usagers ayant des emprunts en retard de restitution.

Ce client Rest interroge les microservices du backend qui expose des API Rest sur les url http://localhost:9001 à 9006 paramétrables dans le fichier application.properties

Pré-requis technique :

Version de Java : 1.8
 JDK : jdk1.8.0_202
 Maven 3.6.0
 Base de données : aucune (la base de données est connectée au backend)
 Avor lancé le serveur SMTP : pour ce projet, un serveur local (port 25) smtp a été utilisé : fakeSMTP cf http://nilhcem.com/FakeSMTP/ 
 une fois téléchargé, se lance par 'java -jar fakeSMTP-2.0.jar'
 Le serveur smtp définitif devra être paramétré dans le fichier application.properties

Installation et déploiement:

Packaging
mvn clean package : le fichier ebibli-batch-1.0.jar qui contient l'application est généré

Il est maintenant possible de lancer l'application directement dans votre IDE en exécutant le Main
ou en ligne de commande (application standalone intégrant un conteneur web grace à SpringBoot) : mvn clean install spring-boot:run

Le backend (https://github.com/GLescroel/ebibli-backend) doit avoir été lancé préalablement

Le port de l'Application est paramétré dans application.propertie : http://localhost:8082/

Documentation : la javadoc peut être générée via la commande mvn javadoc:javadoc puis consultée à partir de la page \target\site\apidocs\index.html

Sources disponibles sur : https://github.com/GLescroel/ebibli-batch
