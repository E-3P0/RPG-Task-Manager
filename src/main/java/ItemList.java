import java.util.ArrayList;
public class ItemList {
    public ArrayList<Item> itemList;

    public ItemList() {
        itemList = new ArrayList<Item>();
    }
    public ItemList(ArrayList<Item> listIn){ itemList = listIn; }

    public void addItem(Item item) {
        itemList.add(item);
    }

    public void addItem(String name, String type, int id) {
        Item newItem = new Item(name, type, id);
        itemList.add(newItem);
    }

    public void removeItem(int id) {
        for (int i = 0; i < itemList.size(); i++) {
            if (id==(itemList.get(i).getID())) {
                itemList.remove(i);
                return;
            }
        }
        System.out.println("No item found");

    }

    public String toString(){
        String result = "{ Name \t|\t Type \t |\t Damage/Value \t |\t Defense }\n";
        if (itemList.size() == 0){
            return result.concat("\t*No items.*\n");
        }
        for(int i = 0; i < itemList.size(); i++){
            result = result.concat(itemList.get(i).toString());
            result = result.concat("\n");
        }
        return result;
    }

}
