package org.thecelestialworkshop.celestisynth.common.block;

import org.thecelestialworkshop.celestisynth.client.gui.starlitfactory.StarlitFactoryMenu;
import org.thecelestialworkshop.celestisynth.common.network.s2c.BlockEntitySetSlotPacket;
import org.thecelestialworkshop.celestisynth.common.recipe.StarlitFactoryRecipe;
import org.thecelestialworkshop.celestisynth.common.registry.CSBlockEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSRecipeTypes;
import org.thecelestialworkshop.celestisynth.manager.CSNetworkManager;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.inventory.StackedContentsCompatible;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.stream.IntStream;

public class StarlitFactoryBlockEntity extends BaseContainerBlockEntity implements WorldlyContainer, RecipeHolder, StackedContentsCompatible, GeoBlockEntity {
    private final RecipeType<StarlitFactoryRecipe> recipeType;
    protected NonNullList<ItemStack> containedItems = NonNullList.withSize(8, ItemStack.EMPTY);
    int energyBurnTime;
    int energyAmount;
    int factoryForgeTime;
    int maxFactoryForgeTime;
    int isHoldingValidRecipe;
    int isResultSlotFilled;
    public final ContainerData dataAccess = new ContainerData() {
        public int get(int pIndex) {
            return switch (pIndex) {
                case 0 -> StarlitFactoryBlockEntity.this.energyBurnTime;
                case 1 -> StarlitFactoryBlockEntity.this.energyAmount;
                case 2 -> StarlitFactoryBlockEntity.this.factoryForgeTime;
                case 3 -> StarlitFactoryBlockEntity.this.maxFactoryForgeTime;
                case 4 -> StarlitFactoryBlockEntity.this.isHoldingValidRecipe;
                case 5 -> StarlitFactoryBlockEntity.this.isResultSlotFilled;
                default -> 0;
            };
        }
        public void set(int pIndex, int pValue) {
            switch (pIndex) {
                case 0: StarlitFactoryBlockEntity.this.energyBurnTime = pValue; break;
                case 1: StarlitFactoryBlockEntity.this.energyAmount = pValue; break;
                case 2: StarlitFactoryBlockEntity.this.factoryForgeTime = pValue; break;
                case 3: StarlitFactoryBlockEntity.this.maxFactoryForgeTime = pValue; break;
                case 4: StarlitFactoryBlockEntity.this.isHoldingValidRecipe = pValue; break;
                case 5: StarlitFactoryBlockEntity.this.isResultSlotFilled = pValue; break;
            }
        }
        public int getCount() {
            return 6;
        }
    };
    private final Object2IntOpenHashMap<ResourceLocation> recipesUsed = new Object2IntOpenHashMap<>();
    private final RecipeManager.CachedCheck<Container, StarlitFactoryRecipe> quickCheck;

    int tickCount;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public StarlitFactoryBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CSBlockEntityTypes.STARLIT_FACTORY_TILE.get(), pPos, pBlockState);
        this.recipeType = CSRecipeTypes.STARLIT_FACTORY_TYPE.get();
        this.quickCheck = RecipeManager.createCheck(this.recipeType);
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "starlit_controller", 5, state -> PlayState.STOP)
                //.triggerableAnim("idle", RawAnimation.begin().thenPlay("animation.starlit_factory.idle"))
                .triggerableAnim("forging", RawAnimation.begin().thenPlay("animation.starlit_factory.forging"))
        );
//        controllers.add(new AnimationController<>(this, 5, (state) -> {
//            if (state.getAnimatable().factoryForgeTime > 0) {
//                return state.setAndContinue(RawAnimation.begin().thenLoop("animation.starlit_factory.forging"));
//            }
//            return state.setAndContinue(RawAnimation.begin().thenLoop("animation.starlit_factory.idle"));
//        }));
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.celestisynth.starlit_factory");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return new StarlitFactoryMenu(pContainerId, pInventory, this, this.dataAccess);
    }

    public static Map<Item, Integer> getFuelMap() {
        Map<Item, Integer> map = Maps.newLinkedHashMap();
        map.put(Items.AMETHYST_SHARD, 5);
        map.put(CSItems.CELESTIAL_CORE.get(), 105);
        map.put(CSItems.CELESTIAL_CORE_HEATED.get(), 120);
        map.put(CSItems.CELESTIAL_DEBUGGER.get(), 1000);
        return map;
    }

    public void playSound(Level pLevel, BlockPos pPos, SoundEvent sound, float volume, float pitch) {
        pLevel.playSound(null, pPos, sound, SoundSource.BLOCKS, volume, pitch);
    }

    public void serverTick(Level pLevel, BlockPos pPos, BlockState pState) {
        ++tickCount;
        if (containedItems.get(StarlitFactoryMenu.RESULT_SLOT).isEmpty()) {
            CSNetworkManager.sendToAll(new BlockEntitySetSlotPacket(getBlockPos(), StarlitFactoryMenu.RESULT_SLOT, ItemStack.EMPTY));
        } else {
            CSNetworkManager.sendToAll(new BlockEntitySetSlotPacket(getBlockPos(), StarlitFactoryMenu.RESULT_SLOT, containedItems.get(StarlitFactoryMenu.RESULT_SLOT)));
        }

        ItemStack fuel = containedItems.get(StarlitFactoryMenu.FUEL_SLOT);

        if (this.energyAmount < 1000) {
            if (getFuelMap().containsKey(fuel.getItem())) {
                int value = getFuelMap().get(fuel.getItem());
                if (this.energyBurnTime < 100) {
                    this.energyBurnTime++;
                } else if (this.energyBurnTime == 100) {
                    this.addEnergy(value);
                    fuel.shrink(1);
                    this.playSound(pLevel, pPos, SoundEvents.BEACON_ACTIVATE, 1.0F, 1.2F);
                    this.energyBurnTime = 0;
                }
            } else {
                this.energyBurnTime = 0;
            }
        }

        StarlitFactoryRecipe recipe = quickCheck.getRecipeFor(this, pLevel).orElse(null);

        if (recipe == null) {
            this.isHoldingValidRecipe = 0;
            this.factoryForgeTime = 0;
            return;
        }

        if (this.isRecipeAvailableToForge(pLevel.registryAccess(), recipe)) {
            this.isHoldingValidRecipe = 1;
            if (this.isEnergyAvailable()) {
                if (factoryForgeTime < recipe.getForgeTime()) {
                    if (factoryForgeTime % 2 == 0) {
                        this.playSound(pLevel, pPos, SoundEvents.GILDED_BLACKSTONE_PLACE, 1.0F, 0.5F);
                    }
                    if (factoryForgeTime % 20 == 0) {
                        this.playSound(pLevel, pPos, SoundEvents.LAVA_POP, 1.0F, (float) (0.8 + pLevel.random.nextGaussian()));
                    }
                    ParticleUtil.sendParticle(pLevel, CSParticleTypes.RAINFALL_ENERGY_SMALL.get(), Vec3.atCenterOf(pPos), Vec3.ZERO.add(0, 1, 0));
                    ++factoryForgeTime;
                    this.addEnergy(-1);
                    this.triggerAnim("starlit_controller", "forging");
                } else if (factoryForgeTime == recipe.getForgeTime()) {
                    this.factoryForgeTime = 0;
                    this.playSound(pLevel, pPos, SoundEvents.ENCHANTMENT_TABLE_USE, 1.2F, 0.85F);
                    if (this.finishForging(pLevel.registryAccess(), recipe)) {
                        for (int i = 0; i < 45; i++) {
                            ParticleUtil.sendParticle(pLevel, CSParticleTypes.RAINFALL_ENERGY_SMALL.get(), Vec3.atCenterOf(pPos).add(0, 0.5, 0), Vec3.ZERO.add(Mth.sin(i) * 0.4, 0, Mth.cos(i) * 0.4));
                        }
                        this.setRecipeUsed(recipe);
                    }
                }
            } else {
                this.factoryForgeTime = 0;
            }
        } else {
            this.isHoldingValidRecipe = 0;
            this.factoryForgeTime = 0;
        }
    }

    public boolean isRecipeAvailableToForge(RegistryAccess access, StarlitFactoryRecipe recipe) {
        if (recipe != null) {
            ItemStack recipeStack = recipe.assemble(this, access);
            if (recipeStack.isEmpty()) {
                return false;
            } else {
                ItemStack result = containedItems.get(StarlitFactoryMenu.RESULT_SLOT);
                this.maxFactoryForgeTime = recipe.getForgeTime();
                if (result.isEmpty()) {
                    return true;
                } else if (!ItemStack.isSameItem(result, recipeStack)) {
                    return false;
                } else if (result.getCount() + recipeStack.getCount() <= this.getMaxStackSize() && result.getCount() + recipeStack.getCount() <= result.getMaxStackSize()) {
                    return true;
                } else {
                    return result.getCount() + recipeStack.getCount() <= recipeStack.getMaxStackSize();
                }
            }
        }
        return false;
    }

    public boolean finishForging(RegistryAccess access, StarlitFactoryRecipe recipe) {
        if (recipe != null && this.isRecipeAvailableToForge(access, recipe)) {
            ItemStack resultStack = containedItems.get(StarlitFactoryMenu.RESULT_SLOT);
            ItemStack formedResultItem = recipe.assemble(this, access);
            if (resultStack.isEmpty()) {
                this.setItem(StarlitFactoryMenu.RESULT_SLOT, formedResultItem.copy());
            } else if (resultStack.is(formedResultItem.getItem())) {
                resultStack.grow(formedResultItem.getCount());
            }

            for (ItemStack ingredientStack : containedItems) {
                if (ingredientStack != containedItems.get(StarlitFactoryMenu.RESULT_SLOT) && ingredientStack != containedItems.get(StarlitFactoryMenu.FUEL_SLOT)) {
                    ingredientStack.shrink(1);
                }
            }
            return true;
        }
        return false;
    }

    public void addEnergy(int value) {
        this.energyAmount = Mth.clamp(this.energyAmount + value, 0, 1000);
    }

    private boolean isEnergyAvailable() {
        return this.energyAmount > 0;
    }

    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.containedItems = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(pTag, this.containedItems);
        this.factoryForgeTime = pTag.getInt("factoryForgeTime");
        this.energyAmount = pTag.getInt("energyAmount");
        this.energyBurnTime = pTag.getInt("energyBurnTime");
        this.maxFactoryForgeTime = pTag.getInt("maxFactoryForgeTime");
        CompoundTag compoundtag = pTag.getCompound("RecipesUsed");
        for(String s : compoundtag.getAllKeys()) {
            this.recipesUsed.put(new ResourceLocation(s), compoundtag.getInt(s));
        }
    }

    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        ContainerHelper.saveAllItems(pTag, this.containedItems);
        pTag.putInt("factoryForgeTime", this.energyBurnTime);
        pTag.putInt("energyAmount", this.energyAmount);
        pTag.putInt("energyBurnTime", this.energyBurnTime);
        pTag.putInt("maxFactoryForgeTime", this.maxFactoryForgeTime);
        CompoundTag compoundtag = new CompoundTag();
        this.recipesUsed.forEach((resLoc, val) -> compoundtag.putInt(resLoc.toString(), val));
        pTag.put("RecipesUsed", compoundtag);
    }

    @Override
    public int getContainerSize() {
        return this.containedItems.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.containedItems) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int pSlot) {
        return this.containedItems.get(pSlot);
    }

    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        ItemStack flag = ContainerHelper.removeItem(this.containedItems, pSlot, pAmount);
        this.update(pAmount);
        return flag;
    }

    public void update(int pAmount) {
        this.setChanged();
        if (this.getLevel() != null) {
            this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), pAmount);
        }

    }

    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        ItemStack flag = ContainerHelper.takeItem(this.containedItems, pSlot);
        return flag;
    }

    @Override
    public void setItem(int pSlot, ItemStack pStack) {
        ItemStack itemstack = this.containedItems.get(pSlot);
        boolean flag = !pStack.isEmpty() && ItemStack.isSameItemSameTags(itemstack, pStack);
        this.containedItems.set(pSlot, pStack);
        if (pStack.getCount() > this.getMaxStackSize()) {
            pStack.setCount(this.getMaxStackSize());
        }
        if (!flag) {
            this.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this, pPlayer);
    }

    @Override
    public void clearContent() {
        this.containedItems.clear();
    }

    @Override
    public void setRecipeUsed(@Nullable Recipe<?> pRecipe) {
        if (pRecipe != null) {
            ResourceLocation resourcelocation = pRecipe.getId();
            this.recipesUsed.addTo(resourcelocation, 1);
        }
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }

    @Override
    public void fillStackedContents(StackedContents pContents) {
        for(ItemStack itemstack : this.containedItems) {
            pContents.accountStack(itemstack);
        }
    }

    @Override
    public int[] getSlotsForFace(Direction pSide) {
        if (pSide == Direction.DOWN) {
            return new int[]{StarlitFactoryMenu.FUEL_SLOT};
        } else {
            return pSide == Direction.UP ? new int[]{StarlitFactoryMenu.RESULT_SLOT} : IntStream.range(StarlitFactoryMenu.BASE_MATERIAL_SLOT, StarlitFactoryMenu.SUPPORTING_MATERIAL_SLOT_2).toArray();
        }
    }

    @Override
    public boolean canPlaceItemThroughFace(int pIndex, ItemStack pItemStack, @Nullable Direction pDirection) {
        return pIndex != StarlitFactoryMenu.RESULT_SLOT;
    }

    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return true;
    }

    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }
}
