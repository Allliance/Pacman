package com.sharif.ce.pac.man.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sharif.ce.pac.man.Pacman;
import com.sharif.ce.pac.man.controller.AssetController;
import com.sharif.ce.pac.man.controller.UserController;
import com.sharif.ce.pac.man.model.MenuButton;

public class SignUpMenu extends Menu {

    private Skin skin;
    private Label usernameLabel;
    private TextField usernameField;
    private Label passwordLabel;
    private TextField passwordField;
    private Label passwordRepeatLabel;
    private TextField passwordRepeatField;
    private MenuButton backButton;
    private MenuButton registerButton;
    private Container<Table> tableContainer;
    private Table itemsTable;
    private Label errorLabel;

    public SignUpMenu(Pacman game) {
        super(game);
        initializeWidgets();
        arrangeWidgets();
        addListeners();
    }

    private void initializeWidgets(){
        skin = AssetController.getDefaultSkin();
        errorLabel = new Label("",skin);
        errorLabel.setAlignment(2);
        usernameLabel = new Label("Username",skin,"button");
        usernameField = new TextField("",skin);
        passwordLabel = new Label("Password",skin,"button");
        passwordField = new TextField("",skin);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        passwordRepeatLabel = new Label("Repeat Password",skin,"button");
        passwordRepeatField = new TextField("",skin);
        passwordRepeatField.setPasswordCharacter('*');
        passwordRepeatField.setPasswordMode(true);
        backButton = new MenuButton("Back",AssetController.getButtonSkin());
        registerButton = new MenuButton("Register",AssetController.getButtonSkin());
        tableContainer = new Container<>();
        itemsTable = new Table();
        tableContainer.setActor(itemsTable);
    }

    private void arrangeWidgets(){
        int windowWidth = Gdx.graphics.getWidth(),windowHeight = Gdx.graphics.getHeight();
        tableContainer.setSize(windowWidth*0.4f,windowHeight*0.8f);
        tableContainer.setPosition(windowWidth/2-tableContainer.getWidth()*0.5f,(windowHeight-tableContainer.getHeight())/2);
        itemsTable.setFillParent(true);
        itemsTable.add(usernameLabel).expandX().colspan(2).padRight(5).left();
        itemsTable.add(usernameField).padBottom(10).expandX().colspan(2).height(usernameLabel.getHeight());
        itemsTable.row();
        itemsTable.add(passwordLabel).expandX().colspan(2).padRight(5).left();
        itemsTable.add(passwordField).padBottom(10).expandX().colspan(2).height(usernameLabel.getHeight());
        itemsTable.row();
        itemsTable.add(passwordRepeatLabel).expandX().colspan(2).padRight(5).left();
        itemsTable.add(passwordRepeatField).padBottom(10).expandX().colspan(2).height(usernameLabel.getHeight());
        itemsTable.row();
        itemsTable.add(errorLabel).fillX().colspan(4).center().padBottom(10);
        itemsTable.row();
        itemsTable.add(backButton).padBottom(10).height(50).expandX().fill(true).colspan(2);
        itemsTable.add(registerButton).padBottom(10).height(50).expandX().padLeft(5).fill(true).colspan(2);
        stage.addActor(tableContainer);
        stage.addActor(musicButton);
    }

    private void addListeners(){
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getStartMenu());
                super.clicked(event, x, y);
            }
        });
        registerButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                submit();
                super.clicked(event,x,y);
            }
        });
        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER)
                    submit();
                if (keycode == Input.Keys.ESCAPE)
                    game.setScreen(game.getStartMenu());
                return super.keyDown(event, keycode);
            }
        });
    }

    private boolean validateFields(){
        if (usernameField.getText().length() < 3){
            errorLabel.setText("too short username!");
            return false;
        }
        if (passwordField.getText().length() < 3){
            errorLabel.setText("too short password!");
            return false;
        }
        if (!UserController.isUsernameAvailable(usernameField.getText())){
            errorLabel.setText("username not available!");
            return false;
        }
        if (!passwordField.getText().equals(passwordRepeatField.getText())){
            errorLabel.setText("repeated password doesn't match password!");
            return false;
        }
        return true;
    }

    private void submit(){
        if (!validateFields())
            return;
        UserController.registerUser(usernameField.getText(),passwordField.getText());
        new DialogBox(stage).showMessage("user \"" + usernameField.getText()+"\" successfully created!",new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getStartMenu());
                super.clicked(event, x, y);
            }
        });
    }

    @Override
    public void show(){
        errorLabel.setText("");
        passwordRepeatField.setText("");
        passwordField.setText("");
        usernameField.setText("");
        super.show();
    }

}
