package gui;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

/**
 * This class represents the about window in the gui.
 *
 * @author Vahid Mavaji
 */
public class AboutWindow extends JDialog {
    private JLabel lblSemester = new JLabel();
    private JLabel lblProjTopic2 = new JLabel();
    private JLabel lblProjTopic1 = new JLabel();
    private JLabel lblStdName = new JLabel();
    private JButton lblClose = new JButton();

    public AboutWindow(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public AboutWindow() {
        this(null, "About...", true);
    }

    private void jbInit() throws Exception {
        lblClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton1_actionPerformed(e);
            }
        });
        lblClose.setBounds(new Rectangle(50, 241, 141, 21));
        lblClose.setText("Close");
        lblStdName.setBounds(new Rectangle(85, 166, 85, 30));
        lblStdName.setText("Vahid Mavaji");
        lblStdName.setForeground(Color.black);
        lblStdName.setFont(new java.awt.Font("Dialog", 1, 12));
        lblProjTopic1.setBounds(new Rectangle(19, 70, 221, 27));
        lblProjTopic1.setText("A Parallel Genetic Algorithm");
        lblProjTopic1.setForeground(Color.black);
        lblProjTopic1.setFont(new java.awt.Font("Dialog", 1, 16));
        lblProjTopic2.setBounds(new Rectangle(39, 93, 186, 23));
        lblProjTopic2.setText("for solving ILP equations");
        lblProjTopic2.setForeground(Color.black);
        lblProjTopic2.setFont(new java.awt.Font("Dialog", 3, 14));
        lblSemester.setBounds(new Rectangle(91, 213, 60, 17));
        lblSemester.setText("2005");
        lblSemester.setForeground(Color.black);
        lblSemester.setFont(new java.awt.Font("Dialog", 3, 12));
        this.getContentPane().setLayout(null);
        this.setResizable(false);
        this.getContentPane().setBackground(SystemColor.inactiveCaption);
        this.getContentPane().add(lblProjTopic1, null);
        this.getContentPane().add(lblSemester, null);
        this.getContentPane().add(lblProjTopic2, null);
        this.getContentPane().add(lblStdName, null);
        this.getContentPane().add(lblClose, null);
    }

    void jButton1_actionPerformed(ActionEvent e) {
        this.setVisible(false);
    }

    public void initiate() {
        this.setSize(260, 310);
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
    }
}