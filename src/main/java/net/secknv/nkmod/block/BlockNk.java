package net.secknv.nkmod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.secknv.nkmod.Naschkatze;

public class BlockNk extends Block
{

	public BlockNk(String name, Material material, SoundType sound, float hardness, float resist)
	{
		super(material);
		setRegistryName(name);
		setUnlocalizedName(name);
		setCreativeTab(Naschkatze.tabNk);
		setSoundType(sound);
		setHardness(hardness);
		setResistance(resist);
		
	}

}