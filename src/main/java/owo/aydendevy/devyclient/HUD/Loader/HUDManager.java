package owo.aydendevy.devyclient.HUD.Loader;

import com.google.common.collect.Sets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.VertexConsumerProvider;
import org.joml.Matrix4f;
import owo.aydendevy.devyclient.HUD.Mods.HPDisplay;
import owo.aydendevy.devyclient.HUD.IRenderer;
import owo.aydendevy.devyclient.HUD.ScreenPosition;
import owo.aydendevy.devyclient.client.DevyMainClient;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class HUDManager {
    private static HUDManager instance;
    private Set<IRenderer> registeredRenderers;
    private final MinecraftClient mc;

    static {
        HUDManager.instance = null;
    }

    public HUDManager() {
        this.registeredRenderers = Sets.newHashSet();
        this.mc = MinecraftClient.getInstance();
    }

    public static HUDManager getInstance() {
        if (HUDManager.instance == null) {
            HUDManager.instance = new HUDManager();
        }
        //EventManager.register(HUDManager.instance = new HUDManager());
        return HUDManager.instance;
    }

    public void register(final IRenderer... renderers) {
        this.registeredRenderers.addAll(Arrays.asList(renderers));
    }

    public void unregister(final IRenderer... renderers) {
        for (final IRenderer render : renderers) {
            this.registeredRenderers.remove(render);
        }
    }

    public Collection<IRenderer> getRegisteredRenderers() {
        return (Collection<IRenderer>)Sets.newHashSet((Iterable)this.registeredRenderers);
    }

    public void openMenuScreen() {
        //this.mc.displayGuiScreen(new ModScreen());
    }
    public void openConfigScreen(Screen lastScreen) {
        this.mc.setScreen(null);
        //new HUDConfigScreen(this, lastScreen)
    }

    public void onRender(DrawContext drawContext) {
        if (this.mc.currentScreen == null || !this.mc.mouse.isCursorLocked() && !this.mc.inGameHud.getDebugHud().shouldShowDebugHud()) {
            for (final IRenderer renderer : this.registeredRenderers) {
                this.callRenderer(renderer, drawContext);
            }
        }
    }

    private void callRenderer(final IRenderer renderer, DrawContext drawContext) {
        if (!renderer.isEnabled()) {
            return;
        }
        ScreenPosition pos = renderer.load();
        if (pos == null) {
            pos = ScreenPosition.fromRelativePosition(1.5, 1.5);
        }
        // Disable HUD Mod from being visible when another menu opens
        if(mc.currentScreen == null)
            renderer.render(pos, drawContext);
    }
}
