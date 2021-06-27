package com.sharif.ce.pac.man.controller;

import com.sharif.ce.pac.man.model.User;

import java.util.ArrayList;

public class UserController {
    private static ArrayList<User> users;
    private static User loggedUser;

    public static boolean loginUser(String username,String password){
        for(User user : users){
            if (user.getUsername().equals(username) && user.getPassword().equals(password)){
                loggedUser = user;
                return true;
            }
        }
        return false;
    }

    public static void loginAsGuest(){
        loggedUser = new User();
    }

    public static void deleteUser(User user){
        System.out.println(users.size());
        for(int i = 0;i<users.size();++i){
            if (users.get(i).getUsername().equals(user.getUsername())){
                users.remove(i);
                return;
            }
        }
        System.out.println(users.size());
    }

    public static void logout(){
        loggedUser = null;
    }

    public static boolean isUsernameAvailable(String username){
        for(User user :users){
            if (user.getUsername().equals(username))
                return false;
        }
        return true;
    }

    public static boolean registerUser(String username,String password){
        User user = new User(username,password);
        users.add(user);
        return true;
    }

    public static void changePassword(User user,String password){
        user.setPassword(password);
    }

    public static void setUsers(ArrayList<User> users) {
        UserController.users = users;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(User loggedUser) {
        UserController.loggedUser = loggedUser;
    }
}
