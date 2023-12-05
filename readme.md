# LP_rpg 
### Description
Projet de création d'un jeu RPG en java, amélioration du projet rpg en console Eclipse avec :  
- Création de l'IHM complétant toutes les possibilités du mode console
- Sélection de personnage par image
- Lecture de map en CSV facilement ajoutable, customization (création magasin, génération aléatoire monstres...)
- Déplacement dans la map importé avec textures avec réactions au clavier
- Réaction aux touches de l'utilisateur (flèches de déplacements, espace pour attaquer etc...)
- Achat armes dans plusieurs magasins et équipement des objets
- Combat avec des monstres, deux options : Attaquer ou Fuir
- Gain d'XP, montée de niveau, difficulté progressive
- Plusieurs niveaux avec un boss et une fin de jeu avec les résultats finaux
### Comment jouer 
1. Télécharger l'exécutable rpg.exe et le dossier assets  
2. Placez les dans le même repertoire et lancez le jeu 
### Screenshot
Choix du personnage :  
![image](https://github.com/teddyfresnes/LP_rpg/assets/80900011/f9037bcd-0d49-4e4b-a27e-336f032b6663)  
Affichage menu et infos :  
![image](https://github.com/teddyfresnes/LP_rpg/assets/80900011/89312ec6-92ac-419b-bf42-5f3dc1caa876)  
Achat magasin, affichage du sac et équipement :  
![image](https://github.com/teddyfresnes/LP_rpg/assets/80900011/c1150e69-6401-4dd2-90cb-3cd4d5240b2f)  
Système de combat et montée de niveau :  
![image](https://github.com/teddyfresnes/LP_rpg/assets/80900011/158306a7-dd61-413e-b955-1ae59c29f143)  
Changement de map lorsque le joueur va sur le prochain niveau :  
![image](https://github.com/teddyfresnes/LP_rpg/assets/80900011/de7d93f0-a7e8-4ee3-a68c-55ca47fd8617)  
Accès au niveau final avec un boss à battre pour finir le jeu :  
![image](https://github.com/teddyfresnes/LP_rpg/assets/80900011/abf87caa-a329-4c92-bd0a-fa4a6b0e7d67)  
### Structure UI
L'ui a été placé en superposition avec les fichiers suivants  
- Un fichier main pour choisir son personnage, initier les maps et proposer les actions à l'utilisateur
- Un fichier magasin qui permet de créer des magasins avec des objets par défaut, les supprimer, gérer le stock, ajouter un objet
- Un fichier player qui s'occupe de gérer le personnage, son sac, ses caractéristiques, sa montée en niveau aléatoire et ses variations selon les sous classes de personnages
- Un dossier characters avec les sous classes Player qui fournit des avantages propres à chacun (Warrior, Tank, Assassin, Goblin, Mage)
- Un dossier armes qui contient les types d'armes, celles ci pourront être utilisés pour avoir des caractéristiques uniques selon leurs type
- Un fichier map qui permet d'importer une map à l'aide du projet lecteur de fichiers, ajout d'une sous classe pour gérer l'importation CSV et la retranscription dans un tableau 2D. La map est ensuite structuré avec des caractères unicode de même taille. Elle permet l'ajout des magasins et la génération aléatoires des monstres de plus en plus fort.
- Un dossier map qui contient les maps en CSV suivant un format précis 36x15 avec des cellules prédefinis
- Un fichier monstre qui contient la liste des types de monstres qui seront générés aléatoirement avec des avantages et des faiblesses
- Un fichier fight qui gère la rencontre avec les monstres, le combat et les options, les récompenses variables selon la puissance de l'ennemi, la défaite du joueur
