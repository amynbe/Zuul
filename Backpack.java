import java.util.HashMap;
import java.util.Iterator;

/**
 * A list of items.
 * 
 * @author  Katia Bennamane
 * @version 2021.12.03
 */
public class Backpack
{
    // A map of item names to items.
    private HashMap<String, Item> items;
    
    /**
     * Create a new item list.
     */
    public Backpack()
    {
        items = new HashMap<String, Item>();
    }
    
    /**
     * Return an Iterator over the items.
     * @return An Iterator.
     */
    public Iterator<Item> iterator()
    {
        return items.values().iterator();
    }

    /**
     * Remove the given item.
     * @param name The name of the item to be removed.
     */
    public Item remove(String name)
    {
        return items.remove(name);
    }
    
    /**
     * Put the given item in the list.
     * @param name The name of the item.
     * @param value The item.
     */
    public void put(String name, Item value)
    {
        items.put(name, value);
    }
    
    /**
     * Return the item with the given name
     * @param name The name of the item to return
     * @return The named item, or null if it is not in the list.
     */
    public Item get(String name)
    {
        return items.get(name);
    }
    
    /**
     * Return a string listing the descriptions of the
     * items in the list.
     */
    public String getLongDescription() 
    {
        String returnString = "";
        for(Iterator<Item> iter = items.values().iterator(); iter.hasNext(); ){
            returnString += "  " + iter.next().getDescription();
        }
        return returnString;     
    }
    
    /**
     * Return a string listing the names of the
     * items in the list.
     */
    public String getShortDescription() 
    {
        String returnString = "";
        for(Iterator<Item> iter = items.values().iterator(); iter.hasNext(); ){
            returnString += iter.next().getName() + ", " ;
        }
        return returnString;     
    }
    
    /**
     * Return the total weight of all items in the list.
     * @return The total weight
     */
    public int getTotalWeight()
    {
        int weight = 0;
        for(Iterator<Item> iter = items.values().iterator(); iter.hasNext(); ) {
            weight += iter.next().getWeight();
        }
        return weight;        
    }
}
