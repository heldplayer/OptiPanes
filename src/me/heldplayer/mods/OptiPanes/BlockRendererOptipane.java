
package me.heldplayer.mods.OptiPanes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class BlockRendererOptipane implements ISimpleBlockRenderingHandler {

    private final int renderId;

    protected BlockRendererOptipane(int renderId) {
        this.renderId = renderId;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if (modelId != this.getRenderId()) {
            return false;
        }
        if (!(block instanceof BlockOptipane)) {
            return false;
        }

        BlockOptipane wire = (BlockOptipane) block;

        Tessellator tess = Tessellator.instance;
        int meta = world.getBlockMetadata(x, y, z);
        Icon texture = block.getIcon(0, meta);

        if (renderer.overrideBlockTexture != null) {
            texture = renderer.overrideBlockTexture;
        }

        tess.setBrightness(block.getMixedBrightnessForBlock(renderer.blockAccess, x, y, z));
        tess.setColorOpaque_I(block.colorMultiplier(renderer.blockAccess, x, y, z));

        // North: Z decreases
        // East: X increases
        // South: Z increases
        // West: X decreases

        // Check the direct neighbours of the pane
        boolean north = wire.canConnect(world.getBlockId(x, y, z - 1), world.getBlockMetadata(x, y, z - 1), meta);
        boolean east = wire.canConnect(world.getBlockId(x + 1, y, z), world.getBlockMetadata(x + 1, y, z), meta);
        boolean south = wire.canConnect(world.getBlockId(x, y, z + 1), world.getBlockMetadata(x, y, z + 1), meta);
        boolean west = wire.canConnect(world.getBlockId(x - 1, y, z), world.getBlockMetadata(x - 1, y, z), meta);

        // Check the diagonal neighbours of the pane if there isn't a neighbouring block that connects in next to it
        boolean northwest = !north && !west && wire.canConnect(world.getBlockId(x - 1, y, z - 1), world.getBlockMetadata(x - 1, y, z - 1), meta);
        boolean northeast = !north && !east && wire.canConnect(world.getBlockId(x + 1, y, z - 1), world.getBlockMetadata(x + 1, y, z - 1), meta);
        boolean southeast = !south && !east && wire.canConnect(world.getBlockId(x + 1, y, z + 1), world.getBlockMetadata(x + 1, y, z + 1), meta);
        boolean southwest = !south && !west && wire.canConnect(world.getBlockId(x - 1, y, z + 1), world.getBlockMetadata(x - 1, y, z + 1), meta);

        // Check if the neighbours are fences if they can't connect
        boolean northFence = north && Block.blocksList[world.getBlockId(x, y, z - 1)] != null && Block.blocksList[world.getBlockId(x, y, z - 1)] instanceof BlockFence;
        boolean eastFence = east && Block.blocksList[world.getBlockId(x + 1, y, z)] != null && Block.blocksList[world.getBlockId(x + 1, y, z)] instanceof BlockFence;
        boolean southFence = south && Block.blocksList[world.getBlockId(x, y, z + 1)] != null && Block.blocksList[world.getBlockId(x, y, z + 1)] instanceof BlockFence;
        boolean westFence = west && Block.blocksList[world.getBlockId(x - 1, y, z)] != null && Block.blocksList[world.getBlockId(x - 1, y, z)] instanceof BlockFence;

        boolean northwestFence = northwest && Block.blocksList[world.getBlockId(x - 1, y, z - 1)] != null && Block.blocksList[world.getBlockId(x - 1, y, z - 1)] instanceof BlockFence;
        boolean northeastFence = northeast && Block.blocksList[world.getBlockId(x + 1, y, z - 1)] != null && Block.blocksList[world.getBlockId(x + 1, y, z - 1)] instanceof BlockFence;
        boolean southeastFence = southeast && Block.blocksList[world.getBlockId(x + 1, y, z + 1)] != null && Block.blocksList[world.getBlockId(x + 1, y, z + 1)] instanceof BlockFence;
        boolean southwestFence = southwest && Block.blocksList[world.getBlockId(x - 1, y, z + 1)] != null && Block.blocksList[world.getBlockId(x - 1, y, z + 1)] instanceof BlockFence;

        double bottomY = (double) y;
        double topY = (double) y + 1.0D;
        double minX = (double) x;
        double maxX = (double) x + 1.0D;
        double centerX = (double) x + 0.5D;
        double minZ = (double) z;
        double maxZ = (double) z + 1.0D;
        double centerZ = (double) z + 0.5D;
        double texU1 = (double) texture.getInterpolatedU(0.0D);
        double texU2 = (double) texture.getInterpolatedU(8.0D);
        double texU3 = (double) texture.getInterpolatedU(16.0D);
        double texV1 = (double) texture.getInterpolatedV(0.0D);
        double texV3 = (double) texture.getInterpolatedV(16.0D);

        // Render hanging/standing barbed wire
        // Boolean to determine if a part of the barbed wire has rendered yet
        boolean rendered = false;

        if (north && south) {
            // Render from north to south on both sides
            tess.addVertexWithUV(centerX, topY, minZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, maxZ, texU1, texV1);
            tess.addVertexWithUV(centerX, bottomY, maxZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, minZ, texU3, texV3);

            tess.addVertexWithUV(centerX, topY, maxZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, minZ, texU1, texV1);
            tess.addVertexWithUV(centerX, bottomY, minZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, maxZ, texU3, texV3);

            rendered = true;
        }
        else if (north) {
            // Render from north to the middle of the block on both sides
            tess.addVertexWithUV(centerX, topY, minZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);
            tess.addVertexWithUV(centerX, bottomY, minZ, texU3, texV3);

            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX, topY, minZ, texU1, texV1);
            tess.addVertexWithUV(centerX, bottomY, minZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);

            rendered = true;
        }
        else if (south) {
            // Render from the middle of the block to south on both sides
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX, topY, maxZ, texU1, texV1);
            tess.addVertexWithUV(centerX, bottomY, maxZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);

            tess.addVertexWithUV(centerX, topY, maxZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);
            tess.addVertexWithUV(centerX, bottomY, maxZ, texU3, texV3);

            rendered = true;
        }

        if (east && west) {
            // Render from east to west on both sides
            tess.addVertexWithUV(minX, topY, centerZ, texU3, texV1);
            tess.addVertexWithUV(maxX, topY, centerZ, texU1, texV1);
            tess.addVertexWithUV(maxX, bottomY, centerZ, texU1, texV3);
            tess.addVertexWithUV(minX, bottomY, centerZ, texU3, texV3);

            tess.addVertexWithUV(maxX, topY, centerZ, texU3, texV1);
            tess.addVertexWithUV(minX, topY, centerZ, texU1, texV1);
            tess.addVertexWithUV(minX, bottomY, centerZ, texU1, texV3);
            tess.addVertexWithUV(maxX, bottomY, centerZ, texU3, texV3);

            rendered = true;
        }
        else if (east) {
            // Render from east to the middle of the block on both sides
            tess.addVertexWithUV(maxX, topY, centerZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);
            tess.addVertexWithUV(maxX, bottomY, centerZ, texU3, texV3);

            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(maxX, topY, centerZ, texU1, texV1);
            tess.addVertexWithUV(maxX, bottomY, centerZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);

            rendered = true;
        }
        else if (west) {
            // Render from the middle of the block to west on both sides
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(minX, topY, centerZ, texU1, texV1);
            tess.addVertexWithUV(minX, bottomY, centerZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);

            tess.addVertexWithUV(minX, topY, centerZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);
            tess.addVertexWithUV(minX, bottomY, centerZ, texU3, texV3);

            rendered = true;
        }

        if (northeast && southwest) {
            // Render from the northeast corner to the southwest corner on both sides
            tess.addVertexWithUV(maxX, topY, minZ, texU3, texV1);
            tess.addVertexWithUV(minX, topY, maxZ, texU1, texV1);
            tess.addVertexWithUV(minX, bottomY, maxZ, texU1, texV3);
            tess.addVertexWithUV(maxX, bottomY, minZ, texU3, texV3);

            tess.addVertexWithUV(minX, topY, maxZ, texU3, texV1);
            tess.addVertexWithUV(maxX, topY, minZ, texU1, texV1);
            tess.addVertexWithUV(maxX, bottomY, minZ, texU1, texV3);
            tess.addVertexWithUV(minX, bottomY, maxZ, texU3, texV3);

            rendered = true;
        }
        else if (northeast) {
            // Render from the northeast corner to the middle of the block on both sides
            tess.addVertexWithUV(maxX, topY, minZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);
            tess.addVertexWithUV(maxX, bottomY, minZ, texU3, texV3);

            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(maxX, topY, minZ, texU1, texV1);
            tess.addVertexWithUV(maxX, bottomY, minZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);

            rendered = true;
        }
        else if (southwest) {
            // Render from the middle of the block to the southwest corner on both sides
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(minX, topY, maxZ, texU1, texV1);
            tess.addVertexWithUV(minX, bottomY, maxZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);

            tess.addVertexWithUV(minX, topY, maxZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);
            tess.addVertexWithUV(minX, bottomY, maxZ, texU3, texV3);

            rendered = true;
        }

        if (northwest && southeast) {
            // Render from the northeast corner to the southwest corner on both sides
            tess.addVertexWithUV(minX, topY, minZ, texU3, texV1);
            tess.addVertexWithUV(maxX, topY, maxZ, texU1, texV1);
            tess.addVertexWithUV(maxX, bottomY, maxZ, texU1, texV3);
            tess.addVertexWithUV(minX, bottomY, minZ, texU3, texV3);

            tess.addVertexWithUV(maxX, topY, maxZ, texU3, texV1);
            tess.addVertexWithUV(minX, topY, minZ, texU1, texV1);
            tess.addVertexWithUV(minX, bottomY, minZ, texU1, texV3);
            tess.addVertexWithUV(maxX, bottomY, maxZ, texU3, texV3);

            rendered = true;
        }
        else if (northwest) {
            // Render from the northeast corner to the middle of the block on both sides
            tess.addVertexWithUV(minX, topY, minZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);
            tess.addVertexWithUV(minX, bottomY, minZ, texU3, texV3);

            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(minX, topY, minZ, texU1, texV1);
            tess.addVertexWithUV(minX, bottomY, minZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);

            rendered = true;
        }
        else if (southeast) {
            // Render from the middle of the block to the southwest corner on both sides
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(maxX, topY, maxZ, texU1, texV1);
            tess.addVertexWithUV(maxX, bottomY, maxZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);

            tess.addVertexWithUV(maxX, topY, maxZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX, bottomY, centerZ, texU2, texV3);
            tess.addVertexWithUV(maxX, bottomY, maxZ, texU3, texV3);

            rendered = true;
        }

        // Apply fence corrections
        if (northFence) {
            tess.addVertexWithUV(centerX, topY, centerZ - 1.0D, texU2, texV1);
            tess.addVertexWithUV(centerX, topY, maxZ - 1.0D, texU1, texV1);
            tess.addVertexWithUV(centerX, bottomY, maxZ - 1.0D, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, centerZ - 1.0D, texU2, texV3);

            tess.addVertexWithUV(centerX, topY, maxZ - 1.0D, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, centerZ - 1.0D, texU2, texV1);
            tess.addVertexWithUV(centerX, bottomY, centerZ - 1.0D, texU2, texV3);
            tess.addVertexWithUV(centerX, bottomY, maxZ - 1.0D, texU3, texV3);

            rendered = true;
        }
        if (southFence) {
            tess.addVertexWithUV(centerX, topY, minZ + 1.0D, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, centerZ + 1.0D, texU2, texV1);
            tess.addVertexWithUV(centerX, bottomY, centerZ + 1.0D, texU2, texV3);
            tess.addVertexWithUV(centerX, bottomY, minZ + 1.0D, texU3, texV3);

            tess.addVertexWithUV(centerX, topY, centerZ + 1.0D, texU2, texV1);
            tess.addVertexWithUV(centerX, topY, minZ + 1.0D, texU1, texV1);
            tess.addVertexWithUV(centerX, bottomY, minZ + 1.0D, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, centerZ + 1.0D, texU2, texV3);

            rendered = true;
        }
        if (eastFence) {
            tess.addVertexWithUV(centerX + 1.0D, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(minX + 1.0D, topY, centerZ, texU1, texV1);
            tess.addVertexWithUV(minX + 1.0D, bottomY, centerZ, texU1, texV3);
            tess.addVertexWithUV(centerX + 1.0D, bottomY, centerZ, texU2, texV3);

            tess.addVertexWithUV(minX + 1.0D, topY, centerZ, texU3, texV1);
            tess.addVertexWithUV(centerX + 1.0D, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX + 1.0D, bottomY, centerZ, texU2, texV3);
            tess.addVertexWithUV(minX + 1.0D, bottomY, centerZ, texU3, texV3);

            rendered = true;
        }
        if (westFence) {
            tess.addVertexWithUV(maxX - 1.0D, topY, centerZ, texU3, texV1);
            tess.addVertexWithUV(centerX - 1.0D, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(centerX - 1.0D, bottomY, centerZ, texU2, texV3);
            tess.addVertexWithUV(maxX - 1.0D, bottomY, centerZ, texU3, texV3);

            tess.addVertexWithUV(centerX - 1.0D, topY, centerZ, texU2, texV1);
            tess.addVertexWithUV(maxX - 1.0D, topY, centerZ, texU1, texV1);
            tess.addVertexWithUV(maxX - 1.0D, bottomY, centerZ, texU1, texV3);
            tess.addVertexWithUV(centerX - 1.0D, bottomY, centerZ, texU2, texV3);

            rendered = true;
        }

        if (northwestFence) {
            tess.addVertexWithUV(centerX - 1.0D, topY, centerZ - 1.0D, texU2, texV1);
            tess.addVertexWithUV(maxX - 1.0D, topY, maxZ - 1.0D, texU1, texV1);
            tess.addVertexWithUV(maxX - 1.0D, bottomY, maxZ - 1.0D, texU1, texV3);
            tess.addVertexWithUV(centerX - 1.0D, bottomY, centerZ - 1.0D, texU2, texV3);

            tess.addVertexWithUV(maxX - 1.0D, topY, maxZ - 1.0D, texU3, texV1);
            tess.addVertexWithUV(centerX - 1.0D, topY, centerZ - 1.0D, texU2, texV1);
            tess.addVertexWithUV(centerX - 1.0D, bottomY, centerZ - 1.0D, texU2, texV3);
            tess.addVertexWithUV(maxX - 1.0D, bottomY, maxZ - 1.0D, texU3, texV3);

            rendered = true;
        }
        if (northeastFence) {
            tess.addVertexWithUV(centerX + 1.0D, topY, centerZ - 1.0D, texU2, texV1);
            tess.addVertexWithUV(minX + 1.0D, topY, maxZ - 1.0D, texU1, texV1);
            tess.addVertexWithUV(minX + 1.0D, bottomY, maxZ - 1.0D, texU1, texV3);
            tess.addVertexWithUV(centerX + 1.0D, bottomY, centerZ - 1.0D, texU2, texV3);

            tess.addVertexWithUV(minX + 1.0D, topY, maxZ - 1.0D, texU3, texV1);
            tess.addVertexWithUV(centerX + 1.0D, topY, centerZ - 1.0D, texU2, texV1);
            tess.addVertexWithUV(centerX + 1.0D, bottomY, centerZ - 1.0D, texU2, texV3);
            tess.addVertexWithUV(minX + 1.0D, bottomY, maxZ - 1.0D, texU3, texV3);

            rendered = true;
        }
        if (southeastFence) {
            tess.addVertexWithUV(minX + 1.0D, topY, minZ + 1.0D, texU3, texV1);
            tess.addVertexWithUV(centerX + 1.0D, topY, centerZ + 1.0D, texU2, texV1);
            tess.addVertexWithUV(centerX + 1.0D, bottomY, centerZ + 1.0D, texU2, texV3);
            tess.addVertexWithUV(minX + 1.0D, bottomY, minZ + 1.0D, texU3, texV3);

            tess.addVertexWithUV(centerX + 1.0D, topY, centerZ + 1.0D, texU2, texV1);
            tess.addVertexWithUV(minX + 1.0D, topY, minZ + 1.0D, texU1, texV1);
            tess.addVertexWithUV(minX + 1.0D, bottomY, minZ + 1.0D, texU1, texV3);
            tess.addVertexWithUV(centerX + 1.0D, bottomY, centerZ + 1.0D, texU2, texV3);

            rendered = true;
        }
        if (southwestFence) {
            tess.addVertexWithUV(maxX - 1.0D, topY, minZ + 1.0D, texU3, texV1);
            tess.addVertexWithUV(centerX - 1.0D, topY, centerZ + 1.0D, texU2, texV1);
            tess.addVertexWithUV(centerX - 1.0D, bottomY, centerZ + 1.0D, texU2, texV3);
            tess.addVertexWithUV(maxX - 1.0D, bottomY, minZ + 1.0D, texU3, texV3);

            tess.addVertexWithUV(centerX - 1.0D, topY, centerZ + 1.0D, texU2, texV1);
            tess.addVertexWithUV(maxX - 1.0D, topY, minZ + 1.0D, texU1, texV1);
            tess.addVertexWithUV(maxX - 1.0D, bottomY, minZ + 1.0D, texU1, texV3);
            tess.addVertexWithUV(centerX - 1.0D, bottomY, centerZ + 1.0D, texU2, texV3);

            rendered = true;
        }

        if (!rendered) {
            // Render the standard square for panes if there are no connections
            tess.addVertexWithUV(centerX, topY, minZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, maxZ, texU1, texV1);
            tess.addVertexWithUV(centerX, bottomY, maxZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, minZ, texU3, texV3);

            tess.addVertexWithUV(centerX, topY, maxZ, texU3, texV1);
            tess.addVertexWithUV(centerX, topY, minZ, texU1, texV1);
            tess.addVertexWithUV(centerX, bottomY, minZ, texU1, texV3);
            tess.addVertexWithUV(centerX, bottomY, maxZ, texU3, texV3);

            tess.addVertexWithUV(minX, topY, centerZ, texU3, texV1);
            tess.addVertexWithUV(maxX, topY, centerZ, texU1, texV1);
            tess.addVertexWithUV(maxX, bottomY, centerZ, texU1, texV3);
            tess.addVertexWithUV(minX, bottomY, centerZ, texU3, texV3);

            tess.addVertexWithUV(maxX, topY, centerZ, texU3, texV1);
            tess.addVertexWithUV(minX, topY, centerZ, texU1, texV1);
            tess.addVertexWithUV(minX, bottomY, centerZ, texU1, texV3);
            tess.addVertexWithUV(maxX, bottomY, centerZ, texU3, texV3);
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return false;
    }

    @Override
    public int getRenderId() {
        return this.renderId;
    }

}
