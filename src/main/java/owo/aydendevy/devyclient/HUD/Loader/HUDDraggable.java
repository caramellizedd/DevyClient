package owo.aydendevy.devyclient.HUD.Loader;

import owo.aydendevy.devyclient.HUD.IRenderer;
import owo.aydendevy.devyclient.HUD.ScreenPosition;
import owo.aydendevy.devyclient.Utils.ConfigAPI;
import owo.aydendevy.devyclient.Utils.ConfigUtils;
import owo.aydendevy.devyclient.client.DevyMainClient;

import java.io.File;
public abstract class HUDDraggable extends HUD implements IRenderer {
    protected ScreenPosition pos;

    public HUDDraggable() {
        pos = loadPosFromJson();
    }
    @Override
    public ScreenPosition load() {
        return pos;
    }
    @Override
    public void save(ScreenPosition pos) {
        this.pos = pos;
        savePosToJson();
    }

    private void savePosToJson() {
        //FileIOManager.writeJsonToFile(new File(getJsonFolder(), "ModPos.json"), pos);
        HUDInstances.cfg.set(this.getClass().getSimpleName() + "_pos", pos);
        try{
            HUDInstances.cfg.save();
        }
        catch (Exception err){

        }
    }
    private ScreenPosition loadPosFromJson() {
        ScreenPosition loadedPos = (ScreenPosition) HUDInstances.cfg.loadSavedValue2(this.getClass().getSimpleName() + "_pos");
        DevyMainClient.logger.info(String.valueOf(loadedPos.getAbsoluteX()));
        DevyMainClient.logger.info(String.valueOf(loadedPos.getAbsoluteY()));
        if(loadedPos == null)
        {
            DevyMainClient.logger.error("The specific HUD (" + this.getClass().getSimpleName() + ") Position cannot be found!");
            loadedPos = ScreenPosition.fromRelativePosition(0.5, 0.5);
            this.pos = loadedPos;
            savePosToJson();
        }
        return loadedPos;
    }
    public final int getLineOffset(ScreenPosition pos, int lineNum) {
        return pos.getAbsoluteY() + getLineOffset(lineNum);

    }

    private int getLineOffset(int lineNum) {
        return (font.fontHeight + 3) * lineNum;
    }

}
