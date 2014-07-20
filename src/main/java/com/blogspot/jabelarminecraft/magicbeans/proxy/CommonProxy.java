package com.blogspot.jabelarminecraft.magicbeans.proxy;

import net.minecraft.util.ChatComponentText;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import com.blogspot.jabelarminecraft.magicbeans.MagicBeans;
import com.blogspot.jabelarminecraft.magicbeans.MagicBeansEventHandler;
import com.blogspot.jabelarminecraft.magicbeans.MagicBeansFMLEventHandler;
import com.blogspot.jabelarminecraft.magicbeans.MagicBeansOreGenEventHandler;
import com.blogspot.jabelarminecraft.magicbeans.MagicBeansTerrainGenEventHandler;
import com.blogspot.jabelarminecraft.magicbeans.commands.CommandStructure;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGoldenEggThrown;
import com.blogspot.jabelarminecraft.magicbeans.entities.EntityGoldenGoose;
import com.blogspot.jabelarminecraft.magicbeans.tileentities.TileEntityMagicBeanStalk;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class CommonProxy 
{
    
    protected int modEntityID = 0;
    protected Configuration config;
     
    public void fmlLifeCycleEvent(FMLPreInitializationEvent event)
    { 
        // load configuration before doing anything else
        // got config tutorial from http://www.minecraftforge.net/wiki/How_to_make_an_advanced_configuration_file
        processConfig(event);

        // register stuff
        registerBlocks();
        registerItems();
        registerTileEntities();
        registerModEntities();
        registerEntitySpawns();
        registerFuelHandlers();
    }
    
    public void fmlLifeCycleEvent(FMLInitializationEvent event)
    {
        // register custom event listeners
        registerEventListeners();
 
        // register networking channel 
        registerNetworkingChannel();
        
        // register server packet handler
        registerServerPacketHandler();
        
        // register recipes here to allow use of items from other mods
        registerRecipes();
    }
    
    public void fmlLifeCycleEvent(FMLPostInitializationEvent event)
    {
        // can do some inter-mod stuff here
    }
    
    protected void processConfig(FMLPreInitializationEvent event)
    {
        // might need to use suggestedConfigFile (event.getSuggestedConfigFile) location to publish
        MagicBeans.configFile = event.getSuggestedConfigurationFile();
        // DEBUG
        System.out.println(MagicBeans.MODNAME+" config path = "+MagicBeans.configFile.getAbsolutePath());
        System.out.println("Config file exists = "+MagicBeans.configFile.canRead());
        
        config = new Configuration(MagicBeans.configFile);
        MagicBeans.config = config;
        
        // don't need to do a config.load() because the constructor already calls it
        MagicBeans.configGiantIsHostile = config.get(Configuration.CATEGORY_GENERAL, "GiantIsHostile", true).getBoolean(true);
        System.out.println("Giant is hostile = "+MagicBeans.configGiantIsHostile);
        MagicBeans.configGiantHealth = config.get(Configuration.CATEGORY_GENERAL, "GiantHealth", 10).getInt(10);
        System.out.println("Giant health = "+MagicBeans.configGiantHealth);
        MagicBeans.configGiantAttackDamage = config.get(Configuration.CATEGORY_GENERAL, "GiantAttackDamage", 4).getInt(4);
        System.out.println("Giant Attack Damage = "+MagicBeans.configGiantAttackDamage);
        MagicBeans.configGoldForBeans = config.get(Configuration.CATEGORY_GENERAL, "GoldForBeans", 100).getInt(100);
        System.out.println("Gold for beans = "+MagicBeans.configGoldForBeans);
        
        // save is useful for the first run where config might not exist
        config.save();
    }

    // register blocks
    public void registerBlocks()
    {
        //example: GameRegistry.registerBlock(blockTomato, "tomatoes");
    	GameRegistry.registerBlock(MagicBeans.blockMagicBeanStalk, "magicbeanstalk");
    	GameRegistry.registerBlock(MagicBeans.blockMagicBeansVine, "magicbeansvine");
    }

    // register fluids
    public void registerFluids()
    {
        // see tutorial at http://www.minecraftforge.net/wiki/Create_a_Fluid
        // Fluid testFluid = new Fluid("testfluid");
        // FluidRegistry.registerFluid(testFluid);
        // testFluid.setLuminosity(0).setDensity(1000).setViscosity(1000).setGaseous(false) ;
     }
    
    // register items
    private void registerItems()
    {
        // DEBUG
        System.out.println("Registering items");

        // spawn eggs are registered during entity registration
 
        GameRegistry.registerItem(MagicBeans.itemGoldenEgg, "goldenEgg");
        GameRegistry.registerItem(MagicBeans.magicBeans, "magicbeans");
        
        // example: GameRegistry.registerCustomItemStack(name, itemStack);
    }
    
    // register tileentities
    public void registerTileEntities()
    {
        // DEBUG
        System.out.println("Registering tile entities");
               
        // example: GameRegistry.registerTileEntity(TileEntityStove.class, "stove_tile_entity");
        GameRegistry.registerTileEntity(TileEntityMagicBeanStalk.class, "tileEntityMagicBeanStalk");
    }

    // register recipes
    public void registerRecipes()
    {
        // DEBUG
        System.out.println("Registering recipes");
               
        // examples:
        //        GameRegistry.addRecipe(recipe);
        //        GameRegistry.addShapedRecipe(output, params);
        //        GameRegistry.addShapelessRecipe(output, params);
        //        GameRegistry.addSmelting(input, output, xp);
    }

    // register entities
    // lots of conflicting tutorials on this, currently following: nly register mod id http://www.minecraftforum.net/topic/1417041-mod-entity-problem/page__st__140#entry18822284
    // another tut says to only register global id like http://www.minecraftforge.net/wiki/How_to_register_a_mob_entity#Registering_an_Entity
    // another tut says to use both: http://www.minecraftforum.net/topic/2389683-172-forge-add-new-block-item-entity-ai-creative-tab-language-localization-block-textures-side-textures/
     public void registerModEntities()
    {    
         // DEBUG
        System.out.println("Registering entities");
        // if you want it with a spawn egg use
        // registerModEntityWithEgg(EntityManEatingTiger.class, "Tiger", 0xE18519, 0x000000);
        // or without spawn egg use
        // EntityRegistry.registerModEntity(parEntityClass, parEntityName, ++modEntityID, MagicBeans.instance, 80, 3, false);

        registerModEntity(EntityGoldenGoose.class, "Golden Goose");
        registerModEntityFastTracking(EntityGoldenEggThrown.class, "Golden Egg");
    }
     
     public void registerModEntity(Class parEntityClass, String parEntityName)
     {
            EntityRegistry.registerModEntity(parEntityClass, parEntityName, ++modEntityID, MagicBeans.instance, 80, 3, false);
     }
     
     public void registerModEntityFastTracking(Class parEntityClass, String parEntityName)
     {
            EntityRegistry.registerModEntity(parEntityClass, parEntityName, ++modEntityID, MagicBeans.instance, 80, 10, true);
     }
          
    public void registerEntitySpawns()
    {
        // register natural spawns for entities
        // EntityRegistry.addSpawn(MyEntity.class, spawnProbability, minSpawn, maxSpawn, enumCreatureType, [spawnBiome]);
        // See the constructor in BiomeGenBase.java to see the rarity of vanilla mobs; Sheep are probability 10 while Endermen are probability 1
        // minSpawn and maxSpawn are about how groups of the entity spawn
        // enumCreatureType represents the "rules" Minecraft uses to determine spawning, based on creature type. By default, you have three choices:
        //    EnumCreatureType.creature uses rules for animals: spawn everywhere it is light out.
        //    EnumCreatureType.monster uses rules for monsters: spawn everywhere it is dark out.
        //    EnumCreatureType.waterCreature uses rules for water creatures: spawn only in water.
        // [spawnBiome] is an optional parameter of type BiomeGenBase that limits the creature spawn to a single biome type. Without this parameter, it will spawn everywhere. 

         // DEBUG
        System.out.println("Registering natural spawns");
        
        // For the biome type you can use an list, but unfortunately the built-in biomeList contains
        // null entries and will crash, so you need to clean up that list.
        // Diesieben07 suggested the following code to remove the nulls and create list of all biomes
        BiomeGenBase[] allBiomes = Iterators.toArray(Iterators.filter(Iterators.forArray(BiomeGenBase.getBiomeGenArray()), Predicates.notNull()), BiomeGenBase.class);

        // // savanna
        // EntityRegistry.addSpawn(EntityLion.class, 6, 1, 5, EnumCreatureType.creature, BiomeGenBase.savanna); //change the values to vary the spawn rarity, biome, etc.              
        // EntityRegistry.addSpawn(EntityElephant.class, 10, 1, 5, EnumCreatureType.creature, BiomeGenBase.savanna); //change the values to vary the spawn rarity, biome, etc.              
    }
    
    public void registerFuelHandlers()
    {
         // DEBUG
        System.out.println("Registering fuel handlers");
        
        // example: GameRegistry.registerFuelHandler(handler);
    }
    
    public void registerEventListeners() 
    {
         // DEBUG
        System.out.println("Registering event listeners");

        MinecraftForge.EVENT_BUS.register(new MagicBeansEventHandler());
        MinecraftForge.TERRAIN_GEN_BUS.register(new MagicBeansTerrainGenEventHandler());
        MinecraftForge.ORE_GEN_BUS.register(new MagicBeansOreGenEventHandler());        

        // some events, especially tick, is handled on FML bus
        FMLCommonHandler.instance().bus().register(new MagicBeansFMLEventHandler());
    }

    public void sendMessageToPlayer(ChatComponentText msg) { }

    public void registerNetworkingChannel()
    {
        MagicBeans.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(MagicBeans.NETWORK_CHANNEL_NAME);
        // when you want to send a packet elsewhere, use one of these methods (server or client):
        //     MagicBeans.channel.sendToServer(FMLProxyPacket);
        //     MagicBeans.channel.sendTo(FMLProxyPacket, EntityPlayerMP); for player-specific GUI interaction
        //     MagicBeans.channel.sendToAll(FMLProxyPacket); for all player sync like entities
        // and there are other sendToxxx methods to check out.
    }
    
    public void registerServerPacketHandler()
    {
        MagicBeans.channel.register(new ServerPacketHandler());
    }

	public void fmlLifeCycleEvent(FMLServerStartingEvent event) 
	{
		// // register server commands
        event.registerServerCommand(new CommandStructure());
	}

	public void fmlLifeCycleEvent(FMLServerAboutToStartEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void fmlLifeCycleEvent(FMLServerStartedEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void fmlLifeCycleEvent(FMLServerStoppingEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void fmlLifeCycleEvent(FMLServerStoppedEvent event) {
		// TODO Auto-generated method stub
		
	}
}