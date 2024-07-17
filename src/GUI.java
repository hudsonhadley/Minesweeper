import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUI {
    /**
     * The board the game is played on
     */
    private static Board gameBoard;

    /**
     * The frame the game is displayed on
     */
    private static JFrame frame;

    /**
     * A panel that uses a CardLayout manager
     */
    private static JPanel cards;

    /**
     * The buttons where the game is played
     */
    private static CellButton[][] buttons;

    /**
     * The current flagCount on the board
     */
    private static int flagCount;

    /**
     * If the player has hit a mine
     */
    private static boolean hitMine = false;

    /**
     * The default font we will use
     */
    private static final Font DEFAULT_FONT = new Font("Dialog.bold", Font.PLAIN, 15);

    /**
     * The color used by each number (note that index 0 is not used since no number is shown for a blank space)
     */
    private static final Color[] NUMBER_COLORS = new Color[]{
           new Color(0, 0, 0), // 0
           new Color(12, 183, 224), // 1
           new Color(127, 206, 90), // 2
           new Color(225, 101, 101), // 3
           new Color(207, 88, 222), // 4
           new Color(253, 148, 113), // 5
           new Color(2, 33, 103), // 6
           new Color(22, 65, 1), // 7
           new Color(155, 155, 61), // 8
    };

    /**
     * A map from difficulty level to size
     */
    private static final int[][] SIZES = new int[][] {{9, 9, 10}, {16, 16, 40}, {30, 16, 99}};

    /**
     * The length of one cell in pixels
     */
    private static final int CELL_SIZE = 30;

    /**
     * Stores the start time and is updated as necessary when the game resets
     */
    private static long startTime;

    /**
     * Makes the GUI and shows it
     */
    private static void createAndShowGUI() {
        frame = new JFrame("Minesweeper");
        frame.setVisible(true);
        // Add 20 on the width and the height as a wiggle room (the 60 makes up for the heading)
        frame.setSize(SIZES[2][0] * CELL_SIZE + 20, SIZES[2][1] * CELL_SIZE + 60 + 20);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cards = new JPanel(new CardLayout());
        frame.add(cards);

        createMenu();
    }

    /**
     * Creates three difficulty buttons on the screen for the user to press
     */
    private static void createMenu() {
        int widthOfButtons = 130;
        int heightOfButtons = 60;
        int heightOfStruts = 30;

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.PAGE_AXIS));
        menuPanel.setBackground(Color.LIGHT_GRAY);
        menuPanel.setVisible(true);

        /* The menu will have three buttons: beginner, intermediate, and expert. We will divide up the screen in height
         * as follows:
         *      1. (height - 260) / 2 - 25 for spacing
         *      2. 50 for description
         *      3. 50 for beginner button
         *      4. 30 for spacing
         *      5. 50 for intermediate button
         *      6. 30 for spacing
         *      7. 50 for expert button
         *      8. (height - 260) / 2 + 25 for spacing
         */


        // Add the appropriate amount so that the intermediate button is centered
        menuPanel.add(Box.createVerticalStrut(
                ((frame.getHeight() - (4 * heightOfButtons) - (2 * heightOfStruts)) / 2)
                        - (heightOfButtons / 2)
        ));

        JLabel descriptionLabel = new JLabel("Select Difficulty", JLabel.CENTER);
        descriptionLabel.setFont(DEFAULT_FONT);
        descriptionLabel.setForeground(Color.BLACK);

        descriptionLabel.setMaximumSize(new Dimension(widthOfButtons, heightOfButtons));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuPanel.add(descriptionLabel);

        JButton beginnerButton = new JButton("Beginner");
        beginnerButton.setFont(DEFAULT_FONT);
        beginnerButton.setMaximumSize(new Dimension(widthOfButtons, heightOfButtons));
        beginnerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        beginnerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBoard(0);
            }
        });

        menuPanel.add(beginnerButton);

        // Add a gap
        menuPanel.add(Box.createVerticalStrut(heightOfStruts));

        JButton intermediateButton = new JButton("Intermediate");
        intermediateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        intermediateButton.setFont(DEFAULT_FONT);
        intermediateButton.setMaximumSize(new Dimension(widthOfButtons, heightOfButtons));

        intermediateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBoard(1);
            }
        });

        menuPanel.add(intermediateButton);

        // Add a gap
        menuPanel.add(Box.createVerticalStrut(heightOfStruts));

        JButton expertButton = new JButton("Expert");
        expertButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        expertButton.setFont(DEFAULT_FONT);
        expertButton.setMaximumSize(new Dimension(widthOfButtons, heightOfButtons));

        expertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBoard(2);
            }
        });

        menuPanel.add(expertButton);

        // Add the appropriate amount so that the intermediate button is centered
        menuPanel.add(Box.createVerticalStrut(
                ((frame.getHeight() - (4 * heightOfButtons) - (2 * heightOfStruts)) / 2)
                        + (heightOfButtons / 2)
        ));

        cards.add(menuPanel);
    }

    /**
     * Plays a game with a certain specified difficulty
     * @param difficulty the difficulty of the game (0, 1, or 2)
     * @throws IllegalArgumentException if the difficulty isn't 0, 1, or 2
     */
    private static void createBoard(int difficulty) {
        if (difficulty > 2 || difficulty < 0)
            throw new IllegalArgumentException("Invalid difficulty");

        flagCount = 0;
        hitMine = false;

        // Make a new panel
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(Color.LIGHT_GRAY);

        // Our game panel will have two sections: (1) the top of the screen and (2) the area where the game is played
        gamePanel.add(makeTopPanel(difficulty), BorderLayout.PAGE_START);
        gamePanel.add(makeBottomPanel(difficulty), BorderLayout.CENTER);

        // Add the panel to the cards and switch to it from the layout manager
        cards.add(gamePanel);
        CardLayout cl = (CardLayout) cards.getLayout();
        cl.last(cards);
    }

    /**
     * Creates the top panel of the game with the score, reset button, and timer
     * @param difficulty the difficulty of the game
     * @return the top panel with its proper containers
     * @throws IllegalArgumentException if the difficulty is invalid (not 0, 1, or 2)
     */
    private static JPanel makeTopPanel(int difficulty) {
        if (difficulty < 0 || difficulty > 2)
            throw new IllegalArgumentException("Invalid difficulty");

        int strutWidth = 50;
        int labelWidth = (frame.getWidth() - 3 * strutWidth) / 4 + 100;
        int buttonWidth = (frame.getWidth() - 3 * strutWidth) / 4 - 100;


        /* For the width at the top we will use the space as follows:
         *      1. 50 for spacing
         *      2. (width - 150) / 4 + 100 for the flagCount
         *      3. (width - 150) / 4 - 100 for the menu button
         *      4. 50 for spacing
         *      5. (width - 150) / 4 - 100 for the retry button
         *      4. (width - 150) / 4 + 100 for the timer
         *      5. 50 for spacing
         */

        JPanel topOfBoard = new JPanel();
        topOfBoard.setMaximumSize(new Dimension(frame.getWidth(), 60));
        topOfBoard.setLayout(new BoxLayout(topOfBoard, BoxLayout.LINE_AXIS));
        topOfBoard.setBackground(Color.LIGHT_GRAY);


        // Add a gap
        topOfBoard.add(Box.createHorizontalStrut(strutWidth));

        // Add the score
        JLabel scoreLabel = new JLabel("" + (SIZES[difficulty][2] - flagCount), JLabel.LEFT);
        scoreLabel.setMaximumSize(new Dimension(labelWidth, 60));
        scoreLabel.setFont(DEFAULT_FONT);
        scoreLabel.setForeground(Color.BLACK);
        topOfBoard.add(scoreLabel);

        // Add the menu button
        JButton menuButton = new JButton("Menu");
        menuButton.setMaximumSize(new Dimension(buttonWidth, 60));
        menuButton.setFont(DEFAULT_FONT);
        menuButton.setForeground(Color.BLACK);
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) cards.getLayout();
                cl.first(cards);
            }
        });
        topOfBoard.add(menuButton);

        // Add a gap
        topOfBoard.add(Box.createHorizontalStrut(strutWidth));

        // Add the retry button
        JButton retryButton = new JButton("Retry");
        retryButton.setMaximumSize(new Dimension(buttonWidth, 60));
        retryButton.setFont(DEFAULT_FONT);
        retryButton.setForeground(Color.BLACK);
        retryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createBoard(difficulty);
            }
        });
        topOfBoard.add(retryButton);

        // Add the timer

        startTime = System.currentTimeMillis(); // Keep track of when the timer is initialized
        JLabel timeLabel = new JLabel("0", JLabel.RIGHT);
        timeLabel.setMaximumSize(new Dimension(labelWidth, 60));
        timeLabel.setFont(DEFAULT_FONT);
        timeLabel.setForeground(Color.BLACK);
        topOfBoard.add(timeLabel);

        // We have to initialize it first, so we can use it in the ActionListener
        Timer timer = new Timer(200, null);
        timer.addActionListener(new ActionListener() {
            // Every second the time will update the JLabels on the top of the board
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;

                // Update the time
                timeLabel.setText("" + elapsed / 1000);

                // Update flag count
                scoreLabel.setText("" + (SIZES[difficulty][2] - flagCount) );

                // If the game is over, stop the timer
                if (gameBoard.hasWon()) {
                    timeLabel.setText("Minefield cleared :)   " + elapsed / 1000);
                    timer.stop();
                } else if (hitMine) {
                    timeLabel.setText("Mine hit :(   " + elapsed / 1000);
                    timer.stop();
                }
            }
        });
        timer.start();

        topOfBoard.add(Box.createHorizontalStrut(50));

        return topOfBoard;
    }

    /**
     * Creates the bottom panel used for the game
     * @param difficulty the difficulty of the game
     * @return the panel where the game is played
     * @throws IllegalArgumentException if the difficulty is invalid (not 0, 1, or 2)
     */
    private static JPanel makeGamePanel(int difficulty) {
        if (difficulty < 0 || difficulty > 2)
            throw new IllegalArgumentException("Invalid difficulty");

        // Create the game board to be used
        gameBoard = new Board(SIZES[difficulty][0], SIZES[difficulty][1], SIZES[difficulty][2]);

        // For the game board we will create a panel
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(gameBoard.getHeight(), gameBoard.getWidth()));

        // Initialize all the buttons
        buttons = new CellButton[gameBoard.getHeight()][gameBoard.getWidth()];

        for (int i = 0; i < gameBoard.getHeight(); i++) {
            for (int j = 0; j < gameBoard.getWidth(); j++) {
                buttons[i][j] = new CellButton(i, j);
                buttons[i][j].setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
                buttons[i][j].setBackground(Color.DARK_GRAY);

                buttons[i][j].addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {}
                    @Override
                    public void mousePressed(MouseEvent e) {}
                    @Override
                    public void mouseReleased(MouseEvent e) { // Once the mouse is released, an action will trigger
                        // If the game is over, do nothing
                        if (hitMine || gameBoard.hasWon())
                            return;

                        CellButton buttonClicked = (CellButton) e.getSource();
                        int row = buttonClicked.getRow();
                        int col = buttonClicked.getCol();

                        // If the left mouse button is clicked
                        if (e.getButton() == MouseEvent.BUTTON1) {

                            // If it's not flagged and isn't revealed already
                            if (!gameBoard.hasFlag(row, col) && !gameBoard.isRevealed(row, col)) {
                                gameBoard.reveal(row, col);
                            }

                            // If it is a mine (NOT ELSE IF)
                            if (gameBoard.isMine(row, col) && !gameBoard.hasFlag(row, col)) {
                                hitMine = true;

                                // Reveal all the mines
                                for (int k = 0; k < gameBoard.getHeight(); k++) {
                                    for (int l = 0; l < gameBoard.getWidth(); l++) {
                                        if (gameBoard.isMine(k, l) && !gameBoard.hasFlag(k, l))
                                            gameBoard.reveal(k, l);
                                    }
                                }
                            }

                        } else if (e.getButton() == MouseEvent.BUTTON3) { // If the right mouse button is clicked

                            // If it isn't revealed already
                            if (!gameBoard.isRevealed(row, col))
                                gameBoard.flag(row, col);
                        }
                        // Update the board accordingly
                        updateBoard();

                        System.out.println(gameBoard);
                    }
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseExited(MouseEvent e) {}
                });

                gamePanel.add(buttons[i][j]);
            }
        }

        return gamePanel;
    }

    /**
     * Creates the bottom panel with the game panel on it
     * @param difficulty the difficulty of the game
     */
    private static JPanel makeBottomPanel(int difficulty) {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.LIGHT_GRAY);
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(makeGamePanel(difficulty));

        return bottomPanel;
    }

    /**
     * Updates the board on the screen according to the Board member in this class
     * @throws IllegalStateException if the gameBoard hasn't been set up yet
     */
    private static void updateBoard() {
        if (gameBoard == null)
            throw new IllegalStateException("If the board hasn't been set up yet, we can't update");

        flagCount = 0;

        for (int i = 0; i < gameBoard.getHeight(); i++) {
            for (int j = 0; j < gameBoard.getWidth(); j++) {
                if (gameBoard.isRevealed(i, j)) {
                    // If it's blank make it white
                    if (gameBoard.isBlank(i, j)) {
                        buttons[i][j].setText("");
                        buttons[i][j].setBackground(Color.WHITE);

                        // If it's a mine make a black X
                    } else if (gameBoard.isMine(i, j)) {
                        buttons[i][j].setText("X");
                        buttons[i][j].setForeground(Color.BLACK);
                        buttons[i][j].setBackground(Color.WHITE);

                        // If it has a number, set it
                    } else {
                        buttons[i][j].setText("" + gameBoard.getNumber(i, j));
                        buttons[i][j].setForeground(NUMBER_COLORS[gameBoard.getNumber(i, j)]);
                        buttons[i][j].setBackground(Color.WHITE);
                    }

                    // If it has a flag, do a red !
                } else if (gameBoard.hasFlag(i, j)) {
                    buttons[i][j].setText("!");
                    buttons[i][j].setForeground(Color.RED);

                    flagCount++;
                } else {
                    buttons[i][j].setText("");
                }

                buttons[i][j].setFont(DEFAULT_FONT);
            }
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
