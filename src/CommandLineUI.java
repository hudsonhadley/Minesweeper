import java.util.Scanner;

public class CommandLineUI {
    /**
     * Gets input from the Scanner that is between bounds. The method also prints out a message
     * after each attempt.
     * @param scanner the Scanner used for input
     * @param message the message that will be printed
     * @param min the minimum bound a number can be (inclusive)
     * @param max the maximum bound a number can be (inclusive)
     * @return an integer the user has entered between min and max inclusive
     * @throws IllegalArgumentException if min is greater than max
     */
    public static int getNumber(Scanner scanner, String message, int min, int max) throws IllegalArgumentException {
        if (min > max)
            throw new IllegalArgumentException("Min must be less than or equal to max");

        int num;

        while (true) {
            System.out.print(message);
            try {
                num = Integer.parseInt(scanner.nextLine());

                if (min <= num && num <= max) {
                    break;
                } else {
                    System.out.printf("Enter a number %d - %d\n", min, max);
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Enter a number");
            }
        }

        return num;
    }

    public static void main(String[] args) {
        Board board;

        Scanner inScanner = new Scanner(System.in);
        int difficulty = getNumber(inScanner,
                "Enter difficulty (1: Beginner, 2: Intermediate, 3: Expert): ", 1, 3);

        if (difficulty == 1)
            board = new Board(9, 9, 10);
        else if (difficulty == 2)
            board = new Board(16, 16, 40);
        else // difficulty == 3
            board = new Board(30, 16, 99);


        boolean hitMine = false;
        boolean flagging = false;
        while (!board.hasWon()) {
            System.out.println(board);

            System.out.print("Flagging? (y/n): ");

            while (true) {
                try {
                    char answer = inScanner.nextLine().toLowerCase().charAt(0);


                    if (answer == 'y') {
                        flagging = true;
                        break;
                    } else if (answer == 'n') {
                        flagging = false;
                        break;
                    }
                } catch (Exception ignore) {}
            }

            int row = getNumber(inScanner, "Enter a row: ", 1, board.getHeight());
            int col = getNumber(inScanner, "Enter a column: ", 1, board.getWidth());

            if (flagging) {
                // If the spot has already been revealed, we can't flag it
                if (board.isRevealed(row - 1, col - 1))
                    System.out.println("Spot already revealed");
                else
                    board.flag(row - 1, col - 1);
            } else {
                // If the spot has a flag, we have to remove it first before revealing
                if (board.hasFlag(row - 1, col - 1))
                    System.out.println("Remove flag first...");

                // If there is no flag, test if it is a mine
                else if (!board.reveal(row - 1, col - 1)) {
                    hitMine = true;
                    break;
                }
            }
        }

        System.out.println(board);

        if (hitMine) {
            System.out.println("Hit mine!");

            // If we hit a mine, reveal all the mines and print the board once again
            for (int i = 0; i < board.getHeight(); i++) {
                for (int j = 0; j < board.getWidth(); j++) {
                    if (board.isMine(i, j))
                        board.reveal(i, j);
                }
            }

            System.out.println(board);
        }
        else
            System.out.println("Minefield cleared!");
    }
}