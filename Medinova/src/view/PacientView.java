package view;

import controller.PacientController;
import model.Pacient;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;

public class PacientView extends BorderPane {

    private PacientController controller = new PacientController();

    // Input fields
    private TextField tfId = new TextField();
    private TextField tfEmer = new TextField();
    private TextField tfMbiemer = new TextField();
    private DatePicker dpDatelindje = new DatePicker();
    private TextField tfAdresa = new TextField();
    private TextField tfTelefon = new TextField();
    private ComboBox<String> cbGrupiGjakut = new ComboBox<>();
    private int mjekId;

    private TableView<Pacient> tabelaPacient = new TableView<>();

    public PacientView() {
        this.setPadding(new Insets(20));

        // Title
        Label title = new Label("Menaxhimi i Pacientëve");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setPadding(new Insets(10));

        // Form for input
        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: #F4F4F4; -fx-border-color: #CCCCCC;");
        form.setPrefWidth(350);

        tfId.setPromptText("ID");
        tfEmer.setPromptText("Emri");
        tfMbiemer.setPromptText("Mbiemri");
        dpDatelindje.setPromptText("Datëlindja");
        tfAdresa.setPromptText("Adresa");
        tfTelefon.setPromptText("Telefoni");

        cbGrupiGjakut.getItems().addAll("A+", "A-", "B+", "B-", "AB+", "AB-", "0+", "0-");
        cbGrupiGjakut.setPromptText("Grupi i gjakut");

        // Buttons
        Button btnKerko = new Button("Kërko");
        Button btnShto = new Button("Shto");
        Button btnPerditeso = new Button("Përditëso");
        Button btnFshi = new Button("Fshi");

        btnKerko.setStyle("-fx-background-color:#6c5ce7; -fx-text-fill:white;");
        btnShto.setStyle("-fx-background-color:#2D8B57; -fx-text-fill:white;");
        btnPerditeso.setStyle("-fx-background-color:#4A90E2; -fx-text-fill:white;");
        btnFshi.setStyle("-fx-background-color:#D9534F; -fx-text-fill:white;");

        HBox buttonBox = new HBox(10, btnKerko, btnShto, btnPerditeso, btnFshi);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        form.getChildren().addAll(
                new Label("Shto / Përditëso Pacient:"),
                tfId, tfEmer, tfMbiemer, dpDatelindje, tfAdresa, tfTelefon, cbGrupiGjakut,
                buttonBox
        );

        // TABLE
        tabelaPacient.setPlaceholder(new Label("Nuk ka pacientë."));

        TableColumn<Pacient, Number> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(d -> new javafx.beans.property.SimpleIntegerProperty(d.getValue().getId()));

        TableColumn<Pacient, String> colEmer = new TableColumn<>("Emri");
        colEmer.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getEmer()));

        TableColumn<Pacient, String> colMbiemer = new TableColumn<>("Mbiemri");
        colMbiemer.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getMbiemer()));

        TableColumn<Pacient, String> colDatelindje = new TableColumn<>("Datëlindja");
        colDatelindje.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getDatelindje().toString()));

        TableColumn<Pacient, String> colAdresa = new TableColumn<>("Adresa");
        colAdresa.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getAdresa()));

        TableColumn<Pacient, String> colTel = new TableColumn<>("Telefoni");
        colTel.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTelefon()));

        TableColumn<Pacient, String> colGrupi = new TableColumn<>("Grupi i gjakut");
        colGrupi.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getGrupiGjakut()));

        tabelaPacient.getColumns().addAll(colId, colEmer, colMbiemer, colDatelindje, colAdresa, colTel, colGrupi);

        // Layout
        this.setTop(title);
        this.setLeft(form);
        this.setCenter(tabelaPacient);

        // EVENTS ----------------------------------------------------------------

        // 1. KËRKO
        btnKerko.setOnAction(e -> {
            try {
                int id = Integer.parseInt(tfId.getText());
                Pacient p = controller.merrPacientById(id);

                if (p != null) {
                    tfEmer.setText(p.getEmer());
                    tfMbiemer.setText(p.getMbiemer());
                    dpDatelindje.setValue(p.getDatelindje());
                    tfAdresa.setText(p.getAdresa());
                    tfTelefon.setText(p.getTelefon());
                    cbGrupiGjakut.setValue(p.getGrupiGjakut());
                } else {
                    showAlert("Kërkim", "Nuk u gjet pacient me këtë ID.", Alert.AlertType.WARNING);
                }
            } catch (Exception ex) {
                showAlert("Gabim", "ID e pavlefshme!", Alert.AlertType.ERROR);
            }
        });

        // 2. SHTO
        btnShto.setOnAction(e -> {
            if (!validInput()) {
                showAlert("Gabim", "Plotëso të gjitha fushat!", Alert.AlertType.WARNING);
                return;
            }

            Pacient p = new Pacient(
                0,
                tfEmer.getText(),
                tfMbiemer.getText(),
                dpDatelindje.getValue(),
                tfAdresa.getText(),
                tfTelefon.getText(),
                cbGrupiGjakut.getValue(),
                mjekId
            );

            if (controller.shtoPacient(p)) {
                showAlert("Sukses", "Pacienti u shtua!", Alert.AlertType.INFORMATION);
                clearInputs();
                ngarkoListen();
            } else {
                showAlert("Gabim", "Pacienti nuk u shtua!", Alert.AlertType.ERROR);
            }
        });
    

        // 3. PËRDITËSO
        btnPerditeso.setOnAction(e -> {
            Pacient p = tabelaPacient.getSelectionModel().getSelectedItem();
            if (p == null) {
                showAlert("Gabim", "Zgjidh një pacient nga tabela!", Alert.AlertType.WARNING);
                return;
            }

            p.setEmer(tfEmer.getText());
            p.setMbiemer(tfMbiemer.getText());
            p.setDatelindje(dpDatelindje.getValue());
            p.setAdresa(tfAdresa.getText());
            p.setTelefon(tfTelefon.getText());
            p.setGrupiGjakut(cbGrupiGjakut.getValue());

            if (controller.perditesoPacient(p)) {
                showAlert("Sukses", "Pacienti u përditësua!", Alert.AlertType.INFORMATION);
                clearInputs();
                ngarkoListen();
            }
        });

        // 4. FSHI
        btnFshi.setOnAction(e -> {
            Pacient p = tabelaPacient.getSelectionModel().getSelectedItem();
            if (p == null) {
                showAlert("Gabim", "Zgjidh një pacient!", Alert.AlertType.WARNING);
                return;
            }

            if (controller.fshiPacient(p.getId())) {
                showAlert("Sukses", "Pacienti u fshi!", Alert.AlertType.INFORMATION);
                clearInputs();
                ngarkoListen();
            }
        });

        // Klikim në tabelë → mbush inputet
        tabelaPacient.setOnMouseClicked(e -> {
            Pacient p = tabelaPacient.getSelectionModel().getSelectedItem();
            if (p != null) {
                tfId.setText(String.valueOf(p.getId()));
                tfEmer.setText(p.getEmer());
                tfMbiemer.setText(p.getMbiemer());
                dpDatelindje.setValue(p.getDatelindje());
                tfAdresa.setText(p.getAdresa());
                tfTelefon.setText(p.getTelefon());
                cbGrupiGjakut.setValue(p.getGrupiGjakut());
            }
        });

        ngarkoListen();
    }

    // Load DB
    private void ngarkoListen() {
        tabelaPacient.getItems().setAll(controller.merrTeGjithePacientet());
    }

    // Validation
    private boolean validInput() {
        return !tfEmer.getText().isEmpty()
                && !tfMbiemer.getText().isEmpty()
                && dpDatelindje.getValue() != null
                && !tfAdresa.getText().isEmpty()
                && !tfTelefon.getText().isEmpty()
                && cbGrupiGjakut.getValue() != null;
    }

    private void clearInputs() {
        tfId.clear();
        tfEmer.clear();
        tfMbiemer.clear();
        dpDatelindje.setValue(null);
        tfAdresa.clear();
        tfTelefon.clear();
        cbGrupiGjakut.setValue(null);
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }
}
