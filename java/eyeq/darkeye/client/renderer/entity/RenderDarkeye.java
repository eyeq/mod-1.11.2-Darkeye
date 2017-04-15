package eyeq.darkeye.client.renderer.entity;

import eyeq.darkeye.client.model.ModelDarkeye;
import eyeq.util.client.renderer.EntityRenderResourceLocation;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import static eyeq.darkeye.Darkeye.MOD_ID;

public class RenderDarkeye extends RenderLiving {
    protected static final ResourceLocation textures = new EntityRenderResourceLocation(MOD_ID, "darkeye");

    public RenderDarkeye(RenderManager renderManager) {
        super(renderManager, new ModelDarkeye(), 0.4F);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return textures;
    }
}
