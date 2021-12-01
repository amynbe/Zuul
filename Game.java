import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes and Katia Bennamane
 * @version 2016.02.29
 */

public class Game 
{
    private Parser parser;
    private Player player;
    private Room previousRoom;
    private Room room;
    private Stack<Room> roomHistory;
    
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {        
        player = new Player();
        Room startRoom = createRooms();        
        player.enterRoom(startRoom); // start game outside
        parser = new Parser();
        roomHistory = new Stack<Room>();
    }
    
    /**
     * Create all the rooms and link their exits together.
     * 
     * @return Returns the starting room
     */
    private Room createRooms()
    {
        Room outside, reception, waitingRoom, janitorOffice, storeRoom,
        doctorOffice, medicineCabinet, surgeryRoom, emergencyRoom, lab;
    
        // create the rooms
        outside = new Room("outside the front door of the hospital");
        reception = new Room("in reception room of the hospital");
        waitingRoom = new Room("in the waiting room");
        janitorOffice = new Room("in the janitor's office");
        storeRoom = new Room("in the store room");
        doctorOffice = new Room("in the doctor's office");
        medicineCabinet = new Room("in the medicine cabinet");
        surgeryRoom = new Room("in the surgery room");
        emergencyRoom = new Room("in the emergency room");
        lab = new Room("in the hospital lab");

        // initialise room exits
        outside.setExit("north", reception);

        reception.setExit("north", waitingRoom);
        reception.setExit("east", emergencyRoom);
        reception.setExit("south", outside);

        emergencyRoom.setExit("south", reception);
        emergencyRoom.setExit("west", surgeryRoom);

        waitingRoom.setExit("west", janitorOffice);
        waitingRoom.setExit("north", doctorOffice);
        waitingRoom.setExit("south", reception);
    
        janitorOffice.setExit("north", storeRoom);
        janitorOffice.setExit("east", waitingRoom);
    
        storeRoom.setExit("south", janitorOffice);
    
        doctorOffice.setExit("north", surgeryRoom);
        doctorOffice.setExit("west", medicineCabinet);
        doctorOffice.setExit("south", waitingRoom);
    
        medicineCabinet.setExit("south", doctorOffice);
    
        surgeryRoom.setExit("north", lab);
        surgeryRoom.setExit("south", doctorOffice);
        surgeryRoom.setExit("east", emergencyRoom);
    
        lab.setExit("south", surgeryRoom);
    
        // put items in the room
        janitorOffice.addItem(new Item("key", "a key that opens the medicine cabinet", 300));
        storeRoom.addItem(new Item("camomile", "camomile, needed for your antidote!", 75));
        doctorOffice.addItem(new Item("mint","mint, needed for your antidote!", 50));
        doctorOffice.addItem(new Item("filing cabinet", "", 2000));
        medicineCabinet.addItem(new Item("honey","honey, needed for your antidote!", 100));
        surgeryRoom.addItem(new Item("mistletoe","mistletoe, needed for your antidote!", 80));
        emergencyRoom.addItem(new Item("mugwort","mugwort, needed for your antidote!", 40));

        return reception;
    }

    /**
     *  Main play routine. Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
    
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
    
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zombies!");
        System.out.println("World of Zombies is a new text-based adventure game.");
        System.out.println("You are in the reception room in an abandoned hospital. There are zombies outside - you can't leave until you have the antidote or you will die!");
        System.out.println("Your task is to collect all the ingredients then go to the lab room to win the game");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * If this command ends the game, true is returned, otherwise false is
     * returned.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;
    
        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }
    
        String commandWord = command.getCommandWord();
        if (commandWord.equals("help"))
            printHelp();
        else if (commandWord.equals("go"))
            goRoom(command);
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        } 
        else if (commandWord.equals("back")) {
            goBack(command);
        }
        else if (commandWord.equals("take")) {
            take(command);
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }        
        else if (commandWord.equals("backpack")) {
            printBackpack();
        }
        return wantToQuit;
    }

    // implementations of user commands:
    
    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }
    
    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }
    
        String direction = command.getSecondWord();
    
        // Try to leave current room.
        Room nextRoom = player.getCurrentRoom().getExit(direction);
    
        if (nextRoom == null)
            System.out.println("There is no door!");
        else {
            roomHistory.push(player.getEnterRoom());
            player.enterRoom(nextRoom);
            System.out.println(player.getLongDescription());
        }
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game. Return true, if this command
     * quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else{
            return true;  // signal that we want to quit
        }
    }
    
    /**
     * Go back to the previous room.
     */
    
    private void goBack(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Back where?");
            return;
        }
        
        //Room nextRoom = player.getCurrentRoom().getExit(direction);
        
        if (roomHistory.isEmpty())
            System.out.println("You can't go back to nothing!");
        else {
            previousRoom = roomHistory.pop();
            //currentRoom = nextRoom;
            //player.currentRoom();
            //player.getEnterRoom();
            //player.enterRoom(nextRoom);
            System.out.println(player.getLongDescription());
        }
    }
    
    /** 
     * Try to take an item from the current room. If the item is there,
     * pick it up, if not print an error message.
     */
    private void take(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            System.out.println("What do you want to take?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.pickUpItem(itemName);

        if(item == null) {
            System.out.println("Can't pick up the item: " + itemName);
        } else {
            System.out.println("Picked up " + item.getName());
        }
    }

    /** 
     * Drops an item into the current room. If the player carries the item
     * drop it, if not print an error message.
     */
    private void drop(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to drop...
            System.out.println("What do you want to drop?");
            return;
        }

        String itemName = command.getSecondWord();
        Item item = player.dropItem(itemName);

        if(item == null) {
            System.out.println("You don't carry the item: " + itemName);
        } else {
            System.out.println("Dropped " + item.getName());
        }
    }

    /**
     * Prints out the items that the player is currently carrying.
     */
    private void printBackpack() {
        System.out.println(player.getBackpackString());   
    }
}
