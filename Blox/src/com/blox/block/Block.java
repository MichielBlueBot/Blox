package com.blox.block;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.blox.component.Node;
import com.blox.component.Player;

public abstract class Block {
	
	/**
	 * DEFAULTS
	 */
	public static final Color DEFAULT_BLOCK_COLOR = Color.black;
	
	/**
	 * VARIABLES AND FIELDS
	 */
	private Color color;
	private int size;
	private BlockType type;
	private ImageIcon image;
	private ArrayList<Node> nodeList = new ArrayList<Node>();
	private boolean isActive;
	private Player owner;
	
	public ArrayList<Node> getNodeList(){
		return nodeList;
	}
	
	public void addNode(Node n){
		this.nodeList.add(n);
	}
	
	public void setColor(Color color){
		this.color = color;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	public int getSize(){
		return this.size;
	}

	public BlockType getType() {
		return type;
	}

	public void setType(BlockType type) {
		this.type = type;
	}

	public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon imageIcon) {
		this.image = imageIcon;
	}
	
	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path) {
	    java.net.URL imgURL = getClass().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}

	public boolean isActive() {
		return isActive;
	}

	private void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public void activate(ArrayList<Node> nodeList){
		this.nodeList = nodeList;
		this.setActive(true);
	}

	public Player getOwner() {
		return owner;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

}
