package characters;

import rpg_game.Player;

public class Mage extends Player
{

    public Mage(String username)
    {
        super(username);
        
        super.category = "Mage";
    	super.exp_multiplier = 2;
    }
}
