
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
    private String colorNames;

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
    if (command.equals("SG")) { setGradient(line.substring(2,line.length())); return; } 
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
    if (colourName.equals("black")) { image.setColour(Color.BLACK); showColor(colourName); return;}
    if (colourName.equals("blue")) { image.setColour(Color.BLUE); showColor(colourName); return;}
    if (colourName.equals("cyan")) { image.setColour(Color.CYAN); showColor(colourName); return;}
    if (colourName.equals("darkgray")) { image.setColour(Color.DARKGRAY); showColor(colourName); return;}
    if (colourName.equals("gray")) { image.setColour(Color.GRAY); showColor(colourName); return;}
    if (colourName.equals("green")) { image.setColour(Color.GREEN); showColor(colourName); return;}
    if (colourName.equals("lightgray")) { image.setColour(Color.LIGHTGRAY); showColor(colourName); return;}
    if (colourName.equals("magenta")) { image.setColour(Color.MAGENTA); showColor(colourName); return;}
    if (colourName.equals("orange")) { image.setColour(Color.ORANGE); showColor(colourName); return;}
    if (colourName.equals("pink")) { image.setColour(Color.PINK); showColor(colourName); return;}
    if (colourName.equals("red")) { image.setColour(Color.RED); showColor(colourName); return;}
    if (colourName.equals("white")) { image.setColour(Color.WHITE); showColor(colourName); return;}
    if (colourName.equals("yellow")) { image.setColour(Color.YELLOW); showColor(colourName); return;}
    throw new ParseException("Invalid colour name");
  }
  
  private void showColor(String str)
  {
      frame.postMessage("Color " + str +" has been set !");
  }

  private void setGradient(String colours) throws ParseException
  {
      int x = 0;
      String[] colourName = new String[2];
      Color[] color = new Color[2];
      StringTokenizer coloursTokenizer = new StringTokenizer(colours);
      
      while (coloursTokenizer.hasMoreTokens()) {
         colourName[x] = coloursTokenizer.nextToken();
         x += 1;
      }
     
    if (colourName[0].equals("black")) { color[0] = (Color.BLACK);}
    else if (colourName[0].equals("blue")) { color[0] = (Color.BLUE);}
    else if (colourName[0].equals("cyan")) { color[0] = (Color.CYAN);}
    else if (colourName[0].equals("darkgray")) { color[0] = (Color.DARKGRAY);}
    else if (colourName[0].equals("gray")) { color[0] = (Color.GRAY);}
    else if (colourName[0].equals("green")) { color[0] = (Color.GREEN); }
    else if (colourName[0].equals("lightgray")) { color[0] = (Color.LIGHTGRAY);}
    else if (colourName[0].equals("magenta")) { color[0] = (Color.MAGENTA);}
    else if (colourName[0].equals("orange")) { color[0] = (Color.ORANGE);}
    else if (colourName[0].equals("pink")) { color[0] = (Color.PINK);}
    else if (colourName[0].equals("red")) { color[0] = (Color.RED); }
    else if (colourName[0].equals("white")) { color[0] = (Color.WHITE);}
    else if (colourName[0].equals("yellow")) { color[0] = (Color.YELLOW);}
    else throw new ParseException("Invalid left start colour name");
    
    if (colourName[1].equals("black")) { color[1] = (Color.BLACK); }
    else if (colourName[1].equals("blue")) { color[1] = (Color.BLUE); }
    else if (colourName[1].equals("cyan")) { color[1] = (Color.CYAN); }
    else if (colourName[1].equals("darkgray")) { color[1] = (Color.DARKGRAY); }
    else if (colourName[1].equals("gray")) { color[1] = (Color.GRAY); }
    else if (colourName[1].equals("green")) { color[1] = (Color.GREEN); }
    else if (colourName[1].equals("lightgray")) { color[1] = (Color.LIGHTGRAY); }
    else if (colourName[1].equals("magenta")) { color[1] = (Color.MAGENTA); }
    else if (colourName[1].equals("orange")) { color[1] = (Color.ORANGE); }
    else if (colourName[1].equals("pink")) { color[1] = (Color.PINK); }
    else if (colourName[1].equals("red")) { color[1] = (Color.RED); }
    else if (colourName[1].equals("white")) { color[1] = (Color.WHITE); }
    else if (colourName[1].equals("yellow")) { color[1] = (Color.YELLOW); }
    else throw new ParseException("Invalid right start colour name");
    
    frame.postMessage("Color gradient has been successfully set !");
    image.setGradient(color[0], color[1]);
    
    
    
  }

  
  
  private int getInteger(StringTokenizer tokenizer) throws ParseException
  {
    if (tokenizer.hasMoreTokens())
      return Integer.parseInt(tokenizer.nextToken());
    else
      throw new ParseException("Missing Integer value");
  }
}
