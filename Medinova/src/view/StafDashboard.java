package view;

import controller.MjekController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Perdorues;

public class StafDashboard {

    private static Perdorues perdorues;

    public static void setPerdorues(Perdorues p) {
        perdorues = p;
    }

    public void show(Stage stage) {
        stage.setTitle("Medinova Hospital Staff Dashboard");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e0f7fa, #e3edf7);");

        /* ================= HEADER ================= */
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(20));
        header.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 15;"
        );
        header.setEffect(new DropShadow(10, Color.rgb(0, 0, 0, 0.15)));

        VBox userInfo = new VBox(4);

        Label lblWelcome = new Label("Mirësevini në sistemin Medinova!");
        lblWelcome.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblWelcome.setTextFill(Color.web("#0f172a"));

        Label lblName = new Label(perdorues.getUsername());
        lblName.setFont(Font.font("Segoe UI", FontWeight.BOLD, 20));
        lblName.setTextFill(Color.web("#0f172a"));

        Label lblRole = new Label("Roli: " + perdorues.getRoli());
        lblRole.setFont(Font.font("Segoe UI", 13));
        lblRole.setTextFill(Color.web("#1e88e5"));

        userInfo.getChildren().addAll(lblWelcome, lblName, lblRole);

        Button btnLogout = new Button("Logout");
        btnLogout.setStyle(
                "-fx-background-color: #ef5350;" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 8 25;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );

        btnLogout.setOnAction(e -> {
            try {
                new LoginView().start(stage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(userInfo, spacer, btnLogout);
        root.setTop(header);

        /* ================= CENTER ================= */
        VBox center = new VBox(35);
        center.setAlignment(Pos.TOP_CENTER);
        center.setPadding(new Insets(30, 0, 0, 0));

        // ---------- STATS ----------
        HBox statsRow = new HBox(25);
        statsRow.setAlignment(Pos.CENTER);

        MjekController mc = new MjekController();
        int mjekId = mc.merrMjekIdNgaPerdoruesId(perdorues.getId());
        int todayAppointments = mc.getTodayAppointmentsByDoctor(mjekId);

        VBox todayStat = createStatCard(
                "Takimet e sotme",
                String.valueOf(todayAppointments),
                "#26a69a"
        );

        statsRow.getChildren().addAll(todayStat);

        // ---------- ACTION CARDS ----------
        HBox actions = new HBox(30);
        actions.setAlignment(Pos.CENTER);

        VBox pacientCard = createActionCard(
                "Pacientët e mi",
                "Shiko pacientët e caktuar",
                "#1e88e5"
        );
        pacientCard.setOnMouseClicked(e ->
                new PacientStafView(mjekId).show(stage)
        );

        VBox takimCard = createActionCard(
                "Takimet e mia",
                "Menaxho takimet",
                "#26a69a"
        );
        takimCard.setOnMouseClicked(e ->
                new TakimStafView(mjekId).show(stage)
        );

        actions.getChildren().addAll(pacientCard, takimCard);

        center.getChildren().addAll(statsRow, actions);
        root.setCenter(center);

        stage.setScene(new Scene(root, 950, 600));
        stage.show();
    }

    /* ================= STAT CARD ================= */
    private VBox createStatCard(String title, String value, String color) {

        Label lblValue = new Label(value);
        lblValue.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        lblValue.setTextFill(Color.web("#0f172a"));

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("Segoe UI", 13));
        lblTitle.setTextFill(Color.web("#546e7a"));

        VBox card = new VBox(5, lblValue, lblTitle);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setPrefSize(260, 140);

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: " + color + ";" +
                "-fx-border-width: 0 0 0 6;" +
                "-fx-background-radius: 14;" +
                "-fx-border-radius: 14;"
        );

        card.setEffect(new DropShadow(12, Color.rgb(0, 0, 0, 0.15)));
        return card;
    }

    /* ================= ACTION CARD ================= */
    private VBox createActionCard(String title, String subtitle, String accent) {

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        lblTitle.setTextFill(Color.web("#0f172a"));

        Label lblSub = new Label(subtitle);
        lblSub.setFont(Font.font("Segoe UI", 12));
        lblSub.setTextFill(Color.web("#607d8b"));

        VBox card = new VBox(10, lblTitle, lblSub);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(25));
        card.setPrefSize(280, 160);

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-color: " + accent + ";" +
                "-fx-border-width: 0 0 0 6;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-cursor: hand;"
        );

        DropShadow shadow = new DropShadow(14, Color.rgb(0, 0, 0, 0.18));
        card.setEffect(shadow);

        card.setOnMouseEntered(e -> {
            card.setTranslateY(-5);
            card.setEffect(new DropShadow(22, Color.rgb(0, 0, 0, 0.25)));
        });

        card.setOnMouseExited(e -> {
            card.setTranslateY(0);
            card.setEffect(shadow);
        });

        return card;
    }
}
