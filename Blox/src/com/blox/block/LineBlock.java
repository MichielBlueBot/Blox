package com.blox.block;

import java.awt.Color;

import com.blox.component.Player;

public class LineBlock extends Block {

//	public LineBlock(){
//		setType(BlockType.Line);
//		setColor(DEFAULT_BLOCK_COLOR);
//		setImage(createImageIcon("images/block_line.gif"));
//	}
	
	public LineBlock(Player player){
		setType(BlockType.Line);
		setColor(player.getColor());
		setOwner(player);
		if(this.getColor()==Color.red){
			setImage(createImageIcon("images/block_line_red.gif"));
		}else{
			setImage(createImageIcon("images/block_line.gif"));
		}
	}
	
}
