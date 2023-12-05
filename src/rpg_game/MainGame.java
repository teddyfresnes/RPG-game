package rpg_game;


import javax.swing.*;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import java.io.IOException;
import java.util.*;
import java.util.Random;
import java.util.Scanner; // pour le debug


import characters.*;
import weapons.Hammer;



public class MainGame
{
    static String playerClass;
    static JButton selectedButton;
    static JButton validateButton;
    static JFrame window;
    
	public static void main(String[] args)
	{
		
        window = new JFrame();
        // set window size (width, height)
        window.setSize(1000, 500); // 83px pour les infos joueurs autour de la map / 417px pour la map
        // forbid the resizing of the window by the user
        window.setResizable(false);
        // place the window in the center of the screen
        window.setLocationRelativeTo(null);
        // exit the application when the user close the frame
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set the window title
        window.setTitle("Sorbonne RPG");
        
        BackgroundMusic backgroundMusic = new BackgroundMusic();
        backgroundMusic.playBackgroundMusic("assets/music/song.wav");
        
        // panel pour le nom
        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.setBackground(new Color(229, 206, 177));
        JLabel nameLabel = new JLabel("Entrez votre nom : ");
        JTextField nameField = new JTextField(20);
        topPanel.add(nameLabel);
        topPanel.add(nameField);

    	
        // panel choix perso
        JPanel middlePanel = new JPanel(new GridLayout(1, 5));
        // boutons de choix de perso
        JButton warriorButton = createCharacterButton("Guerrier", "assets/img/warrior.png");
        middlePanel.add(warriorButton);
        JButton tankButton = createCharacterButton("Tank", "assets/img/tank.png");
        middlePanel.add(tankButton);
        JButton assassinButton = createCharacterButton("Assassin", "assets/img/assassin.png");
        middlePanel.add(assassinButton);
        JButton goblinButton = createCharacterButton("Gobelin", "assets/img/goblin.png");
        middlePanel.add(goblinButton);
        JButton mageButton = createCharacterButton("Mage", "assets/img/mage.png");
        middlePanel.add(mageButton);

        
        // panel inférieur pour valider
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(new Color(229, 206, 177));
        validateButton = new JButton("Valider");
        validateButton.setBackground(new Color(70, 48, 21)); // Couleur marron
        validateButton.setForeground(Color.WHITE); // Texte en blanc
        validateButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // get from form comme en html wouhou
                String username = nameField.getText();
                System.out.println("username : "+username);
                System.out.println("class player : "+playerClass);
                
                Player joueur = 
                		playerClass.equals("Guerrier") ? new Warrior(username) :
                        playerClass.equals("Tank") ? new Tank(username) :
                        playerClass.equals("Assassin") ? new Assassin(username) :
                        playerClass.equals("Gobelin") ? new Goblin(username) :
                        playerClass.equals("Mage") ? new Mage(username) :
                        new Player(username); // sans sous-classe si rien rentré
                
                //suppression elements de la fenetre
                topPanel.setVisible(false);
                middlePanel.setVisible(false);
                bottomPanel.setVisible(false);
                
                window.getContentPane().setBackground(Color.BLACK);
                
                playGame(joueur);
            }
        });
        bottomPanel.add(validateButton);

        
        // affichage sur la fenetre
        window.add(topPanel, BorderLayout.NORTH);
        window.add(middlePanel, BorderLayout.CENTER);
        window.add(bottomPanel, BorderLayout.SOUTH);

        window.setVisible(true);
	}
        
	
	static public void playGame(Player joueur)
	{
		Scanner input;
		String choice;
		// chargement de la carte
		Map carte = new Map("assets/maps/a.csv", window, joueur);
	    int[] spawnCoordinates = carte.search_spawn();
	    carte.playerX = spawnCoordinates[1];
	    carte.playerY = spawnCoordinates[0];
	    
	    //carte.mapPanel.addKeyListener(carte);
        //carte.mapPanel.setFocusable(true);
        //carte.mapPanel.setFocusTraversalKeysEnabled(false);
	    
		WeaponStore shop_spawn = new WeaponStore("Magasin du début",2,12);
		shop_spawn.remove(shop_spawn.stock.get(3)); shop_spawn.remove(shop_spawn.stock.get(2)); shop_spawn.remove(shop_spawn.stock.get(1)); // supprime armes par defaut
		shop_spawn.add(new Hammer("Massue", 75, 20));
		carte.add_shop(shop_spawn);
	    
		WeaponStore shop_default = new WeaponStore("Magasin 308",4,4);
		carte.add_shop(shop_default);
		
		carte.gen_monster(1, "Slime");
		carte.gen_monster(1, "Slime");
		carte.gen_monster(2, "Slime");
		carte.gen_monster(3, "Slime");
		carte.gen_monster(1, "Squelette");
		carte.gen_monster(1, "Squelette");
		carte.gen_monster(2, "Squelette");
		carte.gen_monster(2, "Squelette");
	    System.out.println("\n\n\n\n");
	    
	    carte.display_map_ihm();
	    
	    
	    // affichage du menu
	    boolean done = false;
		/*while (!done)
		{
			clear();
			
			carte.display_legend();
			System.out.println("");
			carte.display_map_ihm();
			System.out.println("");
			System.out.println(joueur.category+" "+joueur.username+" ("+(joueur.damage+joueur.selectedWeapon.damage())+"⚔ "+joueur.hp+"❤)");
			System.out.println("----------------------------------------");
			System.out.println(" Or\t: "+joueur.money+"💲");
			System.out.println(" Lvl\t: "+joueur.level);
			System.out.println(" Exp\t: "+joueur.exp+"/"+joueur.exp_need);
			System.out.println(" Arme\t: "+joueur.selectedWeapon.name());
			System.out.println("----------------------------------------");
			System.out.println("[Z] Haut");
			System.out.println("[S] Bas");
			System.out.println("[Q] Gauche");
			System.out.println("[D] Droite");
			System.out.println("[1] Afficher mon sac");
			
		    input = new Scanner(System.in);
		    System.out.print("\n\nQue voulez vous faire?\n> ");
		    choice = input.nextLine();
		    
		    clear();
		    
		    String next_case_type = "none";
		    switch(choice.toUpperCase())
		    {
		    	case "Z":
		    		next_case_type = carte.move_player("UP");
		    		break;
		    	case "S":
		    		next_case_type = carte.move_player("DOWN");
		    		break;
		    	case "Q":
		    		next_case_type = carte.move_player("LEFT");
		    		break;
		    	case "D":
		    		next_case_type = carte.move_player("RIGHT");
		    		break;
		    	case "1":
		    		joueur.display_bag(); // pour equipe une arme
		    		wait_now();
		    		break;
		    	//default:
		    		//System.out.println("Cette option n'existe pas");
		    }
		    //wait_now();
		    if (next_case_type == "shop") // si la prochaine case est un magasin
		    {
		    	WeaponStore magasin = carte.getShopOnPlayer();
		    	magasin.displayTo(joueur);
		    	wait_now();
		    }
		    if (next_case_type == "monster") // si la prochaine case est un magasin
		    {
		    	Random r = new Random();
		    	Monster monster_case = carte.getEntityOnPlayer();
		    	Fight my_fight = new Fight(joueur, monster_case);
		    	clear();
		    	boolean alive = my_fight.start();
		    	carte.gen_monster(r.nextInt(1)+monster_case.level, monster_case.type); // regeneration du monstre sur la carte avec une chance de montée de niveau
		    	carte.remove_monster(monster_case);
		    	wait_now();
		    	if (alive == false)
		    	{
		    		done = true; // fin du jeu
		    		System.out.println("\nVous êtes mort\n");
		    	}
		    }
		    if (next_case_type == "next_map") // si la prochaine case est un magasin
		    {
		    	if (carte.map_path.equals("assets/maps/a.csv")) // début map b, fin du niveau a
		    	{
		    		carte = new Map("assets/maps/b.csv", window);
		    	    spawnCoordinates = carte.search_spawn();
		    	    carte.playerX = spawnCoordinates[1];
		    	    carte.playerY = spawnCoordinates[0];
		    	    
		    		shop_default = new WeaponStore("Magasin au milieu de nul part",17,7);
		    		carte.add_shop(shop_default);
		    		
		    		carte.gen_monster(2, "Slime");
		    		carte.gen_monster(2, "Slime");
		    		carte.gen_monster(3, "Slime");
		    		carte.gen_monster(3, "Slime");
		    		carte.gen_monster(3, "Slime");
		    		carte.gen_monster(1, "Squelette");
		    		carte.gen_monster(2, "Squelette");
		    		carte.gen_monster(3, "Squelette");
		    		carte.gen_monster(3, "Squelette");
		    	}
		    	else if (carte.map_path.equals("assets/maps/b.csv")) // début map c, fin du niveau b
		    	{
		    		carte = new Map("assets/maps/c.csv", window);
		    	    spawnCoordinates = carte.search_spawn();
		    	    carte.playerX = spawnCoordinates[1];
		    	    carte.playerY = spawnCoordinates[0];
		    		
		    		carte.gen_monster(1, "Boss");
		    	}
		    	else if (carte.map_path.equals("assets/maps/c.csv")) // fin du jeu
		    	{
		    		System.out.println("Félicitations, "+joueur.username+", vous avez terminé Sorbonne RPG avec "+joueur.mana+"xp accumulé.");
		    	}
		    }
		}*/
	}
	
	
	public static void clear()
	{
	    //System.out.print("\033[H\033[2J");  
		System.out.print("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		//System.out.flush();
		
	}
	
	
	public static void wait_now()
	{
		Scanner input = new Scanner(System.in);
		System.out.println("\nEntrez une touche pour continuer...");
	    input.nextLine();
	}
	
	
    private static JButton createCharacterButton(String characterName, String imagePath)
    {
        JButton button = new JButton(characterName, new ImageIcon(imagePath));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setBorderPainted(false);
        button.addActionListener(e -> {
            // surbrillance
            if (selectedButton != null)
            {
                selectedButton.setBorderPainted(false);
            }
            button.setBorderPainted(true); selectedButton = button;
            playerClass = characterName; // recupere le contenu du bouton selectionné
            validateButton.setText("Valider : "+playerClass);
        });
        return button;
    }
}
