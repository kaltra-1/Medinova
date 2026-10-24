package view;

import controller.LoginController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Perdorues;

public class LoginView extends Application {

    private LoginController controller = new LoginController();
    private Text actionTarget = new Text();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Medinova - Hyrja");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #ffffff);");

        Text header = new Text("Medinova");
        header.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 35));
        header.setFill(Color.web("#1565c0"));
        BorderPane.setAlignment(header, Pos.CENTER);
        BorderPane.setMargin(header, new Insets(40, 0, 10, 0));
        root.setTop(header);

        GridPane card = new GridPane();
        card.setAlignment(Pos.CENTER);
        card.setHgap(10);
        card.setVgap(10);
        card.setPadding(new Insets(30));
        card.setStyle("""
            -fx-background-color: white;
            -fx-background-radius: 15;
            -fx-border-radius: 15;
        """);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(10);
        shadow.setOffsetX(0);
        shadow.setOffsetY(3);
        shadow.setColor(Color.rgb(0,0,0,0.2));
        card.setEffect(shadow);

        Text sceneTitle = new Text("Mirë se vini në sistem");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        sceneTitle.setFill(Color.web("#004c8c"));
        card.add(sceneTitle, 0, 0, 2, 1);

        Label userLabel = new Label("Përdoruesi:");
        card.add(userLabel, 0, 1);

        TextField userTextField = new TextField();
        userTextField.setPromptText("Emri i përdoruesit");
        card.add(userTextField, 1, 1);

        Label pwLabel = new Label("Fjalëkalimi:");
        card.add(pwLabel, 0, 2);

        PasswordField pwBox = new PasswordField();
        pwBox.setPromptText("Fjalëkalimi");
        card.add(pwBox, 1, 2);

        Button btnLogin = new Button("Hyr");
        btnLogin.setPrefWidth(140);
        btnLogin.setStyle("""
            -fx-background-color: #1565c0;
            -fx-text-fill: white;
            -fx-background-radius: 8;
            -fx-font-weight: bold;
        """);

        card.add(btnLogin, 1, 3);

        actionTarget.setFill(Color.FIREBRICK);
        card.add(actionTarget, 1, 4);

        root.setCenter(card);

        btnLogin.setOnAction(e -> {

            String username = userTextField.getText();
            String password = pwBox.getText();

            actionTarget.setText("");

            Perdorues perdorues = controller.login(username, password);


            if (perdorues != null) {

                Stage stage = (Stage) btnLogin.getScene().getWindow();

                try {
                    if (perdorues.getRoli().equalsIgnoreCase("admin")) {
                        new MainDashboard().start(stage);
                    } else {
                        StafDashboard.setPerdorues(perdorues);
                        new StafDashboard().show(stage);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    actionTarget.setFill(Color.RED);
                    actionTarget.setText("Gabim në hapjen e sistemit.");
                }

            } else {
                actionTarget.setFill(Color.RED);
                actionTarget.setText("Përdorues ose fjalëkalim i gabuar.");
            }
        }); 

        Scene scene = new Scene(root, 450, 420);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
