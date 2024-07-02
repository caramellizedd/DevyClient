package owo.aydendevy.devyclient.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import owo.aydendevy.devyclient.HUD.IRenderer;
import owo.aydendevy.devyclient.HUD.Loader.HUDManager;
import owo.aydendevy.devyclient.HUD.ScaledResolution;
import owo.aydendevy.devyclient.HUD.ScreenPosition;
import owo.aydendevy.devyclient.StaticStrings;
import owo.aydendevy.devyclient.client.DevyMainClient;

import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Predicate;

public class HUDConfigScreen extends Screen {
    int i = 0;

    // ADDED FOR SMOOTH DRAGGING

    private float smX, smY;

    private boolean dragged = false;

    protected boolean hovered;

    private final HashMap<IRenderer, ScreenPosition> renderers = new HashMap<IRenderer, ScreenPosition>();

    private Optional<IRenderer> selectedRenderer = Optional.empty();

    private int prevX, prevY;
    public Screen lastScreen = null;

    public HUDConfigScreen(HUDManager api, Screen lastScreen) {
        super(Text.of("Mods Placement Configuration"));
        Collection<IRenderer> registeredRenderers = api.getRegisteredRenderers();
        this.lastScreen = lastScreen;
        for (IRenderer ren : registeredRenderers) {
            if (!ren.isEnabled()) {
                continue;
            }

            ScreenPosition pos = ren.load();
            if (pos == null) {
                pos = ScreenPosition.fromRelativePosition(0.5, 0.5);
            }

            adjustBounds(ren, pos);
            this.renderers.put(ren, pos);
        }
    }
    IRenderer selected = null;

    @Override
    protected void applyBlur(float delta) {
        //super.applyBlur(delta);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if(!dragged){
            ScaledResolution sc = new ScaledResolution(MinecraftClient.getInstance());
            //if(mc.theWorld != null) BlurUtils.renderBlurredBackground(sc.getScaledWidth(),sc.getScaledHeight(), 0, 0, width, height, 0);
            selected = null;
        }
        context.drawText(MinecraftClient.getInstance().textRenderer, StaticStrings.version, 2, 2, -1, true);
        if(MinecraftClient.getInstance().world == null) {
            //super.drawDefaultBackground();
            //renderOverlay(DevyClient.getInstance().partialTicks);
            //Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(VersionString.Ver, 2, 2, -1);

        }
        else if (!dragged) {
            //super.drawDefaultBackground();
        }
        //final float zBackup = this.zLevel;
        //this.zLevel = 200;
        for (IRenderer renderer : renderers.keySet()) {
            if(renderer.getHeight() > 0 && renderer.getWidth() > 0) {
                ScreenPosition pos = renderers.get(renderer);
                float absoluteX = pos.getAbsoluteX();
                float absoluteY = pos.getAbsoluteY();
                this.hovered = mouseX >= absoluteX && mouseX <= absoluteX + renderer.getWidth() && mouseY >= absoluteY && mouseY <= absoluteY + renderer.getHeight();
                if (!dragged) {
                    context.drawBorder(pos.getAbsoluteX()-1, pos.getAbsoluteY()-1, renderer.getWidth()+2, renderer.getHeight()+1, 0x88FFFFFF);
                    context.fill(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + renderer.getWidth(), pos.getAbsoluteY() + renderer.getHeight(), 0x33FFFFFF);
                }
                // START OF SMOOTH DRAGGING
                if (this.hovered) {
                    context.drawBorder(pos.getAbsoluteX()-1, pos.getAbsoluteY()-1, renderer.getWidth()+2, renderer.getHeight()+1, 0x8800FF00);
                    //GuiUtils.drawRect(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + renderer.getWidth(), pos.getAbsoluteY() + renderer.getHeight(), 0x3300AA00, context);
                    context.fill(pos.getAbsoluteX(), pos.getAbsoluteY(), pos.getAbsoluteX() + renderer.getWidth(), pos.getAbsoluteY() + renderer.getHeight(), 0x3300AA00);
                    if (dragged) {
                        // Sets the selected HUDs ONCE
                        if(selected == null){
                            selected = renderer;
                        }
                        // Prevents dragging other HUDs
                        if(selected == renderer) {
                            pos.setAbsolute(pos.getAbsoluteX() + mouseX - this.prevX, pos.getAbsoluteY() + mouseY - this.prevY);

                            adjustBounds(renderer, pos);

                            this.prevX = mouseX;
                            this.prevY = mouseY;
                        }
                    }
                }

                renderer.renderDummy(pos, context);
                // END OF SMOOTH DRAGGING
                selectedPos = new ScreenPosition(0, 0);
            }
        }
        this.smX = mouseX;
        this.smY = mouseY;

        //this.zLevel = zBackup;


    }

    ScreenPosition selectedPos = new ScreenPosition(0,0);

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (selectedRenderer.isPresent()) {
            moveSelectedRenderBy((int)mouseX - prevX, (int)mouseY - prevY);
        }

        this.prevX = (int)mouseX;
        this.prevY = (int)mouseY;
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    private void moveSelectedRenderBy(int offsetX, int offsetY) {
        IRenderer renderer = selectedRenderer.get();
        ScreenPosition pos = renderers.get(renderer);

        pos.setAbsolute(pos.getAbsoluteX() + offsetX, pos.getAbsoluteY() + offsetY);

        if (pos.getAbsoluteX() == 0 << 1) {
            pos.setAbsolute(1, pos.getAbsoluteY());
        }

        if (pos.getAbsoluteY() == 0 << 1) {
            pos.setAbsolute(pos.getAbsoluteX(), 1);
        }

        adjustBounds(renderer, pos);
    }
    @Override
    public void close() {
        for (IRenderer renderer : renderers.keySet()) {
            renderer.save(renderers.get(renderer));
        }
        this.client.setScreen(lastScreen);
    }

    @Override
    public boolean shouldPause() {
        return true;
    }

    private void adjustBounds(IRenderer renderer, ScreenPosition pos) {

        ScaledResolution res = new ScaledResolution(MinecraftClient.getInstance());
        selectedPos = pos;
        int screenWidth = res.getScaledWidth();
        int screenHeight = res.getScaledHeight();

        int absoluteX = Math.max(0, Math.min(pos.getAbsoluteX(), Math.max(screenWidth - renderer.getWidth(), 0)));
        int absoluteY = Math.max(0, Math.min(pos.getAbsoluteY(), Math.max(screenHeight - renderer.getHeight(), 0)));

        pos.setAbsolute(absoluteX, absoluteY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.prevX = (int)mouseX;
        this.prevY = (int)mouseY;

        // NEEDED FOR SMOOTH DRAGGING
        dragged = true;
        mouseHeld = true;

        loadMouseOver((int)mouseX, (int)mouseY);
        return super.mouseClicked(mouseX, mouseY, button);
    }
    boolean mouseHeld = false;

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        // NEEDED FOR SMOOTH DRAGGING
        dragged = false;
        mouseHeld = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void loadMouseOver(int x, int y) {
        this.selectedRenderer = renderers.keySet().stream().filter(new MouseOverFinder(x, y)).findFirst();
    }

    private class MouseOverFinder implements Predicate<IRenderer> {

        private float mouseX, mouseY;

        public MouseOverFinder(int x, int y) {
            this.mouseX = x;
            this.mouseY = y;
        }

        @Override
        public boolean test(IRenderer renderer) {

            ScreenPosition pos = renderers.get(renderer);

            float absoluteX = pos.getAbsoluteX();
            float absoluteY = pos.getAbsoluteY();

            if (mouseX >= absoluteX && mouseX <= absoluteX + renderer.getWidth()) {

                if (mouseY >= absoluteY && mouseY <= absoluteY + renderer.getHeight()) {

                    return true;

                }

            }

            return false;
        }

    }
}
