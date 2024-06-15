import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class Minesweeper {
    private class MineTile extends JButton {
        int r = 0; // Row index of the tile
        int c = 0; // Column index of the tile

        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    public static void main(String[] args) throws Exception {
        new Minesweeper();
    }

    // Game settings and UI components
    private int tileSize = 70; // Size of each tile (both width and height)
    private int numRows = 8; // Number of rows on the board
    private int numCols = numRows; // Number of columns on the board (same as rows)
    private int boardWidth = numCols * tileSize; // Total width of the game board
    private int boardHeight = numRows * tileSize; // Total height of the game board

    private JFrame frame = new JFrame("Minesweeper"); // Main JFrame for the game window
    private JLabel textLabel = new JLabel(); // Label to display game status or information
    private JPanel textPanel = new JPanel(); // Panel to hold the textLabel
    private JPanel boardPanel = new JPanel(); // Panel to hold the MineTile buttons

    private int mineCount = 10; // Number of mines on the board

    // Nested ArrayList to hold MineTile objects instead of a 2D array
    public ArrayList<ArrayList<MineTile>> nested_arraylist_instead_of_array = new ArrayList<>();

    private ArrayList<MineTile> mineList; // List to store MineTile objects that contain mines
    private Random random = new Random(); // Random number generator for placing mines randomly

    private int tilesClicked = 0; // Counter to track the number of tiles clicked (excluding mines)
    private boolean gameOver = false; // Flag to indicate if the game is over

    private int rrr = 0; // Variable to iterate through rows during board initialization

    public Minesweeper() {
        // Set up the main JFrame
        frame.setSize(boardWidth, boardHeight); // Set the size of the frame
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setResizable(false); // Disable resizing of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close operation when the frame is closed
        frame.setLayout(new BorderLayout()); // Use BorderLayout for the frame layout

        // Set up the textLabel to display game information
        textLabel.setFont(new Font("Arial", Font.BOLD, 25)); // Set font for the textLabel
        textLabel.setHorizontalAlignment(JLabel.CENTER); // Center align the text in the label
        textLabel.setText("Minesweeper: " + Integer.toString(mineCount)); // Initial text for the label
        textLabel.setOpaque(true); // Make the label opaque for better visibility

        // Set up the textPanel to hold the textLabel
        textPanel.setLayout(new BorderLayout()); // Use BorderLayout for the textPanel layout
        textPanel.add(textLabel); // Add the textLabel to the textPanel
        frame.add(textPanel, BorderLayout.NORTH); // Add the textPanel to the frame's NORTH position

        // Set up the boardPanel to hold the MineTile buttons in a grid layout
        boardPanel.setLayout(new GridLayout(numRows, numCols)); // Use GridLayout for boardPanel (8x8 grid)
        frame.add(boardPanel); // Add the boardPanel to the frame

        // Initialize the game board with MineTile objects
        while (rrr < numRows) { // Loop through each row
            ArrayList<MineTile> row = new ArrayList<>(); // Create a new ArrayList for the current row

            int ccc = 0; // Initialize column index

            while (ccc < numCols) { // Loop through each column
                MineTile tile = new MineTile(rrr, ccc); // Create a new MineTile object for the current position
                row.add(tile); // Add the MineTile to the current row ArrayList
                tile.setFocusable(false); // Disable focusability for the MineTile
                tile.setMargin(new Insets(0, 0, 0, 0)); // Set margin to 0 for compact layout

                // Add a MouseListener to handle mouse events on the MineTile
                tile.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (gameOver) {
                            return; // Exit method if game is over
                        }
                        MineTile tile = (MineTile) e.getSource(); // Get the MineTile that triggered the event

                        // Handle left mouse button click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if ("".equals(tile.getText())) { // If tile text is empty
                                if (mineList.contains(tile)) { // Check if the tile contains a mine
                                    revealMines(); // If it does, reveal all mines and end the game
                                } else {
                                    checkMine(tile.r, tile.c); // Otherwise, check the neighboring tiles
                                }
                            }
                        }
                        // Handle right mouse button click
                        else if (e.getButton() == MouseEvent.BUTTON3) {
                            if ("".equals(tile.getText()) && tile.isEnabled()) { // If tile text is empty and tile is
                                                                                 // enabled
                                tile.setText("🚩"); // Place a flag emoji on the tile
                            } else if ("🚩".equals(tile.getText())) { // If tile already has a flag
                                tile.setText(""); // Remove the flag from the tile
                            }
                        }
                    }

                    // Unused mouse listener methods
                    @Override
                    public void mousePressed(MouseEvent e) {
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                    }
                });

                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45)); // Set font for MineTile text
                boardPanel.add(tile); // Add the MineTile button to the boardPanel
                ccc++; // Increment column index
            }

            nested_arraylist_instead_of_array.add(row); // Add the completed row ArrayList to the nested ArrayList
            rrr++; // Increment row index
        }

        frame.setVisible(true); // Make the frame visible
        setMines(); // Call method to randomly place mines on the board
    }

    // Method to randomly place mines on the game board
    private void setMines() {
        mineList = new ArrayList<MineTile>(); // Initialize ArrayList to hold MineTiles with mines

        int mineLeft = mineCount; // Initialize mine counter

        while (mineLeft > 0) { // Loop until all mines are placed
            // Get a random MineTile from the board
            MineTile tile = nested_arraylist_instead_of_array.get(random.nextInt(numRows)).get(random.nextInt(numCols));

            if (!mineList.contains(tile)) { // If the MineTile doesn't already contain a mine
                mineList.add(tile); // Add the MineTile to the mineList
                mineLeft--; // Decrement the mine counter
            }
        }
    }

    // Method to reveal all mines on the game board
    private void revealMines() {
        for (MineTile tile : mineList) { // Iterate through each MineTile in the mineList
            tile.setText("💣"); // Set text of the MineTile to indicate a mine
        }

        gameOver = true; // Set game over flag to true
        textLabel.setText("Game Over!"); // Update textLabel to indicate game over
    }

    // Method to check neighboring tiles for mines and update tile numbers
    private void checkMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return; // Exit method if indices are out of board bounds
        }

        MineTile tile = nested_arraylist_instead_of_array.get(r).get(c); // Get MineTile at specified position

        if (!tile.isEnabled()) {
            return; // Exit method if tile is disabled (already checked)
        }

        tile.setEnabled(false); // Disable the tile
        tilesClicked++; // Increment the counter for clicked tiles

        int minesFound = 0; // Counter to track neighboring mines

        // Check all neighboring tiles (top, bottom, left, right, and diagonals)
        minesFound = minesFound + countMine(r - 1, c - 1); // Top left
        minesFound = minesFound + countMine(r - 1, c); // Top
        minesFound = minesFound + countMine(r - 1, c + 1); // Top right
        minesFound = minesFound + countMine(r, c - 1); // Left
        minesFound = minesFound + countMine(r, c + 1); // Right
        minesFound = minesFound + countMine(r + 1, c - 1); // Bottom left
        minesFound = minesFound + countMine(r + 1, c); // Bottom
        minesFound = minesFound + countMine(r + 1, c + 1); // Bottom right

        if (minesFound > 0) {
            tile.setText(Integer.toString(minesFound)); // Set tile text to the number of neighboring mines
        } else {
            tile.setText(""); // If no neighboring mines, keep tile text empty

            // Recursively check neighboring tiles if no mines are found
            checkMine(r - 1, c - 1); // Top left
            checkMine(r - 1, c); // Top
            checkMine(r - 1, c + 1); // Top right
            checkMine(r, c - 1); // Left
            checkMine(r, c + 1); // Right
            checkMine(r + 1, c - 1); // Bottom left
            checkMine(r + 1, c); // Bottom
            checkMine(r + 1, c + 1); // Bottom right
        }

        // Check if all non-mine tiles have been clicked
        if (tilesClicked == numRows * numCols - mineList.size()) {
            gameOver = true; // Set game over flag to true
            textLabel.setText("Mines Cleared!"); // Update textLabel to indicate all mines cleared
        }
    }

    // Method to count neighboring mines for a given tile position
    private int countMine(int r, int c) {
        if (r < 0 || r >= numRows || c < 0 || c >= numCols) {
            return 0; // Return 0 if indices are out of board bounds
        }

        if (mineList.contains(nested_arraylist_instead_of_array.get(r).get(c))) {
            return 1; // Return 1 if neighboring tile contains a mine
        }

        return 0; // Return 0 if neighboring tile does not contain a mine
    }
}
