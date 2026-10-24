package view;

import controller.PerdoruesController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Perdorues;

import java.util.List;

public class PerdoruesView extends VBox {

    private TextField tfUsername;
    private PasswordField pfPassword;
    private ComboBox<String> cbRoli;
    private Button btnShto, btnEdito, btnFshije;

    private TableView<Perdorues> tabelaPerdoruesit;
    private ObservableList<Perdorues> listaPerdoruesit;

    private PerdoruesController controller;

    public PerdoruesView() {
        controller = new PerdoruesController();

        setPadding(new Insets(20));
        setSpacing(15);
        setAlignment(Pos.TOP_CENTER);

        Label lblTitle = new Label("Shto Përdorues të Ri");
        lblTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // -------------------- FUSHAT -------------------------
        tfUsername = new TextField();
        tfUsername.setPromptText("Username");
        tfUsername.setMaxWidth(300);

        pfPassword = new PasswordField();
        pfPassword.setPromptText("Password");
        pfPassword.setMaxWidth(300);

        cbRoli = new ComboBox<>();
        cbRoli.getItems().addAll("admin", "mjek");
        cbRoli.setPromptText("Zgjidh rolin");
        cbRoli.setMaxWidth(300);

        // -------------------- BUTONAT -------------------------
        btnShto = new Button("Shto");
        btnShto.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        btnShto.setMaxWidth(90);
        btnShto.setOnAction(e -> shtoPerdorues());

        btnEdito = new Button("Edito");
        btnEdito.setStyle("-fx-background-color: #f39c12; -fx-text-fill: white;");
        btnEdito.setMaxWidth(90);
        btnEdito.setOnAction(e -> editoPerdorues());

        btnFshije = new Button("Fshije");
        btnFshije.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white;");
        btnFshije.setMaxWidth(90);
        btnFshije.setOnAction(e -> fshiPerdorues());

        HBox hboxButtons = new HBox(10, btnShto, btnEdito, btnFshije);
        hboxButtons.setAlignment(Pos.CENTER);

        getChildren().addAll(
                lblTitle,
                tfUsername,
                pfPassword,
                cbRoli,
                hboxButtons
        );

        krijoTabela();
        ngarkoPerdoruesit();
    }

    private void krijoTabela() {
        tabelaPerdoruesit = new TableView<>();
        tabelaPerdoruesit.setMaxWidth(500);
        tabelaPerdoruesit.setPrefHeight(250);

        TableColumn<Perdorues, String> colUsername = new TableColumn<>("Username");
        colUsername.setPrefWidth(150);
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Perdorues, String> colPassword = new TableColumn<>("Password");
        colPassword.setPrefWidth(150);
        colPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<Perdorues, String> colRoli = new TableColumn<>("Roli");
        colRoli.setPrefWidth(120);
        colRoli.setCellValueFactory(new PropertyValueFactory<>("roli"));

        tabelaPerdoruesit.getColumns().addAll(colUsername, colPassword, colRoli);

        getChildren().add(tabelaPerdoruesit);
    }

    private void ngarkoPerdoruesit() {
        List<Perdorues> perdoruesit = controller.merrTeGjithePerdoruesit();
        listaPerdoruesit = FXCollections.observableArrayList(perdoruesit);
        tabelaPerdoruesit.setItems(listaPerdoruesit);
    }

    private void shtoPerdorues() {
        String username = tfUsername.getText().trim();
        String password = pfPassword.getText().trim();
        String roli = cbRoli.getValue();

        if (username.isEmpty() || password.isEmpty() || roli == null) {
            showAlert(Alert.AlertType.WARNING, "Gabim", "Ju lutem plotësoni të gjitha fushat!");
            return;
        }

        Perdorues p = new Perdorues(username, password, roli);
        boolean sukses = controller.shtoPerdorues(p);

        if (sukses) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Përdoruesi u shtua me sukses!");
            tfUsername.clear();
            pfPassword.clear();
            cbRoli.setValue(null);
            ngarkoPerdoruesit();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Pati problem gjatë shtimit në databazë!");
        }
    }

    private void editoPerdorues() {
        Perdorues zgjedhur = tabelaPerdoruesit.getSelectionModel().getSelectedItem();
        if (zgjedhur == null) {
            showAlert(Alert.AlertType.WARNING, "Gabim", "Zgjidhni një përdorues nga tabela.");
            return;
        }

        TextInputDialog editUsername = new TextInputDialog(zgjedhur.getUsername());
        editUsername.setHeaderText("Ndrysho Username");
        String newUsername = editUsername.showAndWait().orElse(null);
        if (newUsername == null) return;

        TextInputDialog editPass = new TextInputDialog(zgjedhur.getPassword());
        editPass.setHeaderText("Ndrysho Password");
        String newPassword = editPass.showAndWait().orElse(null);
        if (newPassword == null) return;

        ChoiceDialog<String> editRole = new ChoiceDialog<>(zgjedhur.getRoli(), "admin", "mjek");
        editRole.setHeaderText("Ndrysho Rolin");
        String newRoli = editRole.showAndWait().orElse(null);
        if (newRoli == null) return;

        zgjedhur.setUsername(newUsername);
        zgjedhur.setPassword(newPassword);
        zgjedhur.setRoli(newRoli);

        if (controller.perditesoPerdorues(zgjedhur)) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Përdoruesi u përditësua me sukses!");
            ngarkoPerdoruesit();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Pati problem gjatë përditësimit!");
        }
    }

    private void fshiPerdorues() {
        Perdorues zgjedhur = tabelaPerdoruesit.getSelectionModel().getSelectedItem();
        if (zgjedhur == null) {
            showAlert(Alert.AlertType.WARNING, "Gabim", "Zgjidhni një përdorues nga tabela.");
            return;
        }

        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Konfirmim");
        conf.setContentText("A jeni të sigurt që doni ta fshini përdoruesin: " + zgjedhur.getUsername() + "?");

        if (conf.showAndWait().get() == ButtonType.OK) {
            if (controller.fshiPerdorues(zgjedhur.getUsername())) {
                listaPerdoruesit.remove(zgjedhur);
                showAlert(Alert.AlertType.INFORMATION, "Sukses", "Përdoruesi u fshi me sukses!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Gabim", "Pati problem gjatë fshirjes!");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
