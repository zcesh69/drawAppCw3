
import javafx.scene.paint.Color;
import javafx.scene.Group;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javax.annotation.Resource;



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
    this.getChildren().add(new Line(x1,y1,x2,y2));
  }

  public void drawRect(int x1, int y1, int width, int height)
  {
    Rectangle rectangle = new Rectangle(x1,y1,width,height);
    rectangle.setFill(null);
    rectangle.setStroke(color);
    this.getChildren().add(rectangle);
  }

  public void fillRect(int x1, int y1, int width, int height)
  {
    Rectangle rect = new Rectangle(x1,y1,width,height) ;
    rect.setFill(color);
    rect.setStroke(color);
    this.getChildren().add(rect);
  }

  public void drawString(int x, int y, String s)
  {
    this.getChildren().add(new Text(x,y,s));
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
  }

  public void drawOval(int x, int y, int width, int height)
  {
    Ellipse ellipse = new Ellipse(x,y,width,height);
    ellipse.setFill(null);
    ellipse.setStroke(color);
    this.getChildren().add(ellipse);
  }

    void setGradient(Color color1, Color color2) {
        Stop[] stops = new Stop[] { new Stop(0, color1), new Stop(1, color2)};
        color = new LinearGradient(0, 0, this.getWidth(), 0, false, CycleMethod.NO_CYCLE, stops);
    }
    
    
}
