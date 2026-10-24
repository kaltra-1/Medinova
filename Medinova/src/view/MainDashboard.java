package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainDashboard extends Application {

    private BorderPane root = new BorderPane();
    private VBox sidebar;
    private Label contentTitle;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Medinova - Sistemi i Menaxhimit Shëndetësor");

        // Sidebar
        sidebar = createSidebar();
        root.setLeft(sidebar);

        // Header
        root.setTop(createHeader());

        // Qendra (Default Dashboard me logo dhe buton Log Out)
        root.setCenter(createDefaultCenter());

        Scene scene = new Scene(root, 900, 650);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // -----------------------------------------
    // SIDEBAR
    // -----------------------------------------
    private VBox createSidebar() {
        VBox menuBox = new VBox(20);
        menuBox.setPadding(new Insets(25, 10, 10, 20));
        menuBox.setPrefWidth(240);
        menuBox.setStyle("-fx-background-color: linear-gradient(to bottom, #1F2A38, #263445);");

        // Logo Sidebar
        Label logo = new Label("MEDINOVA");
        logo.setFont(Font.font("Montserrat", FontWeight.EXTRA_BOLD, 28));
        logo.setTextFill(Color.web("#6acdff"));
        menuBox.getChildren().add(logo);
        menuBox.getChildren().add(new Separator());

        // Menu Buttons
        Button btnDashboard = createMenuButton("Dashboard Kryesor");
        Button btnPacient = createMenuButton("Menaxhimi i Pacientëve");
        Button btnTakim = createMenuButton("Menaxhimi i Takimeve");
        Button btnMjek = createMenuButton("Menaxhimi i Stafit");
        Button btnStok = createMenuButton("Inventari / Stoku");
        Button btnPerdorues = createMenuButton("Menaxhimi i Perdoruesve");
        
        menuBox.getChildren().addAll(
                btnDashboard, btnPacient, btnTakim, btnMjek, btnStok, btnPerdorues
        );

        btnDashboard.setOnAction(e -> root.setCenter(createDefaultCenter()));
        btnPacient.setOnAction(e -> loadPacientView());
        btnMjek.setOnAction(e -> loadStafView());
        btnTakim.setOnAction(e -> loadTakimView());
        btnStok.setOnAction(e -> loadStokView());
        btnPerdorues.setOnAction(e -> loadPerdoruesView());

        return menuBox;
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setFont(Font.font("Roboto", FontWeight.SEMI_BOLD, 15));

        btn.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: #E8E8E8;
            -fx-padding: 12 0 12 15;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
        """);

        btn.setOnMouseEntered(e -> btn.setStyle("""
            -fx-background-color: #a4f8fd;
            -fx-text-fill: black;
            -fx-padding: 12 0 12 15;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
        """));

        btn.setOnMouseExited(e -> btn.setStyle("""
            -fx-background-color: transparent;
            -fx-text-fill: #E8E8E8;
            -fx-padding: 12 0 12 15;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
        """));

        return btn;
    }

    // -----------------------------------------
    // HEADER
    // -----------------------------------------
    private HBox createHeader() {
        HBox header = new HBox();
        header.setPadding(new Insets(15, 25, 15, 25));
        header.setStyle("""
            -fx-background-color: linear-gradient(to right, #2749ae, #75ffac);
            -fx-border-color: #D0D0D0;
            -fx-border-width: 0 0 1 0;
        """);
        header.setAlignment(Pos.CENTER_RIGHT);

        Label user = new Label("I kyçur si: Admin");
        user.setFont(Font.font("Montserrat", FontWeight.BOLD, 14));
        user.setTextFill(Color.WHITE);

        header.getChildren().add(user);
        return header;
    }

    // -----------------------------------------
    // DEFAULT CENTER VIEW me logo dhe log out
    // -----------------------------------------
    private Pane createDefaultCenter() {
        StackPane stack = new StackPane(); // për të lejuar që logo të mbushë qendrën
        stack.setStyle("-fx-background-color: white;"); 
        stack.setAlignment(Pos.CENTER);

        // Logo shumë e madhe që mund të pritet anash
        ImageView logoView;
        try {
            Image logo = new Image(MainDashboard.class.getResource("/logo.png").toExternalForm());
            logoView = new ImageView(logo);
            logoView.setPreserveRatio(true);
            logoView.setFitWidth(1000); // e madhe, mund të pritet anash
        } catch (Exception e) {
            logoView = new ImageView();
            Label placeholder = new Label("LOGO");
            placeholder.setFont(Font.font("Montserrat", FontWeight.BOLD, 100));
            placeholder.setTextFill(Color.LIGHTGRAY);
            stack.getChildren().add(placeholder);
            System.out.println("Logo nuk u gjet!");
        }

     // VBox për titullin dhe subtitle me stil modern
        VBox vboxText = new VBox(6);
        vboxText.setAlignment(Pos.CENTER);
        vboxText.setPadding(new Insets(15, 25, 15, 25));

        // Background me gradient dhe kontur të butë
        vboxText.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #ffffff, #f0f4f7);" + 
            "-fx-background-radius: 15;" +
            "-fx-border-radius: 15;" +
            "-fx-border-color: #d0d7de;" +
            "-fx-border-width: 1.5;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 3);"
        );

        // Titulli kryesor
        contentTitle = new Label("Dashboard Kryesor");
        contentTitle.setFont(Font.font("Montserrat", FontWeight.EXTRA_BOLD, 26));
        contentTitle.setTextFill(Color.web("#2C3E50"));
        contentTitle.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 4, 0, 0, 2);");

        // Subtitle modern
        Label subtitle = new Label("Zgjidhni një opsion nga menuja anësore për të filluar.");
        subtitle.setFont(Font.font("Roboto", 15));
        subtitle.setTextFill(Color.web("#7F8C8D"));
        subtitle.setStyle("-fx-opacity: 0.9;");

        // Vendos titullin dhe subtitle
        vboxText.getChildren().addAll(contentTitle, subtitle);



        // Butoni Log Out
        Button btnLogout = new Button("Log Out");
        btnLogout.setFont(Font.font("Roboto", FontWeight.BOLD, 14));
        btnLogout.setTextFill(Color.WHITE);
        btnLogout.setStyle("""
            -fx-background-color: #00ffc9;
            -fx-background-radius: 8;
            -fx-padding: 10 20 10 20;
        """);
        btnLogout.setOnMouseEntered(e -> btnLogout.setStyle("""
            -fx-background-color: #29b094;
            -fx-background-radius: 8;
            -fx-padding: 10 20 10 20;
        """));
        btnLogout.setOnMouseExited(e -> btnLogout.setStyle("""
            -fx-background-color: #00ffc9;
            -fx-background-radius: 8;
            -fx-padding: 10 20 10 20;
        """));
        
        btnLogout.setOnAction(e -> {
            Stage stage = (Stage) btnLogout.getScene().getWindow();
            LoginView login = new LoginView(); // supozojmë që LoginView është Application ose ka metodë start(Stage)
            try {
                login.start(stage); // ridrejto dashboard te login
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Vendos logo + tekst + buton në stack
        VBox content = new VBox(20, vboxText, btnLogout);
        content.setAlignment(Pos.BOTTOM_CENTER);
        content.setPadding(new Insets(30));

        stack.getChildren().addAll(logoView, content);

        return stack;
    }



    // -----------------------------------------
    // PACIENT VIEW
    // -----------------------------------------
    private void loadPacientView() {
        contentTitle.setText("Menaxhimi i Pacientëve");
        root.setCenter(new PacientView());
    }

    // -----------------------------------------
    // STAF VIEW
    // -----------------------------------------
    private void loadStafView() {
        contentTitle.setText("Menaxhimi i Stafit");
        root.setCenter(new StafView());
    }

    // -----------------------------------------
    // TAKIM VIEW
    // -----------------------------------------
    private void loadTakimView() {
        contentTitle.setText("Menaxhimi i Takimeve");
        root.setCenter(new TakimView());
    }

    // -----------------------------------------
    // STOK VIEW
    // -----------------------------------------
    private void loadStokView() {
        contentTitle.setText("Inventari/Stoku");
        root.setCenter(new StokView());
    }
    
    // -----------------------------------------
    // PERDORUES VIEW
    // -----------------------------------------
    private void loadPerdoruesView() {
        contentTitle.setText("Menaxhimi i Perdoruesve");
        root.setCenter(new PerdoruesView());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
