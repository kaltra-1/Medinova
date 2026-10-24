package view;

import controller.PacientController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Pacient;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PacientStafView {

    private int mjekId;

    public PacientStafView(int mjekId) {
        this.mjekId = mjekId;
    }

    public void show(Stage stage) {
        stage.setTitle("Pacientët e mi");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #e0f7fa, #f0f4f7);");

        // ================= HEADER =================
        HBox header = new HBox();
        header.setPadding(new Insets(15));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12;"
        );
        header.setEffect(new DropShadow(5, Color.rgb(0,0,0,0.15)));

        Text lblTitle = new Text("Pacientët e mi");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        lblTitle.setFill(Color.web("#0f172a"));
        header.getChildren().add(lblTitle);

        root.setTop(header);

        // ================= PACIENT CARDS =================
        List<Pacient> pacientetMjekut = new PacientController().merrPacientetSipasMjekut(mjekId);
        ObservableList<Pacient> data = FXCollections.observableArrayList(pacientetMjekut);

        FlowPane cardsPane = new FlowPane();
        cardsPane.setHgap(20);
        cardsPane.setVgap(20);
        cardsPane.setPadding(new Insets(20));
        cardsPane.setAlignment(Pos.TOP_CENTER);

        for (Pacient p : data) {
            VBox card = createPacientCard(p);
            cardsPane.getChildren().add(card);
        }

        ScrollPane scrollPane = new ScrollPane(cardsPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        root.setCenter(scrollPane);

        // ================= BUTON KTHEHU =================
        Button btnKthehu = new Button("⬅ Kthehu");
        btnKthehu.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        btnKthehu.setStyle(
                "-fx-background-color: #1e88e5;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 8;" +
                "-fx-cursor: hand;"
        );
        btnKthehu.setEffect(new DropShadow(4, Color.rgb(0,0,0,0.2)));
        btnKthehu.setOnMouseEntered(e -> btnKthehu.setStyle(
                "-fx-background-color: #1565c0;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 8;"
        ));
        btnKthehu.setOnMouseExited(e -> btnKthehu.setStyle(
                "-fx-background-color: #1e88e5;" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 8;"
        ));
        btnKthehu.setOnAction(e -> {
            StafDashboard stafDashboard = new StafDashboard();
            stafDashboard.show(stage);
        });

        HBox bottom = new HBox(btnKthehu);
        bottom.setPadding(new Insets(15));
        bottom.setAlignment(Pos.CENTER_LEFT);
        root.setBottom(bottom);

        Scene scene = new Scene(root, 950, 600);
        stage.setScene(scene);
        stage.show();
    }

    // ================= METODA PËR KRIJIMIN E CARD =================
    private VBox createPacientCard(Pacient p) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setPrefSize(220, 180);
        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 16;" +
                "-fx-border-radius: 16;" +
                "-fx-border-color: #1e88e5;" +
                "-fx-border-width: 2;"
        );
        card.setEffect(new DropShadow(6, Color.rgb(0,0,0,0.15)));

        // Hover effect
        card.setOnMouseEntered(e -> card.setEffect(new DropShadow(12, Color.rgb(0,0,0,0.25))));
        card.setOnMouseExited(e -> card.setEffect(new DropShadow(6, Color.rgb(0,0,0,0.15))));

        Text name = new Text(p.getEmer() + " " + p.getMbiemer());
        name.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        name.setFill(Color.web("#0f172a"));

        Text dob = new Text("Datëlindja: " + (p.getDatelindje() != null ?
                p.getDatelindje().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "-"));
        dob.setFont(Font.font("Segoe UI", 12));
        dob.setFill(Color.web("#607d8b"));

        Text adresa = new Text("Adresa: " + p.getAdresa());
        adresa.setFont(Font.font("Segoe UI", 12));
        adresa.setFill(Color.web("#607d8b"));

        Text telefon = new Text("Telefon: " + p.getTelefon());
        telefon.setFont(Font.font("Segoe UI", 12));
        telefon.setFill(Color.web("#607d8b"));

        Text grupi = new Text("Grupi Gjakut: " + p.getGrupiGjakut());
        grupi.setFont(Font.font("Segoe UI", 12));
        grupi.setFill(Color.web("#607d8b"));

        card.getChildren().addAll(name, dob, adresa, telefon, grupi);

        return card;
    }
}
