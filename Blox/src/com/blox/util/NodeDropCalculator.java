package com.blox.util;

import java.util.ArrayList;
import java.util.Collection;

import com.blox.block.Block;
import com.blox.block.BlockType;
import com.blox.component.Node;
import com.blox.component.Player;

public class NodeDropCalculator {
	
	public NodeDropCalculator(){
		//Nothing
	}
	
	/**
	 * Calculate the nodes that have to be activated, given a block, mouse position
	 * and list of current Nodes in the game, if the user would decide to put the block
	 * at that position.
	 * @return List of nodes where that would would be placed, empty array if not valid.
	 */
	public ArrayList<Node> getDrop(Block block, Coordinate c, Player p){
		ArrayList<Node> blockNodes = new ArrayList<Node>();
		BlockType type = block.getType();
		DropDirection dd = p.getDropDirection();
		switch(type){
		case Corner:
			if(c.getX()==Coordinate.maxX){
				c = Coordinate.getCoordinate(c.getX()-1, c.getY());
			}
			if(c.getY()==Coordinate.maxY){
				c = Coordinate.getCoordinate(c.getX(), c.getY()-1);
			}
			Coordinate cMain = calculateLowestFreeCoordinate(c, dd);
			Coordinate cOff = calculateLowestFreeCoordinate(Coordinate.getCoordinate(c.getX()+1, c.getY()), dd);
			if(cMain==null || cOff == null){
				return blockNodes;
			}else{
				int lowestYMain = cMain.getY();
				int lowestYOff = cOff.getY();
				if(lowestYMain!=lowestYOff){
					if(lowestYMain>lowestYOff){
						blockNodes.addAll(getDropNodes(type, c.getX(), lowestYMain, dd));
					} else{
						blockNodes.addAll(getDropNodes(type, c.getX(), lowestYOff, dd));
					}
				} else{
					blockNodes.addAll(getDropNodes(type, c.getX(), lowestYMain, dd));
				}
				break;
			}
		case Line:
			//TODO
			break;
		case Square:
			//TODO
			break;
		}
		return blockNodes;
	}
	
	private Collection<? extends Node> getDropNodes(BlockType type, int x, int lowestYOff, DropDirection dd) {
		ArrayList<Node> result = new ArrayList<Node>();
		switch(dd){
		case UP:
			if(type==BlockType.Corner){
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestYOff)));
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestYOff+1)));
				result.add(Node.getNode(Coordinate.getCoordinate(x+1, lowestYOff+1)));
			} else if(type==BlockType.Line){
				//TODO
			} else if(type==BlockType.Square){
				//TODO
			}
			break;
		case DOWN:
			if(type==BlockType.Corner){
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestYOff)));
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestYOff-1)));
				result.add(Node.getNode(Coordinate.getCoordinate(x+1, lowestYOff)));
			} else if(type==BlockType.Line){
				//TODO
			} else if(type==BlockType.Square){
				//TODO
			}
			break;
		}

		return result;
	}

	private Coordinate calculateLowestFreeCoordinate(Coordinate c, DropDirection dd){
		Coordinate result = null;
		switch(dd){
		case UP:
			result = calculateLowestFreeCoordinateUP(c);
			break;
		case DOWN:
			result = calculateLowestFreeCoordinateDOWN(c);
			break;
		}
		return result;
	}
		

	private Coordinate calculateLowestFreeCoordinateDOWN(Coordinate c) {
		Coordinate result = null;
		if(Node.getNode(Coordinate.getCoordinate(c.getX(), c.getY())).isActive()){
			//return null;
		}
		else{
			result = c;
			for(int i = c.getY()+1; i<=Coordinate.maxY; i++){
				Node n = Node.getNode(Coordinate.getCoordinate(c.getX(), i));
				if(n.isActive()){
					return result;
				}
				else{
					result = n.getCoordinate();
				}
			}
		}
		return result;
	}

	private Coordinate calculateLowestFreeCoordinateUP(Coordinate c) {
		Coordinate result = null;
		if(Node.getNode(Coordinate.getCoordinate(c.getX(), c.getY())).isActive()){
			//return null;
		}
		else{
			result = c;
			for(int i = c.getY()-1; i>=Coordinate.minY; i--){
				Node n = Node.getNode(Coordinate.getCoordinate(c.getX(), i));
				if(n.isActive()){
					return result;
				}
				else{
					result = n.getCoordinate();
				}
			}
		}
		return result;
	}
}
