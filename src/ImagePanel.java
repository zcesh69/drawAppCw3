
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathBuilder;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javax.annotation.Resource;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.animation.PathTransitionBuilder;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.shape.LineTo;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author eeren
 */
public class ImagePanel extends Pane {
    

   private Paint color;
   private double width, height;
   public Node currentNode = null;
   private int currentAngle;
   private TranslateTransition translateTransition;
   
   public ImagePanel(double width, double height)
   {
       this.width = width;
       this.height = height;
       color = Color.BLACK;
       this.setPrefSize(width,height);
       
   }
  
   public void setBackgroundColour(Color colour)
  {
    Rectangle rect = new Rectangle(0,0,width,height) ;
    rect.setFill(color);
    this.getChildren().add(rect);
    color = Color.BLACK;
  }

  public void clear(Color color)
  {
    setBackgroundColour(color);
  }

  public void setColour(Paint color)
  {
      this.color = color;
      
  }

  public void drawLine(int x1, int y1, int x2, int y2)
  {
    Line l = new Line(x1,y1,x2,y2);
    this.getChildren().add(l);
    currentNode = l;
    currentAngle = 90;
  }

  public void drawRect(int x1, int y1, int width, int height)
  {
    Rectangle rectangle = new Rectangle(x1,y1,width,height);
    rectangle.setFill(null);
    rectangle.setStroke(color);
    this.getChildren().add(rectangle);
    currentNode = rectangle;
    currentAngle = 90;
  }

  public void fillRect(int x1, int y1, int width, int height)
  {
    Rectangle rect = new Rectangle(x1,y1,width,height) ;
    rect.setFill(color);
    rect.setStroke(color);
    this.getChildren().add(rect);
    currentNode = rect;
    currentAngle = 90;
  }

  public void drawString(int x, int y, String s)
  {
    Text text1 = new Text(x,y,s); 
    this.getChildren().add(text1);
    currentNode = text1;
    currentAngle = 90;
  }

  public void drawImage(int x, int y, int width, int height, String s) throws ParseException
  {
      
      try {
        ImageView iv = new ImageView("/123.png");
        iv.setFitWidth(width);
        iv.setFitHeight(height);
        iv.setTranslateX(this.getMinWidth() + x);
        iv.setTranslateY(this.getMinHeight() + y);
        this.getChildren().add(iv);
        currentNode = iv;
        currentAngle = 90;
      } catch (Exception ex) {
          throw new ParseException("Image file path invalid.");
      }
    
  }
  
  public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle)
  {
    Arc arc = new Arc(x,y,width,height,startAngle,arcAngle);
    arc.setFill(null);
    arc.setStroke(color);
    this.getChildren().add(arc);
    currentNode = arc;
    currentAngle = 90;
  }

  public void drawOval(int x, int y, int width, int height)
  {
    Ellipse ellipse = new Ellipse(x,y,width,height);
    ellipse.setFill(null);
    ellipse.setStroke(color);
    this.getChildren().add(ellipse);
    currentNode = ellipse;
    currentAngle = 90;
  }

    void setGradient(Color color1, Color color2) {
        Stop[] stops = new Stop[] { new Stop(0, color1), new Stop(1, color2)};
        color = new LinearGradient(0, 0, this.getWidth(), 0, false, CycleMethod.NO_CYCLE, stops);
    }
    
    void rotateTurtle(int degree, int leftright)
    {
        int toTurn = 0;
        if (leftright == 0) 
        {
            currentAngle = currentAngle + degree;
            while (currentAngle >= 360)
            {
                currentAngle = currentAngle - 360;
            }
            toTurn = -degree;
        }
        else
        {
            currentAngle = currentAngle - degree;
            while (currentAngle < 0)
            {
                currentAngle = currentAngle + 360;
            }
            toTurn = degree;
        }
        RotateTransition rt = new RotateTransition(Duration.millis(3000), currentNode);
        rt.setByAngle(toTurn);
        rt.setDuration(Duration.seconds(3));
        rt.setCycleCount(1);
        rt.setAutoReverse(false);
        System.out.println(currentAngle);
        rt.play();
    }
    
    void moveForward(int pixels) throws ParseException
    {
        double ang = 0;
        double y = 0;
        double x = 0;
        
        
        if (currentNode == null)
        {
            throw new ParseException(". Please draw a node first !");
        }
        
        if (currentAngle < 90 && currentAngle > 0)
        {
            y = -(pixels * Math.cos(ang));
            x = pixels * Math.sin(ang);
        }
        if (currentAngle < 180 && currentAngle > 90)
        {
            ang = 180 - currentAngle;
            y = -(pixels * Math.cos(ang));
            x = -(pixels * Math.sin(ang));
        }
        if (currentAngle < 270 && currentAngle > 180)
        {
            ang = 270 - currentAngle;
            ang = 180 - currentAngle;
            y = (pixels * Math.cos(ang));
            x = -(pixels * Math.sin(ang));
        }
        if (currentAngle < 360 && currentAngle > 270)
        {
            ang = 360 - currentAngle;
            ang = 180 - currentAngle;
            y = (pixels * Math.cos(ang));
            x = (pixels * Math.sin(ang));
        }
        if (currentAngle == 0){ x = pixels; y = 0; }
        if (currentAngle == 90){ x = 0; y = -pixels; }
        if (currentAngle == 180){ x = -pixels; y = 0; }
        if (currentAngle == 270){ x = 0; y = pixels; }
        
        
        //Translate move = new Translate(x , y);
        //currentNode.getTransforms().add(move);
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(3000), currentNode);
        tt.setByX(x);
        tt.setByY(y);
        tt.setCycleCount(1);
        tt.setAutoReverse(false);
 
        tt.play();
 
        
        
    }
    
    
}
