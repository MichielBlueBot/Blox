package com.blox.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.blox.component.Player;
import com.blox.game.GamePanel;
import com.blox.game.GamePhase;
import com.blox.util.DropDirection;

public class GUI extends JFrame implements WindowListener {

	/**
	 * ----------------------------------------------------------------------------------------
	 * VARIABLES AND FIELDS
	 * ----------------------------------------------------------------------------------------
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * DEFAULT VALUES
	 */
	private static int DEFAULT_FPS = 80;
	private static int DEFAULT_PWIDTH = 600;
	private static int DEFAULT_PHEIGHT = 900;
	public static int DEFAULT_SIDECONTAINER_WIDTH = 200; // dimensions of sideContainers
	public static int DEFAULT_SIDECONTAINER_HEIGHT = DEFAULT_PHEIGHT; // dimensions of sideContainers
	public static int DEFAULT_HANDCONTAINER_WIDTH = DEFAULT_SIDECONTAINER_WIDTH;
	public static int DEFAULT_HANDCONTAINER_HEIGHT = 3*DEFAULT_SIDECONTAINER_HEIGHT/5;
	public static int DEFAULT_TOPCONTAINER_HEIGHT = 50;
	public static int DEFAULT_COORDINATE_WIDTH = 40;
	public static int DEFAULT_COORDINATE_HEIGHT	= 40;
	
	/**
	 * FIELDS
	 */
	public static int pWidth = DEFAULT_PWIDTH; // dimensions of the panel
	public static int pHeight = DEFAULT_PHEIGHT; // dimensions of the panel
	public static final int coordinateWidth = DEFAULT_COORDINATE_WIDTH;
	public static final int coordinateHeight = DEFAULT_COORDINATE_HEIGHT;
	private GamePanel gp;
	//Textfields top bar
	private JTextField jtfBlockPhase1;
	private JTextField jtfPowerupPhase;
	private JTextField jtfBlockPhase2;
	
	//Fields left sidebar
	private JTextField jtfPlayer1Life;
	private JTextField jtfPlayer1Powerup;
	private JLabel jlPlayer1Hand1;
	private JLabel jlPlayer1Hand2;
	private JLabel jlPlayer1Hand3;
	private JLabel jlPlayer1Hand4;
	private JLabel jlPlayer1Hand5;
	private JPanel jpPlayer1Blocks;
	//Fields right sidebar
	private JTextField jtfPlayer2Life;
	private JTextField jtfPlayer2Powerup;
	private JLabel jlPlayer2Hand1;
	private JLabel jlPlayer2Hand2;
	private JLabel jlPlayer2Hand3;
	private JLabel jlPlayer2Hand4;
	private JLabel jlPlayer2Hand5;
	private JPanel jpPlayer2Blocks;
	
	/**
	 * ----------------------------------------------------------------------------------------
	 * END OF VARIABLES AND FIELDS
	 * ----------------------------------------------------------------------------------------
	 */
	

	public static void main(String args[]) {
	    int fps = DEFAULT_FPS;
	    if (args.length != 0){
	      fps = Integer.parseInt(args[0]);
	    }
	    long period = (long) 1000.0/fps;
	    new GUI(period*1000000L);    // ms --> nanosecs 
	}

	public GUI(long period) {
		super("Search");
		makeGUI(period);
		setPhase(GamePhase.BlockPhase1);
		pack(); // first one (the GUI doesn't include the JPanel yet)
		setResizable(false); // sizes may change when non-resizable
		setResizable(true);
		pack();
		addWindowListener(this);
		setResizable(false);
		setVisible(true);
	}

	private void makeGUI(long period)
	// Create the GUI, minus the JPanel drawing area
	{
		Container c = getContentPane(); // default BorderLayout used
		c.setLayout(new BorderLayout(0, 10)); // specify gaps
		
		/** ADD NORTH GAMESTATE PANEL **/
		JPanel topContainer = new JPanel();
		topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.X_AXIS));
		topContainer.setPreferredSize(new Dimension(pWidth, DEFAULT_TOPCONTAINER_HEIGHT));
		jtfBlockPhase1 = new JTextField("BLOCK PHASE 1");
		jtfBlockPhase1.setHorizontalAlignment(JTextField.CENTER);
		jtfPowerupPhase = new JTextField("POWERUP PHASE");
		jtfPowerupPhase.setHorizontalAlignment(JTextField.CENTER);
		jtfBlockPhase2 = new JTextField("BLOCK PHASE 2");
		jtfBlockPhase2.setHorizontalAlignment(JTextField.CENTER);
		topContainer.add(jtfBlockPhase1);
		topContainer.add(jtfPowerupPhase);
		topContainer.add(jtfBlockPhase2);
		c.add(topContainer, BorderLayout.NORTH);
		
		/** ADD THE SIDEBARS **/ 
		JPanel leftContainer = new JPanel();
		leftContainer.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_SIDECONTAINER_HEIGHT));
		JPanel rightContainer = new JPanel();
		rightContainer.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_SIDECONTAINER_HEIGHT));
		leftContainer.setLayout(new BoxLayout(leftContainer, BoxLayout.Y_AXIS));
		rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));
		jtfPlayer1Life = new JTextField("Player 1 LIFE");
		jtfPlayer1Life.setBackground(new Color(255,255,255));
		jtfPlayer1Life.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_SIDECONTAINER_HEIGHT/5));
		jtfPlayer1Powerup = new JTextField("Player 1 POWERUP");
		jtfPlayer1Powerup.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_SIDECONTAINER_HEIGHT/5));
		jpPlayer1Blocks = new JPanel();
		jpPlayer1Blocks.setLayout(new GridLayout(5,1));
		jpPlayer1Blocks.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT));
		jtfPlayer2Life = new JTextField("Player 2 LIFE");
		jtfPlayer2Life.setBackground(new Color(255,255,255));
		jtfPlayer2Life.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_SIDECONTAINER_HEIGHT/5));
		jtfPlayer2Powerup = new JTextField("Player 2 POWERUP");
		jtfPlayer2Powerup.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_SIDECONTAINER_HEIGHT/5));
		jpPlayer2Blocks = new JPanel();
		jpPlayer2Blocks.setLayout(new GridLayout(5,1));
		jpPlayer2Blocks.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT));
		leftContainer.add(jtfPlayer1Life);
		leftContainer.add(jtfPlayer1Powerup);
		leftContainer.add(jpPlayer1Blocks);
		rightContainer.add(jtfPlayer2Life);
		rightContainer.add(jtfPlayer2Powerup);
		rightContainer.add(jpPlayer2Blocks);
		c.add(leftContainer, BorderLayout.EAST);
		c.add(rightContainer, BorderLayout.WEST);
		
		/** ADD THE HANDS FOR PLAYER 1**/
		jlPlayer1Hand1 = new JLabel();
		jlPlayer1Hand1.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT/5));
		jlPlayer1Hand1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlPlayer1Hand1.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
		jlPlayer1Hand2 = new JLabel();
		jlPlayer1Hand2.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT/5));
		jlPlayer1Hand2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlPlayer1Hand2.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
		jlPlayer1Hand3 = new JLabel();
		jlPlayer1Hand3.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT/5));
		jlPlayer1Hand3.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlPlayer1Hand3.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
		jlPlayer1Hand4 = new JLabel();
		jlPlayer1Hand4.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT/5));
		jlPlayer1Hand4.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlPlayer1Hand4.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
		jlPlayer1Hand5 = new JLabel();
		jlPlayer1Hand5.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT/5));
		jlPlayer1Hand5.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlPlayer1Hand5.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
		jpPlayer1Blocks.add(jlPlayer1Hand1);
		jpPlayer1Blocks.add(jlPlayer1Hand2);
		jpPlayer1Blocks.add(jlPlayer1Hand3);
		jpPlayer1Blocks.add(jlPlayer1Hand4);
		jpPlayer1Blocks.add(jlPlayer1Hand5);
		
		/** ADD THE HANDS FOR PLAYER 1**/
		jlPlayer2Hand1 = new JLabel();
		jlPlayer2Hand1.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT/5));
		jlPlayer2Hand1.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlPlayer2Hand1.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
		jlPlayer2Hand2 = new JLabel();
		jlPlayer2Hand2.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT/5));
		jlPlayer2Hand2.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlPlayer2Hand2.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
		jlPlayer2Hand3 = new JLabel();
		jlPlayer2Hand3.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT/5));
		jlPlayer2Hand3.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlPlayer2Hand3.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
		jlPlayer2Hand4 = new JLabel();
		jlPlayer2Hand4.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT/5));
		jlPlayer2Hand4.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlPlayer2Hand4.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
		jlPlayer2Hand5 = new JLabel();
		jlPlayer2Hand5.setPreferredSize(new Dimension(DEFAULT_SIDECONTAINER_WIDTH, DEFAULT_HANDCONTAINER_HEIGHT/5));
		jlPlayer2Hand5.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		jlPlayer2Hand5.setHorizontalAlignment((int) JPanel.CENTER_ALIGNMENT);
		jpPlayer2Blocks.add(jlPlayer2Hand1);
		jpPlayer2Blocks.add(jlPlayer2Hand2);
		jpPlayer2Blocks.add(jlPlayer2Hand3);
		jpPlayer2Blocks.add(jlPlayer2Hand4);
		jpPlayer2Blocks.add(jlPlayer2Hand5);
		
		/** ADD THE CENTER GAMEPANEL **/
		gp = new GamePanel(this, period, pWidth, pHeight);
		c.add(gp,BorderLayout.CENTER);
		
		/**
		 * -----------------------------------
		 * ADD LISTENERS TO ALL THE COMPONENTS
		 * -----------------------------------
		 */
		
		jlPlayer1Hand1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					setAllHandBordersGray();
					jlPlayer1Hand1.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
					gp.playerHandClicked(1,1);
				}
			}
		});
		jlPlayer1Hand2.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					setAllHandBordersGray();
					jlPlayer1Hand2.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
					gp.playerHandClicked(1,2);
				}
			}
		});
		jlPlayer1Hand3.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					setAllHandBordersGray();
					jlPlayer1Hand3.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
					gp.playerHandClicked(1,3);
				}
			}
		});
		jlPlayer1Hand4.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					setAllHandBordersGray();
					jlPlayer1Hand4.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
					gp.playerHandClicked(1,4);
				}
			}
		});
		jlPlayer1Hand5.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					setAllHandBordersGray();
					jlPlayer1Hand5.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
					gp.playerHandClicked(1,5);
				}
			}
		});
		jlPlayer2Hand1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					setAllHandBordersGray();
					jlPlayer2Hand1.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
					gp.playerHandClicked(2,1);
				}
			}
		});
		jlPlayer2Hand2.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					setAllHandBordersGray();
					jlPlayer2Hand2.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
					gp.playerHandClicked(2,2);
				}
			}
		});
		jlPlayer2Hand3.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					setAllHandBordersGray();
					jlPlayer2Hand3.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
					gp.playerHandClicked(2,3);
				}
			}
		});
		jlPlayer2Hand4.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					setAllHandBordersGray();
					jlPlayer2Hand4.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
					gp.playerHandClicked(2,4);
				}
			}
		});
		jlPlayer2Hand5.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					setAllHandBordersGray();
					jlPlayer2Hand5.setBorder(BorderFactory.createLineBorder(Color.CYAN, 3));
					gp.playerHandClicked(2,5);
				}
			}
		});
	}
	
	private void setAllHandBordersGray(){
		jlPlayer1Hand1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		jlPlayer1Hand2.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		jlPlayer1Hand3.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		jlPlayer1Hand4.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		jlPlayer1Hand5.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		jlPlayer2Hand1.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		jlPlayer2Hand2.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		jlPlayer2Hand3.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		jlPlayer2Hand4.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
		jlPlayer2Hand5.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
	}
	
	public void setPhase(GamePhase phase){
		jtfBlockPhase1.setBackground(new Color(255,255,255));
		jtfPowerupPhase.setBackground(new Color(255,255,255));
		jtfBlockPhase2.setBackground(new Color(255,255,255));
		switch(phase){
			case BlockPhase1:
				jtfBlockPhase1.setBackground(new Color(0,255,0));
				break;
			case PowerupPhase:
				jtfPowerupPhase.setBackground(new Color(0,255,0));
				break;
			case BlockPhase2:
				jtfBlockPhase2.setBackground(new Color(0,255,0));
				break;
		}
	}
	
	public void setPlayerTurn(Player p){
		Color color = p.getColor();
		DropDirection dd = p.getDropDirection();
		switch(dd){
		case UP:
			jtfPlayer1Life.setBackground(color);
			jtfPlayer2Life.setBackground(new Color(255,255,255));
			break;
		case DOWN:
			jtfPlayer1Life.setBackground(new Color(255,255,255));
			jtfPlayer2Life.setBackground(color);
			break;
		}
	}
	
	public void setPlayer1HandAtIndex(ImageIcon icon, int index){
		if(icon!=null){
			switch(index){
				case 1:
					jlPlayer1Hand1.setIcon(icon);
					break;
				case 2:
					jlPlayer1Hand2.setIcon(icon);
					break;
				case 3:
					jlPlayer1Hand3.setIcon(icon);
					break;
				case 4:
					jlPlayer1Hand4.setIcon(icon);
					break;
				case 5:
					jlPlayer1Hand5.setIcon(icon);
					break;
			}
		}
		else{
			System.out.println("ICON IS NULL : "+ index);
		}
	}
	
	public void setPlayer2HandAtIndex(ImageIcon icon, int index){
		switch(index){
			case 1:
				jlPlayer2Hand1.setIcon(icon);
				break;
			case 2:
				jlPlayer2Hand2.setIcon(icon);
				break;
			case 3:
				jlPlayer2Hand3.setIcon(icon);
				break;
			case 4:
				jlPlayer2Hand4.setIcon(icon);
				break;
			case 5:
				jlPlayer2Hand5.setIcon(icon);
				break;
		}
	}
	  // ----------------- window listener methods -------------

	  public void windowActivated(WindowEvent e) 
	  { gp.resumeGame();  }

	  public void windowDeactivated(WindowEvent e) 
	  { gp.pauseGame();  }


	  public void windowDeiconified(WindowEvent e) 
	  { gp.resumeGame();  }

	  public void windowIconified(WindowEvent e) 
	  { gp.pauseGame(); }

	  public void windowClosing(WindowEvent e)
	  { gp.stopGame();  }

	  public void windowClosed(WindowEvent e) {}
	  public void windowOpened(WindowEvent e) {}

	  // ----------------------------------------------------
}
