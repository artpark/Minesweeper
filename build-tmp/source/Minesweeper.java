import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {


//Declare and initialize NUM_ROWS and NUM_COLS = 20
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs; //ArrayList of just the minesweeper buttons that are mined
private static final int NUM_ROWS = 20;
private static final int NUM_COLS = 20;
//Set the number of bombs here
private static final int NUM_BOMBS = 1;

String[] loseMessage = {
  "Y", "O", "U", " ", "L", "O", "S", "E", "!"
};
String[] winMessage = {
  "Y", "O", "U", " ", "W", "I", "N", "!"
};

public void setup ()
{
  size(400, 400);
  textAlign(CENTER, CENTER);

  // make the manager
  Interactive.make( this );

  //declare and initialize buttons
  buttons = new MSButton[NUM_ROWS][NUM_COLS];
  for (int r = 0; r < NUM_ROWS; r++)
  {
    for (int c = 0; c < NUM_COLS; c++)
    {
      buttons[r][c] = new MSButton(r, c);
    }
  }
  bombs = new ArrayList<MSButton>();
  setBombs();
}

public void setBombs()
{
  int num = NUM_BOMBS;
  for (int i = 0; i < num; i++)
  {
    int r = (int)(Math.random()*NUM_ROWS);
    int c = (int)(Math.random()*NUM_COLS);
    if (!bombs.contains(buttons[r][c]))
    {
      bombs.add(buttons[r][c]);
    } else
      num++;
  }
}

public void draw ()
{
  background( 0 );
  if (isWon())
    displayWinningMessage();
}
public boolean isWon()
{
  for (int r = 0; r < NUM_ROWS; r++)
    for (int c = 0; c < NUM_COLS; c++)
      if (!buttons[r][c].isMarked() && bombs.contains(buttons[r][c]))
        return false;

  return true;
}

public void displayLosingMessage()
{
  int i = 0;
  for (int r = 0; r < NUM_ROWS; r++)
  {
    for (int c = 0; c < NUM_COLS; c++)
    {
      buttons[r][c].setLabel(loseMessage[i]);
      i++;
      if (i > loseMessage.length-1)
      {
        i = 0;
      }
    }
  }
}

public void displayWinningMessage()
{
  if (isWon())
  {
    int i = 0;
    for (int r = 0; r < NUM_ROWS; r++)
    {
      for (int c = 0; c < NUM_COLS; c++)
      {
        buttons[r][c].setLabel(winMessage[i]);
        i++;
        if (i > winMessage.length-1)
        {
          i = 0;
        }
      }
    }
  }
}

public class MSButton
{
  private int r, c;
  private float x, y, width, height;
  private boolean clicked, marked, first;
  private String label;

  public MSButton ( int rr, int cc )
  {
    width = 400/NUM_COLS;
    height = 400/NUM_ROWS;
    r = rr;
    c = cc; 
    x = c*width;
    y = r*height;
    label = "";
    marked = clicked = false;
    Interactive.add( this ); // register it with the manager
  }
  public boolean isMarked()
  {
    return marked;
  }
  public boolean isClicked()
  {
    return clicked;
  }
  // called by manager

  public void mousePressed () 
  {
    if (mouseButton == LEFT)
    {
      if (!clicked)
      {
        clicked = true;
        if (keyPressed)
          marked = !marked;
        else if (bombs.contains(this))
          displayLosingMessage();
        else if (countBombs(r, c)>0)
          label = label + countBombs(r, c);
        else
        {
          for (int i=-1; i<2; i++)
            for (int j=-1; j<2; j++)
              if (isValid(r+i, c+j))
                if (!buttons[r+i][c+j].isClicked())
                  buttons[r+i][c+j].mousePressed();
        }
      }
    }


    if (mouseButton==RIGHT)
    {
      marked=!marked;
    }
  }


  public void draw () 
  {    
    if (marked)
      if (!isWon())
        fill(0);
      else
        fill(0xffFFF527);
    else if ( clicked && bombs.contains(this) ) 
      fill(255, 0, 0);
    else if (clicked)
      fill( 200 );
    else 
      fill( 100 );

    rect(x, y, width, height);
    fill(0);
    text(label, x+width/2, y+height/2);
  }
  public void setLabel(String newLabel)
  {
    label = newLabel;
  }
  public boolean isValid(int r, int c)
  {
    if ((r >= 0 && r < NUM_ROWS) && (c >= 0 && c < NUM_COLS))
      return true;
    else
      return false;
  }
  public int countBombs(int row, int col)
  {
    int numBombs = 0;
    for (int r = -1; r < 2; r++)
      for (int c = -1; c < 2; c++)
        if (isValid(row+r, col+c))
          if (bombs.contains(buttons[row+r][col+c]))
            numBombs++;

    return numBombs;
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
