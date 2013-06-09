
package me.heldplayer.mods.OptiPanes;

import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CreativeTabOptipanes extends CreativeTabs {

    public CreativeTabOptipanes(String label) {
        super(label);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getTabIconItemIndex() {
        return ModOptipanes.instance.blockOptiPane.blockID;
    }
}
