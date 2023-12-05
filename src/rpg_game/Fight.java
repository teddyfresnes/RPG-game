package rpg_game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Fight implements KeyListener
{
    public Player joueur;
    public Monster monster;
    boolean fight;
    
    // variables hp
    public int player_hp;
    public int monster_hp;
    public JFrame window;
    public Map carte;
    
    public JLabel playerLabel;
    public JLabel monsterLabel;
    private JTextArea actionTextArea;

    
    public Fight(Player joueur, Monster monster, Map carte, JFrame window)
    {
        this.joueur = joueur;
        this.monster = monster;
        
        this.player_hp = joueur.hp;
        this.monster_hp = monster.hp;
        this.window = window;
        this.carte = carte;
        playerLabel = new JLabel();
        monsterLabel = new JLabel();
        this.fight = true;
        
        start();
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {
        // Non utilisé ici
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_SPACE)
        {
        	if (fight)
        	{
        		fight_scene();
        	}
        	else
        	{
        		window.getContentPane().removeAll();
        		carte.display_map_ihm();
        	}
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // Non utilisé ici
    }

    
    public void start() {
        window.getContentPane().removeAll();
        window.setLayout(new BorderLayout());

        JPanel blackPanel = new JPanel();
        blackPanel.setBackground(new Color(229, 206, 177));
        blackPanel.setLayout(new BorderLayout());
        window.add(blackPanel, BorderLayout.CENTER);

        // Chargement de l'image du joueur
        ImageIcon playerImage = new ImageIcon("assets/img/" + joueur.getClass().getSimpleName() + ".png");
        Image scaledPlayerImage = playerImage.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        playerImage = new ImageIcon(scaledPlayerImage);
        playerLabel = new JLabel(playerImage, SwingConstants.LEFT);

        // Chargement de l'image du monstre
        ImageIcon monsterImage = new ImageIcon(monster.art);
        Image scaledMonsterImage = monsterImage.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        monsterImage = new ImageIcon(scaledMonsterImage);
        monsterLabel = new JLabel(monsterImage, SwingConstants.LEFT);

        // Panel pour afficher les informations du joueur et du monstre
        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(new Color(229, 206, 177));
        playerLabel.setText("Joueur " + joueur.username + " lvl " + joueur.level + " (" + (joueur.damage + joueur.selectedWeapon.damage()) + "⚔ " + player_hp + "/" + joueur.hp + "❤)");
        monsterLabel.setText(monster.type + " lvl " + monster.level + " (" + monster.damage + "⚔ " + monster_hp + "/" + monster.hp + "❤)");
        infoPanel.add(playerLabel);
        infoPanel.add(monsterLabel);
        window.add(infoPanel, BorderLayout.NORTH);

        // Panel d'actions en bas
        JPanel actionPanel = new JPanel(new GridLayout(1, 2));
        actionPanel.setBackground(new Color(229, 206, 177));

        JButton attackButton = new JButton("Attaquer");
        attackButton.setBackground(new Color(70, 48, 21)); // Couleur marron
        attackButton.setForeground(Color.WHITE); // Texte en blanc
        JButton fleeButton = new JButton("Fuir");
        fleeButton.setBackground(new Color(70, 10, 10)); 
        fleeButton.setForeground(Color.WHITE); // Texte en blanc

        actionPanel.add(attackButton);
        actionPanel.add(fleeButton);

        window.add(actionPanel, BorderLayout.SOUTH);
        //carte.display_map_ihm();
        // Ajouter des actions pour les boutons
        attackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.out.println("test_fight");
            	fight_scene();
            }
        });

        fleeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            	window.getContentPane().removeAll();
            	
            	JPanel mainPanel = new JPanel(new BorderLayout());
            	mainPanel.setBackground(new Color(229, 206, 177));
                window.add(mainPanel, BorderLayout.CENTER);
            	
                JLabel resultLabel;
                if (joueur.hp < 6 || (monster.name.equals("boss"))) {
                	resultLabel = new JLabel("Trop faible pour vous enfuir, l'ennemi vous a terrasé.");
                	carte.end_game(false);
                } else {
                	resultLabel = new JLabel("Vous vous êtes enfui, laissant derrière vous argent et cicatrices...");
                    joueur.money = 0; // Perte de l'argent
                    joueur.hp = (int) (joueur.hp - (joueur.hp * 0.25)); // Perte d'un quart des hp définitivement
                }

                
                resultLabel.setHorizontalAlignment(JLabel.CENTER);
                mainPanel.add(resultLabel, BorderLayout.CENTER);

                // "OK" pour revenir à la carte
                JButton exitButton = new JButton("OK");
                exitButton.setBackground(new Color(70, 48, 21)); // Couleur marron
                exitButton.setForeground(Color.WHITE); // Texte en blanc
                exitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mainPanel.removeAll();
                        carte.display_map_ihm();
                    }
                });
                mainPanel.add(exitButton, BorderLayout.SOUTH);

                // Rafraîchir l'affichage
                mainPanel.revalidate();
                mainPanel.repaint();
            }
        });

        KeyListener[] keyListeners = window.getKeyListeners();
        for (KeyListener kl : keyListeners) {
            window.removeKeyListener(kl);
        }
        window.addKeyListener(this);
        window.requestFocus();
        window.revalidate();
        window.repaint();
    }
    
    
    public void fight_scene() {
    	
    	player_hp -= monster.damage;
        monster_hp -= joueur.damage + joueur.selectedWeapon.damage();

        updateLabels();

        if (monster_hp <= 0)
        { // Victoire
        	fight = false;
        	window.getContentPane().removeAll();
        	JPanel mainPanel = new JPanel(new BorderLayout());
        	mainPanel.setBackground(new Color(229, 206, 177));
            window.add(mainPanel, BorderLayout.CENTER);
            int money_reward = (int) (((monster.damage * 7 + monster.hp) / 5) * joueur.money_multiplier);
            int exp_reward = (int) (((monster.damage * 7 + monster.hp) / 3) * joueur.exp_multiplier);
            joueur.money += money_reward;
            String resultLevel = joueur.add_exp(exp_reward);
            JLabel resultLabel = new JLabel("Vous avez gagné le combat (+"+money_reward+"💲 | "+exp_reward+"xp)");
            JLabel resultLevelLabel = new JLabel(resultLevel);
            resultLabel.setHorizontalAlignment(JLabel.CENTER);
            resultLevelLabel.setHorizontalAlignment(JLabel.CENTER);
            JButton exitButton = new JButton("OK");
            exitButton.setBackground(new Color(70, 48, 21)); // Couleur marron
            exitButton.setForeground(Color.WHITE); // Texte en blanc
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainPanel.removeAll();
                    carte.display_map_ihm();
                }
            });
            mainPanel.add(resultLabel, BorderLayout.NORTH);
            mainPanel.add(resultLevelLabel, BorderLayout.CENTER);
            mainPanel.add(exitButton, BorderLayout.SOUTH);
        }
        else if (player_hp <= 0)
        { // Défaite
        	fight = false;
        	window.getContentPane().removeAll();
        	JPanel mainPanel = new JPanel(new BorderLayout());
        	mainPanel.setBackground(new Color(229, 206, 177));
            window.add(mainPanel, BorderLayout.CENTER);
            joueur.hp = 0;
            JLabel resultLabel = new JLabel("Vous avez perdu le combat.");
            resultLabel.setHorizontalAlignment(JLabel.CENTER);
            JButton exitButton = new JButton("OK");
            exitButton.setBackground(new Color(70, 48, 21)); // Couleur marron
            exitButton.setForeground(Color.WHITE); // Texte en blanc
            
            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mainPanel.removeAll();
                    carte.end_game(false);
                }
            });
            mainPanel.add(exitButton, BorderLayout.SOUTH);
            mainPanel.add(resultLabel, BorderLayout.CENTER);
        }
    }

    // Fonction pour mettre à jour les labels d'informations du joueur et du monstre
    private void updateLabels() {
        playerLabel.setText("Joueur "+joueur.username+" lvl "+joueur.level+" ("+(joueur.damage+joueur.selectedWeapon.damage())+"⚔ "+player_hp+"/"+joueur.hp+"❤)");
        monsterLabel.setText(monster.type+" lvl "+monster.level+" ("+monster.damage+"⚔ "+monster_hp+"/"+monster.hp+"❤)");
    }
}