package de.aelpecyem.elementaristics.common.item;

import de.aelpecyem.elementaristics.lib.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.common.base.PatchouliSounds;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.common.book.BookRegistry;

public class ItemLiberElementium extends Item {
    public ItemLiberElementium() {
        super(new Settings().group(Constants.ELEMENTARISTICS_GROUP).maxCount(1)); //some way of displaying the current strength (via HUD?)
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return isRiteMode(stack) ? "item.elementaristics.liber_elementium.rite" : super.getTranslationKey(stack);
    }

    @Override
    public Text getName(ItemStack stack) {
        boolean isRite = isRiteMode(stack);
        return new TranslatableText(this.getTranslationKey(stack)).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(isRite ? Constants.Colors.MAGAN_COLOR : 0xFFFFFF)).withBold(isRite));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (player.isSneaking()){
            setRiteMode(player.getStackInHand(hand), !isRiteMode(player.getStackInHand(hand)));
            return new TypedActionResult(ActionResult.PASS, player.getStackInHand(hand));
        }
        if (isRiteMode(player.getStackInHand(hand)))
            player.setCurrentHand(hand);
        else{
            Book book = BookRegistry.INSTANCE.books.get(new Identifier(Constants.MOD_ID, "liber_elementium"));
            if (player instanceof ServerPlayerEntity) {
                PatchouliAPI.instance.openBookGUI((ServerPlayerEntity)player, book.id);
                SoundEvent sfx = PatchouliSounds.getSound(book.openSound, PatchouliSounds.book_open);
                player.playSound(sfx, 1.0F, (float)(0.7D + Math.random() * 0.4D));
            }
        }
        return new TypedActionResult(ActionResult.SUCCESS, player.getStackInHand(hand));
    }

    public static boolean isRiteMode(ItemStack stack){
        return stack.hasTag() && stack.getTag().getBoolean(Constants.NBTTags.RITE_MODE);
    }

    public static void setRiteMode(ItemStack stack, boolean riteMode){
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        stack.getTag().putBoolean(Constants.NBTTags.RITE_MODE, riteMode);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 140;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity) ((PlayerEntity) user).getItemCooldownManager().set(this, 20);

        return super.finishUsing(stack, world, user);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity) ((PlayerEntity) user).getItemCooldownManager().set(this, 20);
//spawn stuff
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }
}
