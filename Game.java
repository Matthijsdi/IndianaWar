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
 * @author Matthijs Dirksen
 * @version 12-6-2018
 */

public class Game 
{
    private Parser parser;
    private Player player;
    private int time;
    private int started;
    
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        player = new Player();
        parser = new Parser();
        time = 20;
        started = 0;
        createRooms();
    }
    
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        //create the rooms and streets
        Room camp, campExit, blacksmith, warehouse, doctersOffice, mineEntrance, mineMain, mineNorth, mineWest;
        Room minePuzzle, mineBackdoor, bridge, underBridge, enemyFrontline, enemyCamp, winnersRoom; 
          
        // create the rooms
        camp = new Room("You are inside our camp.");
        campExit = new Room("You are about to leave the camp, it will be very dangerous outside. Are you sure?");
        blacksmith = new Room("This is the Blacksmith, Maybe you can find some helpful tools here.");
        warehouse = new Room("Now entering the warehouse. You found some wood.");
        doctersOffice = new Room("This is the docters office. you can heal yourself here.");
        mineEntrance = new Room("Watch out when entering the mine, you can easily lose your way here and may not be able to go back.");
        mineMain = new Room("You've entered the mine."); 
        mineNorth = new Room("Rocks fall as you enter, you can only go back.");
        mineWest = new Room("This way leads to to boss, only enter when ready. You can't go back after you enter.");
        minePuzzle = new Room("");
        mineBackdoor = new Room("You Made it, well done!");
        bridge = new Room("Try to find a plank, they are scattered around the map.");
        underBridge = new Room("There seems to be a way under the bridge but you will need the key.");
        enemyFrontline = new Room("As you walk over the bridge you see the first defenses of the enemy.");
        enemyCamp = new Room("You are now in the enemy camp search foor the documents.");
        winnersRoom = new Room("Congrats! You found the documents! You can now go back to your own camp.");
        
        // create the items and place them in rooms
        blacksmith.addItem(new Item("key", 50));
        mineNorth.addItem(new Item("wood", 10));
        warehouse.addItem(new Item("wood", 10));
        mineBackdoor.addItem(new Item("wood", 10));
             

        // initialise room exits
        camp.setExit("north", blacksmith);
        camp.setExit("east", campExit);
        camp.setExit("south", doctersOffice);
        camp.setExit("west", mineEntrance);
        
        doctersOffice.setExit("north", camp);
        
        blacksmith.setExit("south", camp);
        blacksmith.setExit("west", warehouse);
        
        warehouse.setExit("east", blacksmith);
        warehouse.setExit("south", mineEntrance);
        
        mineEntrance.setExit("north", warehouse);
        mineEntrance.setExit("east", camp);
        mineEntrance.setExit("west", mineMain);
        
        mineMain.setExit("north", mineNorth);
        mineMain.setExit("east", mineEntrance);
        mineMain.setExit("west", mineWest);
        
        mineNorth.setExit("south", mineMain);
        
        mineWest.setExit("east", mineMain);
        mineWest.setExit("south", minePuzzle);
        
        minePuzzle.setExit("east", mineBackdoor);
        
        mineBackdoor.setExit("east", camp);
        
        campExit.setExit("east", bridge);
        campExit.setExit("west", camp);
        
        bridge.setExit("north", underBridge);
        bridge.setExit("west", campExit);
        //bridge.setExit("east", enemyFrontline);
        bridge.setItemRequirement("wood");
                   
        enemyFrontline.setExit("east", enemyCamp);
        
        enemyCamp.setExit("south", winnersRoom);
        
        underBridge.setExit("east", enemyCamp);       
                
        if(started == 0)
        {
            player.setCurrentRoom(camp);  // start game outside
            started += 1; 
        }
        else if(player.getItemRequired()) 
        {   
            bridge.setExit("east", enemyFrontline);
            System.out.println("it worked 2");            
        }
    }
    
    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        player.printPlayerName();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            createRooms();
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.print('\u000C');
        System.out.println();
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("We welcome you to the world of the ancient gods.");
        System.out.println("The enemy has stolen our ancient scroll.");
        System.out.println("Your goal is to get it back.");
        System.out.println("The person that holds the scroll is granted an immortal life.");
        System.out.println("Type 'help' if you dont know what to do.");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("");
        System.out.println();
        player.printLocationInfo();
    }
    
    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("Invalid Command! ");
            availableCommands();
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            player.goRoom(command);
            moveChecker();
        }
        else if (commandWord.equals("look")) {
            player.look();
        }
        else if (commandWord.equals("use")) {
            player.use(command);
        }
        else if (commandWord.equals("get")) {
            player.pickUpItem(command);
        }
        else if (commandWord.equals("drop")) {
            player.dropItem(command);
        }
        else if (commandWord.equals("items")) {
            player.listItems();
        }
        else if (commandWord.equals("back")) {
            player.back(command);
        }
        else if (commandWord.equals("weight")) {
            player.getWeight();
        }
        else if (commandWord.equals("name")) {
            player.printPlayerName();
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = player.quit(command);
        }

        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Checks if the players movements is within
     * borders.
     */
    private void moveChecker()
    {
        time -= 1;
        if(time == 0) {
            System.out.print("You moved too many times " + player.getPlayerName() + ", game over!");
            System.exit(1);
        }
    }
   
    /**
     * Print out valid commands.
     */
    private void availableCommands()
    {
        System.out.println();
        System.out.println("--------------------------");
        System.out.print(player.getPlayerName() + ", your valid Command Words Are:\n");
        String[] validCommands = parser.showCommands();
        for (int i = 0; i < validCommands.length; i++)
        {
            System.out.print("> " + validCommands[i].toUpperCase() + "\n");
        }
        System.out.println("--------------------------");
        System.out.println();
    }

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println('\u000C');
        System.out.println("Try to escape the Mayan camp");
        System.out.println();
        System.out.println();
        System.out.println("--------------------COMMANDS--------------------");
        System.out.println("Type 'go' + direction to move.");
        System.out.println("Type 'look' to look around the room.");
        System.out.println("Type 'help' if you need help.");
        System.out.println("Type 'use' to use an item.");
        System.out.println("Type 'get' to take an item from the room.");
        System.out.println("Type 'drop' to drop your item(s) in the room");
        System.out.println("Type 'items' to see your inventory.");
        System.out.println("Type 'back' to go back to your previous room.");
        System.out.println("Type 'change_name' to change your name.");
        System.out.println("Type 'name' to get your name, if you forgot.");
        System.out.println("Type 'quit' if you want to quit the game.");
        System.out.println("------------------------------------------------");
        player.listItems();
        System.out.println();
        player.printLocationInfo();
    }
}
