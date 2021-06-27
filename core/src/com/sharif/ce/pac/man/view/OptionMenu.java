package com.sharif.ce.pac.man.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.sharif.ce.pac.man.Pacman;
import com.sharif.ce.pac.man.controller.AssetController;
import com.sharif.ce.pac.man.controller.UserController;
import com.sharif.ce.pac.man.model.MenuButton;

public class OptionMenu extends Menu{

    private Label usernameLabel;
    private Label passwordLabel;
    private Label newPasswordLabel;
    private Label newPasswordRepeatLabel;
    private Label errorLabel;
    private TextField passwordField;
    private TextField newPasswordField;
    private TextField newPasswordRepeatField;
    private MenuButton backButton;
    private MenuButton saveButton;
    private MenuButton deleteUserButton;
    private Table itemsTable;
    private Container<Table> tableContainer;

    public OptionMenu(Pacman game) {
        super(game);
        initializeWidgets();
        arrangeWidgets();
        addListeners();
    }

    private void initializeWidgets(){
        tableContainer = new Container<>();
        itemsTable = new Table();
        usernameLabel = new Label(UserController.getLoggedUser().getUsername(), AssetController.getDefaultSkin(),"title");
        passwordLabel = new Label("Password",AssetController.getDefaultSkin(),"title");
        newPasswordLabel = new Label("New Password",AssetController.getDefaultSkin(),"title");
        newPasswordRepeatLabel = new Label("Repeat New Password",AssetController.getDefaultSkin(),"title");
        errorLabel = new Label("",AssetController.getDefaultSkin());
        errorLabel.setAlignment(2);
        passwordField = new TextField("",AssetController.getDefaultSkin());
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        newPasswordField = new TextField("",AssetController.getDefaultSkin());
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');
        newPasswordRepeatField = new TextField("",AssetController.getDefaultSkin());
        newPasswordRepeatField.setPasswordMode(true);
        newPasswordRepeatField.setPasswordCharacter('*');
        backButton = new MenuButton("Back",AssetController.getButtonSkin());
        saveButton = new MenuButton("Save Changes",AssetController.getButtonSkin());
        deleteUserButton = new MenuButton("Delete User",AssetController.getButtonSkin());
        tableContainer.setActor(itemsTable);
    }

    private void arrangeWidgets(){
        int windowWidth = Gdx.graphics.getWidth(),windowHeight = Gdx.graphics.getHeight();
        tableContainer.setSize(windowWidth*0.7f,windowHeight*0.6f);
        tableContainer.setPosition(windowWidth/2-tableContainer.getWidth()*0.5f,(windowHeight-tableContainer.getHeight()*0.5f)/2);
        itemsTable.setFillParent(true);
        itemsTable.add(usernameLabel).expandX().colspan(7).center();
        itemsTable.row();
        itemsTable.add(passwordLabel).colspan(3).left().padRight(5);
        itemsTable.add(passwordField).height(passwordLabel.getHeight()-5).fillX().colspan(4).padBottom(5);
        itemsTable.row();
        itemsTable.add(newPasswordLabel).colspan(3).left().padRight(5);
        itemsTable.add(newPasswordField).height(newPasswordLabel.getHeight()-5).fillX().colspan(4).padBottom(5);
        itemsTable.row();
        itemsTable.add(newPasswordRepeatLabel).colspan(3).left().padRight(10);
        itemsTable.add(newPasswordRepeatField).height(newPasswordRepeatLabel.getHeight()-5).fillX().colspan(4).padBottom(5);
        itemsTable.row();
        itemsTable.add(errorLabel).colspan(7).fillX().expandX().padBottom(5).center();
        itemsTable.row();
        itemsTable.add(backButton).height(50).fillX().colspan(2).padRight(5);
        itemsTable.add(saveButton).height(50).fillX().colspan(3).padRight(5);
        itemsTable.add(deleteUserButton).height(50).fillX().colspan(2);
        itemsTable.row();
        stage.addActor(tableContainer);
        stage.addActor(musicButton);
    }

    private void addListeners(){
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getMainMenu());
            }
        });
        saveButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!passwordField.getText().equals(UserController.getLoggedUser().getPassword())){
                    errorLabel.setText("Password isn't correct!");
                    return;
                }
                if (newPasswordField.getText().length()<3){
                    errorLabel.setText("Too weak password!");
                    return;
                }
                if (!newPasswordField.getText().equals(newPasswordRepeatField.getText())){
                    errorLabel.setText("Repeat password doesn't match!");
                    return;
                }
                changePassword(newPasswordField.getText());
            }
        });
        deleteUserButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!passwordField.getText().equals(UserController.getLoggedUser().getPassword())){
                    errorLabel.setText("The password isn't correct!");
                    return;
                }
                new DialogBox(stage).confirmationDialog("Are you sure , you want to delete user \""
                + usernameLabel.getText() + "\" ?",new ClickListener(){
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        UserController.deleteUser(UserController.getLoggedUser());
                        UserController.logout();
                        game.setScreen(game.getStartMenu());
                    }
                });
            }
        });
    }

    private void changePassword(String newPassword){
        UserController.changePassword(UserController.getLoggedUser(),newPassword);
        new DialogBox(stage).showMessage("Password successfully changed!",new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(game.getMainMenu());
            }
        });
    }

    @Override
    public void show() {
        passwordField.setText("");
        newPasswordField.setText("");
        newPasswordRepeatField.setText("");
        errorLabel.setText("");
        super.show();
    }
}
