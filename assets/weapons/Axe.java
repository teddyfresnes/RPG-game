package weapons;

import rpg_game.Weapon;

public class Axe extends Weapon
{
    public Axe(String name, int damage, int price)
    {
        super(name, damage, price);
        super.define_ascii_art("assets/img/axette.png");
    }
}