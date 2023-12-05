package characters;

import rpg_game.Player;

public class Goblin extends Player
{

    public Goblin(String username)
    {
        super(username);
        
        super.category = "Gobelin";
    	super.money_multiplier = 2;
    	super.money = 25;
    }
}
