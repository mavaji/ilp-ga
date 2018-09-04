package gui;

import communication.IslandID;
import ilp.methods.multipopulation.BootStrap;
import ilp.methods.multipopulation.ILPConstants;
import ilp.methods.multipopulation.Island;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * This class represents the visualize window in the gui.
 *
 * @author Vahid Mavaji
 */
public class VisualizeWindow extends JDialog implements Observer {
    JPanel panel1 = new JPanel();
    JButton btnReturn = new JButton();

    private Vector islandPool = new Vector();

    public VisualizeWindow(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public VisualizeWindow() {
        this(null, "Visualize...", false);
    }

    private void jbInit() throws Exception {
        panel1.setLayout(null);
        btnReturn.setText("Return");
        btnReturn.setBounds(new Rectangle(260, 410, 79, 27));
        btnReturn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnReturn_actionPerformed(e);
            }
        });
        panel1.setBackground(SystemColor.inactiveCaption);
        getContentPane().add(panel1);
        panel1.add(btnReturn, null);
        this.setResizable(false);
    }

    public void initiate(Vector islandPool) {
        this.setSize(600, 500);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        this.setVisible(true);

        this.islandPool = islandPool;
        repaint();
    }

    void btnReturn_actionPerformed(ActionEvent e) {
        this.setVisible(false);
    }

    public synchronized void paint(Graphics g) {

        g.setColor(SystemColor.inactiveCaption);
        g.fillRect(0, 0, getWidth(), getHeight());

        Iterator iter = islandPool.iterator();
        IslandID islandID = (IslandID) iter.next();

        final int x_boot = 50;
        final int y_boot = 100;
        final int r = 20;
        final int diff = 10;
        final int deltaX = 100;
        final int deltaY = 100;

        g.setColor(Color.yellow);
        g.fillOval(x_boot, y_boot, r, r);
        g.setColor(Color.white);
        g.drawString(islandID.IP, x_boot, y_boot - 2 * diff);
        g.drawString(String.valueOf(islandID.port), x_boot, y_boot - diff);

        int x = x_boot;
        int y = y_boot + deltaY;
        while (iter.hasNext()) {
            islandID = (IslandID) iter.next();
            g.setColor(Color.red);
            if (islandID.equals(new IslandID(ILPConstants.IP, ILPConstants.PORT))) {
                g.setColor(Color.blue);
            }
            g.fillOval(x, y, r, r);

            g.setColor(Color.green);
            g.drawLine(x + r / 2, y + r / 2, x_boot + r / 2, y_boot + r / 2);

            g.setColor(Color.white);
            g.drawString(islandID.IP, x, y + diff + r);
            g.drawString(String.valueOf(islandID.port), x, y + 2 * diff + r);

            x += deltaX;
        }
    }

    public void update(Observable o, Object arg) {
        if ((o instanceof BootStrap || o instanceof Island) && arg instanceof Vector) {
            islandPool = new Vector((Vector) arg);
            repaint();
        }
    }
}