import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Deck deck;
    private static Scanner s;
    private static Board board;
    enum GameState {ACTIVE, QUIT, WON}
    private static GameState gameState;
    private static int score;

    public static void main(String[] args) {
        init();

        if(gameWon(board)){
            gameState = GameState.WON;

            System.out.println("Congratulations! You won 🎉🥳\nP to play again:");

            char[] move = s.nextLine().toUpperCase().toCharArray();
            if(move[0] == 'P'){
                init();
            }
        }

        while (gameState == GameState.ACTIVE) {
            System.out.println("\nMake a move:");
            char[] move = s.nextLine().toUpperCase().toCharArray();
            boolean scoreIncreased = false;

            if(move.length == 0){
                System.out.println("Invalid input! Please enter a move.");
                continue;
            }

            if (move.length == 2) {
                if(move[0] == 'P' && move[1] == 'R'){
                    printAll(deck, board);
                    continue;
                }
                else{
                    int attempt = board.moveCard(move[0], move[1]);
                    if(attempt != 0){
                        score += attempt;
                        scoreIncreased = true;
                    }
                }

            } else {
                // this means the user wants to move multiple cards (provided <label1><label2><n_cards>
                if(move.length == 3){
                    int attempt = board.moveCard(move[0], move[1], move[2]);
                    if(attempt != 0){
                        score += attempt;
                        scoreIncreased = true;
                    }
                }


                if(move[0] == '?'){
                    printTextFile("res/how_to_play.txt");
                    continue;
                }

                // otherwise, if only 1 character was provided as input, instead of 2 or 3,
                // the only valid possible move available is 'D', to draw a card from the deck
                if (move[0] == 'D') {
                    Pile drawnCards = board.drawFromDeck();
                    System.out.println("\nDrawn cards:");
                    for (Deck.Card c : drawnCards.getCards()) {
                        if (c != null) {
                            if(drawnCards.getCards().indexOf(c) == drawnCards.getSize()-1){
                                System.out.print(c.getRank() + " of " + c.getSuit() + " " + c.getIcon());
                            }
                            else{
                                System.out.print(c.getRank() + " of " + c.getSuit() + " " + c.getIcon() + ", ");
                            }

                        }
                    }
                    System.out.println();
                } else if (move.length == 1 && move[0] == 'Q') {
                    System.out.println("Goodbye");
                    gameState = GameState.QUIT;
                }
                else{
                    System.out.println("Invalid move!");
                    continue;
                }
            }

            if(scoreIncreased){
                // Print the deck again after a valid move, so the player can see their changes
                printAll(deck, board);
                System.out.println("\n\nSCORE: " + score);
            }

            System.out.println("\nMake a move:");
        }
    }

    static void printAll(Deck d, Board b){
        d.printLanes();
        b.printFoundation();
    }

    static void printTextFile(String filePath) {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream != null) {
            try (Scanner scanner = new Scanner(inputStream)) {
                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
            } catch (Exception e) {
                System.out.println("Error reading file: " + e);
            }
        } else {
            System.out.println("File not found: " + filePath);
        }
    }

    static boolean gameWon(Board b){
        ArrayList<Pile> foundation = b.getFoundation();

        // If all 4 foundation piles contain 13 cards, the game has been won
        for(Pile p: foundation){
            if(p.getSize() != 13){
                return false;
            }
        }
        return true;
    }

    public static void init(){
        printTextFile("res/logo.txt");
        System.out.println("Type ? for rules");

        deck = new Deck();
        s = new Scanner(System.in);

        // Populate the 7 lanes and remove those cards from the main deck.
        ArrayList<Pile> lanes = deck.getLanes();
        board = new Board(deck, lanes);
        gameState = GameState.ACTIVE;

        printAll(deck, board);

        score = 0;
    }
}