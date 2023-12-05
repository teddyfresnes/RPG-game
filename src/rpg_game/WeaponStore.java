package rpg_game;

import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import weapons.Axe;
import weapons.Hammer;
import weapons.Knife;

public class WeaponStore
{
	ArrayList<Weapon> stock;
	private String shop_name;
	
	public int shopX;
	public int shopY;
	
	
	public WeaponStore(String shop_name, int X, int Y)
	{
		this.shop_name = shop_name;
		this.shopX = X;
		this.shopY = Y;
		
		this.stock = new ArrayList<Weapon>();
		
		// Armes ajoutés par défaut à la création de chaque shop
		this.stock.add(new Knife("Cutter", 10, 3));
		this.stock.add(new Hammer("Marteau", 20, 5));
		this.stock.add(new Axe("Hachette", 35, 15));
		this.stock.add(new Knife("Couteau de chasse", 100, 35));
		
		// on peut changer l'image par défaut, par exemple pour le cutter
		this.stock.get(0).define_ascii_art("assets/img/cutter.png");
	}
	
    public void displayStore(Player joueur, JFrame window, Map carte)
    {
    	window.getContentPane().removeAll();
    	
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(229, 206, 177));

        // titre
        JLabel titleLabel = new JLabel("Bienvenue "+joueur.username+"! Vous avez "+joueur.money+"💲 d'argent, que voulez-vous acheter?");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // boutons d'achat
        JPanel buttonsPanel = new JPanel(new GridLayout(1, stock.size()));
        buttonsPanel.setBackground(new Color(229, 206, 177));
        for (int i = 0; i < stock.size(); i++) {
            Weapon itemStock = stock.get(i);

            //pannel bouton
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBackground(new Color(229, 206, 177));
            itemPanel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10)); // marge

            // label top
            JLabel nameLabel = new JLabel(itemStock.name());
            nameLabel.setHorizontalAlignment(JLabel.CENTER);
            itemPanel.add(nameLabel, BorderLayout.NORTH);

            // fat button
            JButton buyButton = new JButton();
            ImageIcon originalIcon = new ImageIcon(itemStock.ascii());
            Image originalImage = originalIcon.getImage();
            Image resizedImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(resizedImage);

            buyButton.setIcon(resizedIcon);
            buyButton.setBackground(Color.LIGHT_GRAY);
            itemPanel.add(buyButton, BorderLayout.CENTER);

            // labels argent degat en dessous
            JLabel priceLabel = new JLabel("Prix: " + itemStock.price() + "💲");
            JLabel damageLabel = new JLabel("Dégâts: " + itemStock.damage() + "⚔");

            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setBackground(new Color(229, 206, 177));
            priceLabel.setHorizontalAlignment(JLabel.CENTER); // Centrer le texte
            damageLabel.setHorizontalAlignment(JLabel.CENTER); // Centrer le texte
            infoPanel.add(priceLabel);
            infoPanel.add(damageLabel);
            itemPanel.add(infoPanel, BorderLayout.SOUTH);
            
            // ajout gestionnaire event au bouton d'achat
            buyButton.addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                	int result = joueur.buyWeapon(itemStock);
                	
                	mainPanel.removeAll();
                	
                    // label final
                    JLabel resultLabel = new JLabel();
                    resultLabel.setHorizontalAlignment(JLabel.CENTER);
                    mainPanel.add(resultLabel, BorderLayout.CENTER);
                    
                    if (result == 1)
                    {
                    	resultLabel.setText("Achat de "+itemStock.name()+" réussi !");
                    }
                    else
                    {
                    	resultLabel.setText("Pas assez d'argent pour acheter "+itemStock.name()+".");
                    }
                    resultLabel.setForeground(result == 1 ? Color.GREEN : Color.RED);
                    
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
                }
            });

            // ajout du pannel final de l'item
            buttonsPanel.add(itemPanel);
        }
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);

        // infos joueurs
        JPanel playerInfoPanel = new JPanel(new GridLayout(2, 1));
        playerInfoPanel.setBackground(new Color(229, 206, 177));
        playerInfoPanel.add(new JLabel("Argent: " + joueur.money));
        playerInfoPanel.add(new JLabel("Dégâts: " + joueur.damage));
        mainPanel.add(playerInfoPanel, BorderLayout.SOUTH);

        // boutons store
        JButton exitButton = new JButton("Quitter le magasin");
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

        // ajout final
        window.add(mainPanel, BorderLayout.CENTER);
        window.revalidate();
        window.repaint();
    }
	
	
	// Efface tout le stock
	public void reset()
	{
		stock.clear();
	}
	
	
	// Ajoute l'item s'il n'existe pas
	public void add(Weapon item)
	{
	    for (int i = 0; i < stock.size(); i++)
	    {
	        Weapon item_stock = stock.get(i);
	        if (item.name().equals(item_stock.name()))
	        {
	            return;
	        }
	    }
	    stock.add(item);
	}
	
	
	// Efface l'item en cherchant son nom
	public void remove(Weapon item)
	{
	    for (int i = 0; i < stock.size(); i++)
	    {
	        Weapon item_stock = stock.get(i);
	        if (item.name().equals(item_stock.name()))
	        {
	            stock.remove(i);
	        }
	    }
	}
}
