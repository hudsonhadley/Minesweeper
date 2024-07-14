import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Has all the functionality of a JButton but also knows where it is on a grid
 * @author Hudson Hadley
 */
public class CellButton extends JButton {
    private int row;
    private int col;

    public CellButton(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
