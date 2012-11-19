
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Scanner;
import java.util.StringTokenizer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

public class Parser
{
  private BufferedReader reader;
  private ImagePanel image;
  private MainWindow frame;
  private String line;
  private Button next;
  private Stage primary;

  public Parser(Reader reader, ImagePanel image, MainWindow frame, Stage primary)
  {
    this.reader = new BufferedReader(reader);
    this.image = image;
    this.frame = frame;
    this.primary = primary;
  }
  

  public void parse()
  {
    try
    {
        line = reader.readLine();
        parseLine(line);
        line = reader.readLine();
      
    }
    catch (IOException e)
    {
      frame.postMessage("Parse failed.");
      return;
    }
    catch (ParseException e)
    {
      frame.postMessage("Parse Exception: " + e.getMessage());
      return;
    }
    next = frame.getNextButton();
    next.setOnAction(new EventHandler<ActionEvent>() {
        public void handle(ActionEvent event) {
            try {
                 
                 parseLine(line);
                 
            } catch (ParseException e)
            {
                frame.postMessage("\nParse Exception: " + e.getMessage());
                return;
            } finally {
                try {
                    line = reader.readLine();
                    if (line == null)
                    {
                        next.setDisable(true);
                        frame.postMessage("\nDrawing Completed");
                    }
                } catch (IOException e) {
                    frame.postMessage("\nParse failed.");
                    return;
                }
                 
            }
          
        }
    });
    
    
    
    
  }

  private void parseLine(String line) throws ParseException
  {
    if (line.length() < 2) return;
    
    String command = line.substring(0, 2);
    
    if (command.equals("SI") && command.length() == 2 ) { saveImage(); return; } 
    if (command.equals("SD")) { setDimension(line.substring(2,line.length())); return; }
    if (command.equals("DL")) { drawLine(line.substring(2,line.length())); return; }
    if (command.equals("DR")) { drawRect(line.substring(2, line.length())); return; }
    if (command.equals("FR")) { fillRect(line.substring(2, line.length())); return; }
    if (command.equals("SC")) { setColour(line.substring(3, line.length())); return; }
    if (command.equals("DS")) { drawString(line.substring(2, line.length())); return; }
    if (command.equals("DI")) { drawImage(line.substring(3, line.length())); return; }
    if (command.equals("DA")) { drawArc(line.substring(2, line.length())); return; }
    if (command.equals("DO")) { drawOval(line.substring(2, line.length())); return; }


    throw new ParseException("Unknown drawing command");
  }

  private void saveImage() 
  {
      WritableImage toSave = image.snapshot(new SnapshotParameters(), null); 
      outputImage(toSave);
  }
  
  private void outputImage(WritableImage toSave)
  {
      BufferedImage image;
      image = javafx.embed.swing.SwingFXUtils.fromFXImage(toSave, null);
      try {
          File outputfile = new File("saved.png");
          ImageIO.write(image, "png", outputfile);
          frame.postMessage("Image Saved !");
      } catch (IOException ex) {
        
      }
  }
  
  private void setDimension(String args) throws ParseException
  {
    int width = 0;
    int height = 0;

    StringTokenizer tokenizer = new StringTokenizer(args);
    
    try {
        width = getInteger(tokenizer);
        height = getInteger(tokenizer);
    } catch (ParseException ex) {
        String errorMsg = ex.getMessage();
        errorMsg = errorMsg.concat(". Tokenizer exception happens at setDimension method");
        throw new ParseException(errorMsg);
    }
    
    double wDiff = width - primary.getWidth();
    double hDiff = height - primary.getHeight();
    primary.setWidth(width);
    primary.setHeight(height);
    frame.reDraw(wDiff,hDiff);
  }
  
  private void drawLine(String args) throws ParseException
  {
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;

    StringTokenizer tokenizer = new StringTokenizer(args);
    
    try {
        x1 = getInteger(tokenizer);
        y1 = getInteger(tokenizer);
        x2 = getInteger(tokenizer);
        y2 = getInteger(tokenizer);
    } catch (ParseException ex) {
        String errorMsg = ex.getMessage();
        errorMsg = errorMsg.concat(". Tokenizer exception happens at drawLine method");
        throw new ParseException(errorMsg);
    }
    image.drawLine(x1,y1,x2,y2);
  }

  private void drawRect(String args) throws ParseException
  {
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;

    StringTokenizer tokenizer = new StringTokenizer(args);
    try {
        x1 = getInteger(tokenizer);
        y1 = getInteger(tokenizer);
        x2 = getInteger(tokenizer);
        y2 = getInteger(tokenizer);
    } catch (ParseException ex) {
        String errorMsg = ex.getMessage();
        errorMsg = errorMsg.concat(". Tokenizer exception happens at drawRect method");
        throw new ParseException(errorMsg);
    }
    image.drawRect(x1, y1, x2, y2);
  }

  private void fillRect(String args) throws ParseException
  {
    int x1 = 0;
    int y1 = 0;
    int x2 = 0;
    int y2 = 0;

    StringTokenizer tokenizer = new StringTokenizer(args);
    try {
        x1 = getInteger(tokenizer);
        y1 = getInteger(tokenizer);
        x2 = getInteger(tokenizer);
        y2 = getInteger(tokenizer);
    } catch (ParseException ex) {
        String errorMsg = ex.getMessage();
        errorMsg = errorMsg.concat(". Tokenizer exception happens at fillRect method");
        throw new ParseException(errorMsg);
    }
    image.fillRect(x1, y1, x2, y2);
  }

  private void drawArc(String args) throws ParseException
  {
    int x = 0;
    int y = 0;
    int width = 0;
    int height = 0;
    int startAngle = 0;
    int arcAngle = 0;

    StringTokenizer tokenizer = new StringTokenizer(args);
    try {
        x = getInteger(tokenizer);
        y = getInteger(tokenizer);
        width = getInteger(tokenizer);
        height = getInteger(tokenizer);
        startAngle = getInteger(tokenizer);
        arcAngle = getInteger(tokenizer);
    } catch (ParseException ex) {
        String errorMsg = ex.getMessage();
        errorMsg = errorMsg.concat(". Tokenizer exception happens at drawArc method");
        throw new ParseException(errorMsg);
    }
    image.drawArc(x, y, width, height, startAngle, arcAngle);
  }

  private void drawOval(String args) throws ParseException
  {
    int x1 = 0;
    int y1 = 0;
    int width = 0;
    int height = 0;

    StringTokenizer tokenizer = new StringTokenizer(args);
    try {
        x1 = getInteger(tokenizer);
        y1 = getInteger(tokenizer);
        width = getInteger(tokenizer);
        height = getInteger(tokenizer);
    } catch (ParseException ex) {
        String errorMsg = ex.getMessage();
        errorMsg = errorMsg.concat(". Tokenizer exception happens at drawOval method");
        throw new ParseException(errorMsg);
    }
    image.drawOval(x1, y1, width, height);
  }

  private void drawString(String args) throws ParseException
  {
    int x = 0;
    int y = 0 ;
    String s = "";
    StringTokenizer tokenizer = new StringTokenizer(args);
    try {
    x = getInteger(tokenizer);
    y = getInteger(tokenizer);
    } catch (ParseException ex) {
        String errorMsg = ex.getMessage();
        errorMsg = errorMsg.concat(". Tokenizer exception happens at drawString method");
        throw new ParseException(errorMsg);
    }
    int position = args.indexOf("@");
    if (position == -1) throw new ParseException(". DrawString: string is missing");
    s = args.substring(position+1,args.length());
    image.drawString(x,y,s);
  }
  
  private void drawImage(String args) throws ParseException
  {
    int x = 0;
    int y = 0 ;
    int width = 0;
    int height = 0;
    String s = "";
    StringTokenizer tokenizer = new StringTokenizer(args);
    try {
    x = getInteger(tokenizer);
    y = getInteger(tokenizer);
    width = getInteger(tokenizer);
    height = getInteger(tokenizer);
    } catch (ParseException ex) {
        String errorMsg = ex.getMessage();
        errorMsg = errorMsg.concat(". Tokenizer exception happens at drawImage method");
        throw new ParseException(errorMsg);
    }
    int position = args.indexOf("@");
    if (position == -1) throw new ParseException(". DrawImage : string is missing");
    s = args.substring(position+1,args.length());
    image.drawImage(x,y,width,height,s);
  }

  private void setColour(String colourName) throws ParseException
  {
    if (colourName.equals("black")) { image.setColour(Color.BLACK); return;}
    if (colourName.equals("blue")) { image.setColour(Color.BLUE); return;}
    if (colourName.equals("cyan")) { image.setColour(Color.CYAN); return;}
    if (colourName.equals("darkgray")) { image.setColour(Color.DARKGRAY); return;}
    if (colourName.equals("gray")) { image.setColour(Color.GRAY); return;}
    if (colourName.equals("green")) { image.setColour(Color.GREEN); return;}
    if (colourName.equals("lightgray")) { image.setColour(Color.LIGHTGRAY); return;}
    if (colourName.equals("magenta")) { image.setColour(Color.MAGENTA); return;}
    if (colourName.equals("orange")) { image.setColour(Color.ORANGE); return;}
    if (colourName.equals("pink")) { image.setColour(Color.PINK); return;}
    if (colourName.equals("red")) { image.setColour(Color.RED); return;}
    if (colourName.equals("white")) { image.setColour(Color.WHITE); return;}
    if (colourName.equals("yellow")) { image.setColour(Color.YELLOW); return;}
    throw new ParseException("Invalid colour name");
  }

  private int getInteger(StringTokenizer tokenizer) throws ParseException
  {
    if (tokenizer.hasMoreTokens())
      return Integer.parseInt(tokenizer.nextToken());
    else
      throw new ParseException("Missing Integer value");
  }
}
