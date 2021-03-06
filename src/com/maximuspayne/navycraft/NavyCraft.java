package com.maximuspayne.navycraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.logging.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

import com.maximuspayne.navycraft.blocks.BlocksInfo;
import com.maximuspayne.navycraft.craft.Craft;
import com.maximuspayne.navycraft.craft.CraftBuilder;
import com.maximuspayne.navycraft.craft.CraftMover;
import com.maximuspayne.navycraft.craft.CraftType;
import com.maximuspayne.navycraft.listeners.NavyCraft_BlockListener;
import com.maximuspayne.navycraft.listeners.NavyCraft_EntityListener;
import com.maximuspayne.navycraft.listeners.NavyCraft_InventoryListener;
import com.maximuspayne.navycraft.listeners.NavyCraft_PlayerListener;
import com.maximuspayne.navycraft.plugins.PermissionInterface;
import com.maximuspayne.navycraft.teleportfix.TeleportFix;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

/**
 * MoveCraft plugin for Hey0 mod (hMod) by Yogoda
 * Ported to Bukkit by SycoPrime
 * Modified by Maximuspayne for NBZ
 * Continuing to be modified by Solmex, for public use
 * Licensed under Apache 2.0 
 * 
 * You are free to modify it for your own server
 * or use part of the code for your own plugins.
 * You don't need to credit me if you do, but I would appreciate it :)
 *
 * You are not allowed to distribute alternative versions of MoveCraft without my consent.
 * If you do cool modifications, please tell me so I can integrate it :)
 */

public class NavyCraft extends JavaPlugin {

	static final String pluginName = "NavyCraft";
	public static String version;
	public static NavyCraft instance;

	public static Logger logger = Logger.getLogger("Minecraft");
	public boolean DebugMode = false;

	public static ArrayList<Player> aaGunnersList = new ArrayList<Player>();
	public static ArrayList<Player> flakGunnersList = new ArrayList<Player>();
	public static ArrayList<Skeleton> flakSkelesList = new ArrayList<Skeleton>();
	public static ArrayList<Skeleton> aaSkelesList = new ArrayList<Skeleton>();
	public static ArrayList<Egg> explosiveEggsList = new ArrayList<Egg>();
	public static HashMap<UUID, Player> shotTNTList = new HashMap<UUID, Player>();
	
	public final NavyCraft_PlayerListener playerListener = new NavyCraft_PlayerListener(this);
	public final NavyCraft_BlockListener blockListener = new NavyCraft_BlockListener(this);
	public final NavyCraft_EntityListener entityListener = new NavyCraft_EntityListener(this);
	public final NavyCraft_InventoryListener inventoryListener = new NavyCraft_InventoryListener(this);
	
    public static int battleMode=-1; //-1 false, 0 queue, 1 battle
    public static int battleType=-1;
    public static boolean battleLockTeams=false;
    public static ArrayList<String> bluePlayers = new ArrayList<String>();
    public static ArrayList<String> redPlayers = new ArrayList<String>();
    public static ArrayList<String> anyPlayers = new ArrayList<String>();

	public static ArrayList<String> playerKits = new ArrayList<String>();
    public static Location redSpawn;
    public static Location blueSpawn;
    public static int redPoints=0;
    public static int bluePoints=0;
    public static long battleStartTime;
    public static long battleLength;
    public static boolean redMerchant = false;
    public static boolean blueMerchant = false;

    public static enum battleTypes { battle1, battle2 }; //1
	

	public static Thread updateThread=null;
	public static Thread npcMerchantThread=null;
	public static boolean shutDown = false;
	
	public static WorldGuardPlugin wgp;
	
	public static ArrayList<Periscope> allPeriscopes = new ArrayList<Periscope>();
	
	public static HashMap<Player, Location> searchLightMap = new HashMap<Player, Location>();
	
	public static HashMap<Player, Block> playerLastBoughtSign = new HashMap<Player, Block>();
	public static HashMap<Player, Integer> playerLastBoughtCost = new HashMap<Player, Integer>();
	public static HashMap<Player, String> playerLastBoughtSignString0 = new HashMap<Player, String>();
	public static HashMap<Player, String> playerLastBoughtSignString1 = new HashMap<Player, String>();
	public static HashMap<Player, String> playerLastBoughtSignString2 = new HashMap<Player, String>();
	
	public static int spawnTime=10;
	
	public static HashMap<String, ArrayList<Sign>> playerSHIP1Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerSHIP2Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerSHIP3Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerSHIP4Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerSHIP5Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerHANGAR1Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerHANGAR2Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerTANK1Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerTANK2Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerMAP1Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerMAP2Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerMAP3Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerMAP4Signs = new HashMap<String, ArrayList<Sign>>();
	public static HashMap<String, ArrayList<Sign>> playerMAP5Signs = new HashMap<String, ArrayList<Sign>>();
	
	public static HashMap<String, Integer> playerSHIP1Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerSHIP2Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerSHIP3Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerSHIP4Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerSHIP5Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerHANGAR1Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerHANGAR2Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerTANK1Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerTANK2Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerMAP1Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerMAP2Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerMAP3Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerMAP4Rewards = new HashMap<String, Integer>();
	public static HashMap<String, Integer> playerMAP5Rewards = new HashMap<String, Integer>();
	
	public static HashMap<Sign, Integer> playerSignIndex = new HashMap<Sign, Integer>();
	
	public static HashMap<String, Integer> playerExp = new HashMap<String, Integer>();
	
	public static HashMap<String, Long> playerPayDays = new HashMap<String, Long>();
	
	public static HashMap<String, Integer> cleanupPlayerTimes = new HashMap<String, Integer>();
	public static ArrayList<String> cleanupPlayers = new ArrayList<String>();
	
	public static HashMap<String, Long> shipTPCooldowns = new HashMap<String, Long>();
	
	public static int schedulerCounter = 0;
	
	public static HashMap<Player, Float> playerEngineVolumes = new HashMap<Player, Float>();
	public static HashMap<Player, Float> playerWeaponVolumes = new HashMap<Player, Float>();
	public static HashMap<Player, Float> playerOtherVolumes = new HashMap<Player, Float>();

	public void loadProperties() {
		getConfig().options().copyDefaults(true);
		File dir = getDataFolder();
		if (!dir.exists())
			dir.mkdir();

		CraftType.loadTypes(dir);
		CraftType.saveTypes(dir);
		
	}
	
	public void onLoad() {
		
	}

	public void onEnable() {
		instance = this;
		
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(playerListener, this);
		pm.registerEvents(entityListener, this);
		pm.registerEvents(blockListener, this);
		pm.registerEvents(inventoryListener, this);
		
		PluginDescriptionFile pdfFile = this.getDescription();
		version = pdfFile.getVersion();

		BlocksInfo.loadBlocksInfo();
		loadProperties();
		PermissionInterface.setupPermissions(this);
		
		PluginManager manager = getServer().getPluginManager();
		 
        manager.registerEvents(new TeleportFix(this, this.getServer()), this);
		
		structureUpdateScheduler();

		System.out.println(pdfFile.getName() + " " + version + " plugin enabled");
		getConfig().options().copyDefaults(true);
		}

	public void onDisable() {
		shutDown = true;
		PluginDescriptionFile pdfFile = this.getDescription();
		System.out.println(pdfFile.getName() + " " + version + " plugin disabled");
	}


	public void ToggleDebug() {
		this.DebugMode = !this.DebugMode;
		System.out.println("Debug mode set to " + this.DebugMode);
	}

	public boolean DebugMessage(String message, int messageLevel) {
		/* Message Levels:
		 * 0: Error
		 * 1: Something I'm currently testing
		 * 2: Something I think I just fixed
		 * 3: Something I'm pretty sure is fixed
		 * 4: Supporting information
		 * 5: Nearly frivolous information
		 */
		
		//if(this.DebugMode == true)
		if(Integer.parseInt(this.getConfig().getString("LogLevel")) >= messageLevel)
			System.out.println(message);
		return this.DebugMode;
	}

	public Craft createCraft(Player player, CraftType craftType, int x, int y, int z, String name, float dr, Block signBlock) {
		//if( npcMerchantThread == null )
			//npcMerchantThread();
		
		if (DebugMode == true)
			player.sendMessage("Attempting to create " + craftType.name
					+ "at coordinates " + Integer.toString(x) + ", "
					+ Integer.toString(y) + ", " + Integer.toString(z));

		if( signBlock == null )
			signBlock = player.getLocation().getBlock();
		Craft craft = new Craft(craftType, player, name, dr, signBlock.getLocation(), this);

		
		// auto-detect and create the craft
		if (!CraftBuilder.detect(craft, x, y, z)) {
			return null;
		}

		CraftMover cm = new CraftMover(craft, this);
		cm.structureUpdate(null,false);

		if( !craft.redTeam && !craft.blueTeam )
		{
			if( checkTeamRegion(player.getLocation()) > 0 )
			{
				if( checkTeamRegion(player.getLocation()) == 1 )
				{
					craft.blueTeam = true;
					player.sendMessage(ChatColor.BLUE + "You start a blue team vehicle!");
				}
				else
				{
					craft.redTeam = true;
					player.sendMessage(ChatColor.RED + "You start a red team vehicle!");
				}
			}
		}

		Craft.addCraftList.add(craft);
		//craft.cloneCraft();
		
		
		if( craft.type.canFly )
		{
			craft.type.maxEngineSpeed = 10;
		}else if( craft.type.isTerrestrial )
		{
			craft.type.maxEngineSpeed = 4;
		}else
		{
			craft.type.maxEngineSpeed = 6;
		}
		
		
		if( checkSpawnRegion(new Location(craft.world, craft.minX, craft.minY, craft.minZ)) || checkSpawnRegion(new Location(craft.world, craft.maxX, craft.maxY, craft.maxZ)) )
		{
			craft.speedChange(player, true);
		}
		{
			craft.driverName = craft.captainName;
			if(craft.type.listenItem == true)
				player.sendMessage(ChatColor.GRAY + "With a gold sword in your hand, right-click in the direction you want to go.");
			if(craft.type.listenAnimation == true)
				player.sendMessage(ChatColor.GRAY + "Swing your arm in the direction you want to go.");
			if(craft.type.listenMovement == true)
				player.sendMessage(ChatColor.GRAY + "Move in the direction you want to go.");
		}
		return craft;
	}
    
    public static int checkTeamRegion(Location loc) /// 0 no region, 1 blue team, 2 red team
    {
    	
    	wgp = (WorldGuardPlugin) instance.getServer().getPluginManager().getPlugin("WorldGuard");
    	if( wgp != null && loc != null)
    	{
    		if( !loc.getWorld().getName().equalsIgnoreCase("warworld1") &&  !loc.getWorld().getName().equalsIgnoreCase("warworld2") &&  !loc.getWorld().getName().equalsIgnoreCase("warworld3") )
    		{
    			return 0;
    		}
	    	RegionManager regionManager = wgp.getRegionManager(loc.getWorld());
		
			ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
			
			Iterator<ProtectedRegion> it = set.iterator();
			while( it.hasNext() )
			{
				String id = it.next().getId();
				String[] splits = id.split("_");
				if( splits.length == 2 )
				{
					if( splits[1].equalsIgnoreCase("blue") )
						return 1;
					else if( splits[1].equalsIgnoreCase("red") )
						return 2;
					
				}
		
				
		    }
			return 0;
		}
    	return 0;
	}

    public static boolean checkStorageRegion(Location loc)
    {
    	
    	wgp = (WorldGuardPlugin) instance.getServer().getPluginManager().getPlugin("WorldGuard");
    	if( wgp != null && loc != null)
    	{
    		if( !PermissionInterface.CheckEnabledWorld(loc) )
    		{
    			return false;
    		}
	    	RegionManager regionManager = wgp.getRegionManager(loc.getWorld());
		
			ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
			
			Iterator<ProtectedRegion> it = set.iterator();
			while( it.hasNext() )
			{
				String id = it.next().getId();
				String[] splits = id.split("_");
				if( splits.length == 2 )
				{
					if( splits[1].equalsIgnoreCase("storage") )
						return true;					
				}
		    }
			return false;
		}
    	return false;
	}
    
    public static boolean checkRepairRegion(Location loc)
    {
    	
    	wgp = (WorldGuardPlugin) instance.getServer().getPluginManager().getPlugin("WorldGuard");
    	if( wgp != null && loc != null)
    	{
    		if( !!PermissionInterface.CheckEnabledWorld(loc) )
    		{
    			return false;
    		}
	    	RegionManager regionManager = wgp.getRegionManager(loc.getWorld());
		
			ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
			
			Iterator<ProtectedRegion> it = set.iterator();
			while( it.hasNext() )
			{
				String id = it.next().getId();
				String[] splits = id.split("_");
				if( splits.length == 2 )
				{
					if( splits[1].equalsIgnoreCase("repair") )
						return true;					
				}
		    }
			return false;
		}
    	return false;
	}
    
    public static boolean checkSafeDockRegion(Location loc)
    {
    	
    	wgp = (WorldGuardPlugin) instance.getServer().getPluginManager().getPlugin("WorldGuard");
    	if( wgp != null && loc != null)
    	{
    		if( !!PermissionInterface.CheckEnabledWorld(loc) )
    		{
    			return false;
    		}
	    	RegionManager regionManager = wgp.getRegionManager(loc.getWorld());
		
			ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
			
			Iterator<ProtectedRegion> it = set.iterator();
			while( it.hasNext() )
			{
				String id = it.next().getId();
				String[] splits = id.split("_");
				if( splits.length == 2 )
				{
					if( splits[1].equalsIgnoreCase("safedock") )
						return true;					
				}
		    }
			return false;
		}
    	return false;
	}
    
    public static boolean checkRecallRegion(Location loc)
    {
    	
    	wgp = (WorldGuardPlugin) instance.getServer().getPluginManager().getPlugin("WorldGuard");
    	if( wgp != null && loc != null)
    	{
    		if( !PermissionInterface.CheckEnabledWorld(loc) )
    		{
    			return false;
    		}
	    	RegionManager regionManager = wgp.getRegionManager(loc.getWorld());
		
			ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
			
			Iterator<ProtectedRegion> it = set.iterator();
			while( it.hasNext() )
			{
				String id = it.next().getId();
				String[] splits = id.split("_");
				if( splits.length == 2 )
				{
					if( splits[1].equalsIgnoreCase("recall") )
						return true;					
				}
		    }
			return false;
		}
    	return false;
	}
    
    public static boolean checkSpawnRegion(Location loc)
    {
    	
    	wgp = (WorldGuardPlugin) instance.getServer().getPluginManager().getPlugin("WorldGuard");
    	if( wgp != null && loc != null)
    	{
    		if( !PermissionInterface.CheckEnabledWorld(loc) )
    		{
    			return false;
    		}
	    	RegionManager regionManager = wgp.getRegionManager(loc.getWorld());
		
			ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
			
			Iterator<ProtectedRegion> it = set.iterator();
			while( it.hasNext() )
			{
				String id = it.next().getId();
				String[] splits = id.split("_");
				if( splits.length == 2 )
				{
					if( splits[1].equalsIgnoreCase("spawn") )
						return true;					
				}
		    }
			return false;
		}
    	return false;
	}
    
    public static boolean checkNoDriveRegion(Location loc)
    {
    	
    	wgp = (WorldGuardPlugin) instance.getServer().getPluginManager().getPlugin("WorldGuard");
    	if( wgp != null && loc != null)
    	{
    		if( !PermissionInterface.CheckEnabledWorld(loc) )
    		{
    			return false;
    		}
	    	RegionManager regionManager = wgp.getRegionManager(loc.getWorld());
		
			ApplicableRegionSet set = regionManager.getApplicableRegions(loc);
			
			Iterator<ProtectedRegion> it = set.iterator();
			while( it.hasNext() )
			{
				String id = it.next().getId();
				String[] splits = id.split("_");
				if( splits.length == 2 )
				{
					if( splits[1].equalsIgnoreCase("nodrive") )
						return true;					
				}
		    }
			return false;
		}
    	return false;
	}

	@SuppressWarnings("deprecation")
	public void dropItem(Block block) {		
		if(NavyCraft.instance.getConfig().getString("Drill").equalsIgnoreCase("true"))
			return;

		int itemToDrop = BlocksInfo.getDropItem(block.getTypeId());
		int quantity = BlocksInfo.getDropQuantity(block.getTypeId());

		if(itemToDrop != -1 && quantity != 0){

			for(int i=0; i<quantity; i++){
				block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(itemToDrop, 1));
			}
		}
	}

	@SuppressWarnings("resource")
	public static void loadExperience()
	{
		String path = File.separator + "NCExp.txt";
        File file = new File(path);
        
        FileReader fr;
        BufferedReader reader;
		try {
			fr = new FileReader(file.getName());
			reader = new BufferedReader(fr);

	        String line = null;        
	        try {
	        	playerExp.clear();
	        	
				while ((line=reader.readLine()) != null) {
					String[] strings = line.split(",");
					if( strings.length != 2 )
					{
						System.out.println("Player EXP Load Error3");
						return;
					}
					playerExp.put(strings[0], Integer.valueOf(strings[1]));
					
				}

				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Player EXP Load Error2");
			}
	        
	        reader.close();  // Close to unlock.

		} catch (FileNotFoundException e) {
			return;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Player EXP Load Error4");
		}
	}
	
	
	@SuppressWarnings("resource")
	public static void saveExperience()
	{
		String path = File.separator + "NCExp.txt";
        File file = new File(path);
        FileWriter fw;
        BufferedWriter writer;
	
		try {
			fw = new FileWriter(file.getName());
			writer = new BufferedWriter(fw);
	        String line = null;
	        
	        if( playerExp.isEmpty() )
	        {
	        	System.out.println("Player Save Exp Error1");
	        	return;
	        }
	        
			for( String s : playerExp.keySet() )
			{
				line = s + "," + playerExp.get(s).toString();
				try {
					writer.write(line);
					writer.newLine();
				} catch (IOException e) {
					System.out.println("Player Save Exp Error2");
					e.printStackTrace();
					return;
				}	
			}
			
			writer.close();
		} catch (IOException e2) {
			System.out.println("Player Save Exp Error4");
			e2.printStackTrace();
			return;
		}
	}
   
   public void structureUpdateScheduler()
   {
   	this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){
   	//new Thread() {
	  //  @Override
		    public void run()
		    {
		    	if( Craft.craftList == null || Craft.craftList.isEmpty() )
		    	{
		    		if( !Craft.addCraftList.isEmpty() )
	    			{
	    				for( Craft c: Craft.addCraftList )
	    				{
	    					Craft.addCraft(c);
	    				}
	    				Craft.addCraftList.clear();
	    			}
		    		return;
		    	}
		    	int vehicleCount = Craft.craftList.size();
	    		int vehicleNum = (schedulerCounter) % vehicleCount;
	    		int updateNum = (schedulerCounter / vehicleCount)%4;
	    
	    		try{
	    		if( vehicleCount < 10 )
	    		{
	    			updateCraft(vehicleNum,updateNum);
	    			schedulerCounter++;
	    		}else if( vehicleCount >= 10 && vehicleCount < 20)
	    		{
	    			//vehicleNum = (vehicleNum + vehicleCount/2)%vehicleCount;
	    			updateCraft(vehicleNum,updateNum);
	    			vehicleNum = (vehicleNum + 1)%vehicleCount;
	    			schedulerCounter = schedulerCounter + 4;
	    			updateCraft(vehicleNum,updateNum);
	    			schedulerCounter = schedulerCounter - 4;
	    			
	    			if( updateNum == 3 )
		    			schedulerCounter+=5;
		    		else
		    			schedulerCounter++;
	    		}else if( vehicleCount >= 20 && vehicleCount < 30 )
	    		{
	    			//vehicleNum = (vehicleNum + vehicleCount/3)%vehicleCount;
	    			vehicleNum = (vehicleNum + 1)%vehicleCount;
	    			schedulerCounter = schedulerCounter + 4;
	    			updateCraft(vehicleNum,updateNum);
	    			vehicleNum = (vehicleNum + 1)%vehicleCount;
	    			schedulerCounter = schedulerCounter + 4;
	    			//vehicleNum = (vehicleNum + vehicleCount/3)%vehicleCount;
	    			updateCraft(vehicleNum,updateNum);
	    			schedulerCounter = schedulerCounter - 4;
	    			schedulerCounter = schedulerCounter - 4;
	    			
	    			if( updateNum == 3 )
		    			schedulerCounter+=9;
		    		else
		    			schedulerCounter++;
	    		}else
	    		{
	    			//vehicleNum = (vehicleNum + vehicleCount/4)%vehicleCount;
	    			vehicleNum = (vehicleNum + 1)%vehicleCount;
	    			schedulerCounter = schedulerCounter + 4;
	    			updateCraft(vehicleNum,updateNum);
	    			//vehicleNum = (vehicleNum + vehicleCount/4)%vehicleCount;
	    			vehicleNum = (vehicleNum + 1)%vehicleCount;
	    			schedulerCounter = schedulerCounter + 4;
	    			updateCraft(vehicleNum,updateNum);
	    			//vehicleNum = (vehicleNum + vehicleCount/4)%vehicleCount;
	    			vehicleNum = (vehicleNum + 1)%vehicleCount;
	    			schedulerCounter = schedulerCounter + 4;
	    			updateCraft(vehicleNum,updateNum);
	    			schedulerCounter = schedulerCounter - 4;
	    			schedulerCounter = schedulerCounter - 4;
	    			schedulerCounter = schedulerCounter - 4;
	    			
	    			if( updateNum == 3 )
		    			schedulerCounter+=13;
		    		else
		    			schedulerCounter++;
	    		}
	    		
	    		
	    		if( updateNum == 3 )
	    		{
	    			if( !Craft.addCraftList.isEmpty() )
	    			{
	    				for( Craft c: Craft.addCraftList )
	    				{
	    					Craft.addCraft(c);
	    				}
	    				Craft.addCraftList.clear();
	    			}
	    		}
	    		}catch(Exception e)
	    		{
	    			schedulerCounter++;
	    		}
		    }
   	}
   	, 4, 1);
	 }
   
   @SuppressWarnings("deprecation")
public void updateCraft(int vehicleNum, int updateNum)
   {
	   int vehicleCount = Craft.craftList.size();
	   if( vehicleNum < Craft.craftList.size() && Craft.craftList.get(vehicleNum) != null )
	   {
			Craft checkCraft = Craft.craftList.get(vehicleNum);
			
			if( checkCraft != null && !checkCraft.isDestroying )
			{

				if( checkCraft.isMoving )
				{
					
					if( updateNum == 0 )
					{
						if( checkCraft.doRemove )
						{
							checkCraft.remove();
						}else if( checkCraft.doDestroy )
						{
							checkCraft.destroy();
						}else if( (Math.abs(checkCraft.gear) == 1 && ((schedulerCounter/4) / vehicleCount)%3 == 0)
								|| (Math.abs(checkCraft.gear) == 2 && ((schedulerCounter/4) / vehicleCount)%2 == 0) 
								|| (Math.abs(checkCraft.gear) == 3) )
						{
							if( (Math.abs(checkCraft.gear) == 1 && (System.currentTimeMillis() - checkCraft.lastMove)/1000 > 8)
									|| (Math.abs(checkCraft.gear) == 2 && (System.currentTimeMillis() - checkCraft.lastMove)/1000 >= 5)
									|| (Math.abs(checkCraft.gear) == 3 && (System.currentTimeMillis() - checkCraft.lastMove)/1000.0f >= 2.5) )
							{
								CraftMover cm = new CraftMover(checkCraft, instance);
								cm.moveUpdate();
								//System.out.println("Ship moveupdate="+ checkCraft.craftID);
							}
						}else
						{
							if( !checkCraft.recentlyUpdated )
							{
								if( (System.currentTimeMillis() - checkCraft.lastUpdate)/1000 > 1 )
								{
									CraftMover cm = new CraftMover(checkCraft, instance);
				    				cm.structureUpdate(null,true);
				    				//System.out.println("Ship structureupdate="+ checkCraft.craftID);
								}
							}else
							{
								checkCraft.recentlyUpdated = false;
							}
						}
					}else if( updateNum == 2 )
					{
						if( checkCraft.type.canFly && Math.abs(checkCraft.gear) == 3 )
						{
							if( (System.currentTimeMillis() - checkCraft.lastMove)/1000.0f >= 2.0 )
							{
								CraftMover cm = new CraftMover(checkCraft, instance);
								cm.moveUpdate();
								//System.out.println("Ship moveupdate="+ checkCraft.craftID);
							}
						}
					}
   				}else
   				{
	   				if( checkCraft.enginesOn )
	   				{		
						if( checkCraft.engineIDLocs.isEmpty() )
						{
							if( checkCraft.driverName != null )
							{
								Player p = instance.getServer().getPlayer(checkCraft.driverName);
								if( p != null )
								{
									p.sendMessage("Error: No engines detected! Check engine signs.");
								}
							}
							checkCraft.enginesOn = false;
							checkCraft.speed = 0;
						}else
						{
							for(int id: checkCraft.engineIDLocs.keySet())
							{
								checkCraft.engineIDIsOn.put(id, true);
								checkCraft.engineIDSetOn.put(id, true);
								CraftMover cm = new CraftMover(checkCraft, instance);
								cm.soundThread(checkCraft, id);
							}
						}
   			    
						checkCraft.isMoving = true;
	   				}
   				
	   				if( !checkCraft.recentlyUpdated && updateNum == 0 )
	    			{
	   					if( (System.currentTimeMillis() - checkCraft.lastUpdate)/1000 > 1 )
						{
		    				CraftMover cm = new CraftMover(checkCraft, instance);
		    				cm.structureUpdate(null,true);
		    				checkCraft.lastUpdate = System.currentTimeMillis();
						}
	    				//System.out.println("Ship structureupdate="+ checkCraft.craftID);
	    			}else if( updateNum == 0 )
	    			{
	    				checkCraft.recentlyUpdated = false;
	    			}
	   				
	   				if( checkCraft.doRemove )
					{
						checkCraft.remove();
					}else if( checkCraft.doDestroy )
					{
						checkCraft.destroy();
					}
   				}
				
				
			}
		}
   }

	public static void loadRewardsFile()
	{
		String path = File.separator + "PlayerPlotRewards.txt";
       File file = new File(path);
       
       
       FileReader fr;
       BufferedReader reader;
		try {
			fr = new FileReader(file.getName());
			reader = new BufferedReader(fr);

	        String line = null;
	        
	        try {
	        	
				while ((line=reader.readLine()) != null) 
				{
					String[] strings = line.split(",");
					if( strings.length != 4 )
					{
						System.out.println("Player Reward Load Error1");
						reader.close(); 
						return;
					}
					
					if(strings[1].equalsIgnoreCase("ship1") )
					{
						if( playerSHIP1Rewards.containsKey(strings[0]) )
							 playerSHIP1Rewards.put(strings[0], playerSHIP1Rewards.get(strings[0]) + 1);
						else
							 playerSHIP1Rewards.put(strings[0], 1);
					}else if(strings[1].equalsIgnoreCase("ship2") )
					{
						{
							if( playerSHIP2Rewards.containsKey(strings[0]) )
								playerSHIP2Rewards.put(strings[0], playerSHIP2Rewards.get(strings[0]) + 1);
							else
								playerSHIP2Rewards.put(strings[0], 1);
						}
					}else if(strings[1].equalsIgnoreCase("ship3") )
					{
						{
							if( playerSHIP3Rewards.containsKey(strings[0]) )
								playerSHIP3Rewards.put(strings[0], playerSHIP3Rewards.get(strings[0]) + 1);
							else
								playerSHIP3Rewards.put(strings[0], 1);
						}
					}else if(strings[1].equalsIgnoreCase("ship4") )
					{
						{
							if( playerSHIP4Rewards.containsKey(strings[0]) )
								playerSHIP4Rewards.put(strings[0], playerSHIP4Rewards.get(strings[0]) + 1);
							else
								playerSHIP4Rewards.put(strings[0], 1);
						}
					}else if(strings[1].equalsIgnoreCase("ship5") )
					{
						{
							if( playerSHIP5Rewards.containsKey(strings[0]) )
								playerSHIP5Rewards.put(strings[0], playerSHIP5Rewards.get(strings[0]) + 1);
							else
								playerSHIP5Rewards.put(strings[0], 1);
						}
					}else if( strings[1].equalsIgnoreCase("hangar1") )
					{
						{
							if( playerHANGAR1Rewards.containsKey(strings[0]) )
								playerHANGAR1Rewards.put(strings[0], playerHANGAR1Rewards.get(strings[0]) + 1);
							else
								playerHANGAR1Rewards.put(strings[0], 1);
						}
					}else if( strings[1].equalsIgnoreCase("hangar2") )
					{
						{
							if( playerHANGAR2Rewards.containsKey(strings[0]) )
								playerHANGAR2Rewards.put(strings[0], playerHANGAR2Rewards.get(strings[0]) + 1);
							else
								playerHANGAR2Rewards.put(strings[0], 1);
						}
					}else if( strings[1].equalsIgnoreCase("tank1") )
					{
						{
							if( playerTANK1Rewards.containsKey(strings[0]) )
								playerTANK1Rewards.put(strings[0], playerTANK1Rewards.get(strings[0]) + 1);
							else
								playerTANK1Rewards.put(strings[0], 1);
						}
					}else if( strings[1].equalsIgnoreCase("tank2") )
					{
						{
							if( playerTANK2Rewards.containsKey(strings[0]) )
								playerTANK2Rewards.put(strings[0], playerTANK2Rewards.get(strings[0]) + 1);
							else
								playerTANK2Rewards.put(strings[0], 1);
						}
					}else if(strings[1].equalsIgnoreCase("MAP1") )
						{
							if( playerMAP1Rewards.containsKey(strings[0]) )
								 playerMAP1Rewards.put(strings[0], playerMAP1Rewards.get(strings[0]) + 1);
							else
								 playerMAP1Rewards.put(strings[0], 1);
						}else if(strings[1].equalsIgnoreCase("MAP2") )
						{
							{
								if( playerMAP2Rewards.containsKey(strings[0]) )
									playerMAP2Rewards.put(strings[0], playerMAP2Rewards.get(strings[0]) + 1);
								else
									playerMAP2Rewards.put(strings[0], 1);
							}
						}else if(strings[1].equalsIgnoreCase("MAP3") )
						{
							{
								if( playerMAP3Rewards.containsKey(strings[0]) )
									playerMAP3Rewards.put(strings[0], playerMAP3Rewards.get(strings[0]) + 1);
								else
									playerMAP3Rewards.put(strings[0], 1);
							}
						}else if(strings[1].equalsIgnoreCase("MAP4") )
						{
							{
								if( playerMAP4Rewards.containsKey(strings[0]) )
									playerMAP4Rewards.put(strings[0], playerMAP4Rewards.get(strings[0]) + 1);
								else
									playerMAP4Rewards.put(strings[0], 1);
							}
						}else if(strings[1].equalsIgnoreCase("MAP5") )
						{
							{
								if( playerMAP5Rewards.containsKey(strings[0]) )
									playerMAP5Rewards.put(strings[0], playerMAP5Rewards.get(strings[0]) + 1);
								else
									playerMAP5Rewards.put(strings[0], 1);
							}
					}else
					{
						System.out.println("Player Reward Load Error: Unknown Reward");
					}
					
				}

				
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Player Reward Load Error2");
				return;
			}
	        
	        reader.close();  // Close to unlock.

		} catch (FileNotFoundException e) {
			//System.out.println("Player Reward Load Error3");
			return;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Player Reward Load Error4");
			return;
		}
	}
	
	public static void saveRewardsFile(String s)
	{
		String path = File.separator + "PlayerPlotRewards.txt";
        File file = new File(path);
        FileWriter fw;
        BufferedWriter writer;
	
		try {
			fw = new FileWriter(file.getName(), true);
			writer = new BufferedWriter(fw);
	        
			try {
				writer.write(s);
				writer.newLine();
			} catch (IOException e) {
				System.out.println("Player Save Reward Error1");
				e.printStackTrace();
				writer.close();
				return;
			}
			
			writer.close();
		} catch (IOException e2) {
			System.out.println("Player Save Reward Error2");
			e2.printStackTrace();
			return;
		}
	}
   
	@SuppressWarnings("deprecation")
	public static void explosion(int explosionRadius, Block warhead, boolean signs)
	{
		short powerMatrix[][][];
		powerMatrix = new short[explosionRadius*2+1][explosionRadius*2+1][explosionRadius*2+1];
		
		powerMatrix[explosionRadius][explosionRadius][explosionRadius] = (short)(explosionRadius*50);
		
		warhead.setType(Material.WALL_SIGN);
		Sign firstSign = (Sign) warhead.getState();
		firstSign.setLine(0, "refPower="+powerMatrix[explosionRadius][explosionRadius][explosionRadius]);
		firstSign.update();
		
		int refI=0;
		int refJ=0;
		int refK=0;
		int curX=0;		
		int curY=0;
		int curZ=0;
		int refX=0;		
		int refY=0;
		int refZ=0;
		boolean doPower=false;
		int fuseDelay = 5;
		
		
		for( int r=1; r<explosionRadius; r++ )
		{
			for( int j=-r; j<=r; j++ )
			{
				for( int i=-r; i<=r; i++ )
				{
					for( int k=-r; k<=r; k++ )
					{
						
						
						float refPowerMult = 1.0f;
						curX = i + explosionRadius;
						curY = j + explosionRadius;
						curZ = k + explosionRadius;
						
						if( powerMatrix[curX][curY][curZ] > 0 )
							continue;
						
						if( Math.abs(i) == r )//if on x edges
						{
							refI = (int)((Math.abs(i) - 1)*Math.signum(i));
							if( Math.abs(j) == r )//if on xy edges 
							{
								refJ = (int)((Math.abs(j) - 1)*Math.signum(j));
								if( Math.abs(k) == r && Math.abs(k) > 2) //corner piece, not innermost corners
								{
									refK = (int)((Math.abs(k) - 1)*Math.signum(k));
									refPowerMult = 0.10f;
								}else if( Math.abs(k) >= r - 2 && Math.abs(k) > 2 ) //near corner piece
								{
									refPowerMult = 0.25f;
								}else	//middle xy edge
								{
									refPowerMult = 0.40f;
								}
							}else if( Math.abs(k) == r ) //if on xz edges
							{
								refK = (int)((Math.abs(k) - 1)*Math.signum(k));
								refPowerMult = 0.40f;
								if( Math.abs(j) >= r - 2 && Math.abs(j) > 2 ) //near corner
									refPowerMult = 0.25f;
							}else if( Math.abs(j) == r - 1 )//if near xy edge
							{
								refPowerMult = 0.40f;
								if( Math.abs(k) >= r - 2 && Math.abs(k) > 2 ) //near xyz corner
									refPowerMult = 0.25f;
							}else if( Math.abs(k) == r - 1 )//if near xz edge
							{
								refPowerMult = 0.40f;
								if( Math.abs(j) >= r - 2 && Math.abs(j) > 2 )//near xyz corner
									refPowerMult = 0.25f;
							}else //somewhere else on x sides
							{
								refPowerMult = 0.60f;
							}
							doPower=true;
						}else if( Math.abs(j) == r )//if on y sides
						{
							refJ = (int)((Math.abs(j) - 1)*Math.signum(j));
							if( Math.abs(k) == r ) //if on yz edge
							{
								refK = (int)((Math.abs(k) - 1)*Math.signum(k));
								refPowerMult = 0.40f;
								if( Math.abs(i) >= r - 2 && Math.abs(i) > 2 )//near xyz corner
									refPowerMult = 0.25f;
							}else if( Math.abs(i) == r - 1 )//near xy edge
							{
								refPowerMult = 0.40f;
								if( Math.abs(k) >= r - 2 && Math.abs(k) > 2 )//near xyz corner
									refPowerMult = 0.25f;
							}else if( Math.abs(k) == r - 1 )//near yz edge
							{
								refPowerMult = 0.40f;
								if( Math.abs(i) >= r - 2 && Math.abs(i) > 2 )//near xyz corner
									refPowerMult = 0.25f;
							}else //somewhere else on y sides
							{
								refPowerMult = 0.60f;
							}
							doPower=true;
						}else if( Math.abs(k) == r )//if on z sides
						{
							refK = (int)((Math.abs(k) - 1)*Math.signum(k));
							if( Math.abs(i) >= r - 2 && Math.abs(i) > 2 )//near xz side
							{
								refPowerMult = 0.40f;
								if( Math.abs(j) >= r - 2 && Math.abs(j) > 2 )//near corner
									refPowerMult = 0.25f;
							}else if( Math.abs(j) == r - 1 )//near yz side
							{
								refPowerMult = 0.40f;
								if( Math.abs(i) >= r - 2 && Math.abs(i) > 2 )//near corner
									refPowerMult = 0.25f;
							}else //somewhere else on z sides
							{
								refPowerMult = 0.60f;
							}
							doPower=true;
						}
						
						if( doPower )
						{
							refX = refI + explosionRadius;
							refY = refJ + explosionRadius;
							refZ = refK + explosionRadius;
							
							Block theBlock = warhead.getRelative(i,j,k);
					    	int blockType = theBlock.getTypeId();
							
							short refPower = (short)(powerMatrix[refX][refY][refZ] * refPowerMult) ;
							if( refPower > 0 )
							{
								
								short blockResist;
								if( Craft.blockHardness(blockType) == 4 )
								{
									blockResist = -1;
								}else if( Craft.blockHardness(blockType) == 3 )//obsidian
						    	{
									blockResist = (short)(40 + 40*Math.random());
						    	}else if( Craft.blockHardness(blockType) == 2 )//iron
						    	{
						    		blockResist = (short)(20 + 20*Math.random());
						    	}else if( Craft.blockHardness(blockType) == 1 )//wood
						    	{
						    		blockResist = (short)(10 + 15*Math.random());
						    	}else if( Craft.blockHardness(blockType) == -3 )//water
						    	{
						    		blockResist=(short)(5+5*Math.random());
						    	}else
						    	{
						    		blockResist = (short)(2*Math.random());
						    	}
								
								if( Craft.blockHardness(blockType) == -1 )
								{
									theBlock.setType(Material.AIR);
									TNTPrimed tnt = (TNTPrimed)theBlock.getWorld().spawnEntity(new Location(theBlock.getWorld(), theBlock.getX(), theBlock.getY(), theBlock.getZ()), EntityType.PRIMED_TNT);
						    		tnt.setFuseTicks(fuseDelay);
									fuseDelay = fuseDelay + 2;
								}else if( Craft.blockHardness(blockType) == -2 )
								{
									theBlock.setType(Material.AIR);
									TNTPrimed tnt = (TNTPrimed)theBlock.getWorld().spawnEntity(new Location(theBlock.getWorld(), theBlock.getX(), theBlock.getY(), theBlock.getZ()), EntityType.PRIMED_TNT);
						    		tnt.setFuseTicks(fuseDelay);
						    		tnt.setYield(tnt.getYield()*0.5f);
									fuseDelay = fuseDelay + 2;
								}else
								{

									if( refPower > blockResist && blockResist >= 0 )
									{

										if( signs ) {
											theBlock.setType(Material.WALL_SIGN);
											Sign newSign = (Sign) theBlock.getState();
											newSign.setLine(0, "refPower="+refPower);
											newSign.setLine(1, "blockResist="+blockResist);
											newSign.setLine(2, "refPowerMult="+refPowerMult);
											newSign.update();
										}else{
											if( theBlock.getY() > 62 )
												theBlock.setType(Material.AIR);
											else
												theBlock.setType(Material.WATER);
										}
										
									}else
									{
										refPower = 0;
									}
								}
								short newPower = (short)(refPower - blockResist);
								
								if( newPower < 5 )
								{
									powerMatrix[curX][curY][curZ] = 0;
								}else
								{
									powerMatrix[curX][curY][curZ] = newPower;
								}
							}
							
						}
					}
				}
			}
		}
		warhead.getWorld().createExplosion(warhead.getLocation(), explosionRadius/2.0f);
	}
}