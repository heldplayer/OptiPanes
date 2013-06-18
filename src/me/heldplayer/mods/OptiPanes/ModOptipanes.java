
package me.heldplayer.mods.OptiPanes;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

@Mod(name = "Optipanes", modid = "Optipanes", version = "1.2")
@NetworkMod(clientSideRequired = false, serverSideRequired = false)
public class ModOptipanes {
    @Instance(value = "Optipanes")
    protected static ModOptipanes instance;

    // Blocks
    public BlockOptipane blockOptiPane;

    // Items

    // Lists
    private int[] blockIds = new int[1];
    private int[] renderIds = new int[1];

    // Others
    protected CreativeTabs creativeTab = new CreativeTabOptipanes("optipane");
    private String[] paneTextures = new String[] { "glass", "stone", "wood", "wood_spruce", "wood_birch", "wood_jungle", "brick", "stoneMoss", "obsidian", "snow", "clay", "stonebricksmooth", "stonebricksmooth_mossy", "stonebricksmooth_cracked", "fenceIron", "netherBrick" };
    private String[] paneNames = new String[] { "tile.glass.name", "tile.stonebrick.name", "tile.wood.oak.name", "tile.wood.spruce.name", "tile.wood.birch.name", "tile.wood.jungle.name", "tile.brick.name", "tile.stoneMoss.name", "tile.obsidian.name", "tile.snow.name", "tile.clay.name", "tile.stonebricksmooth.default.name", "tile.stonebricksmooth.mossy.name", "tile.stonebricksmooth.cracked.name", "tile.fenceIron.name", "tile.netherBrick.name" };
    private boolean[] paneRegisterers = new boolean[] { false, true, false, false, false, false, true, true, true, true, true, false, false, false, true, true };
    private int[] paneBlocks = new int[] { 20, 4, 5, 5, 5, 5, 45, 48, 49, 80, 82, 98, 98, 98, 101, 112 };

    public static Logger log;

    @PreInit
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();

        this.blockIds = new int[1];
        this.renderIds = new int[1];

        Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());

        try {
            cfg.load();

            this.blockIds[0] = cfg.getBlock("optipane", 3000).getInt();
        }
        catch (Exception e) {
            log.log(Level.WARNING, "Mod '" + event.getModMetadata().name + "' failed to read its config file.", e);
        }
        finally {
            cfg.save();
        }

        this.blockIds[0] = 3000;

        this.renderIds[0] = RenderingRegistry.getNextAvailableRenderId();
    }

    @Init
    public void init(FMLInitializationEvent event) {
        //// Blocks
        // Setup
        this.blockOptiPane = new BlockOptipane(this.blockIds[0], this.paneTextures, this.paneNames, Material.rock);
        this.blockOptiPane.setRenderId(this.renderIds[0]).setCreativeTab(this.creativeTab);

        // Rendering
        RenderingRegistry.registerBlockHandler(new BlockRendererOptipane(this.renderIds[0]));

        // Names
        this.blockOptiPane.setUnlocalizedName("barbedWire");
        LanguageRegistry.instance().addStringLocalization("tile.optipane.name", "Optipane");

        // Finalize
        GameRegistry.registerBlock(this.blockOptiPane, ItemOptipaneBlock.class, "heldplayer.mods.optipane", "Optipanes");

        // Recipes
        GameRegistry.addRecipe(new ItemStack(this.blockOptiPane, 16, 0), "ggg", "ggg", "ggg", 'g', Block.blocksList[Block.glass.blockID]);

        for (int i = 0; i < this.paneTextures.length; i++) {
            if (this.paneRegisterers[i]) {
                GameRegistry.addRecipe(new ItemStack(this.blockOptiPane, 8, i), new String[] { "ppp", "pbp", "ppp" }, 'b', Block.blocksList[this.paneBlocks[i]], 'p', new ItemStack(this.blockOptiPane, 1, 0));
            }
        }

        GameRegistry.addRecipe(new ItemStack(this.blockOptiPane, 8, 2), new String[] { "ppp", "pbp", "ppp" }, 'b', new ItemStack(Block.blocksList[this.paneBlocks[2]], 1, 0), 'p', new ItemStack(this.blockOptiPane, 1, 0));
        GameRegistry.addRecipe(new ItemStack(this.blockOptiPane, 8, 3), new String[] { "ppp", "pbp", "ppp" }, 'b', new ItemStack(Block.blocksList[this.paneBlocks[3]], 1, 1), 'p', new ItemStack(this.blockOptiPane, 1, 0));
        GameRegistry.addRecipe(new ItemStack(this.blockOptiPane, 8, 4), new String[] { "ppp", "pbp", "ppp" }, 'b', new ItemStack(Block.blocksList[this.paneBlocks[4]], 1, 2), 'p', new ItemStack(this.blockOptiPane, 1, 0));
        GameRegistry.addRecipe(new ItemStack(this.blockOptiPane, 8, 5), new String[] { "ppp", "pbp", "ppp" }, 'b', new ItemStack(Block.blocksList[this.paneBlocks[5]], 1, 3), 'p', new ItemStack(this.blockOptiPane, 1, 0));
        GameRegistry.addRecipe(new ItemStack(this.blockOptiPane, 8, 11), new String[] { "ppp", "pbp", "ppp" }, 'b', new ItemStack(Block.blocksList[this.paneBlocks[11]], 1, 0), 'p', new ItemStack(this.blockOptiPane, 1, 0));
        GameRegistry.addRecipe(new ItemStack(this.blockOptiPane, 8, 12), new String[] { "ppp", "pbp", "ppp" }, 'b', new ItemStack(Block.blocksList[this.paneBlocks[12]], 1, 1), 'p', new ItemStack(this.blockOptiPane, 1, 0));
        GameRegistry.addRecipe(new ItemStack(this.blockOptiPane, 8, 13), new String[] { "ppp", "pbp", "ppp" }, 'b', new ItemStack(Block.blocksList[this.paneBlocks[13]], 1, 2), 'p', new ItemStack(this.blockOptiPane, 1, 0));

        //// Others
        LanguageRegistry.instance().addStringLocalization("itemGroup.optipane", "Optipanes");
    }

    @PostInit
    public void postInit(FMLPostInitializationEvent event) {}
}
