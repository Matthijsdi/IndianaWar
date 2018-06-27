import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  The exits are labelled north, 
 * east, south, west.  For each direction, the room stores a reference
 * to the neighboring room, or null if there is no exit in that direction.
 * 
 * @author Matthijs Dirksen
 * @version 12-6-2018
 */
public class Room 
{
    private String description;
    private HashMap<String, Room> exits;
    private ArrayList<Item> items;
    private String itemRequirement;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
        items = new ArrayList<Item>();
        
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }

    /**
     * Return the exits for this room.
     */
    public Room getExit(String direction)
    {
        return (Room)exits.get(direction);
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString() {
        String returnString = "Exits in Room:";
        Set keys = exits.keySet();
        for(Iterator iter = keys.iterator(); iter.hasNext();)
        {
            returnString += " [" + iter.next() + "]";
        }
        return returnString.toUpperCase();
    }
    
    /**
     * Set the required item ID to access a room
     * @param int The required item ID 
     */
    public void setItemRequirement(String itemRequirement)
    {
        this.itemRequirement = itemRequirement;
    }
    
    /**
     * Return the required item ID to access a room
     * @return int The required item ID 
     */
    public String getItemRequirement()
    {
        return itemRequirement;
    }

    /**
     * Get the item's weight.
     */
    public Item getItemWeight(int itemWeight)
    {
        return (Item)items.get(itemWeight);
    }

    /**
     * @return The description of the room.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * gets all items in that particulal room
     */
    public Item getItem(String itemName) {
        for (Item item : items) {
            if (item.getItemDescription().equalsIgnoreCase(itemName)) {
                return item;
            }
        }
        return null;
    }

    /**
     * Return a string with the items in a room.
     * & Tekst
     */
    public String getItemsInRoom() {
        String returnString = "Items In Room:";
        for (Item item : items) {
            returnString += " [" + item.getItemDescription() + "]";
        }
        return returnString.toUpperCase();
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return description + "\n" + getExitString() + "\n" + getItemsInRoom();
    }

    /**
     * Has Items as object and as Strings.
     */
    public void addItem(Item item)
    {
        items.add(item);
    }

    /**
     * Remove an item from the room.
     */
    public void removeItem(String itemDescription)
    {
        Iterator<Item> itit = items.iterator();

        if (itit.hasNext()) {
            Item item = itit.next();

            if (item.getItemDescription().equalsIgnoreCase(itemDescription)) {
                itit.remove();
            }

        }
    }

}
