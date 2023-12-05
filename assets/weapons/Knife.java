package weapons;

import rpg_game.Weapon;

public class Knife extends Weapon
{
    public Knife(String name, int damage, int price)
    {
        super(name, damage, price);
        super.define_ascii_art("assets/img/knife.png");
    }
}
