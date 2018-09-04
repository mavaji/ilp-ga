package gui;

import communication.IslandID;
import communication.Message;
import ilp.methods.multipopulation.BootStrap;
import ilp.methods.multipopulation.ILPConstants;
import ilp.methods.multipopulation.Island;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

/**
 * This class represents the main window in the gui.
 *
 * @author Vahid Mavaji
 */
public class MainWindow extends JFrame implements Observer {
    JPanel contentPane;
    JMenuBar jMenuBar = new JMenuBar();
    JMenu fileMenu = new JMenu();
    JMenu helpMenu = new JMenu();
    JMenu toolsMenu = new JMenu();
    JCheckBoxMenuItem islandCheckBoxMenuItem = new JCheckBoxMenuItem();
    JCheckBoxMenuItem bootstrapCheckBoxMenuItem = new JCheckBoxMenuItem();
    JMenuItem exitMenuItem = new JMenuItem();
    JMenuItem aboutMenuItem = new JMenuItem();
    JMenuItem setconfigMenuItem = new JMenuItem();
    JMenuItem editconfigMenuItem = new JMenuItem();
    TextArea txaIPAddress = new TextArea();
    JLabel lblIslands = new JLabel();
    JLabel lblIPAddress = new JLabel();
    JLabel lblPort = new JLabel();
    JLabel lblMessages = new JLabel();
    TextArea txaPort = new TextArea();
    TextArea txaMessages = new TextArea();
    JButton btnVisualize = new JButton();
    JButton btnStart = new JButton();
    JButton btnStop = new JButton();
    JTextField txtTime = new JTextField();

    private VisualizeWindow visualizeWindow = new VisualizeWindow();
    private BootStrap bootStrap;
    private Island island;
    private Vector islandPool = new Vector();
    private Thread timer = null;
    private Thread islandThread;

    /**
     * Construct the frame
     */
    public MainWindow() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Component initialization
     */
    private void jbInit() throws Exception {
        //setIconImage(Toolkit.getDefaultToolkit().createImage(MainWindow.class.getResource("[Your Icon]")));
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(null);
        this.getContentPane().setBackground(SystemColor.inactiveCaption);
        this.setJMenuBar(jMenuBar);
        this.setSize(new Dimension(563, 534));
        this.setTitle("Main Window...");
        this.setResizable(false);
        fileMenu.setText("File");
        helpMenu.setText("Help");
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitMenuItem_actionPerformed(e);
            }
        });
        aboutMenuItem.setText("About...");
        aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                aboutMenuItem_actionPerformed(e);
            }
        });
        setconfigMenuItem.setEnabled(false);
        setconfigMenuItem.setText("Set config File");
        setconfigMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setconfigMenuItem_actionPerformed(e);
            }
        });
        editconfigMenuItem.setEnabled(false);
        editconfigMenuItem.setText("Edit configuration");
        editconfigMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editconfigMenuItem_actionPerformed(e);
            }
        });
        txaIPAddress.setEditable(false);
        txaIPAddress.setBounds(new Rectangle(16, 72, 147, 171));
        lblIslands.setFont(new java.awt.Font("Dialog", 1, 14));
        lblIslands.setForeground(Color.black);
        lblIslands.setText("Islands");
        lblIslands.setBounds(new Rectangle(132, 28, 51, 21));
        txaPort.setBounds(new Rectangle(174, 72, 83, 170));
        txaPort.setEditable(false);
        contentPane.setBackground(SystemColor.inactiveCaption);
        lblIPAddress.setForeground(Color.white);
        lblIPAddress.setText(" IP Address");
        lblIPAddress.setBounds(new Rectangle(53, 51, 77, 21));
        lblPort.setForeground(Color.white);
        lblPort.setText("port");
        lblPort.setBounds(new Rectangle(208, 51, 30, 19));
        txaMessages.setEditable(false);
        txaMessages.setBounds(new Rectangle(15, 291, 467, 162));
        lblMessages.setBounds(new Rectangle(16, 259, 82, 26));
        lblMessages.setText("Messages");
        lblMessages.setForeground(Color.black);
        lblMessages.setFont(new java.awt.Font("Dialog", 1, 14));
        btnVisualize.setText("Visualize!");
        btnVisualize.setBounds(new Rectangle(297, 215, 102, 28));
        btnVisualize.setEnabled(false);
        btnVisualize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnVisualize_actionPerformed(e);
            }
        });
        btnStart.setText("Start");
        btnStart.setBounds(new Rectangle(297, 73, 102, 28));
        btnStart.setEnabled(false);
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnStart_actionPerformed(e);
            }
        });
        btnStop.setText("Stop");
        btnStop.setBounds(new Rectangle(297, 123, 102, 28));
        btnStop.setEnabled(false);
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                btnStop_actionPerformed(e);
            }
        });
        txtTime.setBounds(new Rectangle(425, 73, 50, 28));
        txtTime.setText("0");
        txtTime.setHorizontalAlignment(JTextField.RIGHT);
        txtTime.setEditable(false);
        txtTime.setEnabled(false);
        toolsMenu.setText("Tools");
        islandCheckBoxMenuItem.setText("Island");
        islandCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                islandCheckBoxMenuItem_actionPerformed(e);
            }
        });
        bootstrapCheckBoxMenuItem.setText("BootStrap");
        bootstrapCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                bootstrapCheckBoxMenuItem_actionPerformed(e);
            }
        });
        jMenuBar.add(fileMenu);
        jMenuBar.add(toolsMenu);
        jMenuBar.add(helpMenu);
        helpMenu.add(aboutMenuItem);
        fileMenu.add(setconfigMenuItem);
        fileMenu.add(editconfigMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        contentPane.add(lblIslands, null);
        contentPane.add(txaIPAddress, null);
        contentPane.add(txaPort, null);
        contentPane.add(lblPort, null);
        contentPane.add(lblIPAddress, null);
        contentPane.add(lblMessages, null);
        contentPane.add(txaMessages, null);
        contentPane.add(btnVisualize, null);
        contentPane.add(btnStart, null);
        contentPane.add(btnStop, null);
        contentPane.add(txtTime, null);
        toolsMenu.add(islandCheckBoxMenuItem);
        toolsMenu.add(bootstrapCheckBoxMenuItem);
    }

    /**
     * Overridden so we can exit when window is closed
     */
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }

    void aboutMenuItem_actionPerformed(ActionEvent e) {
        AboutWindow aboutWindow = new AboutWindow();
        aboutWindow.initiate();
    }

    void exitMenuItem_actionPerformed(ActionEvent e) {
        System.exit(0);
    }

    void setconfigMenuItem_actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        int state;
        do {
            state = fileChooser.showOpenDialog(this);
        } while (!(state == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().exists()
                || state == JFileChooser.CANCEL_OPTION));
        if (state == JFileChooser.APPROVE_OPTION) {
            ILPConstants.CONFIG_FILE = new String(fileChooser.getSelectedFile().getPath());
            ILPConstants.init();
            editconfigMenuItem.setEnabled(true);
            btnStart.setEnabled(true);
            btnVisualize.setEnabled(true);
        }
    }

    void editconfigMenuItem_actionPerformed(ActionEvent e) {
        ConfigWindow configWindow = new ConfigWindow();

        configWindow.txtTestCasePath.setText(ILPConstants.TEST_CASE_FILE);
        configWindow.txtGenerations.setText(String.valueOf(ILPConstants.GENERATIONS));
        configWindow.txtChromosomes.setText(String.valueOf(ILPConstants.NUM_CHROMS));
        configWindow.txtBootStrapIP.setText(ILPConstants.BOOT_STRAP_IP);
        configWindow.txtBootStrapPort.setText(String.valueOf(ILPConstants.BOOT_STRAP_PORT));

        String s = new String();
        for (int i = 0; i < ILPConstants.NUM_CONSTRAINTS - 1; i++) {
            s += ILPConstants.PENALTIES[i] + " ";
        }
        s += ILPConstants.PENALTIES[ILPConstants.NUM_CONSTRAINTS - 1];
        configWindow.txtPenalties.setText(s);
        configWindow.txtMigrationRate.setText(String.valueOf(ILPConstants.MIGRATION_RATE));
        configWindow.txtIterations.setText(String.valueOf(ILPConstants.ITERATIONS));
        configWindow.txtPort.setText(String.valueOf(ILPConstants.PORT));

        configWindow.initiate();
    }

    void islandCheckBoxMenuItem_actionPerformed(ActionEvent e) {
        if (bootstrapCheckBoxMenuItem.isSelected()) {
            bootstrapCheckBoxMenuItem.setSelected(false);
        }
        islandCheckBoxMenuItem.setSelected(true);

        if (!setconfigMenuItem.isEnabled()) {
            setconfigMenuItem.setEnabled(true);
        }
    }

    void bootstrapCheckBoxMenuItem_actionPerformed(ActionEvent e) {
        if (islandCheckBoxMenuItem.isSelected()) {
            islandCheckBoxMenuItem.setSelected(false);
        }
        bootstrapCheckBoxMenuItem.setSelected(true);
        if (!setconfigMenuItem.isEnabled()) {
            setconfigMenuItem.setEnabled(true);
        }
    }

    void btnStart_actionPerformed(ActionEvent e) {
        if (bootstrapCheckBoxMenuItem.isSelected()) {
            bootStrap = new BootStrap();
            bootStrap.addObserver(this);
            bootStrap.addObserver(visualizeWindow);
            bootStrap.execute();
        } else {
            island = new Island();
            island.addObserver(this);
            island.addObserver(visualizeWindow);
            islandThread = new Thread(island);
            islandThread.start();
        }

        btnStop.setEnabled(true);
        btnStart.setEnabled(false);

        if (timer == null || !timer.isAlive()) {
            Timer t = new Timer();
            t.addObserver(this);
            timer = new Thread(t);
            timer.start();
        }

    }

    void btnStop_actionPerformed(ActionEvent e) {
        if (bootstrapCheckBoxMenuItem.isSelected()) {
            bootStrap.isFinished = true;
            bootStrap.update(null, null);
        } else {
            island.isFinished = true;
        }
        timer.stop();
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
    }

    void btnVisualize_actionPerformed(ActionEvent e) {
        visualizeWindow.initiate(islandPool);
    }

    public void update(Observable o, Object arg) {
        if (o instanceof BootStrap || o instanceof Island) {
            if (arg instanceof Vector) {
                txaIPAddress.setText("");
                txaPort.setText("");
                islandPool = new Vector();
                islandPool = (Vector) arg;

                for (Iterator iter = islandPool.iterator(); iter.hasNext();) {
                    IslandID islandID = (IslandID) iter.next();
                    txaIPAddress.append(islandID.IP + "\n");
                    txaPort.append(String.valueOf(islandID.port) + "\n");
                }

            } else if (arg instanceof Message) {
                Message msg = (Message) arg;
                String tag = new String();
                switch (msg.getTag()) {
                    case 00:
                        tag = "NIL";
                        break;
                    case -11:
                        tag = "ADD_ME";
                        break;
                    case -22:
                        tag = "REMOVE_ME";
                        break;
                    case -33:
                        tag = "BROADCAST_ME";
                        break;
                    case -44:
                        tag = "ACCEPTED";
                        break;
                    case -55:
                        tag = "REMOVED";
                        if (o instanceof Island) {
                            timer.stop();
                            btnStart.setEnabled(true);
                            btnStop.setEnabled(false);

                            island.isFinished = true;
                        }
                        break;
                    case -66:
                        tag = "NEW_IMMIGRANTS";
                        break;
                    case -77:
                        tag = "ISLAND_POOL";
                        break;
                }
                String s = "Message from " + (msg.getSourceIP() == null ? "local IP" : msg.getSourceIP())
                        + " : " + msg.getSourcePort() + " to " + msg.getDestIP()
                        + " : " + msg.getDestPort() + " tag = " + tag + "\n";
                txaMessages.append(s);

            } else if (arg instanceof StringBuffer) {
                txaMessages.append(((StringBuffer) arg).toString());
            }

        } else if (o instanceof Timer && arg instanceof Long) {
            Long t = (Long) arg;
            txtTime.setText(String.valueOf(t.longValue()));
        }
    }
}

