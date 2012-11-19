
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.io.Console;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application
{ 
    private int windowWidth;
    private int windowHeight;
    private Stage thisStage;
    
  public double getWindowWidth(){
      return (double) windowWidth;
  }
    
  public double getWindowHeight()
  {
      return (double) windowHeight;
  }
  
 
  
  public double getSampleWidth() 
  {
      return 800; 
  }
 
  public double getSampleHeight() 
  { 
      return 800; 
  }
  
  public static void main(String[] args) 
  { 
      launch(args); 
  }
  
  @Override 
  public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
  }

  private void init(Stage primaryStage) 
  {
        final Stage primary = primaryStage;
        thisStage = primary;
        Platform.runLater(
        new Runnable()
        {
        public void run()
        {
            BorderPane borderpane = new BorderPane();
            
            Group root = new Group();
            primary.setScene(new Scene(root, 500,430));
            primary.setTitle("Draw App");
            primary.setResizable(false);
            
            ImagePanel cPanel = new ImagePanel(500,300);
            MainWindow window = new MainWindow(primary);
            
            Reader reader = new InputStreamReader(System.in);
            Parser parser = new Parser(reader,cPanel,window,primary);
            
            
            
            
            borderpane.setCenter(cPanel);
            borderpane.setBottom(window);
            
            root.getChildren().add(borderpane);
            primary.show();
            
            
            
            parser.parse();
        }
        }
    );
  }
  
}
