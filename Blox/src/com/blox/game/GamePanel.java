package com.blox.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.blox.block.Block;
import com.blox.block.BlockDatabase;
import com.blox.component.Node;
import com.blox.component.Player;
import com.blox.util.Coordinate;
import com.blox.util.DropDirection;
import com.blox.util.Grid;
import com.blox.util.NodeDropCalculator;

import com.blox.ui.GUI;

public class GamePanel extends JPanel implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static long MAX_STATS_INTERVAL = 1000000000L;
	// private static long MAX_STATS_INTERVAL = 1000L;
	// record stats every 1 second (roughly)

	private static final int NO_DELAYS_PER_YIELD = 16;
	/*
	 * Number of frames with a delay of 0 ms before the animation thread yields
	 * to other running threads.
	 */

	private static int MAX_FRAME_SKIPS = 5; // was 2;
	// no. of frames that can be skipped in any one animation loop
	// i.e the games state is updated but not rendered

	private static int NUM_FPS = 10;
	// number of FPS values stored to get an average

	private int pWidth, pHeight; // dimensions of the panel

	// used for gathering statistics
	private long statsInterval = 0L; // in ns
	private long prevStatsTime;
	private long totalElapsedTime = 0L;
	private long gameStartTime;
	private int timeSpentInGame = 0; // in seconds

	private long frameCount = 0;
	private double fpsStore[];
	private long statsCount = 0;
	private double averageFPS = 0.0;

	private long framesSkipped = 0L;
	private long totalFramesSkipped = 0L;
	private double upsStore[];
	private double averageUPS = 0.0;

	private DecimalFormat df = new DecimalFormat("0.##"); // 2 dp
	private DecimalFormat timedf = new DecimalFormat("0.####"); // 4 dp

	private Thread animator; // the thread that performs the animation
	private volatile boolean running = false; // used to stop the animation
												// thread
	private volatile boolean isPaused = false;
	private long period; // period between drawing in _nanosecs_

	private GUI gui;
	/* GAME ELEMENTS */
	private Grid grid;
	/** NODES **/
	private NodeDropCalculator ndc = new NodeDropCalculator();
	private ArrayList<Node> dropList = new ArrayList<Node>();
	private ArrayList<Node> nodeList;
	/** BLOCKS **/
	private Block currentSelectedBlock;
	/** PLAYERS **/
	private Player currentPlayer;
	/** PLAYER1 **/
	private Player player1;
	/** PLAYER2 **/
	private Player player2;
	/** GAMESTATE **/
	private GamePhase gamePhase;
	
	/** FLAGS **/
	private boolean placeBlockFlag;
	//TODO ADD GAME ELEMENTS HERE

	// used at game termination
	private volatile boolean gameOver = false;
	private Font font;
	private FontMetrics metrics;

	// off screen rendering
	private Graphics dbg;
	private Image dbImage = null;

	

	public GamePanel(GUI gui, long period, int w, int h) {
		this.gui = gui;
		this.period = period;
		pWidth = w;
		pHeight = h;

		setBackground(Color.white);
		//TODO Investigate this -10 hack?
		setPreferredSize(new Dimension(pWidth-10, pHeight));

		setFocusable(true);
		requestFocus(); // the JPanel now has focus, so receives key events
		readyForTermination();

		//TODO CREATE GAME COMPONENTS
		// create game components
		this.grid = Grid.getGridWithSpaceBetweenStripes(pWidth, pHeight, GUI.coordinateHeight, GUI.coordinateWidth);
		this.nodeList = new ArrayList<Node>();
		initialiseAllNodes();
		/** set up player 1 with deck and hand **/
		this.player1 = new Player(null, null, 100, Color.red, DropDirection.UP); //player
		player1.setBlockDeck(BlockDatabase.getRandomDeck(10, player1)); //deck
		player1.popBlock(5); //hand
		/** set up player 2 with deck and hand **/
		this.player2 = new Player(null, null, 100, Color.blue, DropDirection.DOWN); //player
		this.player2.setColor(Color.blue);
		player2.setBlockDeck(BlockDatabase.getRandomDeck(10, player2)); //deck
		player2.popBlock(5); //hand
		
		this.gui.setPlayerLeft(player2);
		this.gui.setPlayerRight(player1);
		setCurrentPlayer(player1);
		this.gamePhase = GamePhase.BlockPhase1;
		switchTurn();
		// END create game components END
		// FLAGS
		this.placeBlockFlag = false;
		// END FLAGS
		
		gameOver = false;

		//Handle click events
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isLeftMouseButton(e)){
					leftMouseButtonClicked(e.getX(), e.getY());
				}
				else if(SwingUtilities.isRightMouseButton(e)){
					rightMouseButtonClicked(e.getX(), e.getY());
				}
			}
		});
		
		//Handle mouse motion events
		addMouseMotionListener(new MouseAdapter() {
			
			private int lastX = -1;
			private int lastY = -1;
			
			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				if(y<880){
					int xCoordinate = x/GUI.coordinateWidth;
					int yCoordinate = y/GUI.coordinateHeight;
					if(xCoordinate!=lastX || yCoordinate!=lastY){
						mouseMotionEvent(xCoordinate, yCoordinate);
						lastX = xCoordinate;
						lastY = yCoordinate;
					}
				}
			}
		});

		// set up message font
		font = new Font("SansSerif", Font.BOLD, 14);
		metrics = this.getFontMetrics(font);

		// initialise timing elements
		fpsStore = new double[NUM_FPS];
		upsStore = new double[NUM_FPS];
		for (int i = 0; i < NUM_FPS; i++) {
			fpsStore[i] = 0.0;
			upsStore[i] = 0.0;
		}
	} // end of WormPanel()
	
	private void initialiseAllNodes(){
		int minX = Coordinate.minX;
		int minY = Coordinate.minY;
		int maxX = Coordinate.maxX;
		int maxY = Coordinate.maxY;
		for(int x = minX; x<=maxX; x++){
			for(int y = minY; y<=maxY; y++){
				Coordinate c = Coordinate.getCoordinate(x, y);
				Node n = Node.getNode(c);
				this.nodeList.add(n);
			}
		}
		
	}

	private void readyForTermination() {
		addKeyListener(new KeyAdapter() {
			// listen for esc, q, end, ctrl-c on the canvas to
			// allow a convenient exit from the full screen configuration
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				if ((keyCode == KeyEvent.VK_ESCAPE)
						|| (keyCode == KeyEvent.VK_Q)
						|| (keyCode == KeyEvent.VK_END)
						|| ((keyCode == KeyEvent.VK_C) && e.isControlDown())) {
					running = false;
				}
			}
		});
	} // end of readyForTermination()

	public void addNotify()
	// only start the animation once the JPanel has been added to the JFrame
	{
		super.addNotify(); // creates the peer
		startGame(); // start the thread
	}

	private void startGame()
	// initialise and start the thread
	{
		if (animator == null || !running) {
			animator = new Thread(this);
			animator.start();
		}
	}

	// ------------- game life cycle methods ------------
	// called by the JFrame's window listener methods

	public void resumeGame()
	// called when the JFrame is activated / deiconified
	{
		isPaused = false;
	}

	public void pauseGame()
	// called when the JFrame is deactivated / iconified
	{
		isPaused = true;
	}

	public void stopGame()
	// called when the JFrame is closing
	{
		running = false;
	}

	// ----------------------------------------------

	private void leftMouseButtonClicked(int x, int y)
	{
		System.out.println(x+" "+y);
		if (!isPaused && !gameOver) {
			if(this.currentPlayer!=null && this.currentSelectedBlock!=null){
				setPlaceBlockFlag(true);
			}
		}
	}
	
	private void rightMouseButtonClicked(int x, int y)
	{
		if (!isPaused && !gameOver) {
			//TODO DO SOMETHING
		}
	}
	
	private void mouseMotionEvent(int xCoordinate, int yCoordinate){
		if(this.currentSelectedBlock!=null){
			for(Node n : this.dropList){
				n.setFocussed(false);
			}
			this.dropList.clear();
			this.dropList.addAll(this.ndc.getDrop(currentSelectedBlock, Coordinate.getCoordinate(xCoordinate, yCoordinate), this.currentPlayer));
			for(Node n : dropList){
				n.setFocussed(true);
			}
		}
	}
	
	public void playerHandClicked(int i, int j) {
		if(i==1){
			Object[] currentHand = player1.getHand().toArray();
			this.currentSelectedBlock = ((Block) currentHand[j-1]);
			if(this.currentPlayer!=player1){
				switchTurn();
			}
		}
		else if(i==2){
			Object[] currentHand = player2.getHand().toArray();
			this.currentSelectedBlock = ((Block) currentHand[j-1]);
			if(this.currentPlayer!=player2){
				switchTurn();
			}
		}
	}

	public void run()
	/* The frames of the animation are drawn inside the while loop. */
	{
		long beforeTime, afterTime, timeDiff, sleepTime;
		long overSleepTime = 0L;
		int noDelays = 0;
		long excess = 0L;
		Graphics g;

		gameStartTime = System.nanoTime();
		prevStatsTime = gameStartTime;
		beforeTime = gameStartTime;

		running = true;

		while (running) {
			gameUpdate();
			gameRender(); // render the game to a buffer
			paintScreen(); // draw the buffer on-screen

			afterTime = System.nanoTime();
			timeDiff = afterTime - beforeTime;
			sleepTime = (period - timeDiff) - overSleepTime;

			if (sleepTime > 0) { // some time left in this cycle
				try {
					Thread.sleep(sleepTime / 1000000L); // nano -> ms
				} catch (InterruptedException ex) {
				}
				overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
			} else { // sleepTime <= 0; the frame took longer than the period
				excess -= sleepTime; // store excess time value
				overSleepTime = 0L;

				if (++noDelays >= NO_DELAYS_PER_YIELD) {
					Thread.yield(); // give another thread a chance to run
					noDelays = 0;
				}
			}

			beforeTime = System.nanoTime();

			/*
			 * If frame animation is taking too long, update the game state
			 * without rendering it, to get the updates/sec nearer to the
			 * required FPS.
			 */
			int skips = 0;
			while ((excess > period) && (skips < MAX_FRAME_SKIPS)) {
				excess -= period;
				gameUpdate(); // update state but don't render
				skips++;
			}
			framesSkipped += skips;

			storeStats();
		}

//		printStats();
		System.exit(0); // so window disappears
	} // end of run()

	private void gameUpdate() {
		if (!isPaused && !gameOver) {
			//TODO Do something with game state
			// You can for instance set certain flags and here you would then put: if(flag) => do something
			/** PLACE A BLOCK **/
			if(placeBlockFlag){
				//Place the block
				currentSelectedBlock.activate(new ArrayList<Node>(dropList));
				for(Node n : dropList){
					n.setFocussed(false);
					if(n.isActive()){
						if(this.currentPlayer!=player1){
							player1.loseLife();
							gui.updatePlayerHealth();
						} else{
							player2.loseLife();
							gui.updatePlayerHealth();
						}
					}
					n.activate(currentSelectedBlock, currentSelectedBlock.getColor());
				}
				currentSelectedBlock = null;
				gui.clearHandSelection();
				setPlaceBlockFlag(false);
				switchPhase();
			}
		}
	} // end of gameUpdate()

	private void gameRender() {
		if (dbImage == null) {
			dbImage = createImage(pWidth, pHeight);
			if (dbImage == null) {
				System.out.println("dbImage is null");
				return;
			} else
				dbg = dbImage.getGraphics();
		}

		// clear the background
		dbg.setColor(Color.white);
		dbg.fillRect(0, 0, pWidth, pHeight);

		dbg.setColor(Color.blue);
		dbg.setFont(font);

		// report frame count & average FPS and UPS at top left
		// dbg.drawString("Frame Count " + frameCount, 10, 25);
//		dbg.drawString(
//				"Average FPS/UPS: " + df.format(averageFPS) + ", "
//						+ df.format(averageUPS), 20, 20 ); // was (10,55)

		dbg.setColor(Color.black);

		// draw game elements
		/**
		 * Draw Nodes
		 */
		ArrayList<Node> nodeCopy = new ArrayList<Node>(nodeList);
		for(Node n : nodeCopy){
			n.draw(dbg);
		}
		/**
		 * Draw player1 hand
		 */
		ArrayDeque<Block> p1Hand = player1.getHand().clone();
		Iterator<Block> it = p1Hand.iterator();
		int i = 1;
		while(it.hasNext()){
			Block b = (Block) it.next();
			gui.setPlayer1HandAtIndex(b.getImage(), i);
			i++;
		}
		/**
		 * Draw player2 hand
		 */
		ArrayDeque<Block> p2Hand = player2.getHand().clone();
		Iterator<Block> it2 = p2Hand.iterator();
		int j = 1;
		while(it2.hasNext()){
			Block b = (Block) it2.next();
			gui.setPlayer2HandAtIndex(b.getImage(), j);
			j++;
		}
		
		/**
		 * Draw grid (last to go over the nodes).
		 */
		this.grid.draw(dbg);
		// TODO Make game objects draw themselves eg: point.draw(dbg).	

		if (gameOver)
			gameOverMessage(dbg);
	} // end of gameRender()

	private void gameOverMessage(Graphics g)
	// center the game-over message in the panel
	{
		// TODO display a game over message
		// String msg = "Game Over. Your Score: " + score;
		// int x = (pWidth - metrics.stringWidth(msg))/2;
		// int y = (pHeight - metrics.getHeight())/2;
		// g.setColor(Color.red);
		// g.setFont(font);
		// g.drawString(msg, x, y);
	} // end of gameOverMessage()

	private void paintScreen()
	// use active rendering to put the buffered image on-screen
	{
		Graphics g;
		try {
			g = this.getGraphics();
			if ((g != null) && (dbImage != null))
				g.drawImage(dbImage, 0, 0, null);
			// Sync the display on some systems.
			// (on Linux, this fixes event queue problems)
			Toolkit.getDefaultToolkit().sync();
			g.dispose();
		} catch (Exception e) // quite commonly seen at applet destruction
		{
			System.out.println("Graphics error: " + e);
		}
	} // end of paintScreen()

	private void storeStats()
	/*
	 * The statistics: - the summed periods for all the iterations in this
	 * interval (period is the amount of time a single frame iteration should
	 * take), the actual elapsed time in this interval, the error between these
	 * two numbers;
	 * 
	 * - the total frame count, which is the total number of calls to run();
	 * 
	 * - the frames skipped in this interval, the total number of frames
	 * skipped. A frame skip is a game update without a corresponding render;
	 * 
	 * - the FPS (frames/sec) and UPS (updates/sec) for this interval, the
	 * average FPS & UPS over the last NUM_FPSs intervals.
	 * 
	 * The data is collected every MAX_STATS_INTERVAL (1 sec).
	 */
	{
		frameCount++;
		statsInterval += period;

		if (statsInterval >= MAX_STATS_INTERVAL) { // record stats every
													// MAX_STATS_INTERVAL
			long timeNow = System.nanoTime();
			long realElapsedTime = timeNow - prevStatsTime; // time since last
															// stats collection
			totalElapsedTime += realElapsedTime;

			totalFramesSkipped += framesSkipped;

			double actualFPS = 0; // calculate the latest FPS and UPS
			double actualUPS = 0;
			if (totalElapsedTime > 0) {
				actualFPS = (((double) frameCount / totalElapsedTime) * 1000000000L);
				actualUPS = (((double) (frameCount + totalFramesSkipped) / totalElapsedTime) * 1000000000L);
			}

			// store the latest FPS and UPS
			fpsStore[(int) statsCount % NUM_FPS] = actualFPS;
			upsStore[(int) statsCount % NUM_FPS] = actualUPS;
			statsCount = statsCount + 1;

			double totalFPS = 0.0; // total the stored FPSs and UPSs
			double totalUPS = 0.0;
			for (int i = 0; i < NUM_FPS; i++) {
				totalFPS += fpsStore[i];
				totalUPS += upsStore[i];
			}

			if (statsCount < NUM_FPS) { // obtain the average FPS and UPS
				averageFPS = totalFPS / statsCount;
				averageUPS = totalUPS / statsCount;
			} else {
				averageFPS = totalFPS / NUM_FPS;
				averageUPS = totalUPS / NUM_FPS;
			}
			/*
			 * System.out.println(timedf.format( (double)
			 * statsInterval/1000000000L) + " " + timedf.format((double)
			 * realElapsedTime/1000000000L) + "s " + df.format(timingError) +
			 * "% " + frameCount + "c " + framesSkipped + "/" +
			 * totalFramesSkipped + " skip; " + df.format(actualFPS) + " " +
			 * df.format(averageFPS) + " afps; " + df.format(actualUPS) + " " +
			 * df.format(averageUPS) + " aups" );
			 */
			framesSkipped = 0;
			prevStatsTime = timeNow;
			statsInterval = 0L; // reset
		}
	} // end of storeStats()

	private void printStats() {
		System.out.println("Frame Count/Loss: " + frameCount + " / "
				+ totalFramesSkipped);
		System.out.println("Average FPS: " + df.format(averageFPS));
		System.out.println("Average UPS: " + df.format(averageUPS));
	} // end of printStats()
	
	/**
	 * -----------------------------------------------------------------------------------------------------
	 * -----------------------------------------------------------------------------------------------------
	 * -----------------------------------------------------------------------------------------------------
	 * ----------------------------------------GAME SPECIFIC METHODS----------------------------------------
	 * -----------------------------------------------------------------------------------------------------
	 * -----------------------------------------------------------------------------------------------------
	 * -----------------------------------------------------------------------------------------------------
	 */
	
	public void switchTurn(){
		if(player1!=null && player2!=null){
			if(this.currentPlayer==player1){
				setCurrentPlayer(player2);
			}else{
				setCurrentPlayer(player1);
			}
		}
		else{
			System.out.println("GamePanel.switchTurn(): ERROR, PLAYERS NOT INITIALISED YET.");
		}
		gui.setPlayerTurn(this.currentPlayer);
	}
	
	/**
	 * Switches the game phase to the next phase:
	 * BlockPhase1 => PowerupPhase => BlockPhase2 => repeat...
	 * if the current phase is BlockPhase2, the players also switch turn.
	 */
	public void switchPhase(){
		if(this.gamePhase!=null){
			switch(gamePhase){
			case BlockPhase1:
				this.gamePhase = GamePhase.PowerupPhase;
				break;
			case PowerupPhase:
				this.gamePhase = GamePhase.BlockPhase2;
				break;
			case BlockPhase2:
				switchTurn();
				this.gamePhase = GamePhase.BlockPhase1;
				break;
			}
		}
		gui.setPhase(this.gamePhase);
	}
	
	public void setPlaceBlockFlag(boolean b){
		this.placeBlockFlag = b;
	}
	
	public void setCurrentPlayer(Player player){
		this.currentPlayer = player;
		gui.currentPlayer = player;
	}
}
