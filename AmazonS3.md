# AmazonS3

Presentation 
S3 est une base donnée orienté pour les application. Elle permet de prendre en compte de grande capacité. 
Avantage : sécurisé durable et des objets scalable.

- Sécurisé  : 
	-  Les données sont cryptés soit du côté client soit du coté serveur.
	- Réplication des données
	- Versionning
- Durable :
	-Vérification de l'intégrité des donné via un checksum.
	Si une donnée est corrompu, S3 va chercher une réplique de la donnée.
	-Vérifie durant le stockage ou la récuparation de la donnée, S3 vérifie les packets des données sur le réseau .
- Scalable selon la donnée à stocker. 

Type de données stocké :
Tous types de données dans n'importe quelle format
selon le nombre d'accès  à ces données il y a :

**3  classes de stockage :**
- Amazon S3 standard  pour un accès frequent à la donnée haute disponibilité
- Amazon S3 standard pour un accès non fréquent à la donnée comme les archives et dont on a besoin quand meme un accès rapide
- Amazon Glacier :  données très peu  récupérer pas besoin de rapidité. 
![image](https://i.ibb.co/jVf7dTt/image-528x169.png)
## Organisation des données S3 par Bucket.
Les données S3 sont organisés en unité logique appellé bucket.
Un bucket contient une donnée et une métadata.
Il faut créer d'abord un bucket avant de pousser des données dans S3.
Choix de stockage de la donnée.
Le prix, la localisation, le temps d'accès, la disponibilité du service

## Transfert de données entre deux entrepôts.
Il est possible de faire des réplications sur différentes régions (CrossRegionReplication).
Pour transferer il y a 3 manières:
- La voie classique par Internet.
- TransfertAcceleration : 
Le transfert des données se fait de manière sécurisé à travers le Cloud appelé Amazon's CloudFront. La donnée est transférer sur une localisation plus proche de la destination puis la donnée est routé dans une bucket sur un chemin optimisé.
cette méthode peut transfert jusqu'à 75TB en 1Gb par s.
- La méthode Snowball  La donnée est transfert de manière physique. Amazon envoie un support afin de transférer les données dessus puis celui ci est envoyé au nouveau siège. Cette méthode est privilègié pour de gros quantités de données à déplacer.  Le temps de traitement pour cette méthode est de 5 à 7 jours.
 La version gratuite 5 donne accès à 5GB de stockage 
-  20000 requetes de récupération  2000 requete de stockage  et 15 GB de transfert chaque mois pour 1 an.
- 

<!--stackedit_data:
eyJoaXN0b3J5IjpbMTU1MjA0NDU4MywxMTE0Njg0NDU2LDMyOD
E5OTA4NSwzMTQyMzI0NjEsMTAyOTA3MTQxNywtMzMwODE5Njk3
LC02NTc2NTAwODddfQ==
-->