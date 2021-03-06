import java.util.InputMismatchException;
import java.util.Scanner;

public class CombatUI {
    public Player player;
    public Monster enemy;
    private int currentTurnNum;
    private String result; //W- Win | L- Lose | F- Fled
    Scanner input;

    CombatUI(Player player, Monster enemy) {
        this.player=player;
        this.enemy=enemy;
        currentTurnNum = 0;
        result = "";

        input = new Scanner(System.in);
    }

    public void handleTurn(){
        if(player.getLevel() < enemy.getLevel()){
            System.out.println("[WARNING][ This " + enemy.getName() +" is a higher level than you!]");
        }

        while(result.equals("")) {
            System.out.println("[ *** TURN " + (currentTurnNum + 1) + " *** ]");
            if (currentTurnNum % 2 == 0) {
                handlePlayerTurn();
            } else if (currentTurnNum % 2 == 1) {
                handleEnemyTurn();
            }
            currentTurnNum = currentTurnNum + 1;
        }
    }

    public void handlePlayerTurn(){
        if(player.getAlive()){
            System.out.println("\t"+player.getName()+ "'s Turn!");
            System.out.println();
            int barFill = (int) Math.ceil(((double) this.enemy.getCurHealth()/(double) this.enemy.getMaxHealth())*10);
            System.out.print("[" + this.enemy.getName() + "][");
            for(int i = 0; i < 10; i++){
                if(i <= (barFill-1)) {
                    System.out.print("+");
                }else{
                    System.out.print(" ");
                }
            }
            System.out.println("]");
            System.out.println();
            System.out.println("[" + this.player.getName() + "][" + this.player.getCurHealth() + "/" + this.player.getMaxHealth() + "]");
            commandPrompt();
        }else{
            eventPlayerDeath();
        }
    }

    public void handleEnemyTurn(){
        if(enemy.getAlive()){
            System.out.println("\t"+enemy.getName()+ "'s Turn!");
            actionAttack(this.enemy, this.player);
        }else{
            eventEnemyDeath();
        }
    }

    public void commandPrompt(){

        int action = -1;
        outer: //goto label used if an action fails for an internal reason and the player shouldn't lose a turn
        while(action==-1) {
            System.out.println();
            System.out.println("What will you do?");
            System.out.println("[1] ATTACK");
            System.out.println("[2] USE ITEM");
            System.out.println("[3] FLEE");
            System.out.print("[ACTION][> ");
            try {
                action = input.nextInt();

            } catch (InputMismatchException e) {
                action = -1;
            }

            switch (action) {
                case 1:
                    actionAttack(this.player, this.enemy);
                    break;
                case 2:
                    if(!actionUse(this.player)){ action=-1; continue outer;} //if the use didn't work, repeat the loop so player doesn't lose turn
                    break;
                case 3:
                    actionFlee(this.player);
                    break;
                default:
                    action = -1;
                    System.out.println("[ERROR][ Invalid action entered! ]");
                    input.nextLine(); //without this it goes on an infinite loop for some reason
                    break;
            }
        }
    }

    public void actionAttack(Actor attacker, Actor target){
        System.out.println("\t"+attacker.getName()+" attacks " + target.getName() + "!");
        int damageOutput = attacker.attack(target);
        System.out.println("\t"+attacker.getName()+" dealt " + damageOutput + " damage to " + target.getName() + ".");
    }

    public boolean actionUse(Actor attacker){
        input.nextLine(); //clears the newline after entering an int for action
        System.out.println("What usable do you want to use?");
        ItemList inventory = attacker.getInventory();
        System.out.println("Your inventory: \n" + inventory.toString());
        System.out.print("[ITEM NAME][> ");
        String name = input.nextLine();
        Item itemToUse;
        try {
            itemToUse = inventory.getItem(name);
            if (!itemToUse.getType().equals("Health")){
                System.out.println("[ERROR: You can't use that item!]");
                return false;
            }
            attacker.use((Usable) itemToUse);
            System.out.println("\t"+attacker.getName()+" uses " + itemToUse.getName() +"!");
            System.out.println("\t"+attacker.getName()+" regained " + ((Usable) itemToUse).getValue() + "health!");

            return true;
        } catch (NonExistentObjectException e) {
            System.out.println("[ERROR: You don't own that item. "+ e.getMessage() +"]");
            return false;
        }
    }

    public void actionFlee(Actor runner){
        if(Math.floor(Math.random()*100) > 50){
            System.out.println(runner.getName() + " has fled!");
            result = "F";
        }else{
            System.out.println(runner.getName() + " is unable to flee!");
        }
    }

    public void eventPlayerDeath(){
        result = "L";
        System.out.println("Your vision fades to black as you lose consciousness...");
        int lostCur = (int)((double) player.getCurrency() * 0.25);
        System.out.println("\nHey! Are you ok? Let's get you back to town. Maybe you should do some tasks to level up?\n[NOTICE][ You have lost " + lostCur + " gold.]");
        try {
            player.subtractFromCurrency(lostCur);
        } catch (InsufficientCurrencyException ignored) {} //This won't happen anyway
    }

    public void eventEnemyDeath(){
        result = "W";
        System.out.println("Victory! " + enemy.getName() + " has been defeated!");
        System.out.println("You have looted " + enemy.getCurrency() + " gold.");
        ItemList enemyInv = enemy.getInventory();
        boolean hasLooted = false; int i = 0;
        //TODO allow duplicate items to sell? need proper id system
        while(!hasLooted && i < enemyInv.getSize()){ //while nothing has been looted or there are items left to look at
            if(!player.getInventory().contains(enemyInv.getItemAt(i))){ //if player doesn't have enemy's i'th item
                player.pickUp(enemyInv.getItemAt(i));
                System.out.println("You found: " + enemyInv.getItemAt(i).getName() + " and it has been added to your inventory.");
                hasLooted = true;
            }
            i++;
        }
        player.grantCurrency(enemy.getCurrency());
    }
}
