package com.nxclient.modules.render;

import com.nxclient.modules.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

import java.util.HashSet;
import java.util.Set;

public class XRay extends Module {

    public static boolean active = false;

    private static final Set<Block> ORES = new HashSet<>();
    static {
        ORES.add(Blocks.DIAMOND_ORE);
        ORES.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        ORES.add(Blocks.GOLD_ORE);
        ORES.add(Blocks.DEEPSLATE_GOLD_ORE);
        ORES.add(Blocks.IRON_ORE);
        ORES.add(Blocks.DEEPSLATE_IRON_ORE);
        ORES.add(Blocks.COAL_ORE);
        ORES.add(Blocks.DEEPSLATE_COAL_ORE);
        ORES.add(Blocks.COPPER_ORE);
        ORES.add(Blocks.DEEPSLATE_COPPER_ORE);
        ORES.add(Blocks.EMERALD_ORE);
        ORES.add(Blocks.DEEPSLATE_EMERALD_ORE);
        ORES.add(Blocks.REDSTONE_ORE);
        ORES.add(Blocks.DEEPSLATE_REDSTONE_ORE);
        ORES.add(Blocks.LAPIS_ORE);
        ORES.add(Blocks.DEEPSLATE_LAPIS_ORE);
        ORES.add(Blocks.NETHER_QUARTZ_ORE);
        ORES.add(Blocks.NETHER_GOLD_ORE);
        ORES.add(Blocks.ANCIENT_DEBRIS);
        ORES.add(Blocks.SPAWNER);
        ORES.add(Blocks.BUDDING_AMETHYST);
        ORES.add(Blocks.AMETHYST_CLUSTER);
    }

    public XRay() {
        super("XRay", "Hides non-ore blocks so ores show through walls.", Category.RENDER);
    }

    public static boolean isOre(Block block) {
        return ORES.contains(block);
    }

    @Override
    public void onEnable() {
        active = true;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.worldRenderer != null) {
            client.worldRenderer.reload();
        }
    }

    @Override
    public void onDisable() {
        active = false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.worldRenderer != null) {
            client.worldRenderer.reload();
        }
    }
}
