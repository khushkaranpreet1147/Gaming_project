import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameWindow extends JFrame {
    private JTextArea displayArea;
    private JTextField inputField;
    private JLabel healthLabel;
    private JLabel inventoryLabel;

    private Player player;
    private Room currentRoom;
    private AIManager aiManager;

    public GameWindow() {
        setTitle("AI-Powered OOP Text RPG - Minor Project");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        player = new Player(20);
        aiManager = new AIManager();
        setupWorld();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(30, 30, 30));

        // Display Text Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(new Color(20, 20, 20));
        displayArea.setForeground(new Color(220, 220, 220));
        displayArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Top Status Panel
        JPanel statusPanel = new JPanel(new GridLayout(1, 2));
        statusPanel.setBackground(new Color(40, 40, 40));
        
        healthLabel = new JLabel(" ❤️ Health: " + player.health + "/" + player.maxHealth);
        healthLabel.setForeground(Color.RED);
        healthLabel.setFont(new Font("Arial", Font.BOLD, 13));

        inventoryLabel = new JLabel(" 🎒 Inventory: [Empty]");
        inventoryLabel.setForeground(Color.YELLOW);
        inventoryLabel.setFont(new Font("Arial", Font.BOLD, 13));

        statusPanel.add(healthLabel);
        statusPanel.add(inventoryLabel);
        panel.add(statusPanel, BorderLayout.NORTH);

        // Bottom Controls Panel (Buttons + Input Box)
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Quick Action Buttons Panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 2, 2));
        buttonPanel.setBackground(new Color(45, 45, 45));
        
        JButton btnNorth = new JButton("North");
        JButton btnSouth = new JButton("South");
        JButton btnEast = new JButton("East");
        JButton btnWest = new JButton("West");
        JButton btnAttack = new JButton("Attack");
        JButton btnTake = new JButton("Take");

        styleButton(btnNorth);
        styleButton(btnSouth);
        styleButton(btnEast);
        styleButton(btnWest);
        styleButton(btnAttack);
        styleButton(btnTake);

        buttonPanel.add(btnNorth);
        buttonPanel.add(btnSouth);
        buttonPanel.add(btnEast);
        buttonPanel.add(btnWest);
        buttonPanel.add(btnAttack);
        buttonPanel.add(btnTake);
        bottomPanel.add(buttonPanel, BorderLayout.NORTH);

        // Text Input Field Panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputField = new JTextField();
        inputField.setBackground(new Color(45, 45, 45));
        inputField.setForeground(Color.WHITE);
        inputField.setFont(new Font("Consolas", Font.PLAIN, 14));
        
        JButton submitButton = new JButton("Send");
        submitButton.setBackground(new Color(70, 70, 70));
        submitButton.setForeground(Color.WHITE);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(submitButton, BorderLayout.EAST);
        bottomPanel.add(inputPanel, BorderLayout.SOUTH);

        panel.add(bottomPanel, BorderLayout.SOUTH);
        add(panel);

        // Action Listeners for Buttons & Input
        ActionListener commandListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processCommand(inputField.getText().trim().toLowerCase());
                inputField.setText("");
            }
        };

        inputField.addActionListener(commandListener);
        submitButton.addActionListener(commandListener);

        // Button Click Listeners
        btnNorth.addActionListener(e -> processCommand("north"));
        btnSouth.addActionListener(e -> processCommand("south"));
        btnEast.addActionListener(e -> processCommand("east"));
        btnWest.addActionListener(e -> processCommand("west"));
        btnAttack.addActionListener(e -> processCommand("attack"));
        btnTake.addActionListener(e -> processCommand("take"));

        appendOutput("=== WELCOME TO THE AI-POWERED OOP TEXT RPG ===");
        updateRoomNarration();
    }

    private void styleButton(JButton btn) {
        btn.setBackground(new Color(60, 60, 60));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 11));
        btn.setFocusPainted(false);
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

    private void updateRoomNarration() {
        appendOutput("\n----------------------------------------");
        appendOutput("🤖 AI Narrator is thinking...");
        
        new Thread(() -> {
            // Safe, strict prompt so the AI stays on track with simple sentences
            String prompt = "Describe this room in exactly two short sentences using very easy words: " + currentRoom.description;
            String narration = aiManager.generateNarration(prompt);
            
            SwingUtilities.invokeLater(() -> {
                appendOutput("🤖 AI Narrator: " + narration);

                if (currentRoom.monster != null && currentRoom.monster.health > 0) {
                    appendOutput("⚠️ A wild " + currentRoom.monster.name + " (HP: " + currentRoom.monster.health + ") is blocking your path!");
                }

                if (currentRoom.item != null) {
                    appendOutput("✨ You see a health " + currentRoom.item + " on the ground.");
                }
            });
        }).start();
    }

    private void processCommand(String input) {
        if (input.isEmpty()) return;

        appendOutput("\n> " + input);

        if (input.equals("quit")) {
            appendOutput("Thanks for playing! You can close this window.");
            inputField.setEnabled(false);
        } 
        else if (input.equals("status")) {
            appendOutput("Your Health: " + player.health + "/" + player.maxHealth);
        }
        else if (input.equals("inventory")) {
            if (player.inventory.isEmpty()) {
                appendOutput("Your inventory is empty.");
            } else {
                appendOutput("Inventory: " + player.inventory);
            }
        }
        else if (input.equals("take")) {
            if (currentRoom.item != null) {
                player.inventory.add(currentRoom.item);
                appendOutput("You picked up the " + currentRoom.item + "!");
                inventoryLabel.setText(" 🎒 Inventory: " + player.inventory);
                currentRoom.item = null;
            } else {
                appendOutput("There is nothing here to take!");
            }
        }
        else if (input.equals("drink")) {
            if (player.inventory.contains("potion")) {
                if (player.health < player.maxHealth) {
                    player.heal(player.maxHealth);
                    player.inventory.remove("potion");
                    appendOutput("🧪 You drank the health potion. Your health is fully restored!");
                    healthLabel.setText(" ❤️ Health: " + player.health + "/" + player.maxHealth);
                    inventoryLabel.setText(" 🎒 Inventory: " + (player.inventory.isEmpty() ? "[Empty]" : player.inventory));
                } else {
                    appendOutput("Your health is already full!");
                }
            } else {
                appendOutput("You don't have a health potion in your inventory!");
            }
        }
        else if (input.equals("attack")) {
            if (currentRoom.monster != null && currentRoom.monster.health > 0) {
                int playerDamage = 5;
                currentRoom.monster.health -= playerDamage;
                appendOutput("You strike the " + currentRoom.monster.name + " for " + playerDamage + " damage!");

                if (currentRoom.monster.health <= 0) {
                    appendOutput("🎉 You defeated the " + currentRoom.monster.name + "!");
                } else {
                    player.takeDamage(currentRoom.monster.attackPower);
                    healthLabel.setText(" ❤️ Health: " + player.health + "/" + player.maxHealth);
                    appendOutput("The " + currentRoom.monster.name + " hits you back for " + currentRoom.monster.attackPower + " damage!");
                    
                    if (player.health <= 0) {
                        appendOutput("💀 You have been slain! Game Over.");
                        inputField.setEnabled(false);
                    }
                }
            } else {
                appendOutput("There is nothing here to attack!");
            }
        }
        else if (input.equals("north")) {
            if (currentRoom.north != null) {
                currentRoom = currentRoom.north;
                updateRoomNarration();
            } else {
                appendOutput("You can't go that way!");
            }
        } 
        else if (input.equals("south")) {
            if (currentRoom.south != null) {
                currentRoom = currentRoom.south;
                updateRoomNarration();
            } else {
                appendOutput("You can't go that way!");
            }
        }
        else if (input.equals("east")) {
            if (currentRoom.monster != null && currentRoom.monster.health > 0) {
                appendOutput("The " + currentRoom.monster.name + " blocks your way! You must defeat it first.");
            } else {
                if (currentRoom.east != null) {
                    currentRoom = currentRoom.east;
                    updateRoomNarration();
                } else {
                    appendOutput("You can't go that way!");
                }
            }
        }
        else if (input.equals("west")) {
            if (currentRoom.west != null) {
                currentRoom = currentRoom.west;
                updateRoomNarration();
            } else {
                appendOutput("You can't go that way!");
            }
        }
        else {
            appendOutput("Unknown command!");
        }
    }

    private void appendOutput(String text) {
        displayArea.append(text + "\n");
        displayArea.setCaretPosition(displayArea.getDocument().getLength());
    }
}