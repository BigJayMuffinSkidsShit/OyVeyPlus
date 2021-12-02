package me.muffin.oyveyplus;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import me.muffin.oyveyplus.api.command.CommandManager;
import me.muffin.oyveyplus.api.event.EventHandler;
import me.muffin.oyveyplus.api.event.events.*;
import me.muffin.oyveyplus.api.manager.InventoryManager;
import me.muffin.oyveyplus.api.manager.PositionManager;
import me.muffin.oyveyplus.api.manager.TextManager;
import me.muffin.oyveyplus.api.module.Module;
import me.muffin.oyveyplus.api.module.ModuleManager;
import me.muffin.oyveyplus.api.utils.advanced.Renderer;
import me.muffin.oyveyplus.impl.gui.click.ClickGui;
import me.muffin.oyveyplus.impl.gui.click.hud.HUDEditor;
import me.muffin.oyveyplus.impl.gui.click.hud.HudComponentManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.lwjgl.input.Keyboard;
import me.muffin.oyveyplus.api.friends.FriendManager;
import me.muffin.oyveyplus.api.settings.SettingManager;
import me.muffin.oyveyplus.api.wrapper.Wrapper;

import java.util.ArrayList;

@Mod(modid = OyVeyPlus.MOD_ID, name = OyVeyPlus.NAME, version = OyVeyPlus.VERSION)
public class OyVeyPlus implements Wrapper {
    public static final String MOD_ID = "oyveyplus";
    public static final String NAME = "OyVeyPlus";
    public static final String VERSION = "0.1"; // just because doesn't have modules yet

    public static final Logger logger = (Logger) LogManager.getLogger("oyveyplus");

    public static final EventBus EVENTBUS = new EventBus();

    private ArrayList<Module> modules = new ArrayList<>();
    private Module module;

    public static FriendManager friendManager;
    public static SettingManager settingManager;
    public static ModuleManager moduleManager;
    public static TextManager textManager;
    public static InventoryManager inventoryManager;
    public static PositionManager positionManager;
    public static HudComponentManager hudComponentManager;
    public static ClickGui gui;
    public static HUDEditor hudEditor;
    public static CommandManager commandManager;
    public static float TIMER_VALUE;

    @Mod.EventHandler public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        EVENTBUS.register(this);

        logger.info("make me a cheese sandwich - bigjmuffin");
    }

    @Mod.EventHandler public void init(FMLInitializationEvent event) {
        logger.info("Welcome To OyVey +");
        friendManager = new FriendManager();
        settingManager = new SettingManager();
        moduleManager = new ModuleManager();
        textManager = new TextManager();
        inventoryManager = new InventoryManager();
        positionManager = new PositionManager();
        hudComponentManager = new HudComponentManager();
        commandManager = new CommandManager();
        TIMER_VALUE = 1.0f;
        gui = new ClickGui();
        hudEditor = new HUDEditor();
        MinecraftForge.EVENT_BUS.register(new Renderer());
    }

    public void renderOverlay(RenderGameOverlayEvent event) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        FontRenderer fontRenderer= mc.fontRenderer;

        if (event.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
            fontRenderer.drawStringWithShadow(OyVeyPlus.NAME + VERSION, 1,2, 0xa300ff);
        }
    }

    @Mod.EventHandler public void postInit(FMLPostInitializationEvent event) {}

    @Subscribe public void onUpdate(EventTick event) {
        moduleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::onUpdate);
    }

    @Subscribe public void onTick(EventTick eventTick) {
        moduleManager.getModules().stream().filter(Module::isEnabled).forEach(Module::onTick);
    }

    @Subscribe
    public void onRender3D(Render3DEvent event) {
        this.modules.stream().filter(Module::isEnabled).forEach(module -> module.onRender3D(event));
    }

    @Subscribe public void onKey(EventKey event) {
        if (mc.player != null && mc.world != null) OyVeyPlus.moduleManager.getModules().stream().filter(module -> module.getKey() == Keyboard.getEventKey()).forEach(Module::toggle);
    }

    /*@Subscribe
    public void onWorldRender(RenderWorldLastEvent event) {
        if (event.isCanceled()) {
            return;
        }
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GlStateManager.disableDepth();
        GlStateManager.glLineWidth(1.0f);
        Render3DEvent render3dEvent = new Render3DEvent(event.getPartialTicks());
        GLUProjection projection = GLUProjection.getInstance();
        IntBuffer viewPort = GLAllocation.createDirectIntBuffer(16);
        FloatBuffer modelView = GLAllocation.createDirectFloatBuffer(16);
        FloatBuffer projectionPort = GLAllocation.createDirectFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projectionPort);
        GL11.glGetInteger(2978, viewPort);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        projection.updateMatrices(viewPort, modelView, projectionPort, (double) scaledResolution.getScaledWidth() / (double) Minecraft.getMinecraft().displayWidth, (double) scaledResolution.getScaledHeight() / (double) Minecraft.getMinecraft().displayHeight);
        module.onRender3D(render3dEvent);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.enableCull();
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.enableDepth();
    }
    */
}
