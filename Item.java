import java.util.HashMap;

/**
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This class contains the properties of an item.
 *
 * @author Matthijs Dirksen
 * @version 12-6-2018
 */
public class Item
{
    private String itemDescription;     // holds the description of an item
    private int itemWeight;             // holds the weight of an item in grams

    /**
     * Constructor for objects from the class Item
     */
    public Item(String itemDescription, int itemWeight)
    {
        this.itemDescription = itemDescription;
        this.itemWeight = itemWeight;
    }

    /**
     * Returns the item description
     */
    public String getItemDescription()
    {
        return itemDescription;
    }

    /**
     * Returns the item weight in grams
     */
    public int itemWeight()
    {
        return itemWeight;
    }
}
