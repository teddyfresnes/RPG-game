package rpg_game;

public abstract class Entity
{
    public int x;
    public int y;
    public int hp; 

    
    public Entity(int x, int y, int hp)
    {
        this.x = x;
        this.y = y;
        this.hp = hp;
    }
}