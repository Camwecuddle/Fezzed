import java.awt.Image;
import java.io.*;
import javax.imageio.ImageIO;
import java.lang.Object;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.*;




public class Fezzed {

  /* parsed input line, array of words */
  private ArrayList<String> inputArray;

  private String[] images;

  // File representing the folder that you select using a FileChooser
  private File fezletters;

  /* User input */
  private String input;

  /* final image of png conglomoration */
  private BufferedImage result;

  private Graphics2D g;

  /* Width in pixels of result */
  private int resWidth;

  /* Height in pixels of result */
  private int resHeight;

  private int rows;

  private int columns;

  private static int counter;


  public Fezzed(){
      this.input = System.console().readLine();
      this.inputArray = new ArrayList<String>();
      this.images = new String[inputArray.size()];
      this.fezletters = new File("../fezletters");
  }

  public void parseInput() {
        String [] tokens = input.split("[\\s]");
        for(String s:tokens){
            inputArray.add(s);
            //System.out.println(s);
        }
  }

  public void setUpRes() {
      //System.out.println(inputArray.size()/3);
      this.columns = (int)Math.ceil((double)inputArray.size()/3);
      this.rows = getResHeight();
      this.resWidth = columns * 130 + 30;
      if (inputArray.size() == 1) {
          this.resHeight = rows * 130 + 30;
      }
      else if (inputArray.size() == 2) {
          this.resHeight = rows * 130 + 160;
      }
      else {
          this.resHeight = rows * 130 + 290;
      }
      //System.out.println("Res H = " + resHeight + ", Res W = " + resWidth);
      this.result = new BufferedImage(this.resWidth, this.resHeight, BufferedImage.TYPE_INT_ARGB);
      this.g = result.createGraphics();
      this.g.setBackground(Color.white);
      this.g.clearRect(0, 0, this.resWidth, this.resHeight);
  }

  public int getResHeight() {
      int hold = 0;
      int maxHeight = 0;
          for (int i = 0; i < inputArray.size(); i+=3) {
              hold += inputArray.get(i).length();
              try {
                  hold += inputArray.get(i+1).length();
              } catch (Exception e) {}
              try {
                  hold += inputArray.get(i+2).length();
              } catch (Exception e) {}
              if (hold > maxHeight) maxHeight = hold;
              hold = 0;
          }
      return maxHeight;
  }

  public void fezzitup(){
      int x = this.resWidth - 130;
      int y = 30;
      int k = 0;
      for(int i = 0; i < columns; i++){ // keep track of the column

          for(int j = 0; (j/3 != 1) && (k < inputArray.size()); j++){ // keep track of the row
              for(char image : inputArray.get(k).toCharArray()){
                  BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
                  try {
                      System.out.println("i = "+i+" j = "+j+" image = "+image + " x = " + x + " y = " + y);
                      bi = ImageIO.read(new File("fezletters/" + image + ".png"));
                      this.g.drawImage(bi, x, y, null);
                  } catch (IOException e) {
                      System.out.println(e);
                  }
                  y += 130; /* next row */
              }
              y += 130; /* for space between words */
              k++;
          }
          x -= 130; /* next column */
          y = 30;
      }
  }

  /*
  public void fezzitup(){
      int x = 100;
      int y = 100;
      for(String image : images){
          try {
            BufferedImage bi = ImageIO.read(new File("fezletters/" + image));
            g.drawImage(bi, x, y, null);
            x += 100;
            if(x > result.getWidth()){
              x = 0;
              y += 100;
            }
          } catch (IOException e) {
            System.out.println(e);
          }
      }
  }
  */

  /*
  public void drawA(){
    BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);

    try {
       bi = ImageIO.read(new File(images[0]));
    } catch (IOException e) {
      System.out.println(e);
    }
    g.drawImage(bi, 870, 30, null);
  }
  */

  public void loadCounter(){
    List<Integer> list = new ArrayList<Integer>();
    File file = new File("count.txt");
    BufferedReader reader = null;
    try{
    reader = new BufferedReader(new FileReader(file));
    String txt;
    while ((txt = reader.readLine()) != null) {
        list.add(Integer.parseInt(txt));
    }
  } catch (FileNotFoundException e) {
    e.printStackTrace();
  } catch (IOException e) {
    e.printStackTrace();
  } finally {
    try {
    reader.close();
  } catch (IOException e) {
    e.printStackTrace();
  }
  }

    this.counter = list.get(0);
  }

  public void saveCounter(){
    this.counter++;
    PrintWriter p = null;
    try {
    p = new PrintWriter("count.txt");
    p.write("" + this.counter);
    p.close();
  } catch (IOException e) {
    e.printStackTrace();
  }
  }

  public void loadImages(){
    images = fezletters.list();
    // System.out.println(images[0]);
  }

  public static void main(String[] args) {
        Fezzed fez = new Fezzed();
        fez.parseInput();
        fez.setUpRes();
        fez.loadImages();
        fez.fezzitup();
        fez.loadCounter();
        try {
          ImageIO.write(fez.result,"png",new File("result" + counter + ".png"));
        } catch (IOException e) {
          System.out.println(e);
        }
        fez.saveCounter();
  }

}
