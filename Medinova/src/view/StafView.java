package view;

import controller.MjekController;
import model.Mjek;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;
import java.util.List;

public class StafView extends VBox {

    private TableView<Mjek> tabelaMjekeve;
    private TextField tfEmer, tfMbiemer, tfSpecializimi, tfTelefoni, tfEmail, tfKerko;
    private DatePicker dpDataFillimit;
    private MjekController controller = new MjekController();
    private ObservableList<Mjek> listaMjekeve;

    public StafView() {
        setSpacing(10);
        setPadding(new Insets(15));
        setStyle("-fx-background-color: #f0f0f0;");

        // Titulli i madh në krye
        Label titulli = new Label("Menaxhimi i Stafit");
        titulli.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        getChildren().add(titulli); 

        createSearchBar();
        createTable();
        createForm();
        createButtons();

        ngarkoMjeket();
    }

    
    private void createSearchBar() {
        tfKerko = new TextField();
        tfKerko.setPromptText("Kërko mjek sipas emrit ose mbiemrit...");
        tfKerko.textProperty().addListener((obs, oldVal, newVal) -> filtroMjeket(newVal));

        HBox searchBox = new HBox(tfKerko);
        searchBox.setPadding(new Insets(10));
        getChildren().add(searchBox);
    }

    private void createTable() {
        tabelaMjekeve = new TableView<>();
        TableColumn<Mjek, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colId.setMinWidth(50);

        TableColumn<Mjek, String> colEmer = new TableColumn<>("Emër");
        colEmer.setCellValueFactory(new PropertyValueFactory<>("emer"));

        TableColumn<Mjek, String> colMbiemer = new TableColumn<>("Mbiemër");
        colMbiemer.setCellValueFactory(new PropertyValueFactory<>("mbiemer"));

        TableColumn<Mjek, String> colSpec = new TableColumn<>("Specializimi");
        colSpec.setCellValueFactory(new PropertyValueFactory<>("specializimi"));

        TableColumn<Mjek, String> colTel = new TableColumn<>("Telefoni");
        colTel.setCellValueFactory(new PropertyValueFactory<>("telefoni"));

        TableColumn<Mjek, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Mjek, LocalDate> colData = new TableColumn<>("Data Fillimit");
        colData.setCellValueFactory(new PropertyValueFactory<>("dataFillimitPune"));

        tabelaMjekeve.getColumns().addAll(colId, colEmer, colMbiemer, colSpec, colTel, colEmail, colData);
        tabelaMjekeve.setPrefHeight(250);
        tabelaMjekeve.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) populoFormularin(newSel);
        });

        getChildren().add(tabelaMjekeve);
    }

    private void createForm() {
        tfEmer = new TextField();
        tfEmer.setPromptText("Emër");
        tfMbiemer = new TextField();
        tfMbiemer.setPromptText("Mbiemër");
        tfSpecializimi = new TextField();
        tfSpecializimi.setPromptText("Specializimi");
        tfTelefoni = new TextField();
        tfTelefoni.setPromptText("Telefoni");
        tfEmail = new TextField();
        tfEmail.setPromptText("Email");
        dpDataFillimit = new DatePicker();
        dpDataFillimit.setPromptText("Data Fillimit");

        HBox form = new HBox(10, tfEmer, tfMbiemer, tfSpecializimi, tfTelefoni, tfEmail, dpDataFillimit);
        form.setPadding(new Insets(10));
        getChildren().add(form);
    }

    private void createButtons() {
        Button btnShto = new Button("Shto");
        btnShto.setOnAction(e -> shtoMjek());

        Button btnPerditeso = new Button("Përditëso");
        btnPerditeso.setOnAction(e -> perditesoMjek());

        Button btnFshi = new Button("Fshi");
        btnFshi.setOnAction(e -> fshiMjek());

        HBox butonat = new HBox(10, btnShto, btnPerditeso, btnFshi);
        butonat.setPadding(new Insets(10));
        getChildren().add(butonat);
        
     // Ngjyrat e butonave
        btnShto.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnPerditeso.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        btnFshi.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");

        // efekt hover
        btnShto.setOnMouseEntered(e -> btnShto.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-font-weight: bold;"));
        btnShto.setOnMouseExited(e -> btnShto.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;"));

        btnPerditeso.setOnMouseEntered(e -> btnPerditeso.setStyle("-fx-background-color: #FB8C00; -fx-text-fill: white; -fx-font-weight: bold;"));
        btnPerditeso.setOnMouseExited(e -> btnPerditeso.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;"));

        btnFshi.setOnMouseEntered(e -> btnFshi.setStyle("-fx-background-color: #E53935; -fx-text-fill: white; -fx-font-weight: bold;"));
        btnFshi.setOnMouseExited(e -> btnFshi.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;"));

    }

    private void ngarkoMjeket() {
        List<Mjek> lista = controller.merrTeGjitheMjeket();
        listaMjekeve = FXCollections.observableArrayList(lista);
        tabelaMjekeve.setItems(listaMjekeve);
    }

    private void populoFormularin(Mjek m) {
        tfEmer.setText(m.getEmer());
        tfMbiemer.setText(m.getMbiemer());
        tfSpecializimi.setText(m.getSpecializimi());
        tfTelefoni.setText(m.getTelefoni());
        tfEmail.setText(m.getEmail());
        dpDataFillimit.setValue(m.getDataFillimitPune());
    }

    private void pastroniFormularin() {
        tfEmer.clear();
        tfMbiemer.clear();
        tfSpecializimi.clear();
        tfTelefoni.clear();
        tfEmail.clear();
        dpDataFillimit.setValue(null);
    }

    private void shtoMjek() {
        TextInputDialog dialogUser = new TextInputDialog();
        dialogUser.setTitle("Krijo login për mjekun");
        dialogUser.setHeaderText("Vendos username për përdoruesin e mjekut");
        dialogUser.setContentText("Username:");
        String username = dialogUser.showAndWait().orElse("");
        if (username.isEmpty()) return;

        TextInputDialog dialogPass = new TextInputDialog();
        dialogPass.setTitle("Krijo login për mjekun");
        dialogPass.setHeaderText("Vendos password për përdoruesin e mjekut");
        dialogPass.setContentText("Password:");
        String password = dialogPass.showAndWait().orElse("");
        if (password.isEmpty()) return;

        Mjek m = new Mjek();
        m.setEmer(tfEmer.getText());
        m.setMbiemer(tfMbiemer.getText());
        m.setSpecializimi(tfSpecializimi.getText());
        m.setTelefoni(tfTelefoni.getText());
        m.setEmail(tfEmail.getText());
        m.setDataFillimitPune(dpDataFillimit.getValue());

        if (controller.shtoMjek(m, username, password)) {  
            ngarkoMjeket();
            pastroniFormularin();
            shfaqAlert("Sukses", "Mjeku u shtua me sukses!");
        } else {
            shfaqAlert("Gabim", "Nuk mund të shtohet mjeku.");
        }
    }


    private void perditesoMjek() {
        Mjek m = tabelaMjekeve.getSelectionModel().getSelectedItem();
        if (m == null) return;

        m.setEmer(tfEmer.getText());
        m.setMbiemer(tfMbiemer.getText());
        m.setSpecializimi(tfSpecializimi.getText());
        m.setTelefoni(tfTelefoni.getText());
        m.setEmail(tfEmail.getText());
        m.setDataFillimitPune(dpDataFillimit.getValue());

        if (controller.perditesoMjek(m)) {
            ngarkoMjeket();
            pastroniFormularin();
        }
    }

    private void fshiMjek() {
        Mjek m = tabelaMjekeve.getSelectionModel().getSelectedItem();
        if (m == null) return;

        if (controller.fshiMjek(m.getId())) {
            ngarkoMjeket();
            pastroniFormularin();
        }
    }

    private void filtroMjeket(String query) {
        if (query == null || query.isEmpty()) {
            tabelaMjekeve.setItems(listaMjekeve);
            return;
        }
        String lowerQuery = query.toLowerCase();
        ObservableList<Mjek> filtruar = FXCollections.observableArrayList();
        for (Mjek m : listaMjekeve) {
            if (m.getEmer().toLowerCase().contains(lowerQuery) || m.getMbiemer().toLowerCase().contains(lowerQuery)) {
                filtruar.add(m);
            }
        }
        tabelaMjekeve.setItems(filtruar);
    }

    private void shfaqAlert(String title, String mesazh) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(mesazh);
        alert.showAndWait();
    }
}
