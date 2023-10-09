public class Main {
    public static void main(String[] args) {
        Deck d = new Deck();
        //d.getDeck();
        d.deal();

        d.printTableau();

        //d.getDeck(); // will be only 23 cards after having dealt the tableau

        Board b = new Board(d);
        b.getFoundation();
    }
}