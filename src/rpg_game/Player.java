package rpg_game;


import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;

import weapons.Knife;

public class Player
{
	public String username;
	
	public String category;
	public int money;
	public int level;
	public int exp;
	public int exp_need;
	public int mana; // mana = exp accumulé ici
	
	public int hp;
	public int damage;
	
	protected double hp_multiplier;
	protected double damage_multiplier;
	protected double exp_multiplier;
	protected double money_multiplier;
	
	
	ArrayList<Weapon> bag;
	public Weapon selectedWeapon;
	
	
	public Player(String username)
	{
		this.username = username;
		
		this.category = "Citoyen";
		this.money = 10;
		this.level = 1;
		this.exp = 0;
		this.exp_need = 15;
		this.mana = 0;
		
		
		this.hp = 10;
		this.damage = 1;
		
		this.hp_multiplier = 1;
		this.damage_multiplier = 1;
		this.exp_multiplier = 1;
		this.money_multiplier = 1;
		
		this.bag = new ArrayList<>();
		this.selectedWeapon = new Weapon("Mains nus", 0, 0);
	}
	
	
    public void display_bag()
    {
        if (bag.isEmpty())
        {
            System.out.println("Votre sac est vide.");
        }
        else
        {
            System.out.println("Contenu du sac :");
            for (int i = 0; i<bag.size(); i++)
            {
                Weapon weapon = bag.get(i);
                System.out.println("["+(i+1)+"] "+weapon.name()+" ("+weapon.damage()+"⚔)");
            }
            
            Scanner scanner = new Scanner(System.in);
            System.out.print("\nSélectionnez une arme en entrant le nombre :\n> ");
            int choice = scanner.nextInt();

            if (choice >= 1 && choice <= bag.size())
            {
                this.selectedWeapon = bag.get(choice - 1);
                System.out.println(this.selectedWeapon.name()+" selectionné.");
            }
            else
            {
            	System.out.println("Erreur !");
            }
        }
    }
	
	
    public int buyWeapon(Weapon weapon)
    {
        if (weapon.price() <= this.money)
        {
            this.bag.add(weapon);
            this.money -= weapon.price();
            System.out.println("Vous avez acheté "+weapon.name()+" pour "+weapon.price());
            return 1;
        }
        else
        {
            System.out.println("Pas assez d'argent ! (Argent : "+this.money+"💲 / Prix : "+weapon.price()+"💲)");
            return 0;
        }
    }
    
    
    public String add_exp(int exp_add)
    {
    	this.exp = this.exp + exp_add;
    	if (this.exp >= exp_need)
    	{
    		this.exp = this.exp-this.exp_need;
    		
    		//montée de niveau
    		this.level++;
    		System.out.println("\nVous montez au niveau "+this.level+" :");
    		this.exp_need = (int) (this.exp_need * 1.3); // 30% d'exp de plus nécessaire pour monter de niveau
    		
    		Random r = new Random();
    		int add_damage = (int) ((r.nextInt(1) + 1) * this.damage_multiplier);
    		int add_hp = (int) ((r.nextInt(8) + 2) * this.hp_multiplier);
    		
    		this.damage = this.damage + add_damage;
    		this.hp = this.hp + add_hp;
    		return("Montée au niveau "+this.level+":   Dégats "+this.damage+"⚔ (+"+add_damage+"⚔) | HP "+this.hp+"❤ (+"+add_hp+"❤)");
    	}
    	return "";
    }
}
