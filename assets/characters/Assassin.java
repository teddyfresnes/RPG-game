package characters;

import rpg_game.Player;

public class Assassin extends Player
{
    public Assassin(String username)
    {
        super(username);
        
        super.category = "Assassin";
    	super.hp_multiplier = 0.5;
    	super.hp = 5;
    	super.damage_multiplier = 1.75;
    	super.damage = 4;
    }
}
