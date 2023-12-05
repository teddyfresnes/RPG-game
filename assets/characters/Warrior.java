package characters;

import rpg_game.Player;

public class Warrior extends Player
{

    public Warrior(String username)
    {
        super(username);
        
        super.category = "Guerrier";
    	super.damage_multiplier = 1.5;
    	super.damage = 2;
    }
}
