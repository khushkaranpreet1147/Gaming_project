import java.util.ArrayList;
import java.util.List;

public class Player {
    public int health;
    public int maxHealth;
    public List<String> inventory; // Can hold multiple items (potions, keys, swords, etc.)

    public Player(int startingHealth) {
        this.health = startingHealth;
        this.maxHealth = startingHealth;
        this.inventory = new ArrayList<>(); // Initialize an empty inventory list
    }

    // Helper method to handle damage
    public void takeDamage(int amount) {
        this.health -= amount;
        if (this.health < 0) {
            this.health = 0;
        }
    }

    // Helper method to handle healing
    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }
}

