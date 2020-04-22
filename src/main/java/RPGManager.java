import com.fasterxml.jackson.annotation.JsonIgnore;

public class RPGManager {
    public LocationList locations;
    //public achievements AchivementList;

    @JsonIgnore
    public User user;
    @JsonIgnore
    public PlayerCharacter player;
    @JsonIgnore
    private CombatUI combat;

    /************* BACKEND ****************/

    public RPGManager(){
        this(false, new User("TEMPORARY","1234")); //TODO
            }

    public RPGManager(boolean genDefault, User user){
        this.user = user;
        this.player = user.getCharacter();

        if(genDefault){
            System.out.println("[NOTICE][Generating Default RPGManager unimplemented.]");
        }
    }

    public LocationList getLocations() {
        return locations;
    }
    /************* LOCATIONS *************/

    public String goTo(Location location){ //TODO delete if uneeded
        return "[NOTICE][ Unimplemented content.]";
    }
    public String goTo(String locationName){
        //return "[SUCCESS][ Went to "+locationName+"].";

        return "[NOTICE][ Unimplemented content.]";
    }

    /**
     * @return String describing player's current location
     */
    public String look(){
        //TODO player char obj stores current location
        return "[NOTICE][ Unimplemented content.]";
    }

    public String viewLocations(){
        return locations.toString();
    }

    /************* NPCs *************/

    public void shop(String name){
        //TODO return anything? handle more in UI?
    }

    public String talk(String name){ //TODO dialogue options? handle that in UI? return list of Strings maybe?
        return "[NOTICE][ Unimplemented content.]";
    }

    /************* CHARACTER *************/

    public String equip(String name){
        //return "[SUCCESS][ Item equipped.]";
        return "[NOTICE][ Unimplemented content.]";
    }

    public String viewCharacter(){
        return player.toString();
    }

    /************* COMBAT *************/

    public void fight(String name){
        //combat.commandHandler(player, enemy);
    }


}
