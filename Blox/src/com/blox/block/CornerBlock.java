package com.blox.block;

import java.awt.Color;
import com.blox.block.BlockType;
import com.blox.component.Player;

public class CornerBlock extends Block{
	
//	public CornerBlock(){
//		setType(BlockType.Corner);
//		setColor(DEFAULT_BLOCK_COLOR);
//		setImage(createImageIcon("images/block_corner.gif"));
//	}
	
	public CornerBlock(Player player){
		setType(BlockType.Corner);
		setColor(player.getColor());
		setOwner(player);
		if(this.getColor()==Color.red){
			setImage(createImageIcon("images/block_corner_red.gif"));
		}else{
			setImage(createImageIcon("images/block_corner.gif"));
		}
	}

}
