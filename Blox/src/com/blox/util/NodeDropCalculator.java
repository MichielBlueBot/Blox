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
			Coordinate cMain = calculateLowestFreeCoordinate(c, p, dd);
			Coordinate cOff = calculateLowestFreeCoordinate(Coordinate.getCoordinate(c.getX()+1, c.getY()), p, dd);
			if(cMain==null || cOff == null){
				return blockNodes;
			}else{
				int lowestYMain = cMain.getY();
				int lowestYOff = cOff.getY();
				if(lowestYMain>=0 && lowestYOff>=0 && lowestYMain<=Coordinate.maxY && lowestYOff<=Coordinate.maxY){
					if(lowestYMain!=lowestYOff){
						if(dd==DropDirection.UP){
							if(lowestYMain>lowestYOff){
								blockNodes.addAll(getDropNodes(type, c.getX(), lowestYMain, dd));
							} else{
								blockNodes.addAll(getDropNodes(type, c.getX(), lowestYOff-1, dd));
							}
						}
						else{
							if(lowestYMain>lowestYOff){
								blockNodes.addAll(getDropNodes(type, c.getX(), lowestYOff, dd));
							} else{
								blockNodes.addAll(getDropNodes(type, c.getX(), lowestYMain, dd));
							}
						}
					} else{
						blockNodes.addAll(getDropNodes(type, c.getX(), lowestYMain, dd));
					}
				} else{
					return blockNodes;
				}
				break;
			}
		case Line:
			Coordinate cMainLine = calculateLowestFreeCoordinate(c, p, dd);
			if(cMainLine==null){
				return blockNodes;
			} else{
				int lowestY = cMainLine.getY();
				if(lowestY>=0 && lowestY<=Coordinate.maxY){
					blockNodes.addAll(getDropNodes(type, c.getX(), lowestY, dd));
				} else{
					return blockNodes;
				}
			}
			break;
		case Square:
			if(c.getX()==Coordinate.maxX){
				c = Coordinate.getCoordinate(c.getX()-1, c.getY());
			}
			if(c.getY()==Coordinate.maxY){
				c = Coordinate.getCoordinate(c.getX(), c.getY()-1);
			}
			Coordinate cMainSquare = calculateLowestFreeCoordinate(c, p, dd);
			Coordinate cOffSquare = calculateLowestFreeCoordinate(Coordinate.getCoordinate(c.getX()+1, c.getY()), p, dd);
			if(cMainSquare==null || cOffSquare == null){
				return blockNodes;
			}else{
				int lowestYMain = cMainSquare.getY();
				int lowestYOff = cOffSquare.getY();
				if(lowestYMain>=0 && lowestYOff>=0  && lowestYMain<=Coordinate.maxY && lowestYOff<=Coordinate.maxY){
					if(lowestYMain!=lowestYOff){
						if(dd==DropDirection.UP){
							if(lowestYMain>lowestYOff){
								blockNodes.addAll(getDropNodes(type, c.getX(), lowestYMain, dd));
							} else{
								blockNodes.addAll(getDropNodes(type, c.getX(), lowestYOff, dd));
							}
						} else {
							if(lowestYMain>lowestYOff){
								blockNodes.addAll(getDropNodes(type, c.getX(), lowestYOff, dd));
							} else{
								blockNodes.addAll(getDropNodes(type, c.getX(), lowestYMain, dd));
							}
						}
					} else{
						int lowestY = cMainSquare.getY();
						blockNodes.addAll(getDropNodes(type, c.getX(), lowestY, dd));
					}
				} else {
					return blockNodes;
				}
			}
			break;
		}
		return blockNodes;
	}
	
	private Collection<? extends Node> getDropNodes(BlockType type, int x, int lowestY, DropDirection dd) {
		ArrayList<Node> result = new ArrayList<Node>();
		switch(dd){
		case UP:
			if(type==BlockType.Corner){
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY)));
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY+1)));
				result.add(Node.getNode(Coordinate.getCoordinate(x+1, lowestY+1)));
			} else if(type==BlockType.Line){
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY)));
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY+1)));
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY+2)));
			} else if(type==BlockType.Square){
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY)));
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY+1)));
				result.add(Node.getNode(Coordinate.getCoordinate(x+1, lowestY)));
				result.add(Node.getNode(Coordinate.getCoordinate(x+1, lowestY+1)));
			}
			break;
		case DOWN:
			if(type==BlockType.Corner){
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY)));
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY-1)));
				result.add(Node.getNode(Coordinate.getCoordinate(x+1, lowestY)));
			} else if(type==BlockType.Line){
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY)));
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY-1)));
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY-2)));
			} else if(type==BlockType.Square){
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY)));
				result.add(Node.getNode(Coordinate.getCoordinate(x, lowestY-1)));
				result.add(Node.getNode(Coordinate.getCoordinate(x+1, lowestY)));
				result.add(Node.getNode(Coordinate.getCoordinate(x+1, lowestY-1)));
			}
			break;
		}

		return result;
	}

	private Coordinate calculateLowestFreeCoordinate(Coordinate c, Player p, DropDirection dd){
		Coordinate result = null;
		switch(dd){
		case UP:
			result = calculateLowestFreeCoordinateUP(c, p);
			break;
		case DOWN:
			result = calculateLowestFreeCoordinateDOWN(c, p);
			break;
		}
		return result;
	}
		

	private Coordinate calculateLowestFreeCoordinateDOWN(Coordinate c, Player p) {
		Coordinate result = null;
		Node nodeHoveringOver = Node.getNode(Coordinate.getCoordinate(c.getX(), c.getY()));
		if(nodeHoveringOver.isActive() && nodeHoveringOver.getOwner().getOwner()==p){
			//return null;
		}
		else{
			result = c;
			for(int i = c.getY()+1; i<=Coordinate.maxY; i++){
				Node n = Node.getNode(Coordinate.getCoordinate(c.getX(), i));
				if(n.isActive() && n.getOwner().getOwner()==p){
					return result;
				}
				else{
					result = n.getCoordinate();
				}
			}
		}
		return result;
	}

	private Coordinate calculateLowestFreeCoordinateUP(Coordinate c, Player p) {
		Coordinate result = null;
		Node nodeHoveringOver = Node.getNode(Coordinate.getCoordinate(c.getX(), c.getY()));
		if(nodeHoveringOver.isActive() && nodeHoveringOver.getOwner().getOwner()==p){
			//return null;
		}
		else{
			result = c;
			for(int i = c.getY()-1; i>=Coordinate.minY; i--){
				Node n = Node.getNode(Coordinate.getCoordinate(c.getX(), i));
				if(n.isActive() && n.getOwner().getOwner()==p){
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
