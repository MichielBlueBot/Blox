package com.blox.util;

import java.awt.Color;
import java.awt.Graphics;

public class Grid {
	
	private int width;
	private int height;
	private int nbOfStripesHorizontal;
	private int nbOfStripesVertical;
	private int spaceBetweenHorizontalStripes;
	private int spaceBetweenVerticalStripes;
	
	public Grid(int width, int height, int nbOfStripesHorizontal, int nbOfStripesVertical, int spaceBetweenHorizontalStripes, int spaceBetweenVerticalStripes){
		setWidth(width);
		setHeight(height);
		setNbOfStripesHorizontal(nbOfStripesHorizontal);
		setNbOfStripesVertical(nbOfStripesVertical);
		setSpaceBetweenHorizontalStripes(spaceBetweenHorizontalStripes);
		setSpaceBetweenVerticalStripes(spaceBetweenVerticalStripes);
	}
	
	public static Grid getGridWithNbOfStripes(int width, int height, int nbOfStripesHorizontal, int nbOfStripesVertical){
		return new Grid(width, height, nbOfStripesHorizontal, nbOfStripesVertical,height/(nbOfStripesHorizontal+1), width/(nbOfStripesVertical+1));
	}
	
	public static Grid getGridWithSpaceBetweenStripes(int width, int height, int spaceBetweenHorizontalStripes, int spaceBetweenVerticalStripes){
		return new Grid(width, height, (height/spaceBetweenHorizontalStripes)-1, (width/spaceBetweenVerticalStripes)-1,spaceBetweenHorizontalStripes, spaceBetweenVerticalStripes);
	}
	
	public static Grid getStandardGrid(int width, int height){
		return new Grid(width, height, (height/10)-1, (width/10)-1, 10,10);
	}
	
	public void draw(Graphics g){
		g.setColor(Color.black);
		for(int i = this.getSpaceBetweenHorizontalStripes(); i<this.getHeight(); i = i + this.getSpaceBetweenHorizontalStripes()){
			g.drawLine(0, i, (this.getWidth()/this.getSpaceBetweenVerticalStripes())*this.getSpaceBetweenVerticalStripes(),i);
		}
		for(int j = this.getSpaceBetweenVerticalStripes(); j<this.getWidth(); j = j + this.getSpaceBetweenVerticalStripes()){
			g.drawLine(j, 0, j , (this.getHeight()/this.getSpaceBetweenHorizontalStripes())*this.getSpaceBetweenHorizontalStripes());
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getNbOfStripesHorizontal() {
		return nbOfStripesHorizontal;
	}

	public int getNbOfStripesVertical() {
		return nbOfStripesVertical;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setNbOfStripesHorizontal(int nbOfStripesHorizontal) {
		this.nbOfStripesHorizontal = nbOfStripesHorizontal;
	}

	public void setNbOfStripesVertical(int nbOfStripesVertical) {
		this.nbOfStripesVertical = nbOfStripesVertical;
	}

	public int getSpaceBetweenHorizontalStripes() {
		return spaceBetweenHorizontalStripes;
	}

	public void setSpaceBetweenHorizontalStripes(
			int spaceBetweenHorizontalStripes) {
		this.spaceBetweenHorizontalStripes = spaceBetweenHorizontalStripes;
	}

	public int getSpaceBetweenVerticalStripes() {
		return spaceBetweenVerticalStripes;
	}

	public void setSpaceBetweenVerticalStripes(int spaceBetweenVerticalStripes) {
		this.spaceBetweenVerticalStripes = spaceBetweenVerticalStripes;
	}
}
