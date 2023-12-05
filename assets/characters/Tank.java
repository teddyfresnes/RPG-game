package characters;

import rpg_game.Player;

public class Tank extends Player
{
    public Tank(String username)
    {
        super(username);
        
        super.category = "Tank";
    	super.hp_multiplier = 1.5;
    	super.hp = 20;
    }
}
