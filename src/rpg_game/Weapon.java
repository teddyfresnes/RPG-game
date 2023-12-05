package rpg_game;

public class Weapon
{
	private String name;
	private int price;
	private int damage;
	private String ascii_art;
	
	
	public Weapon(String name, int price, int damage)
	{
		this.name = name;
		this.price = price;
		this.damage = damage;
		this.ascii_art = ""; // par défaut pas d'ascii mais possible de l'ajouter
	}
	
	
	public String name()
	{
		return name;
	}	
	public int price()
	{
		return price;
	}
	public int damage()
	{
		return damage;
	}
	public String ascii()
	{
		return ascii_art;
	}
	
	
	public void define_ascii_art(String ascii_art)
	{
		this.ascii_art = ascii_art;
	}
}

