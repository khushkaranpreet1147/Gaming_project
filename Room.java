public class Room {
    public String description;
    public Room north;
    public Room south;
    public Room east;
    public Room west;
    public Monster monster; 
    public String item; // Holds an item name like "potion" or null if empty

    public Room(String description) {
        this.description = description;
        this.monster = null; 
        this.item = null;
    }
}