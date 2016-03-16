package com.DrasticDemise.TerrainCrystals.Items;

import java.util.ArrayList;
import java.util.Random;

import com.DrasticDemise.TerrainCrystals.blocks.CStorageCellTileEntity;
import com.mojang.realmsclient.dto.PlayerInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TerrainCrystalPlains extends Item{
	public TerrainCrystalPlains(){
		setUnlocalizedName("terrainCrystalPlains");
		setRegistryName("terrainCrystalPlains");
		setCreativeTab(CreativeTabs.tabBlock);
		setHarvestLevel("stone", 0);
        GameRegistry.registerItem(this);
	}
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn){
		int blocksGenerated = 0;
		if(!worldIn.isRemote){
			int posX = MathHelper.floor_double(playerIn.posX);
			int posY = MathHelper.floor_double(playerIn.posY);
			int posZ = MathHelper.floor_double(playerIn.posZ);
			int center;
			int diameter = 11;
			double radius = diameter/2.0;
			BlockPos playerLocation = new BlockPos(posX, posY, posZ);
			setBiome(worldIn, playerLocation);
			if(diameter%2 != 0){
				center = (int) (radius + 0.5);
			}else{
				center = (int) (radius);
			}
			int offsetXFirstHalf = (int) (posX + radius);
			//Not sure why this has to be offset by 1 extra, but it does.
			int offsetXSecondHalf = (int) (posX - radius + 1);
			//Generates the first half
			int yDown = 1;
			int fakeCenter = center;
			ArrayList<BlockPos> posList = new ArrayList<BlockPos>(68);
			for(int i = 0; i < (fakeCenter); i ++){
				//Creates the outline of the circle
				//Each shell is respective to its quadrant
				//These are added in the loop already
				//BlockPos shellOne = new BlockPos(offsetXFirstHalf - i, posY-yDown, posZ - i);
				//BlockPos shellTwo = new BlockPos(offsetXFirstHalf - i, posY - yDown, posZ + i);
				for(int placeInwards = 0; placeInwards < i+1; placeInwards++){
					//Fills across the circle
					BlockPos fillShellOne = new BlockPos(offsetXFirstHalf - i, posY - yDown, posZ - i + placeInwards);
					posList.add(fillShellOne);
					BlockPos fillShellTwo = new BlockPos(offsetXFirstHalf - i, posY - yDown, posZ + i - placeInwards);
					posList.add(fillShellTwo);
				}
			}
			//Generates the second half
			for(int i = 0; i < (center); i ++){
				BlockPos shellThree = new BlockPos(offsetXSecondHalf + i, posY - 1, posZ  + i);
				BlockPos shellFour = new BlockPos(offsetXSecondHalf + i, posY - 1, posZ - i);
				posList.add(shellThree); 
				posList.add(shellFour);
				
				for(int placeInwards = 0; placeInwards < i + 1; placeInwards++){
					BlockPos fillShellThree = new BlockPos(offsetXSecondHalf + i, posY - 1, posZ + i - placeInwards);
					BlockPos fillShellFour = new BlockPos(offsetXSecondHalf + i, posY - 1, posZ - i + placeInwards);
					posList.add(fillShellThree);
					posList.add(fillShellFour);
				}
			}
			for(BlockPos p : posList){
				blocksGenerated = generateSpike(posList, worldIn, playerIn, blocksGenerated);
			}
		}
		//System.out.println(blocksGenerated);
		return itemStackIn;
	}
	 public boolean setBiome(World worldIn, BlockPos position) {
	        Chunk chunk = worldIn.getChunkFromChunkCoords(position.getX(), position.getZ());
	        if ((chunk != null) && (chunk.isLoaded())) {
	        	//chunk.getBiomeArray()[((position.getZ() & 0xF) << 4 | position.getX() & 0xF)] = (byte) BiomeGenBase.beach.biomeID;
	        	byte[]biomeArray = chunk.getBiomeArray();
	        	System.out.println(chunk.getBiome(position, worldIn.getWorldChunkManager()).biomeID);
	        	//System.out.println("Get array1 at posX: " + position.getX() + " and posZ: " + position.getZ() + "Chunk coored int pair: " + chunk.getChunkCoordIntPair());
	        	//System.out.println("");
	        	for(byte b : biomeArray){
	        //		System.out.print(b);
	        	}
	            biomeArray[((position.getZ() & 0xF) << 4 | position.getX() & 0xF)] = (byte) BiomeGenBase.desert.biomeID;
	            chunk.setBiomeArray(biomeArray);
	            //System.out.println(" ");
	           // System.out.println("Get array2 at posX: " + position.getX() + " and posZ: " + position.getZ() + "Chunk coored int pair: " + chunk.getChunkCoordIntPair());
	            System.out.println(chunk.getBiome(position, worldIn.getWorldChunkManager()).biomeID);
	            for(byte b : biomeArray){
	        	//	System.out.print(b);
	        	}
	            chunk.needsSaving(true);
	            return true;
	        }
	        return false;
	    }
	public int generateSpike(ArrayList<BlockPos> posList, World worldIn, EntityPlayer playerIn, int blocksGenerated){
		ArrayList<BlockPos> recursiveList = new ArrayList<BlockPos>();
		for(BlockPos pos : posList){
			int surroundingBlocks = 0;
			
				blocksGenerated = generateInWorld(pos, worldIn, playerIn, blocksGenerated);
				
				if(worldIn.getBlockState(pos.north()) != Blocks.air.getDefaultState()){
					//System.out.println("entered northCheck");
					surroundingBlocks++;
				}
				
				if(worldIn.getBlockState(pos.east()) != Blocks.air.getDefaultState()){
					surroundingBlocks++;
				}
				
				if(worldIn.getBlockState(pos.south()) != Blocks.air.getDefaultState()){
					surroundingBlocks++;
				}
				
				if(worldIn.getBlockState(pos.west()) != Blocks.air.getDefaultState()){
					surroundingBlocks++;
				}
				if(surroundingBlocks >= 3 || Math.random() < 0.05){
					blocksGenerated = generateInWorld(pos.down(), worldIn, playerIn, blocksGenerated);
					recursiveList.add(pos.down());
				}
			}
		if(!recursiveList.isEmpty()){
			blocksGenerated = generateSpike(recursiveList, worldIn, playerIn, blocksGenerated);
		}
		return blocksGenerated;
	}
	private int generateInWorld(BlockPos pos, World worldIn, EntityPlayer playerIn, int blocksGenerated){
		if(worldIn.getBlockState(pos) == Blocks.air.getDefaultState()){
			int posY = MathHelper.floor_double(playerIn.posY);
			if(posY - pos.getY() == 1){
				worldIn.setBlockState(pos, Blocks.grass.getDefaultState());
				boneMeal(worldIn, pos);
				blocksGenerated++;
			}else{
				worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
				blocksGenerated++;
			}
		}
		return blocksGenerated;
	}
	//Code taken from Lumien's Random Things Nature Core tile entity
	private void boneMeal(World worldIn, BlockPos pos){
		IBlockState state = worldIn.getBlockState(pos);
		Random rand = new Random();
		//Try-catching our worries away!
		try{
			if(Math.random() < 0.10){
				if (state.getBlock() instanceof IGrowable)
				{
					IGrowable growable = (IGrowable) state.getBlock();
					if (growable.canGrow(worldIn, pos, state, worldIn.isRemote))
					{
						worldIn.playAuxSFX(2005, pos, 0);
						growable.grow(worldIn, rand, pos, state);
						if(Math.random() <= 0.1){
							growTree(worldIn, pos);
						}
					}
				}
			}
		}catch(IllegalArgumentException e){
			//System.out.println("Caught an error in tree growing! Tossing it out, goodbye chunk error!");
			return;
		}
		}
	private void growTree(World worldIn, BlockPos pos){
		if (Blocks.sapling.canPlaceBlockAt(worldIn, pos.up())){
			if(Math.random() < .5){
				worldIn.setBlockState(pos.up(), Blocks.sapling.getStateFromMeta(2));
			}else{
				worldIn.setBlockState(pos.up(), Blocks.sapling.getDefaultState());
			}
			//worldIn.setBlockState(pos.up(), Blocks.sapling.getDefaultState());
			IGrowable growable = (IGrowable) worldIn.getBlockState(pos.up()).getBlock();
			Random rand = new Random();	
			//System.out.println("X: " + pos.getX() + " " + pos.up().getY());
			while(worldIn.getBlockState(pos.up()) != Blocks.log.getDefaultState()){
				//System.out.println("Attempting to grow at y: " + pos.up().getY());
				growable.grow(worldIn, rand, pos.up(), worldIn.getBlockState(pos.up()));
			}
		}
	}
	@SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
