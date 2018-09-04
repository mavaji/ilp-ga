package gui;

import ilp.methods.multipopulation.ILPConstants;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.StringTokenizer;

/**
 * This class represents the configuration window in the gui.
 *
 * @author Vahid Mavaji
 */
public class ConfigWindow extends JDialog {
    JPanel panel1 = new JPanel();
    JLabel lblTitle = new JLabel();
    JTextField txtTestCasePath = new JTextField();
    JTextField txtGenerations = new JTextField();
    JTextField txtChromosomes = new JTextField();
    JTextField txtBootStrapIP = new JTextField();
    JTextField txtBootStrapPort = new JTextField();
    JTextField txtPenalties = new JTextField();
    JTextField txtMigrationRate = new JTextField();
    JTextField txtIterations = new JTextField();
    JTextField txtPort = new JTextField();
    JLabel lblTestCasePath = new JLabel();
    JLabel lblGenerations = new JLabel();
    JLabel lblChromosomes = new JLabel();
    JLabel lblBootStrapIP = new JLabel();
    JLabel lblBootStrapPort = new JLabel();
    JLabel lblPenalties = new JLabel();
    JLabel lblMigrationRate = new JLabel();
    JLabel lblIterations = new JLabel();
    JLabel lblPort = new JLabel();
    JButton lblBrowse = new JButton();
    JButton lblOK = new JButton();

    public ConfigWindow(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ConfigWindow() {
        this(null, "Configuration...", true);
    }

    void jbInit() throws Exception {
        panel1.setLayout(null);
        lblTitle.setFont(new java.awt.Font("Dialog", 1, 14));
        lblTitle.setForeground(Color.yellow);
        lblTitle.setText("Configuration Window");
        lblTitle.setBounds(new Rectangle(151, 16, 166, 27));
        panel1.setBackground(SystemColor.inactiveCaption);
        txtTestCasePath.setEditable(false);
        txtTestCasePath.setBounds(new Rectangle(150, 68, 176, 22));
        txtGenerations.setBounds(new Rectangle(150, 93, 176, 22));
        txtChromosomes.setBounds(new Rectangle(151, 120, 176, 22));
        txtBootStrapIP.setBounds(new Rectangle(149, 147, 176, 22));
        txtBootStrapPort.setBounds(new Rectangle(150, 174, 176, 22));
        txtPenalties.setBounds(new Rectangle(150, 203, 176, 22));
        txtMigrationRate.setBounds(new Rectangle(150, 231, 176, 22));
        txtIterations.setBounds(new Rectangle(149, 259, 176, 22));
        txtPort.setBounds(new Rectangle(149, 288, 176, 22));
        lblTestCasePath.setForeground(Color.white);
        lblTestCasePath.setText("path of test case file");
        lblTestCasePath.setBounds(new Rectangle(19, 66, 121, 21));
        lblGenerations.setBounds(new Rectangle(18, 95, 79, 17));
        lblGenerations.setForeground(Color.white);
        lblGenerations.setText("generations");
        lblChromosomes.setBounds(new Rectangle(19, 122, 86, 16));
        lblChromosomes.setForeground(Color.white);
        lblChromosomes.setText("chromosomes");
        lblBootStrapIP.setBounds(new Rectangle(20, 149, 81, 17));
        lblBootStrapIP.setForeground(Color.white);
        lblBootStrapIP.setText("boot-strap IP");
        lblBootStrapPort.setBounds(new Rectangle(20, 176, 89, 19));
        lblBootStrapPort.setForeground(Color.white);
        lblBootStrapPort.setText("boot-strap port");
        lblPenalties.setBounds(new Rectangle(20, 203, 84, 18));
        lblPenalties.setForeground(Color.white);
        lblPenalties.setText("penalty factors");
        lblMigrationRate.setBounds(new Rectangle(23, 232, 90, 19));
        lblMigrationRate.setForeground(Color.white);
        lblMigrationRate.setText("migration rate");
        lblIterations.setBounds(new Rectangle(22, 262, 65, 20));
        lblIterations.setForeground(Color.white);
        lblIterations.setText("iterations");
        lblPort.setBounds(new Rectangle(23, 290, 41, 17));
        lblPort.setForeground(Color.white);
        lblPort.setText("port");
        lblBrowse.setText("Browse");
        lblBrowse.setBounds(new Rectangle(343, 67, 78, 24));
        lblBrowse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton1_actionPerformed(e);
            }
        });
        lblOK.setText("OK");
        lblOK.setBounds(new Rectangle(193, 328, 79, 27));
        lblOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jButton2_actionPerformed(e);
            }
        });
        this.setResizable(false);
        getContentPane().add(panel1);
        panel1.add(lblTestCasePath, null);
        panel1.add(txtTestCasePath, null);
        panel1.add(lblBrowse, null);
        panel1.add(txtGenerations, null);
        panel1.add(lblGenerations, null);
        panel1.add(lblChromosomes, null);
        panel1.add(txtChromosomes, null);
        panel1.add(lblBootStrapIP, null);
        panel1.add(txtBootStrapIP, null);
        panel1.add(lblBootStrapPort, null);
        panel1.add(txtBootStrapPort, null);
        panel1.add(lblPenalties, null);
        panel1.add(txtPenalties, null);
        panel1.add(txtMigrationRate, null);
        panel1.add(lblMigrationRate, null);
        panel1.add(lblIterations, null);
        panel1.add(txtIterations, null);
        panel1.add(txtPort, null);
        panel1.add(lblPort, null);
        panel1.add(lblOK, null);
        panel1.add(lblTitle, null);


    }

    public void initiate() {
        setSize(450, 400);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        setVisible(true);
    }

    void jButton1_actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int state;
        do {
            state = fileChooser.showOpenDialog(this);
        } while (!(state == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().exists()
                || state == JFileChooser.CANCEL_OPTION));

        if (state == JFileChooser.APPROVE_OPTION) {
            txtTestCasePath.setText(fileChooser.getSelectedFile().getPath());
        }
    }

    void jButton2_actionPerformed(ActionEvent e) {
        ILPConstants.TEST_CASE_FILE = txtTestCasePath.getText();
        ILPConstants.GENERATIONS = Integer.valueOf(txtGenerations.getText()).intValue();
        ILPConstants.NUM_CHROMS = Integer.valueOf(txtChromosomes.getText()).intValue();
        ILPConstants.BOOT_STRAP_IP = txtBootStrapIP.getText();
        ILPConstants.BOOT_STRAP_PORT = Integer.valueOf(txtBootStrapPort.getText()).intValue();

        StringTokenizer tokenizer = new StringTokenizer(txtPenalties.getText());
        ILPConstants.NUM_CONSTRAINTS = 0;
        for (int i = 0; tokenizer.hasMoreTokens(); i++) {
            ILPConstants.PENALTIES[i] = Integer.valueOf(tokenizer.nextToken()).intValue();
            ILPConstants.NUM_CONSTRAINTS++;
        }

        ILPConstants.MIGRATION_RATE = Float.valueOf(txtMigrationRate.getText()).floatValue();
        ILPConstants.ITERATIONS = Integer.valueOf(txtIterations.getText()).intValue();
        ILPConstants.PORT = Integer.valueOf(txtPort.getText()).intValue();
        this.setVisible(false);
    }
}