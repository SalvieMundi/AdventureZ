package net.adventurez.entity.render;

//import net.adventurez.entity.BlazeGuardianEntity;
//import net.adventurez.entity.model.BlazeGuardianModel;
import net.adventurez.init.RenderInit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

//@Environment(EnvType.CLIENT)
//public class BlazeGuardianRenderer extends MobEntityRenderer<BlazeGuardianEntity, BlazeGuardianModel<BlazeGuardianEntity>> {
//    private static final Identifier TEXTURE = new Identifier("adventurez:textures/entity/blaze_guardian.png");
//
//    public BlazeGuardianRenderer(EntityRendererFactory.Context context) {
//        super(context, new BlazeGuardianModel<>(context.getPart(RenderInit.BLAZE_GUARDIAN_LAYER)), 0.7F);
//    }
//
//    @Override
//    public void scale(BlazeGuardianEntity blazeGuardianEntity, MatrixStack matrixStack, float f) {
//        matrixStack.scale(1.2F, 1.2F, 1.2F);
//    }
//
//    @Override
//    public Identifier getTexture(BlazeGuardianEntity blazeGuardianEntity) {
//        return TEXTURE;
//    }
//}
