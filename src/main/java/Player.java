public class Player extends Actor{
    Player(){
        super("Player", 1, 8, 1, 1);
    }

    Player(String name, int level, int health, int baseAttack, int baseDefense) throws IllegalArgumentException{
        super(name, level, health, baseAttack, baseDefense);
    }

    public void die(){
        setAlive(false);
        setCurrency((int) (getCurrency()*0.75));
    }
}
