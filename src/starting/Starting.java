
package starting;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import gui.frmHoaDon;

public class Starting {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Quản lý nhân viên");

            frmHoaDon hoaDon = new frmHoaDon();

            frame.add(hoaDon);
            frame.setSize(1100, 650);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
