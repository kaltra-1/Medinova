package view;

import controller.TakimController;
import controller.PacientController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Takim;
import model.Pacient;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TakimStafView {

    private int mjekId;

    public TakimStafView(int mjekId) {
        this.mjekId = mjekId;
    }

    public void show(Stage stage) {
        stage.setTitle("Takimet e mia");

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

        Text lblTitle = new Text("Takimet e mia");
        lblTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        lblTitle.setFill(Color.web("#0f172a"));
        header.getChildren().add(lblTitle);
        root.setTop(header);

        // ================= TABELA E TAKIMEVE =================
        TableView<Takim> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setStyle(
                "-fx-background-color: white;" +
                "-fx-border-radius: 12;" +
                "-fx-background-radius: 12;" +
                "-fx-table-cell-border-color: transparent;" +
                "-fx-table-header-border-color: transparent;"
        );

        TableColumn<Takim, Integer> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));

        TableColumn<Takim, String> colPacient = new TableColumn<>("Pacienti");
        colPacient.setCellValueFactory(cell -> {
            int pacientId = cell.getValue().getPacientId();
            Pacient pacient = new PacientController().merrPacientById(pacientId);
            return javafx.beans.property.SimpleStringProperty.stringExpression(
                    javafx.beans.binding.Bindings.createStringBinding(() ->
                            pacient.getEmer() + " " + pacient.getMbiemer())
            );
        });

        TableColumn<Takim, String> colDataOra = new TableColumn<>("Data dhe Ora");
        colDataOra.setCellValueFactory(cell -> javafx.beans.property.SimpleStringProperty.stringExpression(
                javafx.beans.binding.Bindings.createStringBinding(() ->
                        cell.getValue().getDataOra().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                )
        ));

        TableColumn<Takim, String> colArsyeja = new TableColumn<>("Arsyeja");
        colArsyeja.setCellValueFactory(cellData -> javafx.beans.property.SimpleStringProperty.stringExpression(
                javafx.beans.binding.Bindings.createStringBinding(cellData.getValue()::getArsyeja)
        ));

        TableColumn<Takim, String> colStatusi = new TableColumn<>("Statusi");
        colStatusi.setCellValueFactory(cellData -> javafx.beans.property.SimpleStringProperty.stringExpression(
                javafx.beans.binding.Bindings.createStringBinding(cellData.getValue()::getStatusi)
        ));

        table.getColumns().addAll(colId, colPacient, colDataOra, colArsyeja, colStatusi);

        // Merr të gjitha takimet dhe filtro sipas mjekId
        TakimController takimController = new TakimController();
        List<Takim> teGjithaTakimet = takimController.merrTeGjithaTakimet();

        ObservableList<Takim> takimetMjekut = FXCollections.observableArrayList();
        for (Takim t : teGjithaTakimet) {
            if (t.getMjekId() == mjekId) {
                takimetMjekut.add(t);
            }
        }

        table.setItems(takimetMjekut);
        root.setCenter(table);
        BorderPane.setMargin(table, new Insets(20, 0, 0, 0));

        // ================= BUTONI KTHEHU =================
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

        Scene scene = new Scene(root, 950, 550);
        stage.setScene(scene);
        stage.show();
    }
}
