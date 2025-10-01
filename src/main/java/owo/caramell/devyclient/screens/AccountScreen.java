package owo.caramell.devyclient.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import owo.caramell.devyclient.client.DevyMainClient;

public class AccountScreen extends Screen {

    protected AccountScreen() {
        super(Text.translatable("owo.caramell.accscreen"));
    }

    TextFieldWidget user, password;
    boolean userFilled, passFilled;

    @Override
    protected void init() {
        GridWidget gridWidget = new GridWidget();
        GridWidget.Adder adder = gridWidget.createAdder(2);
        gridWidget.getMainPositioner().margin(4, 4, 4, 0);
        user = new TextFieldWidget(textRenderer, 208, 20, Text.translatable("owo.caramell.userfield"));
        password = new TextFieldWidget(textRenderer, 208, 20, Text.translatable("owo.caramell.passfield"));
//        adder.add(new TextWidget(Text.translatable("owo.caramell.acc.warning1"), client.textRenderer).alignCenter(),2);
//        adder.add(new TextWidget(Text.translatable("owo.caramell.acc.warning2"), client.textRenderer).alignCenter(),4);
        adder.add(new TextWidget(Text.translatable("owo.caramell.userfieldtext"), textRenderer), adder.copyPositioner().marginTop(10));
        adder.add(user);
        adder.add(new TextWidget(Text.translatable("owo.caramell.passfieldtext"), textRenderer), adder.copyPositioner().marginTop(10));
        adder.add(password);
        adder.add(ButtonWidget.builder(Text.translatable("owo.caramell.loginbutton"), button -> {
            if(DevyMainClient.instance.accountManager.login(user.getText(), password.getText())){
                DevyMainClient.logger.info("Logged in!");
            }
        }).width(106).build());
        adder.add(ButtonWidget.builder(Text.translatable("owo.caramell.registerbutton"), button -> {
            if(DevyMainClient.instance.accountManager.register(user.getText(), password.getText())){
                DevyMainClient.logger.info("Registered!");
            }
        }).width(106).build());
        adder.add(ButtonWidget.builder(Text.translatable("owo.caramell.genericback"), button -> {
            this.close();
        }).width(220).build(), 2);
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, 0, this.width, this.height, 0.5f, 0.4f);
        gridWidget.forEachChild(this::addDrawableChild);
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.drawCenteredTextWithShadow(textRenderer, Text.translatable("owo.caramell.acc.warning1"),client.currentScreen.width/2,textRenderer.fontHeight,-1);
        context.drawCenteredTextWithShadow(textRenderer, Text.translatable("owo.caramell.acc.warning2"),client.currentScreen.width/2,textRenderer.fontHeight*2,-1);
        super.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public void tick() {
        if(user.getText().length() > 0)
            userFilled = true;
        else
            userFilled = false;
        if(password.getText().length() > 0)
            passFilled = true;
        else
            passFilled = false;
        super.tick();
    }
}
