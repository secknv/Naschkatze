package net.sknv.nkmod.blocks.machines.base;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.tterrag.registrate.builders.ContainerBuilder;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.sknv.nkmod.Naschkatze;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Creates a ContainerScreen for the specified Container type.<br>
 * All you have to do is pass the texture location -> {@link MachineScreenFactory#MachineScreenFactory(String textureLocation)}.<br>
 * @param <C> The Container type this screen will be for.
 */
public class MachineScreenFactory<C extends AbstractMachineContainer> implements ContainerBuilder.ScreenFactory<C, ContainerScreen<C>> {

    private final String textureLocation;

    /**
     * Creates the ScreenFactory for the specific texture.<br>
     * Ex: {@code "textures/gui/grinder_gui.png"}
     * @param textureLocation The texture for your Screen.
     */
    public MachineScreenFactory(String textureLocation) {
        this.textureLocation = textureLocation;
    }

    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ContainerScreen<C> create(C p_create_1_, PlayerInventory p_create_2_, ITextComponent p_create_3_) {
        return new ContainerScreen<C>(p_create_1_, p_create_2_, p_create_3_) {

            private final ResourceLocation GUI = new ResourceLocation(Naschkatze.MODID, textureLocation);

            @Override
            public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
                this.renderBackground(matrixStack);
                super.render(matrixStack, mouseX, mouseY, partialTicks);
                this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
            }

            @Override
            protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
            }

            @Override
            protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
                RenderSystem.color4f(1f, 1f, 1f, 1f);
                // todo: Should I worry about this NPE warning?
                this.minecraft.getTextureManager().bindTexture(GUI);
                int relX = (this.width - this.xSize) / 2;
                int relY = (this.height - this.ySize) / 2;
                this.blit(matrixStack, relX, relY, 0, 0, this.xSize, this.ySize);
                if (this.container.isBurning()) {
                    int k = this.container.getBurnLeftScaled();
                    this.blit(matrixStack, relX + 56, relY + 36 + 12 - k, 176, 12 - k, 14, k + 1);
                }
            }
        };
    }
}
