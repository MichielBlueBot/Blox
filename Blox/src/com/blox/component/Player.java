package com.blox.component;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;

import com.blox.block.Block;
import com.blox.powerup.Powerup;
import com.blox.util.DropDirection;

public class Player {
	
	/**
	 * DEFAULTS
	 */
	public static final int DEFAULT_PLAYER_HEALTH = 100;
	public static final Color DEFAULT_PLAYER_COLOR = Color.black;
	/**
	 * VARIABLES AND FIELDS
	 */
	private ArrayDeque<Block> blockDeck; //Blocks in the players deck.
	private ArrayDeque<Powerup> powerupDeck; //Powerups in the players deck.
	private int health; //The amount of health this player has left.
	private boolean isDead;
	private Powerup currentPowerup;
	private ArrayDeque<Block> hand;	//The blocks in this players hand, these are available for play.
	private Color color;
	private DropDirection dropDirection; //UP or DOWN
	
	public Player(){
		//TODO work out
		setBlockDeck(new ArrayDeque<Block>());
		setPowerupDeck(new ArrayDeque<Powerup>());
		setCurrentPowerup(null);
		setHealth(DEFAULT_PLAYER_HEALTH);
		setHand(new ArrayDeque<Block>());
		setColor(DEFAULT_PLAYER_COLOR);
		setDropDirection(DropDirection.UP);
	}
	
	public Player(ArrayDeque<Block> blockDeck, ArrayDeque<Powerup> powerupDeck, int health, Color color, DropDirection drop){
		//TODO work out
		setBlockDeck(blockDeck);
		setPowerupDeck(powerupDeck);
		setCurrentPowerup(null);
		setHealth(health);
		setHand(new ArrayDeque<Block>());
		setColor(color);
		setDropDirection(drop);
	}

	public ArrayDeque<Block> getBlockDeck() {
		return blockDeck;
	}

	public ArrayDeque<Powerup> getPowerupDeck() {
		return powerupDeck;
	}

	public int getHealth() {
		return health;
	}

	public ArrayDeque<Block> getHand() {
		return hand;
	}

	public void setBlockDeck(ArrayDeque<Block> blockDeck) {
		this.blockDeck = blockDeck;
	}

	public void setPowerupDeck(ArrayDeque<Powerup> powerupDeck) {
		this.powerupDeck = powerupDeck;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setHand(ArrayDeque<Block> hand) {
		this.hand = hand;
	}

	public Powerup getCurrentPowerup() {
		return currentPowerup;
	}

	public void setCurrentPowerup(Powerup currentPowerup) {
		this.currentPowerup = currentPowerup;
	}
	
	/**
	 * Pops a block from this players deck and puts it into the players hand.
	 * Excess cards in the players hand (>5) will trigger a discard in a FIFO manner.
	 */
	public void popBlock(){
		if(this.blockDeck.size()>0){
			Block block = blockDeck.poll();
			if(block!=null){
				if(this.hand.size()==5){
					this.hand.removeFirst();
				}
				this.hand.add(block);
			}
		}
	}
	
	/**
	 * Pops 'amount' blocks from this players deck and puts it into the players hand.
	 * Excess cards in the players hand (>5) will trigger a discard in a FIFO manner.
	 */
	public void popBlock(int amount){
		for(int i = 0; i<amount; i++){
			this.popBlock();
		}
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public DropDirection getDropDirection() {
		return dropDirection;
	}

	private void setDropDirection(DropDirection dropDirection) {
		this.dropDirection = dropDirection;
	}

	/**
	 * Lowers this players health by 1.
	 */
	public void loseLife() {
		this.health = this.health-1;
	}
	
	/**
	 * Lowest this players health by 'amount'.
	 */
	public void loseLife(int amount){
		this.health = this.health-amount;
	}

	public boolean isDead() {
		return health<=0;
	}

}
