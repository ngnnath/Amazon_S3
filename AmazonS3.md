
# AmazonS3

**Presentation** 
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

Type de données stocké : Tous types de données dans n'importe quelle format

**3  types de stockage selon l'usage des données:**
- Amazon S3 standard  pour un accès frequent à la donnée haute disponibilité
- Amazon S3 standard pour un accès non fréquent à la donnée comme les archives et dont on a besoin quand meme un accès rapide
- Amazon Glacier :  données très peu  récupérer pas besoin de rapidité. Il stocke les données sous forme d'archive sous des vault dont la taille peut atteindre 40 Terabyte
- Le nom du vault est unique comme les buckets.
![image](https://i.ibb.co/jVf7dTt/image-528x169.png)
## Organisation des données S3 par Bucket.
Les données S3 sont organisés en unité logique appellé bucket.
Un bucket contient une donnée et une métadata. et une clé qui lui est associé.
Il faut créer d'abord un bucket avant de pousser des données dans S3.
Lors de la creation d'un bucket le nom qui lui donnée est unique dans tous les volumes amazon.
La gestion des accès à ces buckets osnt déterminer par un fichier json appelé bucket Policy.
Il permet de déterminer précisément les actions autorisés sur un bucket par les utilisateur.
Choix de stockage de la donnée.
Le prix, la localisation, le temps d'accès, la disponibilité du service

## Transfert de données entre deux entrepôts.
Il est possible de faire des réplications sur différentes régions (CrossRegionReplication).
Pour transférer il y a 3 manières:
- La voie classique par Internet.
- TransfertAcceleration : 
Le transfert des données se fait de manière sécurisé à travers le Cloud appelé Amazon's CloudFront. La donnée est transférer sur une localisation plus proche de la destination puis la donnée est routé dans une bucket sur un chemin optimisé.
cette méthode peut transfert jusqu'à 75TB en 1Gb par s.
- La méthode Snowball  La donnée est transfert de manière physique. Amazon envoie un support afin de transférer les données dessus puis celui ci est envoyé au nouveau siège. Cette méthode est privilègié pour de gros quantités de données à déplacer.  Le temps de traitement pour cette méthode est de 5 à 7 jours.
 
 La version **gratuite** donne accès à :
 - 5GB de stockage 
-  20000 requetes de récupération  (get)
- -2000 requete de stockage  (put)
- -15 GB de transfert chaque mois pour 1 an.


Archivage 
Il est possible d'archiver les données via les LifeCycle Rules. Elle permet de planifier une procedure de sauvegarde (Par exemple de bucket S3 dans le système de donnée Glacier).

ISCSI protole de transfert
EBS storage
<!--stackedit_data:
eyJoaXN0b3J5IjpbNzYwODg4NTM5LC00Mjg3NjAyNDcsNDMwNj
k0NTUwLDExMTQ2ODQ0NTYsMzI4MTk5MDg1LDMxNDIzMjQ2MSwx
MDI5MDcxNDE3LC0zMzA4MTk2OTcsLTY1NzY1MDA4N119
-->