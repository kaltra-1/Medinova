package view;

import controller.TakimController;
import controller.PacientController;
import controller.MjekController;
import model.Takim;
import model.Pacient;
import model.Mjek;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakimView extends VBox {

    private TableView<Takim> tabelaTakimeve;
    private ComboBox<String> cbPacienti, cbMjeku;
    private TextField tfArsyeja, tfStatusi, tfKerko;
    private DatePicker dpData;
    private TakimController controller = new TakimController();
    private ObservableList<Takim> listaTakimeve;

    private Map<String, Integer> pacientMap = new HashMap<>();
    private Map<String, Integer> mjekMap = new HashMap<>();

    public TakimView() {
        setSpacing(10);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: #f0f0f0;");

        createSearchBar();
        createTable();
        createForm();
        createButtons();

        ngarkoPacientetDheMjeket();
        ngarkoTakimet();
    }

    // ------------------- SEARCH -------------------
    private void createSearchBar() {
        tfKerko = new TextField();
        tfKerko.setPromptText("Kërko takime sipas ID Pacienti ose ID Mjeku...");
        tfKerko.textProperty().addListener((obs, oldVal, newVal) -> filtroTakimet(newVal));

        HBox searchBox = new HBox(tfKerko);
        searchBox.setPadding(new Insets(10));
        getChildren().add(searchBox);
    }

    // ------------------- TABLE -------------------
    private void createTable() {
        tabelaTakimeve = new TableView<>();

        TableColumn<Takim, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Takim, Integer> colPacient = new TableColumn<>("Pacient ID");
        colPacient.setCellValueFactory(new PropertyValueFactory<>("pacientId"));

        TableColumn<Takim, Integer> colMjek = new TableColumn<>("Mjek ID");
        colMjek.setCellValueFactory(new PropertyValueFactory<>("mjekId"));

        TableColumn<Takim, LocalDateTime> colDataOra = new TableColumn<>("Data / Ora");
        colDataOra.setCellValueFactory(new PropertyValueFactory<>("dataOra"));

        TableColumn<Takim, String> colArsyeja = new TableColumn<>("Arsyeja");
        colArsyeja.setCellValueFactory(new PropertyValueFactory<>("arsyeja"));

        TableColumn<Takim, String> colStatusi = new TableColumn<>("Statusi");
        colStatusi.setCellValueFactory(new PropertyValueFactory<>("statusi"));

        tabelaTakimeve.getColumns().addAll(colId, colPacient, colMjek, colDataOra, colArsyeja, colStatusi);
        tabelaTakimeve.setPrefHeight(250);

        // Stilizo sipas statusit
        tabelaTakimeve.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Takim item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    switch (item.getStatusi().toLowerCase()) {
                        case "planifikuar" -> setStyle("-fx-background-color: #BBDEFB;"); // light blue
                        case "kryer" -> setStyle("-fx-background-color: #C8E6C9;"); // light green
                        case "anuluar" -> setStyle("-fx-background-color: #FFCDD2;"); // light red
                        default -> setStyle("");
                    }
                }
            }
        });

        tabelaTakimeve.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) populoFormularin(newSel);
        });

        getChildren().add(tabelaTakimeve);
    }

    // ------------------- FORM -------------------
    private void createForm() {
        cbPacienti = new ComboBox<>();
        cbPacienti.setPromptText("Zgjidh pacientin");

        cbMjeku = new ComboBox<>();
        cbMjeku.setPromptText("Zgjidh mjekun");

        dpData = new DatePicker();
        dpData.setPromptText("Data");
        dpData.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                filtroMjeketSipasDisponueshmerise(newDate);
            }
        });


        tfArsyeja = new TextField();
        tfArsyeja.setPromptText("Arsyeja");

        tfStatusi = new TextField();
        tfStatusi.setPromptText("Statusi (Planifikuar/Kryer/Anuluar)");

        HBox form = new HBox(10, cbPacienti, cbMjeku, dpData, tfArsyeja, tfStatusi);
        form.setPadding(new Insets(10));
        getChildren().add(form);
    }

    // ------------------- BUTTONS -------------------
    private void createButtons() {
        Button btnShto = new Button("Shto");
        btnShto.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        btnShto.setOnAction(e -> shtoTakim());

        Button btnPerditeso = new Button("Përditëso");
        btnPerditeso.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        btnPerditeso.setOnAction(e -> perditesoTakim());

        Button btnFshi = new Button("Fshi");
        btnFshi.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        btnFshi.setOnAction(e -> fshiTakim());

        HBox butonat = new HBox(10, btnShto, btnPerditeso, btnFshi);
        butonat.setPadding(new Insets(10));
        getChildren().add(butonat);
    }

    // ------------------- LOAD DATA -------------------
    private void ngarkoPacientetDheMjeket() {
        PacientController pc = new PacientController();
        for (Pacient p : pc.merrTeGjithePacientet()) {
            String emri = p.getEmer() + " " + p.getMbiemer();
            pacientMap.put(emri, p.getId());
            cbPacienti.getItems().add(emri);
        }

        MjekController mc = new MjekController();
        for (Mjek m : mc.merrTeGjitheMjeket()) {
            String emri = m.getEmer() + " " + m.getMbiemer() + " (" + m.getSpecializimi() + ")";
            mjekMap.put(emri, m.getId());
            cbMjeku.getItems().add(emri);
        }
    }

    private void ngarkoTakimet() {
        List<Takim> lista = controller.merrTeGjithaTakimet();
        listaTakimeve = FXCollections.observableArrayList(lista);
        tabelaTakimeve.setItems(listaTakimeve);
    }

    // ------------------- ACTIONS -------------------
    private void populoFormularin(Takim t) {
        cbPacienti.setValue(getPacientName(t.getPacientId()));
        cbMjeku.setValue(getMjekName(t.getMjekId()));
        dpData.setValue(t.getDataOra().toLocalDate());
        tfArsyeja.setText(t.getArsyeja());
        tfStatusi.setText(t.getStatusi());
    }

    private void pastroniFormularin() {
        cbPacienti.setValue(null);
        cbMjeku.setValue(null);
        dpData.setValue(null);
        tfArsyeja.clear();
        tfStatusi.clear();
    }

    private void shtoTakim() {
        try {
            if (cbPacienti.getValue() == null || cbMjeku.getValue() == null || dpData.getValue() == null) {
                shfaqAlert("Gabim", "Zgjidh pacientin, mjekun dhe datën!");
                return;
            }

            Takim t = new Takim();
            t.setPacientId(pacientMap.get(cbPacienti.getValue()));
            t.setMjekId(mjekMap.get(cbMjeku.getValue()));
            t.setDataOra(dpData.getValue().atStartOfDay());
            t.setArsyeja(tfArsyeja.getText());
            t.setStatusi(tfStatusi.getText());

            if (controller.shtoTakim(t)) {
                ngarkoTakimet();
                pastroniFormularin();
            } else {
                shfaqAlert("Gabim", "Nuk mund të shtohet takimi.");
            }
        } catch (Exception e) {
            shfaqAlert("Gabim", "Kontrollo të dhënat e futura!");
        }
    }

    private void perditesoTakim() {
        Takim t = tabelaTakimeve.getSelectionModel().getSelectedItem();
        if (t == null) return;

        t.setPacientId(pacientMap.get(cbPacienti.getValue()));
        t.setMjekId(mjekMap.get(cbMjeku.getValue()));
        t.setDataOra(dpData.getValue().atStartOfDay());
        t.setArsyeja(tfArsyeja.getText());
        t.setStatusi(tfStatusi.getText());

        if (controller.perditesoTakim(t)) {
            ngarkoTakimet();
            pastroniFormularin();
        }
    }

    private void fshiTakim() {
        Takim t = tabelaTakimeve.getSelectionModel().getSelectedItem();
        if (t == null) return;

        if (controller.fshiTakim(t.getId())) {
            ngarkoTakimet();
            pastroniFormularin();
        }
    }

    private void filtroTakimet(String query) {
        if (query == null || query.isEmpty()) {
            tabelaTakimeve.setItems(listaTakimeve);
            return;
        }

        ObservableList<Takim> filtruar = FXCollections.observableArrayList();
        for (Takim t : listaTakimeve) {
            if (String.valueOf(t.getPacientId()).contains(query) || String.valueOf(t.getMjekId()).contains(query)) {
                filtruar.add(t);
            }
        }
        tabelaTakimeve.setItems(filtruar);
    }

    private void shfaqAlert(String title, String mesazh) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(mesazh);
        alert.showAndWait();
    }

    // ------------------- HELPERS -------------------
    private String getPacientName(int id) {
        return pacientMap.entrySet().stream()
                .filter(e -> e.getValue() == id)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
    }
    
    private void filtroMjeketSipasDisponueshmerise(java.time.LocalDate data) {
        MjekController mc = new MjekController();
        List<Mjek> teGjitheMjeket = mc.merrTeGjitheMjeket();
        
        // Merr listën e mjekëve që kanë takime në atë datë
        List<Takim> takimetNeDate = controller.merrTakimetNeDate(data);
        ObservableList<String> mjeketLirë = FXCollections.observableArrayList();

        for (Mjek m : teGjitheMjeket) {
            boolean iLirë = takimetNeDate.stream().noneMatch(t -> t.getMjekId() == m.getId());
            if (iLirë) {
                String emri = m.getEmer() + " " + m.getMbiemer() + " (" + m.getSpecializimi() + ")";
                mjeketLirë.add(emri);
                mjekMap.put(emri, m.getId());
            }
        }

        cbMjeku.setItems(mjeketLirë);
        cbMjeku.setValue(null); // pastron përzgjedhjen e mëparshme
    }


    private String getMjekName(int id) {
        return mjekMap.entrySet().stream()
                .filter(e -> e.getValue() == id)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse("");
    }
}
