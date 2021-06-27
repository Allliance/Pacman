package com.sharif.ce.pac.man.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sharif.ce.pac.man.Pacman;
import com.sharif.ce.pac.man.controller.SoundController;
import com.sharif.ce.pac.man.controller.TextureController;

public class Menu extends ScreenAdapter {
    protected Pacman game;
    protected Stage stage;
    protected Image background;
    protected SoundButton musicButton;

    public Menu(Pacman game) {
        this.game = game;
        stage = new Stage();
        musicButton = new SoundButton(0,Gdx.graphics.getHeight()-30,30,30);
        background = new Image(TextureController.getBackground());
        stage.addActor(background);
    }

    public Menu(Pacman game,boolean hasBackground){
        this.game = game;
        stage = new Stage();
        musicButton = new SoundButton(0,Gdx.graphics.getHeight()-30,30,30);
        if (hasBackground) {
            background = new Image(TextureController.getBackground());
            stage.addActor(background);
        }
    }


    @Override
    public void show() {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        if (!SoundController.isMute())
            musicButton.setCurrentIcon(TextureController.getMuteTexture());
        else
            musicButton.setCurrentIcon(TextureController.getUnmuteTexture());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        super.dispose();
    }
}

class SoundButton extends Actor {

    public Texture currentIcon;

    public SoundButton(int x,int y,int width,int height){
        super();
        currentIcon = TextureController.getMuteTexture();
        setBounds(x,y,width,height);
        addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (SoundController.isMute()){
                    SoundController.unMute();
                    currentIcon = TextureController.getMuteTexture();
                }else{
                    SoundController.mute();
                    currentIcon = TextureController.getUnmuteTexture();
                }
            }
        });
    }

    public void setCurrentIcon(Texture currentIcon) {
        this.currentIcon = currentIcon;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentIcon,getX(),getY());
    }
}
