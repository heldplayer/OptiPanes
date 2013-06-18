
package me.heldplayer.mods.OptiPanes;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPane;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOptipane extends BlockPane {

    protected Icon[] icons;
    protected String[] textures;
    protected String[] names;

    protected BlockOptipane(int id, String[] textures, String[] names, Material material) {
        super(id, null, null, material, true);
        this.setHardness(0.5F);
        this.setStepSound(soundMetalFootstep);

        this.textures = textures;
        this.icons = new Icon[this.textures.length];
        this.names = names;
    }

    private int renderId = 0;

    protected Block setRenderId(int id) {
        this.renderId = id;
        return this;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister register) {
        for (int i = 0; i < this.textures.length; i++) {
            this.icons[i] = register.registerIcon(this.textures[i]);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getIcon(int side, int meta) {
        try {
            return this.icons[meta];
        }
        catch (Exception ex) {
            return this.icons[0];
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubBlocks(int id, CreativeTabs tabs, List itemList) {
        for (int i = 0; i < this.icons.length; i++) {
            itemList.add(new ItemStack(id, 1, i));
        }
    }

    @Override
    public int getRenderType() {
        return this.renderId;
    }

    public boolean canConnectToBlockID(int id) {
        return Block.opaqueCubeLookup[id] || id == this.blockID || Block.blocksList[id] instanceof BlockFence;
    }

    public boolean canConnect(int id, int meta, int myMeta) {
        return Block.opaqueCubeLookup[id] || (id == this.blockID && meta == myMeta) || Block.blocksList[id] instanceof BlockFence;
    }

    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void addAABB(World world, int x, int y, int z, AxisAlignedBB AABB, List list, Entity entity) {
        AxisAlignedBB blockAABB = this.getCollisionBoundingBoxFromPool(world, x, y, z);

        if (blockAABB != null && AABB.intersectsWith(blockAABB)) {
            list.add(blockAABB);
        }
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB AABB, List list, Entity entity) {
        boolean north = this.canConnect(world.getBlockId(x, y, z - 1), world.getBlockMetadata(x, y, z - 1), world.getBlockMetadata(x, y, z));
        boolean east = this.canConnect(world.getBlockId(x + 1, y, z), world.getBlockMetadata(x + 1, y, z), world.getBlockMetadata(x, y, z));
        boolean south = this.canConnect(world.getBlockId(x, y, z + 1), world.getBlockMetadata(x, y, z + 1), world.getBlockMetadata(x, y, z));
        boolean west = this.canConnect(world.getBlockId(x - 1, y, z), world.getBlockMetadata(x - 1, y, z), world.getBlockMetadata(x, y, z));

        boolean northwest = !north && !west && this.canConnect(world.getBlockId(x - 1, y, z - 1), world.getBlockMetadata(x - 1, y, z - 1), world.getBlockMetadata(x, y, z));
        boolean northeast = !north && !east && this.canConnect(world.getBlockId(x + 1, y, z - 1), world.getBlockMetadata(x + 1, y, z - 1), world.getBlockMetadata(x, y, z));
        boolean southeast = !south && !east && this.canConnect(world.getBlockId(x + 1, y, z + 1), world.getBlockMetadata(x + 1, y, z + 1), world.getBlockMetadata(x, y, z));
        boolean southwest = !south && !west && this.canConnect(world.getBlockId(x - 1, y, z + 1), world.getBlockMetadata(x - 1, y, z + 1), world.getBlockMetadata(x, y, z));

        float min = 0.0F;
        float center1 = 0.4375F;
        float center = 0.5F;
        float center2 = 0.5625F;
        float max = 1.0F;

        boolean hasBounds = false;

        if (north) {
            this.setBlockBounds(center1, min, min, center2, max, center);
            this.addAABB(world, x, y, z, AABB, list, entity);

            hasBounds = true;
        }
        if (south) {
            this.setBlockBounds(center1, min, center, center2, max, max);
            this.addAABB(world, x, y, z, AABB, list, entity);

            hasBounds = true;
        }

        if (east) {
            this.setBlockBounds(center, min, center1, max, max, center2);
            this.addAABB(world, x, y, z, AABB, list, entity);

            hasBounds = true;
        }
        if (west) {
            this.setBlockBounds(min, min, center1, center, max, center2);
            this.addAABB(world, x, y, z, AABB, list, entity);

            hasBounds = true;
        }

        if (northeast) {
            this.setBlockBounds(center1, min, min, max, max, center2);
            this.addAABB(world, x, y, z, AABB, list, entity);

            hasBounds = true;
        }
        if (southwest) {
            this.setBlockBounds(min, min, center1, center2, max, max);
            this.addAABB(world, x, y, z, AABB, list, entity);

            hasBounds = true;
        }

        if (northwest) {
            this.setBlockBounds(min, min, min, center2, max, center2);
            this.addAABB(world, x, y, z, AABB, list, entity);

            hasBounds = true;
        }
        if (southeast) {
            this.setBlockBounds(center1, min, center1, max, max, max);
            this.addAABB(world, x, y, z, AABB, list, entity);

            hasBounds = true;
        }

        // @formatter:off
        /*
         * if (northFence) {
         * this.setBlockBounds(center1, min, min - 0.5F, center2, max, min);
         * this.addAABB(world, x, y, z, AABB, list, entity);
         * }
         * if (southFence) {
         * this.setBlockBounds(center1, min, max, center2, max, max + 0.5F);
         * this.addAABB(world, x, y, z, AABB, list, entity);
         * }
         * if (eastFence) {
         * this.setBlockBounds(max, min, center1, max + 0.5F, max, center2);
         * this.addAABB(world, x, y, z, AABB, list, entity);
         * }
         * if (westFence) {
         * this.setBlockBounds(min - 0.5F, min, center1, min, max, center2);
         * this.addAABB(world, x, y, z, AABB, list, entity);
         * }
         * 
         * if (northwestFence) {
         * this.setBlockBounds(min - 0.5F, min, min - 0.5F, min, max, min);
         * this.addAABB(world, x, y, z, AABB, list, entity);
         * }
         * if (northeastFence) {
         * this.setBlockBounds(max, min, min - 0.5F, max + 0.5F, max, min);
         * this.addAABB(world, x, y, z, AABB, list, entity);
         * }
         * if (southeastFence) {
         * this.setBlockBounds(max, min, max, max + 0.5F, max, max + 0.5F);
         * this.addAABB(world, x, y, z, AABB, list, entity);
         * }
         * if (southwestFence) {
         * this.setBlockBounds(min - 0.5F, min, max, min, max, max + 0.5F);
         * this.addAABB(world, x, y, z, AABB, list, entity);
         * }
         */
        // @formatter:on

        if (!hasBounds) {
            this.setBlockBounds(center1, min, min, center2, max, center);
            this.addAABB(world, x, y, z, AABB, list, entity);

            this.setBlockBounds(center1, min, center, center2, max, max);
            this.addAABB(world, x, y, z, AABB, list, entity);

            this.setBlockBounds(center, min, center1, max, max, center2);
            this.addAABB(world, x, y, z, AABB, list, entity);

            this.setBlockBounds(min, min, center1, center, max, center2);
            this.addAABB(world, x, y, z, AABB, list, entity);
        }

        this.setBlockBoundsForItemRender();
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess access, int x, int y, int z) {
        boolean north = this.canConnect(access.getBlockId(x, y, z - 1), access.getBlockMetadata(x, y, z - 1), access.getBlockMetadata(x, y, z));
        boolean east = this.canConnect(access.getBlockId(x + 1, y, z), access.getBlockMetadata(x + 1, y, z), access.getBlockMetadata(x, y, z));
        boolean south = this.canConnect(access.getBlockId(x, y, z + 1), access.getBlockMetadata(x, y, z + 1), access.getBlockMetadata(x, y, z));
        boolean west = this.canConnect(access.getBlockId(x - 1, y, z), access.getBlockMetadata(x - 1, y, z), access.getBlockMetadata(x, y, z));

        boolean northwest = !north && !west && this.canConnect(access.getBlockId(x - 1, y, z - 1), access.getBlockMetadata(x - 1, y, z - 1), access.getBlockMetadata(x, y, z));
        boolean northeast = !north && !east && this.canConnect(access.getBlockId(x + 1, y, z - 1), access.getBlockMetadata(x + 1, y, z - 1), access.getBlockMetadata(x, y, z));
        boolean southeast = !south && !east && this.canConnect(access.getBlockId(x + 1, y, z + 1), access.getBlockMetadata(x + 1, y, z + 1), access.getBlockMetadata(x, y, z));
        boolean southwest = !south && !west && this.canConnect(access.getBlockId(x - 1, y, z + 1), access.getBlockMetadata(x - 1, y, z + 1), access.getBlockMetadata(x, y, z));

        float min = 0.0F;
        float center1 = 0.4375F;
        float center = 0.5F;
        float center2 = 0.5625F;
        float max = 1.0F;

        float minX = 0.0F;
        float maxX = 1.0F;
        float minZ = 0.0F;
        float maxZ = 1.0F;

        boolean hasBounds = false;

        if (north) {
            if (!hasBounds) {
                minX = center1;
                maxX = center2;
                minZ = min;
                maxZ = center;
            }
            else {
                minX = center1 < minX ? center1 : minX;
                maxX = center2 > maxX ? center2 : maxX;
                minZ = min < minZ ? min : minZ;
                maxZ = center > maxZ ? center : maxZ;
            }

            hasBounds = true;
        }
        if (south) {
            if (!hasBounds) {
                minX = center1;
                maxX = center2;
                minZ = center;
                maxZ = max;
            }
            else {
                minX = center1 < minX ? center1 : minX;
                maxX = center2 > maxX ? center2 : maxX;
                minZ = center < minZ ? center : minZ;
                maxZ = max > maxZ ? max : maxZ;
            }

            hasBounds = true;
        }

        if (east) {
            if (!hasBounds) {
                minX = center;
                maxX = max;
                minZ = center1;
                maxZ = center2;
            }
            else {
                minX = center < minX ? center : minX;
                maxX = max > maxX ? max : maxX;
                minZ = center1 < minZ ? center1 : minZ;
                maxZ = center2 > maxZ ? center2 : maxZ;
            }

            hasBounds = true;
        }
        if (west) {
            if (!hasBounds) {
                minX = min;
                maxX = center;
                minZ = center1;
                maxZ = center2;
            }
            else {
                minX = min < minX ? min : minX;
                maxX = center > maxX ? center : maxX;
                minZ = center1 < minZ ? center1 : minZ;
                maxZ = center2 > maxZ ? center2 : maxZ;
            }

            hasBounds = true;
        }

        if (northeast) {
            if (!hasBounds) {
                minX = center;
                maxX = max;
                minZ = min;
                maxZ = center;
            }
            else {
                minX = center < minX ? center : minX;
                maxX = max > maxX ? max : maxX;
                minZ = min < minZ ? min : minZ;
                maxZ = center > maxZ ? center : maxZ;
            }

            hasBounds = true;
        }
        if (southwest) {
            if (!hasBounds) {
                minX = min;
                maxX = center;
                minZ = center;
                maxZ = max;
            }
            else {
                minX = min < minX ? min : minX;
                maxX = center > maxX ? center : maxX;
                minZ = center < minZ ? center : minZ;
                maxZ = max > maxZ ? max : maxZ;
            }

            hasBounds = true;
        }

        if (northwest) {
            if (!hasBounds) {
                minX = min;
                maxX = center;
                minZ = min;
                maxZ = center;
            }
            else {
                minX = min < minX ? min : minX;
                maxX = center > maxX ? center : maxX;
                minZ = min < minZ ? min : minZ;
                maxZ = center > maxZ ? center : maxZ;
            }

            hasBounds = true;
        }
        if (southeast) {
            if (!hasBounds) {
                minX = center;
                maxX = max;
                minZ = center;
                maxZ = max;
            }
            else {
                minX = center < minX ? center : minX;
                maxX = max > maxX ? max : maxX;
                minZ = center < minZ ? center : minZ;
                maxZ = max > maxZ ? max : maxZ;
            }

            hasBounds = true;
        }

        // @formatter:off
        /*
         * if (northFence) {
         * minX = center1 < minX ? center1 : minX;
         * maxX = center2 > maxX ? center2 : maxX;
         * minZ = min - 0.5F < minZ ? min - 0.5F : minZ;
         * maxZ = center > maxZ ? center : maxZ;
         * }
         * if (southFence) {
         * minX = center1 < minX ? center1 : minX;
         * maxX = center2 > maxX ? center2 : maxX;
         * minZ = center < minZ ? center2 : minZ;
         * maxZ = max + 0.5F > maxZ ? max + 0.5F : maxZ;
         * }
         * if (eastFence) {
         * minX = center < minX ? center : minX;
         * maxX = max + 0.5F > maxX ? max + 0.5F : maxX;
         * minZ = center1 < minZ ? center1 : minZ;
         * maxZ = center2 > maxZ ? center2 : maxZ;
         * }
         * if (westFence) {
         * minX = min - 0.5F < minX ? min - 0.5F : minX;
         * maxX = center > maxX ? center : maxX;
         * minZ = center1 < minZ ? center1 : minZ;
         * maxZ = center2 > maxZ ? center2 : maxZ;
         * }
         * 
         * if (northwestFence) {
         * minX = min - 0.5F < minX ? min - 0.5F : minX;
         * maxX = center > maxX ? center : maxX;
         * minZ = min - 0.5F < minZ ? min - 0.5F : minZ;
         * maxZ = center > maxZ ? center : maxZ;
         * }
         * if (northeastFence) {
         * minX = center < minX ? center : minX;
         * maxX = max + 0.5F > maxX ? max + 0.5F : maxX;
         * minZ = min - 0.5F < minZ ? min - 0.5F : minZ;
         * maxZ = center > maxZ ? center : maxZ;
         * }
         * if (southeastFence) {
         * minX = center < minX ? center : minX;
         * maxX = max + 0.5F > maxX ? max + 0.5F : maxX;
         * minZ = center < minZ ? center2 : minZ;
         * maxZ = max + 0.5F > maxZ ? max + 0.5F : maxZ;
         * }
         * if (southwestFence) {
         * minX = min - 0.5F < minX ? min - 0.5F : minX;
         * maxX = center > maxX ? center : maxX;
         * minZ = center < minZ ? center2 : minZ;
         * maxZ = max + 0.5F > maxZ ? max + 0.5F : maxZ;
         * }
         */
        // @formatter:on

        if (!hasBounds) {
            this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        }
        else {
            this.setBlockBounds(minX, 0.0F, minZ, maxX, 1.0F, maxZ);
        }
    }
}
