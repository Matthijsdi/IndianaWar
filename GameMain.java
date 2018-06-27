/**
 * The Starting point of the Doel game.
 *
 * @author Matthijs Dirksen
 * @version 12-6-2018
 */
public class GameMain
{
    private String playerName;
    private String args[];

    /**
     * The starting point of the Doel game.
     * @param args Program arguments
     */
    public static void main(String[] args)
    {
        if(args.length == 1) {
            System.out.println("Hello" + args[0]);
        }
        Game game = new Game();
        game.play();
    }
}
