package owo.caramell.devyclient.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import owo.caramell.devyclient.client.DevyMainClient;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.text.Format;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicPlayer extends Screen {
    /**
     * MusicPlayer
     * Currently used for an easter egg but it doesn't work currently.
     * TODO: Find a MP3 Player library that WORKS and ISN'T OUTDATED.
     */
    protected MusicPlayer() {
        super(Text.translatable("owo.caramell.musicplayeregg"));
    }
    String musicFile = "";
    @Override
    protected void init() {
        GridWidget gridWidget = new GridWidget();
        GridWidget.Adder adder = gridWidget.createAdder(2);
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        adder.add(ButtonWidget.builder(Text.of("Choose File"), button -> {
            System.setProperty("java.awt.headless", "false");
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("MP3 File","mp3");
            fileChooser.addChoosableFileFilter(filter);
            if(fileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
                musicFile = fileChooser.getSelectedFile().getPath();
            }
        }).width(102).build());
        adder.add(ButtonWidget.builder(Text.of("Play"), button -> {

        }).width(102).build());
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.4f);
        gridWidget.forEachChild(this::addDrawableChild);
        super.init();
    }

}
