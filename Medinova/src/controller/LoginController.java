package controller;

import model.Perdorues;

public class LoginController {

    private PerdoruesController perdoruesController = new PerdoruesController();

    public Perdorues login(String username, String password) {
        return perdoruesController.login(username, password);
    }
}
