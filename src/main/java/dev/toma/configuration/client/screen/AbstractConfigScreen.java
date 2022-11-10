package dev.toma.configuration.client.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.client.IValidationHandler;
import dev.toma.configuration.client.widget.ConfigEntryWidget;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.validate.NotificationSeverity;
import dev.toma.configuration.config.io.ConfigIO;
import dev.toma.configuration.config.value.ConfigValue;
import dev.toma.configuration.config.value.ObjectValue;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.lwjgl.opengl.GL11;

import java.util.Collection;
import java.util.List;

public abstract class AbstractConfigScreen extends Screen {

    public static final int HEADER_HEIGHT = 35;
    public static final int FOOTER_HEIGHT = 30;
    public static final Marker MARKER = MarkerManager.getMarker("Screen");
    protected final Screen last;
    protected final String configId;

    protected int index;
    protected int pageSize;

    public AbstractConfigScreen(ITextComponent title, Screen previous, String configId) {
        super(title);
        this.last = previous;
        this.configId = configId;
    }

    @Override
    public void onClose() {
        super.onClose();
        this.saveConfig(true);
    }

    public static void renderScrollbar(MatrixStack stack, int x, int y, int width, int height, int index, int valueCount, int paging) {
        if (valueCount <= paging)
            return;
        double step = height / (double) valueCount;
        int min = MathHelper.floor(index * step);
        int max = MathHelper.ceil((index + paging) * step);
        int y1 = y + min;
        int y2 = y + max;
        fill(stack, x, y, x + width, y + height, 0xFF << 24);

        fill(stack, x, y1, x + width, y2, 0xFF888888);
        fill(stack, x, y1, x + width - 1, y2 - 1, 0xFFEEEEEE);
        fill(stack, x + 1, y1 + 1, x + width - 1, y2 - 1, 0xFFCCCCCC);
    }

    protected void addFooter() {
        int centerY = this.height - FOOTER_HEIGHT + (FOOTER_HEIGHT - 20) / 2;
        addButton(new Button(20, centerY, 50, 20, ConfigEntryWidget.BACK, this::buttonBackClicked));
        addButton(new Button(75, centerY, 120, 20, ConfigEntryWidget.REVERT_DEFAULTS, this::buttonRevertToDefaultClicked));
        addButton(new Button(200, centerY, 120, 20, ConfigEntryWidget.REVERT_CHANGES, this::buttonRevertChangesClicked));
    }

    protected void correctScrollingIndex(int count) {
        if (index + pageSize > count) {
            index = Math.max(count - pageSize, 0);
        }
    }

    protected Screen getFirstNonConfigScreen() {
        Screen screen = last;
        while (screen instanceof ConfigScreen) {
            screen = ((ConfigScreen) screen).last;
        }
        return screen;
    }

    private void buttonBackClicked(Button button) {
        this.minecraft.setScreen(this.last);
        this.saveConfig();
    }

    private void buttonRevertToDefaultClicked(Button button) {
        Configuration.LOGGER.info(MARKER, "Reverting config {} to default values", this.configId);
        ConfigHolder.getConfig(this.configId).ifPresent(holder -> {
            revertToDefault(holder.values());
            ConfigIO.saveClientValues(holder);
        });
        this.backToConfigList();
    }

    private void buttonRevertChangesClicked(Button button) {
        ConfigHolder.getConfig(this.configId).ifPresent(ConfigIO::reloadClientValues);
        this.backToConfigList();
    }

    private void revertToDefault(Collection<ConfigValue<?>> configValues) {
        configValues.forEach(val -> {
            if (val instanceof ObjectValue) {
                ObjectValue objVal = (ObjectValue) val;
                this.revertToDefault(objVal.get().values());
            } else {
                val.useDefaultValue();
            }
        });
    }

    private void backToConfigList() {
        this.minecraft.setScreen(this.getFirstNonConfigScreen());
        this.saveConfig();
    }

    private void saveConfig() {
        saveConfig(false);
    }

    private void saveConfig(boolean force) {
        if (force || !(last instanceof AbstractConfigScreen)) {
            ConfigHolder.getConfig(this.configId).ifPresent(ConfigIO::saveClientValues);
        }
    }

    public void renderNotification(NotificationSeverity severity, MatrixStack stack, List<ITextComponent> texts, int mouseX, int mouseY) {
        if (!texts.isEmpty()) {
            int maxTextWidth = 0;
            int iconOffset = 13;
            for(ITextComponent textComponent : texts) {
                int textWidth = this.font.width(textComponent);
                if (!severity.isOkStatus()) {
                    textWidth += iconOffset;
                }
                if (textWidth > maxTextWidth) {
                    maxTextWidth = textWidth;
                }
            }

            int startX = mouseX + 12;
            int startY = mouseY - 12;
            int heightOffset = 8;
            if (texts.size() > 1) {
                heightOffset += 2 + (texts.size() - 1) * 10;
            }

            if (startX + maxTextWidth > this.width) {
                startX -= 28 + maxTextWidth;
            }

            if (startY + heightOffset + 6 > this.height) {
                startY = this.height - heightOffset - 6;
            }

            stack.pushPose();
            int l = -267386864;
            int i1 = 1347420415;
            int j1 = 1344798847;
            int k1 = 400;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuilder();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            Matrix4f matrix4f = stack.last().pose();
            fillGradient(matrix4f, bufferbuilder, startX - 3, startY - 4, startX + maxTextWidth + 3, startY - 3, k1, l, l);
            fillGradient(matrix4f, bufferbuilder, startX - 3, startY + heightOffset + 3, startX + maxTextWidth + 3, startY + heightOffset + 4, k1, l, l);
            fillGradient(matrix4f, bufferbuilder, startX - 3, startY - 3, startX + maxTextWidth + 3, startY + heightOffset + 3, k1, l, l);
            fillGradient(matrix4f, bufferbuilder, startX - 4, startY - 3, startX - 3, startY + heightOffset + 3, k1, l, l);
            fillGradient(matrix4f, bufferbuilder, startX + maxTextWidth + 3, startY - 3, startX + maxTextWidth + 4, startY + heightOffset + 3, k1, l, l);
            fillGradient(matrix4f, bufferbuilder, startX - 3, startY - 3 + 1, startX - 3 + 1, startY + heightOffset + 3 - 1, k1, i1, j1);
            fillGradient(matrix4f, bufferbuilder, startX + maxTextWidth + 2, startY - 3 + 1, startX + maxTextWidth + 3, startY + heightOffset + 3 - 1, k1, i1, j1);
            fillGradient(matrix4f, bufferbuilder, startX - 3, startY - 3, startX + maxTextWidth + 3, startY - 3 + 1, k1, i1, i1);
            fillGradient(matrix4f, bufferbuilder, startX - 3, startY + heightOffset + 2, startX + maxTextWidth + 3, startY + heightOffset + 3, k1, j1, j1);
            RenderSystem.enableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.shadeModel(GL11.GL_SMOOTH);
            bufferbuilder.end();
            WorldVertexBufferUploader.end(bufferbuilder);
            RenderSystem.shadeModel(GL11.GL_FLAT);
            RenderSystem.enableTexture();

            if (!severity.isOkStatus()) {
                minecraft.getTextureManager().bind(severity.getIcon());
                bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                float min = -0.5f;
                float max = 8.5f;
                bufferbuilder.vertex(matrix4f, startX + min, startY + min, k1).uv(0.0F, 0.0F).endVertex();
                bufferbuilder.vertex(matrix4f, startX + min, startY + max, k1).uv(0.0F, 1.0F).endVertex();
                bufferbuilder.vertex(matrix4f, startX + max, startY + max, k1).uv(1.0F, 1.0F).endVertex();
                bufferbuilder.vertex(matrix4f, startX + max, startY + min, k1).uv(1.0F, 0.0F).endVertex();
                bufferbuilder.end();
                WorldVertexBufferUploader.end(bufferbuilder);
            }


            RenderSystem.disableBlend();
            IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
            stack.translate(0.0D, 0.0D, k1);

            int textOffset = severity.isOkStatus() ? 0 : iconOffset;
            for(int i = 0; i < texts.size(); i++) {
                ITextComponent textComponent = texts.get(i);
                if (textComponent != null) {
                    this.font.drawInBatch(textComponent, (float)startX + textOffset, (float)startY, -1, true, matrix4f, irendertypebuffer$impl, false, 0, 0xf000f0);
                }

                if (i == 0) {
                    startY += 2;
                }

                startY += 10;
            }

            irendertypebuffer$impl.endBatch();
            stack.popPose();
        }
    }

    protected <T> void initializeGuiValue(ConfigValue<T> value, IValidationHandler handler) {
        T t = value.get();
        value.setWithValidationHandler(t, handler);
    }
}
