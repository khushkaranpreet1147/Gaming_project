import java.util.Scanner;

public class Game {
    private Scanner scanner;
    private Player player;
    private Room currentRoom;
    private AIManager aiManager; // Our AI Narrator

    public Game() {
        scanner = new Scanner(System.in);
        player = new Player(20);
        aiManager = new AIManager(); // Initialize AI
        setupWorld();
    }

    private void setupWorld() {
        Room dungeonEntrance = new Room("You are standing in a dark, damp stone entryway. A faint light flickers to the north.");
        Room armory = new Room("You are in an old armory. Rusted swords litter the floor. A path leads south back to the entrance, or east.");
        Room treasureRoom = new Room("You are in a glowing chamber filled with gold coins! You found the treasure!");

        armory.monster = new Monster("Goblin", 10, 4);
        armory.item = "potion";

        dungeonEntrance.north = armory;
        armory.south = dungeonEntrance;
        armory.east = treasureRoom;
        treasureRoom.west = armory;

        currentRoom = dungeonEntrance;
    }

    public void start() {
        boolean playing = true;
        System.out.println("=== AI-POWERED OOP TEXT RPG ===");

        while (playing) {
            System.out.println("\n----------------------------------------");
            
            // Ask AI to dynamically describe the room based on state!
            String prompt = "Describe this fantasy text RPG room in 2 atmospheric sentences: " + currentRoom.description;
            System.out.println("🤖 AI Narrator: " + aiManager.generateNarration(prompt));

            if (currentRoom.monster != null && currentRoom.monster.health > 0) {
                System.out.println("⚠️ A wild " + currentRoom.monster.name + " (HP: " + currentRoom.monster.health + ") is blocking your path!");
            }

            if (currentRoom.item != null) {
                System.out.println("✨ You see a health " + currentRoom.item + " on the ground.");
            }

            System.out.print("What do you want to do? (north, south, east, west, attack, take, inventory, drink, status, quit) > ");
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("quit")) {
                System.out.println("Thanks for playing!");
                playing = false;
            } 
            else if (input.equals("status")) {
                System.out.println("Your Health: " + player.health + "/" + player.maxHealth);
            }
            else if (input.equals("inventory")) {
                if (player.inventory.isEmpty()) {
                    System.out.println("Your inventory is empty.");
                } else {
                    System.out.println("Inventory: " + player.inventory);
                }
            }
            else if (input.equals("take")) {
                if (currentRoom.item != null) {
                    player.inventory.add(currentRoom.item);
                    System.out.println("You picked up the " + currentRoom.item + "!");
                    currentRoom.item = null;
                } else {
                    System.out.println("There is nothing here to take!");
                }
            }
            else if (input.equals("drink")) {
                if (player.inventory.contains("potion")) {
                    if (player.health < player.maxHealth) {
                        player.heal(player.maxHealth);
                        player.inventory.remove("potion");
                        System.out.println("🧪 You drank the health potion. Your health is fully restored!");
                    } else {
                        System.out.println("Your health is already full!");
                    }
                } else {
                    System.out.println("You don't have a health potion in your inventory!");
                }
            }
            else if (input.equals("attack")) {
                if (currentRoom.monster != null && currentRoom.monster.health > 0) {
                    int playerDamage = 5;
                    currentRoom.monster.health -= playerDamage;
                    System.out.println("You strike the " + currentRoom.monster.name + " for " + playerDamage + " damage!");

                    if (currentRoom.monster.health <= 0) {
                        System.out.println("🎉 You defeated the " + currentRoom.monster.name + "!");
                    } else {
                        player.takeDamage(currentRoom.monster.attackPower);
                        System.out.println("The " + currentRoom.monster.name + " hits you back for " + currentRoom.monster.attackPower + " damage!");
                        
                        if (player.health <= 0) {
                            System.out.println("💀 You have been slain! Game Over.");
                            playing = false;
                        }
                    }
                } else {
                    System.out.println("There is nothing here to attack!");
                }
            }
            else if (input.equals("north")) {
                if (currentRoom.north != null) {
                    currentRoom = currentRoom.north;
                } else {
                    System.out.println("You can't go that way!");
                }
            } 
            else if (input.equals("south")) {
                if (currentRoom.south != null) {
                    currentRoom = currentRoom.south;
                } else {
                    System.out.println("You can't go that way!");
                }
            }
            else if (input.equals("east")) {
                if (currentRoom.monster != null && currentRoom.monster.health > 0) {
                    System.out.println("The " + currentRoom.monster.name + " blocks your way! You must defeat it first.");
                } else {
                    if (currentRoom.east != null) {
                        currentRoom = currentRoom.east;
                    } else {
                        System.out.println("You can't go that way!");
                    }
                }
            }
            else if (input.equals("west")) {
                if (currentRoom.west != null) {
                    currentRoom = currentRoom.west;
                } else {
                    System.out.println("You can't go that way!");
                }
            }
            else {
                System.out.println("Unknown command!");
            }
        }
        scanner.close();
    }
}