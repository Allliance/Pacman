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

public class StartMenu extends Menu {

    //Button
    private Skin skin;
    private MenuButton guestButton;
    private MenuButton logInButton;
    private MenuButton signUpButton;
    private MenuButton exitButton;
    private Label usernameLabel;
    private Label passwordLabel;
    private TextField usernameField;
    private TextField passwordField;
    private Label errorLabel;
    private Container<Table> tableContainer;
    private Table itemsTable;

    public StartMenu(Pacman game) {
        super(game);
        initializeWidgets();
        arrangeWidgets();
        addListeners();
    }

    private void initializeWidgets(){
        skin = AssetController.getDefaultSkin();
        usernameLabel = new Label("Username",skin,"button");
        usernameField = new TextField("",skin);
        passwordLabel = new Label("Password",skin,"button");
        passwordField = new TextField("",skin);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        errorLabel = new Label("",skin);
        guestButton = new MenuButton("Play as Guest",AssetController.getButtonSkin());
        logInButton = new MenuButton("Log In", AssetController.getButtonSkin());
        signUpButton = new MenuButton("Sign Up", AssetController.getButtonSkin());
        exitButton = new MenuButton("Exit", AssetController.getButtonSkin());
        tableContainer = new Container<>();
        itemsTable = new Table();
        tableContainer.setActor(itemsTable);
    }

    private void arrangeWidgets(){
        int windowWidth = Gdx.graphics.getWidth(),windowHeight = Gdx.graphics.getHeight();
        tableContainer.setSize(windowWidth*0.4f,windowHeight*0.7f);
        tableContainer.setPosition(windowWidth/2-tableContainer.getWidth()/2,(windowHeight-tableContainer.getHeight())/2);
        itemsTable.setFillParent(true);
        itemsTable.add(usernameLabel).expandX().colspan(1).padRight(5);
        itemsTable.add(usernameField).padBottom(10).expandX().colspan(1).height(usernameLabel.getHeight());
        itemsTable.row();
        itemsTable.add(passwordLabel).expandX().colspan(1).padRight(5);
        itemsTable.add(passwordField).padBottom(10).expandX().colspan(1).height(usernameLabel.getHeight());
        itemsTable.row();
        itemsTable.add(errorLabel).expandX().colspan(2).center().padBottom(10);
        itemsTable.row();
        itemsTable.add(guestButton).padBottom(10).height(50).expandX().colspan(2).fill(true);
        itemsTable.row();
        itemsTable.add(logInButton).padBottom(10).height(50).expandX().colspan(1).fill(true);
        itemsTable.add(signUpButton).padBottom(10).height(50).expandX().padLeft(5).colspan(1).fill(true);
        itemsTable.row();
        itemsTable.add(exitButton).expandX().width(tableContainer.getWidth()).colspan(2);
        stage.addActor(tableContainer);
        stage.addActor(musicButton);
    }

    private void addListeners(){
        guestButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                UserController.loginAsGuest();
                game.setScreen(game.getMainMenu());
                super.clicked(event,x,y);
            }
        });
        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                new DialogBox(stage).askToQuit();
                super.clicked(event, x, y);
            }
        });
        signUpButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getSignUpMenu());
                super.clicked(event, x, y);
            }

        });
        logInButton.addListener(new ClickListener(){
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
                    new DialogBox(stage).askToQuit();
                return super.keyDown(event, keycode);
            }
        });
    }

    private void submit(){
        if (UserController.isUsernameAvailable(usernameField.getText())){
            errorLabel.setText("no user with this username!");
            return;
        }
        if (!UserController.loginUser(usernameField.getText(),passwordField.getText())) {
            errorLabel.setText("username and password do not match!");
            return;
        }
        game.setScreen(game.getMainMenu());
    }

    @Override
    public void show(){
        errorLabel.setText("");
        passwordField.setText("");
        usernameField.setText("");
        super.show();
    }

}
