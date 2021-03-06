import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("gear")
public class Gear extends Item {
    public int durability;
    public int damage;
    public int defense;

    public Gear(){
        super("","",0, 0);
    }

    public Gear(String name, String type, int id, int durability, int damage, int defense, int worth){
        super(name,type, id, worth);
        this.durability=durability;
        this.damage=damage;
        this.defense=defense;
    }

    public int getDurability() {
        return durability;
    }

    public int getDamage() {
        return damage;
    }

    public int getDefense() {
        return defense;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public void setDurability(int damage) {
        this.durability = durability;
    }

    @Override
    public String toString(){
        String result = ""; //TODO add durability and id if they matter
        result = "[NAME: "+ name+"]  [TYPE: "+type+"]  [WORTH:" + worth+"]  [DAMAGE: "+damage+"]  [DEFENSE: "+defense+"]";
        return result;
    }

}
