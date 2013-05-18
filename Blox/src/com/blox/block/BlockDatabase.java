package com.blox.block;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Random;

import com.blox.component.Player;

public class BlockDatabase {
	
//	public static ArrayDeque<Block> getRandomDeck(){
//		return BlockDatabase.getRandomDeck(10, Color.blue);
//	}
	
	public static ArrayDeque<Block> getRandomDeck(Player player){
		return BlockDatabase.getRandomDeck(10, player);
	}
	
	public static ArrayDeque<Block> getRandomDeck(int amount, Player player){
		ArrayDeque<Block> result = new ArrayDeque<Block>();
		BlockType[] bTypes = BlockType.values();
		Random rand = new Random();
		int min = 0;
		int max = bTypes.length-1;
		
		for(int i = 0; i<amount; i++){
			int randomNum = rand.nextInt(max - min + 1) + min;
			BlockType type = bTypes[randomNum];
			switch(type){
				case Square:
					result.add(new SquareBlock(player));
					break;
				case Line:
					result.add(new LineBlock(player));
					break;
				case Corner:
					result.add(new CornerBlock(player));
					break;
			}
		}
		return result;
	}
}
