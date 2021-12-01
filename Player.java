/**
 * This is the representation of a player in the game Zuul.
 * 
 * @author Katia Bennamane
 * @version 2021.12.03
 */

public class Player
{
    // The player's name.
    private String name;
    // The room the player is in.
    private Room currentRoom;
    // The item's the player is holding.
    private Room previousRoom;

    private Backpack items = new Backpack();
    // The maximum weight the player can hold.
    private int maxWeight;

    /**
     * Constructor for objects of class Player
     */
    public Player()
    {
        this.maxWeight = 345;
    }

    /**
     * Enter the given room.
     * @param room The room entered.
     */
    public void enterRoom(Room room)
    {
        currentRoom = room;
    }

    public Room getEnterRoom()
    {
        return currentRoom;
    }

    /**
     * Gets the room in which the player is currently located.
     * @return The current room.
     */
    public Room getCurrentRoom()
    {
        return currentRoom;
    }

    // /**
     // * Get the name of the player.
     // * @return The player's name
     // */
    // public String getName()
    // {
        // return name;
    // }

    /**
     * Returns a string describing the items that the player carries.
     * @return A description of the items held.
     */
    public String getBackpackString()
    {
        return "You are carrying: " + items.getShortDescription();
    }

    /**
     * Returns a string describing the players current location and which
     * items the player carries.
     * @return A description of the room and items held.
     */
    public String getLongDescription()
    {       
        String returnString = currentRoom.getLongDescription();
        //returnString += "\" + getBackpackString();
        return returnString;
    }

    /**
     * Tries to pick up the item from the current room.
     * @param itemName The item to be picked up.
     * @return If successful this method will return the item that was picked up.
     */
    public Item pickUpItem(String itemName)
    {
        if(canPickItem(itemName)) {
            Item item = currentRoom.removeItem(itemName);
            items.put(itemName, item);            
            return item;
        } 
        else {
            return null;
        }
    }

    /**
     * Tries to drop an item into the current room.
     * @param itemName The item to be dropped.
     * 
     * @return If successful this method will return the item that was dropped.
     */
    public Item dropItem(String itemName)
    {
        Item item = items.remove(itemName);
        if(item != null) {
            currentRoom.addItem(item);            
        }
        return item;
    }

    /**
     * Checks if we can pick up the given item. This depends on whether the item 
     * actually is in the current room and if it is not too heavy.
     * @parem itemName The item to be picked up.
     * @return true if the item can be picked up, false otherwise.
     */
    private boolean canPickItem(String itemName)
    {
        boolean canPick = true;
        Item item = currentRoom.getItem(itemName);
        if(item == null) {
            canPick = false;
        }
        else {
            int totalWeight = items.getTotalWeight() + item.getWeight();
            if(totalWeight > maxWeight) {
                canPick = false;
            }
        }
        return canPick;         
    }
}