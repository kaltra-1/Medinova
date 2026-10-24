package view;

import controller.StokController;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Stok;

import java.util.List;
import java.util.Optional;

public class StokView extends VBox {

    private TableView<Stok> tabelaStokut;
    private StokController stokController;
    private FilteredList<Stok> filteredData;
    private TextField tfSearch; // vendosim si field global për të ruajtur filtrimin

    public StokView() {
        stokController = new StokController();
        setPadding(new Insets(20));
        setSpacing(15);

        Label titulli = new Label("Menaxhimi i Stokut");
        titulli.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        // Search Bar
        tfSearch = new TextField();
        tfSearch.setPromptText("Kërko artikull...");
        tfSearch.setMaxWidth(300);

        // Butona Shto, Ndrysho, Fshi
        HBox btnBox = new HBox(10);
        Button btnShto = new Button("Shto Artikull");
        Button btnNdrysho = new Button("Ndrysho Artikull");
        Button btnFshi = new Button("Fshi Artikull");
        btnBox.getChildren().addAll(btnShto, btnNdrysho, btnFshi);
        
     // Ngjyrat e butonave
        btnShto.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        btnNdrysho.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        btnFshi.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;");

        // Opsionale – efekt hover
        btnShto.setOnMouseEntered(e -> btnShto.setStyle("-fx-background-color: #45A049; -fx-text-fill: white; -fx-font-weight: bold;"));
        btnShto.setOnMouseExited(e -> btnShto.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;"));

        btnNdrysho.setOnMouseEntered(e -> btnNdrysho.setStyle("-fx-background-color: #FB8C00; -fx-text-fill: white; -fx-font-weight: bold;"));
        btnNdrysho.setOnMouseExited(e -> btnNdrysho.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;"));

        btnFshi.setOnMouseEntered(e -> btnFshi.setStyle("-fx-background-color: #E53935; -fx-text-fill: white; -fx-font-weight: bold;"));
        btnFshi.setOnMouseExited(e -> btnFshi.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-weight: bold;"));


        // TableView
        tabelaStokut = new TableView<>();
        tabelaStokut.setPrefHeight(400);
        krijoTabela();

        // 🔹 Merr te dhenat ekzistuese direkt kur hap pamjen
        refreshTabela();

        // Filtrimi sipas search bar
        tfSearch.textProperty().addListener((obs, oldVal, newVal) -> {
            if (filteredData != null) {
                filteredData.setPredicate(stok -> {
                    if (newVal == null || newVal.isEmpty()) return true;
                    return stok.getEmerArtikulli().toLowerCase().contains(newVal.toLowerCase());
                });
            }
        });

        // -------------------
        // BUTONAT
        // -------------------
        btnShto.setOnAction(e -> {
            Stok artikull = krijoDialogArtikull(null);
            if (artikull != null && stokController.shtoArtikullStoku(artikull)) {
                refreshTabela();
            }
        });

        btnNdrysho.setOnAction(e -> {
            Stok selektuar = tabelaStokut.getSelectionModel().getSelectedItem();
            if (selektuar != null) {
                Stok artikullNdryshuar = krijoDialogArtikull(selektuar);
                if (artikullNdryshuar != null &&
                        stokController.perditesoSasineStokut(selektuar.getId(), artikullNdryshuar.getSasiaAktuale())) {
                    refreshTabela();
                }
            } else {
                showAlert("Zgjidh një artikull për ta ndryshuar.");
            }
        });

        btnFshi.setOnAction(e -> {
            Stok selektuar = tabelaStokut.getSelectionModel().getSelectedItem();
            if (selektuar != null) {
                Alert konfirmo = new Alert(Alert.AlertType.CONFIRMATION,
                        "A je i sigurt që dëshiron të fshish artikullin?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> rez = konfirmo.showAndWait();
                if (rez.isPresent() && rez.get() == ButtonType.YES) {
                    if (stokController.fshiArtikullStoku(selektuar.getId())) {
                        refreshTabela();
                    }
                }
            } else {
                showAlert("Zgjidh një artikull për ta fshirë.");
            }
        });

        getChildren().addAll(titulli, tfSearch, btnBox, tabelaStokut);
    }

    private void krijoTabela() {
        TableColumn<Stok, String> kolEmer = new TableColumn<>("Emri Artikullit");
        kolEmer.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmerArtikulli()));

        TableColumn<Stok, String> kolNjesia = new TableColumn<>("Njësia");
        kolNjesia.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNjesia()));

        TableColumn<Stok, Integer> kolSasiaAktuale = new TableColumn<>("Sasia Aktuale");
        kolSasiaAktuale.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getSasiaAktuale()).asObject());

        TableColumn<Stok, Integer> kolSasiaMinimale = new TableColumn<>("Sasia Minimale");
        kolSasiaMinimale.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getSasiaMinimale()).asObject());

        TableColumn<Stok, String> kolVendndodhja = new TableColumn<>("Vendndodhja");
        kolVendndodhja.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getVendndodhja()));

        TableColumn<Stok, String> kolDataSkadences = new TableColumn<>("Data Skadences");
        kolDataSkadences.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDataSkadences().toString()));

        tabelaStokut.getColumns().addAll(kolEmer, kolNjesia, kolSasiaAktuale, kolSasiaMinimale, kolVendndodhja, kolDataSkadences);

        tabelaStokut.setRowFactory(tv -> new TableRow<Stok>() {
            @Override
            protected void updateItem(Stok item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else if (item.eshteNeNivelAlarmi()) {
                    setStyle("-fx-background-color: #FFCDD2;");
                } else {
                    setStyle("");
                }
            }
        });

        tabelaStokut.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void refreshTabela() {
        List<Stok> stokList = stokController.merrTeGjitheStokun();
        filteredData = new FilteredList<>(FXCollections.observableArrayList(stokList), p -> true);
        tabelaStokut.setItems(filteredData);

        // Aplikoj filtrimin aktual pas rifreskimit
        String tekstKerko = tfSearch.getText();
        if (tekstKerko != null && !tekstKerko.isEmpty()) {
            filteredData.setPredicate(stok -> stok.getEmerArtikulli().toLowerCase().contains(tekstKerko.toLowerCase()));
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // Metoda për dialog (shto/ndrysho) mbetet e njëjtë
    private Stok krijoDialogArtikull(Stok ekzistues) {
        Dialog<Stok> dialog = new Dialog<>();
        dialog.setTitle(ekzistues != null ? "Ndrysho Artikull" : "Shto Artikull");

        Label lblEmer = new Label("Emri Artikullit:");
        TextField tfEmer = new TextField();
        Label lblNjesia = new Label("Njësia:");
        TextField tfNjesia = new TextField();
        Label lblSasia = new Label("Sasia Aktuale:");
        TextField tfSasia = new TextField();
        Label lblSasiaMin = new Label("Sasia Minimale:");
        TextField tfSasiaMin = new TextField();
        Label lblVendndodhja = new Label("Vendndodhja:");
        TextField tfVendndodhja = new TextField();
        Label lblData = new Label("Data Skadences:");
        DatePicker dpData = new DatePicker();

        if (ekzistues != null) {
            tfEmer.setText(ekzistues.getEmerArtikulli());
            tfNjesia.setText(ekzistues.getNjesia());
            tfSasia.setText(String.valueOf(ekzistues.getSasiaAktuale()));
            tfSasiaMin.setText(String.valueOf(ekzistues.getSasiaMinimale()));
            tfVendndodhja.setText(ekzistues.getVendndodhja());
            dpData.setValue(ekzistues.getDataSkadences());
        }

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.add(lblEmer, 0, 0); grid.add(tfEmer, 1, 0);
        grid.add(lblNjesia, 0, 1); grid.add(tfNjesia, 1, 1);
        grid.add(lblSasia, 0, 2); grid.add(tfSasia, 1, 2);
        grid.add(lblSasiaMin, 0, 3); grid.add(tfSasiaMin, 1, 3);
        grid.add(lblVendndodhja, 0, 4); grid.add(tfVendndodhja, 1, 4);
        grid.add(lblData, 0, 5); grid.add(dpData, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                return new Stok(
                        ekzistues != null ? ekzistues.getId() : 0,
                        tfEmer.getText(),
                        tfNjesia.getText(),
                        Integer.parseInt(tfSasia.getText()),
                        Integer.parseInt(tfSasiaMin.getText()),
                        tfVendndodhja.getText(),
                        dpData.getValue()
                );
            }
            return null;
        });

        Optional<Stok> result = dialog.showAndWait();
        return result.orElse(null);
    }
}
