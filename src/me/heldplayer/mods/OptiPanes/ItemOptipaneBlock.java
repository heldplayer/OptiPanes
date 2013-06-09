
package me.heldplayer.mods.OptiPanes;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.StatCollector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOptipaneBlock extends ItemBlock {
    private final BlockOptipane block;

    public ItemOptipaneBlock(int id) {
        super(id);
        this.block = ModOptipanes.instance.blockOptiPane;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public Icon getIconFromDamage(int meta) {
        try {
            return this.block.icons[meta];
        }
        catch (Exception ex) {
            return this.block.icons[0];
        }
    }

    @Override
    public int getMetadata(int meta) {
        return meta % this.block.icons.length;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack stack, int pass) {
        return (stack.getItemDamage() == 0 || stack.getItemDamage() == 9) ? 0xDDDDDD : 0xFFFFFF;
    }

    @Override
    public String getItemDisplayName(ItemStack stack) {
        int meta = stack.getItemDamage();

        if (meta < 0 || meta >= this.block.names.length) {
            meta = 0;
        }

        try {
            return StatCollector.translateToLocal(this.block.names[meta]) + " " + StatCollector.translateToLocal("tile.optipane.name");
        }
        catch (Exception ex) {
            return "Unknown Pane";
        }
    }
}
