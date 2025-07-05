package owo.caramell.devyclient.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import owo.caramell.devyclient.client.DevyMainClient;

public class AccountScreen extends Screen {

    protected AccountScreen() {
        super(Text.translatable("owo.caramell.accscreen"));
    }

    @Override
    protected void init() {
        GridWidget gridWidget = new GridWidget();
        GridWidget.Adder adder = gridWidget.createAdder(2);
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        TextFieldWidget user = new TextFieldWidget(textRenderer, 208, 20, Text.translatable("owo.caramell.userfield"));
        TextFieldWidget password = new TextFieldWidget(textRenderer, 208, 20, Text.translatable("owo.caramell.passfield"));
        adder.add(new TextWidget(Text.translatable("owo.caramell.userfieldtext"), textRenderer), adder.copyPositioner().marginTop(10));
        adder.add(user);
        adder.add(new TextWidget(Text.translatable("owo.caramell.passfieldtext"), textRenderer), adder.copyPositioner().marginTop(10));
        adder.add(password);
        adder.add(ButtonWidget.builder(Text.translatable("owo.caramell.registerbutton"), button -> {
            DevyMainClient.instance.accountManager.register(user.getText(), password.getText());
        }).width(212).build(), 2);
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.4f);
        gridWidget.forEachChild(this::addDrawableChild);
        super.init();
    }
}
