import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.HashMap;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This class contains the properties of a player.
 *
 * @author @author Matthijs Dirksen
 * @version 12-6-2018
 */
public class Player
{
    private String playerName;
    private int playerHealth;
    private int maxCarryWeight;
    private Room currentRoom;
    private ArrayList<Item> itemList;
    private Stack lastRoom;
    private int weight;
    private boolean itemRequired;

    /**
     * Constructor for objects from the class Player.
     */
    public Player()
    {
        playerName = "You didn't set your name yet! Use 'change_name' + \n [your name].";
        playerHealth = 100;
        lastRoom = new Stack();
        maxCarryWeight = 5000;          // set maxcarryweight 5kg
        itemList = new ArrayList();     //makes new arraylist named itemList to save items
    }

    /**
     * Print out the player name
     */
    public void printPlayerName()
    {
        System.out.println("Your name is: " + getPlayerName() + ".");
    }

    /**
     * Get player name.
     */
    public String getPlayerName()
    {
        return playerName.substring(0, 1).toUpperCase() + playerName.substring(1).toLowerCase();
    }

    /**
     * Change the name of the player.
     */
    public void setPlayerName(Command commandName)
    {
        if(!commandName.hasSecondWord())
        {
            System.out.println("Usage: change_name + [your name].");
            return;
        }
        else 
        {
            playerName = commandName.getSecondWord();
            printPlayerName();
        }
    }

    /** 
     * Try to go in one direction. If there is an exit, enter
     * the new room, otherwise print an error message.
     */
    public void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Usage: 'go' + direction you want to go");
            return;
        }

        String direction = command.getSecondWord();  

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom == null) {
            System.out.println("Invalid move! You can't go to: '" + command.getSecondWord() + "'.");
        }
        else {
            lastRoom.push(currentRoom);
            currentRoom = nextRoom;
            printLocationInfo();
        }
    }

    /**
     * Changes the maxCarryWeight of the player.
     */
    public void setMaxCarryWeight(int maxCarryWeight)
    {
        this.maxCarryWeight = maxCarryWeight;
    }

    /**
     * Changes the playerHealth of the player.
     */
    public void playerHealth(int playerHealth)
    {
        this.playerHealth = playerHealth;
    }

    /**
     * Return the player's maximum carry weight.
     */
    public int getMaxCarryWeight()
    {
        return maxCarryWeight;
    }

    /**
     * Return the room the player is currently in.
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    /**
     * Return all the rooms a player has visited.
     */
    public Stack getLastRoom()
    {
        return lastRoom;
    }

    /**
     * Changes the current room the player is in.
     */
    public void setCurrentRoom(Room currentRoom)
    {
        this.currentRoom = currentRoom;
    }

    /**
     * Return a list of items the player has.
     */
    public ArrayList getItemList()
    {
        return itemList;
    }

    /**
     * Adds item from room to player's inventory.
     */
    private void pickUpItem(Item item)
    {
        itemList.add(item);
    }

    /**
     * Removes an item from the player's inventory.
     */
    private void dropItem(Item item)
    {
        itemList.remove(item);
    }

    /**
     * Return the weight of the items carrying.
     */
    private int getTotalWeight()
    {
        int weight = 0;
        for (Item item : itemList) {
            weight += item.itemWeight();
        }
        return weight;
    }

    /**
     * Print the weight of the items carrying.
     * and the max carrying weight
     */
    public void getWeight()
    {
        System.out.println("The weight of the items you are carrying is: " + getTotalWeight());
        System.out.println("The max weight you can carry is: " + maxCarryWeight);
    }

    /**
     * Moves item from current room to player's inventory.
     */
    public void pickUpItem(Command command)
    {
        if(!command.hasSecondWord())
        {
            System.out.println("Usage: 'get' + [item to pick up]");
            return;
        }
        String itemDescription = command.getSecondWord();
        Item itemInRoom = currentRoom.getItem(itemDescription);
        if(itemInRoom == null)
        {
            System.out.println("That item is not in this room.");
            return;
        }
        if (getTotalWeight() > maxCarryWeight || (getTotalWeight() + itemInRoom.itemWeight() > maxCarryWeight)) {
            System.out.println("Not enough space");
            return;
        }
        if(itemInRoom != null)
        {
            pickUpItem(itemInRoom);
            currentRoom.removeItem(itemDescription);
            System.out.println("Got: " + itemDescription); 
            return;
        }
    }

    /**
     * gets all items in that particular room.
     */
    public Item getItem(String itemName) {
        for (Item item : itemList) {
            if (item.getItemDescription().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Moves item from player's inventory to current room.
     */
    public void dropItem(Command command)
    {
        if(!command.hasSecondWord())
        {
            System.out.println("Usage: 'drop' + [item to drop]");
            return;
        }
        String itemDescription = command.getSecondWord();
        Item itemInInventory = getItem(itemDescription);
        if(itemInInventory != null)
        {
            dropItem(itemInInventory);
            System.out.println("Dropped: " + itemDescription);
            currentRoom.addItem(itemInInventory);
        }
        else {
            System.out.println("You don't have this item in your inventory!");
        }
    }

    /**
     * Use an item to get to new places
     */
    public void use(Command command)
    {
        if(!command.hasSecondWord())
        {
            System.out.println("Usage: 'use' + [what to use]");
            return;
        }
        String itemDescription = command.getSecondWord();
        Item itemInInventory = getItem(itemDescription);
        
        if(itemInInventory != null)
        {
            if (itemDescription.equals("wood") && currentRoom.getDescription().equals("Try to find a plank, they are scattered around the map."))
            {
                itemRequired = true;
                dropItem(itemInInventory);
            }
            else
            {
                System.out.println("You can't use that here.");
            }
        }
        else
        {
            System.out.println("You don't have this item in your inventory.");
        }
    }
    
    /**
     * Return itemRequired
     * lets the game know you have the right item.
     */
    public boolean getItemRequired()
    {
        return itemRequired;
    }
    
    /**
     * Prints out what is in the room for when the user
     * cant read it anymore.
     */
    public void look()
    {
        System.out.print('\u000C');
        printLocationInfo();
    }

    /**
     * Go back to the previous room.
     */
    public void back(Command command)
    {
        if(command.hasSecondWord())
        {
            System.out.println("Usage: 'back'");
            return;
        }

        if(!lastRoom.empty())
        {
            currentRoom = (Room) lastRoom.pop();    //deletes last object from the stack
            System.out.println("Went back to the previous room.");
            printLocationInfo();
        }
        else
        {
            System.out.println("You can't go back here. this is where you started");
            printLocationInfo();
        }
    }

    /**
     * List the items the player has in his inventory.
     */
    public void listItems()
    {
        String returnString = "Items in your inventory:";
        for(Iterator iter = itemList.iterator(); iter.hasNext();)
        {
            Item arrayItem = (Item)iter.next();
            returnString += " [" + (String) arrayItem.getItemDescription().toUpperCase() + "]";
        }
        System.out.println(returnString);
    }

    /**
     * Print information about the current room.
     */
    public void printLocationInfo()
    {
        System.out.println(currentRoom.getLongDescription());
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    public boolean quit(Command command)
    {
        if(command.hasSecondWord())
        {
            System.out.println("If you want to quit the game, type: 'quit'.");
            return false;
        }
        else 
        {
            System.out.print('\u000C');
            return true;  // signal that we want to quit
        }
    }
}