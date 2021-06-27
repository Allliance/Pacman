package com.sharif.ce.pac.man.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sharif.ce.pac.man.controller.AssetController;
import com.sharif.ce.pac.man.model.MenuButton;

public class DialogBox {

    private Stage stage;

    public DialogBox(Stage stage) {
        this.stage = stage;
    }

    public void showMessage(String message,final ClickListener listener) {
        Label messageLabel = new Label(message, AssetController.getDefaultSkin(), "title");
        MenuButton okButton = new MenuButton("Ok", AssetController.getDefaultSkin());
        final Dialog dialog = new Dialog("Notification", AssetController.getDefaultSkin()) {
            @Override
            public float getPrefWidth() {
                return 700;
            }

            @Override
            public float getPrefHeight() {
                return 200;
            }
        };
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                killDialog(dialog);
                listener.clicked(event,x,y);
            }
        });
        dialog.setMovable(false);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.getButtonTable().add(okButton).expandX();
        dialog.getContentTable().add(messageLabel).fillX().expandX().center();
        dialog.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.ENTER) {
                    killDialog(dialog);
                    listener.clicked(event,0,0);
                }
                return true;
            }
        });
        dialog.show(stage);
    }

    public void confirmationDialog(String confirmationMessage,final ClickListener listener) {
        Label messageLabel = new Label(confirmationMessage, AssetController.getScoreSkin(), "title-2");
        MenuButton yesButton = new MenuButton("Yes", AssetController.getDefaultSkin());
        MenuButton noButton = new MenuButton("No", AssetController.getDefaultSkin());
        final Dialog dialog = new Dialog("", AssetController.getScoreSkin()) {
            @Override
            public float getPrefWidth() {
                return 700;
            }

            @Override
            public float getPrefHeight() {
                return 200;
            }
        };
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                killDialog(dialog);
                listener.clicked(event,x,y);
            }
        });
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                killDialog(dialog);
            }
        });
        dialog.setMovable(false);
        dialog.setResizable(false);
        dialog.setModal(true);
        dialog.setWidth(200);
        dialog.setHeight(70);
        dialog.getButtonTable().add(yesButton).expandX().colspan(1).padRight(5);
        dialog.getButtonTable().add(noButton).expandX().colspan(1);
        dialog.getButtonTable().row();
        dialog.getContentTable().add(messageLabel).fillX().expandX().colspan(2).center();
        dialog.getContentTable().row();
        dialog.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER)
                    listener.clicked(event,0,0);
                if (keycode == Input.Keys.ESCAPE)
                    killDialog(dialog);
                return true;
            }
        });
        dialog.show(stage);
    }

    private void killDialog(Dialog dialog) {
        dialog.hide();
        dialog.cancel();
        dialog.remove();
    }

    public void askToQuit() {
        confirmationDialog("Are you sure , you want to quit?",new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
    }

}
