package net.adventurez.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.At;

import net.adventurez.init.ItemInit;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {

    public SmithingScreenHandlerMixin(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory,
            ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "updateResult", at = @At("HEAD"), cancellable = true)
    private void updateResultMixin(CallbackInfo info) {
        if (FabricLoader.getInstance().isModLoaded("patchouli")) {
            ItemStack itemStack = this.input.getStack(0);
            ItemStack itemStack2 = this.input.getStack(1);
            if (itemStack.getItem() == Items.BOOK && itemStack2.getItem() == ItemInit.ORC_SKIN) {
                Item item = (Item) Registry.ITEM.get(new Identifier("patchouli:guide_book"));
                ItemStack itemStack3 = new ItemStack(item);
                CompoundTag tag = new CompoundTag();
                tag.putString("patchouli:book", "adventurez:adventurez");
                itemStack3.setTag(tag);
                this.output.setStack(0, itemStack3);
                info.cancel();
            }
        }
    }

    @Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
    public void canTakeOutputMixin(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> info) {
        if (FabricLoader.getInstance().isModLoaded("patchouli")) {
            ItemStack itemStack = this.input.getStack(0);
            ItemStack itemStack2 = this.input.getStack(1);
            if (itemStack.getItem() == Items.BOOK && itemStack2.getItem() == ItemInit.ORC_SKIN) {
                info.setReturnValue(true);
            }
        }
    }

}