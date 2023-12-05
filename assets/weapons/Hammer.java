package weapons;

import rpg_game.Weapon;

public class Hammer extends Weapon
{
    public Hammer(String name, int damage, int price)
    {
        super(name, damage, price);
        super.define_ascii_art("assets/img/hammer.png");
    }
}