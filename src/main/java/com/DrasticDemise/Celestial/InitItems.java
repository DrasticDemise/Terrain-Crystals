package com.DrasticDemise.Celestial;

import com.DrasticDemise.Celestial.Items.TerrainCrystalDesert;
import com.DrasticDemise.Celestial.Items.TerrainCrystalEnd;
import com.DrasticDemise.Celestial.Items.TerrainCrystalMesa;
import com.DrasticDemise.Celestial.Items.TerrainCrystalMushroom;
import com.DrasticDemise.Celestial.Items.TerrainCrystalNether;
import com.DrasticDemise.Celestial.Items.TerrainCrystalPlains;

import net.minecraftforge.oredict.OreDictionary;

public class InitItems {
	
	//Creates dust objects
	//public static IronDust ironDust;
	public static TerrainCrystalPlains plainsCrystal;
	public static TerrainCrystalNether netherCrystal;
	public static TerrainCrystalDesert desertCrystal;
	public static TerrainCrystalMesa mesaCrystal;
	public static TerrainCrystalMushroom mushroomCrystal;
	public static TerrainCrystalEnd endCrystal;
	//Initializes new dust objects
	public static void init(){
		//ironDust = new IronDust();
		plainsCrystal = new TerrainCrystalPlains();
		netherCrystal = new TerrainCrystalNether();
		desertCrystal = new TerrainCrystalDesert();
		mesaCrystal = new TerrainCrystalMesa();
		mushroomCrystal = new TerrainCrystalMushroom();
		endCrystal = new TerrainCrystalEnd();
	}
	
	//Adds dusts to the ore dictionary
	public static void oreRegistration(){
		//OreDictionary.registerOre("dustIron", ironDust);
	}
}
