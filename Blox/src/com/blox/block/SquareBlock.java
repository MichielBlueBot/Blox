package com.blox.block;

import java.awt.Color;

import com.blox.component.Player;

public class SquareBlock extends Block {
	
//	public SquareBlock(){
//		setType(BlockType.Square);
//		setColor(DEFAULT_BLOCK_COLOR);
//		setImage(createImageIcon("images/block_square.gif"));
//	}
	
	public SquareBlock(Player player){
		setType(BlockType.Square);
		setColor(player.getColor());
		setOwner(player);
		if(this.getColor()==Color.red){
			setImage(createImageIcon("images/block_square_red.gif"));
		}else{
			setImage(createImageIcon("images/block_square.gif"));
		}
	}

}
