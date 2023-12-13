package a01a.e2;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

public class ControllerImpl implements Controller {
    private static final int LAST_BTNS_NUMS = 3;
    private final int ROW_SIZE;

    private final List<JButton> cells = new ArrayList<>();
    private final JButton[] lastButtons = { null, null, null };
    private int lastInserted = 0;

    public ControllerImpl(int size) {
        this.ROW_SIZE = size;
    }

    @Override
    public void registerButtonClick(JButton btn) {
        lastButtons[lastInserted] = btn;
        lastInserted = (lastInserted + 1) % LAST_BTNS_NUMS;
        this.checkExitCondition();
    }

    private void checkExitCondition() {
        if (lastInserted == 0) {
            final List<JButton> btnList = List.of(lastButtons);
            System.out.println(btnList.stream().map((btn) -> cells.indexOf(btn)).toList());
            /* if all btns are active and are in row */
            int lastRow = -1;
            int lastCol = -1;
            boolean match = true;
            if (btnList.stream().allMatch((btn) -> btn.getText().equals(View.ACTIVE_BTN_LABEL))) {
                for (final JButton btn : lastButtons) {
                    if (match) {
                        final int row = cells.indexOf(btn) % ROW_SIZE;
                        final int col = cells.indexOf(btn) / ROW_SIZE;
                        if (lastRow != -1 && lastCol != -1) {
                            match = (row - 1 == lastRow || row + 1 == lastRow)
                                    && (col - 1 == lastCol || col + 1 == lastCol);
                        }
                        lastRow = row;
                        lastCol = col;
                    }
                }

                if (match) {
                    this.quit();
                }
            }
        }
    }

    public void quit() {
        System.exit(0);
    }

    @Override
    public void addCell(final JButton btn) {
        this.cells.add(btn);
    }
}