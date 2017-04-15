package eyeq.darkeye;

import eyeq.util.block.BlockUtils;
import eyeq.util.block.state.pattern.BlockPatternFactory;
import eyeq.util.block.state.pattern.UBlockPattern;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.USoundCreator;
import eyeq.util.client.resource.gson.SoundResourceManager;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import eyeq.util.common.registry.UEntityRegistry;
import eyeq.util.common.registry.USoundEventRegistry;
import eyeq.util.world.biome.BiomeUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import eyeq.darkeye.entity.monster.EntityDarkeye;
import eyeq.darkeye.client.renderer.entity.RenderDarkeye;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.List;

import static eyeq.darkeye.Darkeye.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
public class Darkeye {
    public static final String MOD_ID = "eyeq_darkeye";

    @Mod.Instance(MOD_ID)
    public static Darkeye instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static SoundEvent entityDarkeyeAmbient;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        registerEntities();
        registerSoundEvents();
        if(event.getSide().isServer()) {
            return;
        }
        registerEntityRenderings();
        createFiles();
    }

    public static void registerEntities() {
        UEntityRegistry.registerModEntity(resource, EntityDarkeye.class, "Darkeye", 0, instance, 0xFFA7BB, 0x334B55);
        List<Biome> biomes = BiomeUtils.getSpawnBiomes(EntitySlime.class, EnumCreatureType.MONSTER);
        EntityRegistry.addSpawn(EntityDarkeye.class, 1, 5, 20, EnumCreatureType.MONSTER);
        BlockUtils.SUMMON_ENTITY_MANAGER.register(new UBlockPattern(BlockPatternFactory.create(new String[]{"XY", "AY"},
                'X', Blocks.TNT, 'Y', Blocks.SLIME_BLOCK, 'A', Material.AIR), Blocks.TNT),
                EntityDarkeye::new, EnumParticleTypes.SLIME);
    }

    public static void registerSoundEvents() {
        entityDarkeyeAmbient = new SoundEvent(resource.createResourceLocation("darkeye"));

        USoundEventRegistry.registry(entityDarkeyeAmbient);
    }

    @SideOnly(Side.CLIENT)
    public static void registerEntityRenderings() {
        RenderingRegistry.registerEntityRenderingHandler(EntityDarkeye.class, RenderDarkeye::new);
    }

    public static void createFiles() {
        File project = new File("../1.11.2-Darkeye");

        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, EntityDarkeye.class, "Darkeye");
        language.register(LanguageResourceManager.JA_JP, EntityDarkeye.class, "ダークアイ");

        ULanguageCreator.createLanguage(project, MOD_ID, language);

        SoundResourceManager sound = new SoundResourceManager();

        sound.register(entityDarkeyeAmbient, SoundCategory.HOSTILE.getName());

        USoundCreator.createSoundJson(project, MOD_ID, sound);
    }
}
