
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eeren
 */
public class MainWindow extends Group {
    
    private Stage primaryStage;
    private TextArea messageArea;
    private Button nextButton;
    private Button closeButton;
    private VBox lowerBox;
    
    public MainWindow(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        setUpGUI();
    }
    
    public void setUpGUI()
    {
        lowerBox = new VBox();
        HBox buttonBox = new HBox();
        messageArea = new TextArea();
    
        messageArea.setPrefRowCount(6);
        messageArea.setWrapText(true);
        messageArea.setEditable(false);
        messageArea.setPrefWidth(500);
        
        Button closeButton = new Button("Close Window");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                
                primaryStage.close();
            
            }
        });
        this.closeButton = closeButton;
        
        Button nextButton = new Button("Next");
        this.nextButton = nextButton;
        
        
        lowerBox.getChildren().add(messageArea);
        buttonBox.getChildren().add(nextButton);
        buttonBox.getChildren().add(closeButton);
        lowerBox.getChildren().add(buttonBox);
        this.getChildren().add(lowerBox);
       
    }
    
    public void reDraw(double wDiff, double hDiff)
    {
        messageArea.setPrefWidth(primaryStage.getWidth());
        messageArea.setTranslateY(hDiff);
        nextButton.setTranslateY(hDiff);
        closeButton.setTranslateY(hDiff);
    }
    
    public Button getNextButton()
    {
        return nextButton;
    }
    
    public void postMessage(final String s)
    {
        Platform.runLater(
        new Runnable()
        {
          public void run()
          {
            messageArea.appendText(s);
          }
        });
    }
    
    
    
    
    
    
}
