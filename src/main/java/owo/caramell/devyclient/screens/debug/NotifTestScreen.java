package owo.caramell.devyclient.screens.debug;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import owo.caramell.devyclient.client.DevyMainClient;

public class NotifTestScreen extends Screen {
    public NotifTestScreen() {
        super(Text.translatable("owo.caramell.debug.notif"));
    }

    @Override
    protected void init() {
        super.init();
        TextFieldWidget title, content;
        GridWidget grid = new GridWidget();
        GridWidget.Adder adder = grid.createAdder(2);
        grid.getMainPositioner().margin(4, 4, 4, 0);
        adder.add(new TextWidget(Text.translatable("owo.caramell.debug.notif.texttitle"), textRenderer), adder.copyPositioner().marginTop(10));
        adder.add(title = new TextFieldWidget(textRenderer, 208, 20, Text.translatable("owo.caramell.debug.notif.titlecontent")));
        adder.add(new TextWidget(Text.translatable("owo.caramell.debug.notif.textcontent"), textRenderer), adder.copyPositioner().marginTop(10));
        adder.add(content = new TextFieldWidget(textRenderer, 208, 20, Text.translatable("owo.caramell.debug.notif.titlecontent")));
        content.setMaxLength(99999);
        adder.add(ButtonWidget.builder(Text.translatable("owo.caramell.debug.notif.send"), button -> {
            DevyMainClient.instance.notifAPI.requestNotification(title.getText(), content.getText());
        }).width(204).build(), 2);
        adder.add(ButtonWidget.builder(Text.translatable("owo.caramell.genericback"), button -> {
            close();
        }).width(204).build(), 2);

        grid.refreshPositions();
        SimplePositioningWidget.setPos(grid, 0, 0, this.width, this.height, 0.5f, 0.4f);
        grid.forEachChild(this::addDrawableChild);
    }
}
