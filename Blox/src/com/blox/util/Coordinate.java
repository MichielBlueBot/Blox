package com.blox.util;

import java.util.ArrayList;
import com.blox.ui.GUI;

public class Coordinate {
	
	/**
	 * CONSTANTS
	 */
	public static final int minX = 0;
	public static final int minY = 0;
	public static final int maxX = GUI.pWidth/GUI.coordinateWidth-1;
	public static final int maxY = GUI.pHeight/GUI.coordinateHeight-1;
	/**
	 * FIELDS
	 */
	private int x;
	private int y;
	private static ArrayList<Coordinate> existingCoordinates = new ArrayList<Coordinate>();
	
	private Coordinate(int x, int y) throws IllegalArgumentException{
		if(isValidX(x)){
			setX(x);
		}else{
			throw new IllegalArgumentException("Invalid x: "+x+" ["+this.toString()+"]");
		}
		if(isValidY(y)){
			setY(y);
		}else{
			throw new IllegalArgumentException("Invalid y: "+y+" ["+this.toString()+"]");
		}
	}
	
	public static Coordinate getCoordinate(int x, int y) throws IllegalArgumentException{
		Coordinate newCoordinate = new Coordinate (x,y);
		for(Coordinate c : existingCoordinates){
			if(c.equals(newCoordinate)){
				return c;
			}
		}
		existingCoordinates.add(newCoordinate);
		return newCoordinate;
	}
	
	private boolean isValidY(int y) {
		boolean result = false;
		if((y>=0) && (y<=getMaxY())){
			result = true;
		}
		return result;
	}

	private boolean isValidX(int x) {
		boolean result = false;
		if((x>=0) && (x<=getMaxX())){
			result = true;
		}
		return result;
	}
	
	/**
	 * Check whether the given y is in the ranges [minY, maxY]
	 */
	public static boolean isValidY(int y, int minY, int maxY) {
		boolean result = false;
		if((y>=minY) && (y<=maxY)){
			result = true;
		}
		return result;
	}

	/**
	 * Check whether the given x is in the ranges [minX, maxX]
	 */
	public static boolean isValidX(int x, int minX, int maxX) {
		boolean result = false;
		if((x>=minX) && (x<=maxX)){
			result = true;
		}
		return result;
	}
	
	public static int getMaxX(){
		return Coordinate.maxX;
	}
	
	public static int getMaxY(){
		return Coordinate.maxY;
	}
	
	public static int getMinX(){
		return Coordinate.minX;
	}
	
	public static int getMinY(){
		return Coordinate.minY;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object other){
		boolean result = false;
		if(other instanceof Coordinate){
			Coordinate c = (Coordinate) other;
			if(this.getX()==c.getX() && this.getY() == c.getY()){
				result = true;
			}
		}
		return result;
	}	
}