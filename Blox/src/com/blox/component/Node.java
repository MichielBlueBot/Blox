package com.blox.component;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import com.blox.block.Block;
import com.blox.ui.GUI;
import com.blox.util.Coordinate;

public class Node {

	/**
	 * DEFAULTS
	 */
	public static final Color DEFAULT_NODE_COLOR = Color.white;
	public static final Color DEFAULT_NODE_FOCUS_COLOR = Color.yellow;
	public static final int DEFAULT_NODE_FOCUS_WIDTH = 10;
	
	/**
	 * VARIABLES AND FIELDS
	 */
	private static ArrayList<Node> existingNodes = new ArrayList<Node>();
	private Coordinate coordinate;
	private Color color;
	private Block owner;
	private boolean isActive;
	private boolean isFocussed;
	
	/**
	 * -----------------------------------------------------------------	
	 */
	
	@Deprecated
	private Node(){
		setCoordinate(null);
		setOwner(null);
		setColor(DEFAULT_NODE_COLOR);
	}
	
	@Deprecated
	private Node(Coordinate coordinate, Color color, Block owner){
		//No need to check for valid coordinate as they are checked on creation. If a coordinate exists, it is valid within this game.
		setCoordinate(coordinate);
		setOwner(owner);
		setColor(color);
	}
	
	@Deprecated
	private Node(Coordinate coordinate, Block owner){
		setCoordinate(coordinate);
		setOwner(owner);
		setColor(owner.getColor());
	}

	private Node(Coordinate c) {
		setCoordinate(c);
		setOwner(null);
		setColor(DEFAULT_NODE_COLOR);
	}
	
	public static Node getNode(Coordinate c){
		Node newNode = new Node(c);
		for(Node n : existingNodes){
			if(n.equals(newNode)){
				return n;
			}
		}
		existingNodes.add(newNode);
		return newNode;
	}

	public Coordinate getCoordinate() {
		return coordinate;
	}
	
	public int getX(){
		return this.getCoordinate().getX();
	}
	
	public int getY(){
		return this.getCoordinate().getY();
	}

	public Color getColor() {
		return color;
	}

	public Block getOwner() {
		return owner;
	}

	private void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}

	private void setColor(Color color) {
		this.color = color;
	}

	private void setOwner(Block owner) {
		this.owner = owner;
		if(owner!=null){
			setActive(true);
		}
		else{
			setActive(false);
		}
	}
	
	/**
	 * Activate this node to have the given block as owner and also set its color appropriately.
	 * Puts this node into the Active state.
	 */
	public void activate(Block owner, Color color){
		setOwner(owner); //implicitly sets active state
		setColor(color);
	}
	
	/**
	 * Deactivate this node to owner==null, and color to DEFAULT_NODE_COLOR.
	 * Puts this node into the Non-Active state.
	 */
	public void deactivate(){
		setOwner(null);
		setColor(DEFAULT_NODE_COLOR);
	}
	
	@Override
	public boolean equals(Object other){
		boolean result = false;
		if(other instanceof Node){
			Node n = (Node) other;
			if(this.getCoordinate().equals(((Node) other).getCoordinate())){
				result = true;
			}
		}
		return result;
	}
	
	public void draw(Graphics g){
		if(this.isFocussed()){
			g.setColor(Color.yellow);
			g.fillRect(this.getX()*GUI.coordinateWidth, this.getY()*GUI.coordinateHeight, GUI.coordinateWidth, GUI.coordinateHeight);
			g.setColor(this.getColor());
			g.fillRect(this.getX()*GUI.coordinateWidth+DEFAULT_NODE_FOCUS_WIDTH, this.getY()*GUI.coordinateHeight+DEFAULT_NODE_FOCUS_WIDTH, 
					GUI.coordinateWidth-(2*DEFAULT_NODE_FOCUS_WIDTH), GUI.coordinateHeight-(2*DEFAULT_NODE_FOCUS_WIDTH));
		}else{
			g.setColor(this.getColor());
			g.fillRect(this.getX()*GUI.coordinateWidth, this.getY()*GUI.coordinateHeight, GUI.coordinateWidth, GUI.coordinateHeight);
		}
	}

	public boolean isActive() {
		return isActive;
	}

	private void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isFocussed() {
		return isFocussed;
	}

	public void setFocussed(boolean isFocussed) {
		this.isFocussed = isFocussed;
	}
}
