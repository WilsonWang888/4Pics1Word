import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class Tester extends JFrame implements ActionListener
{
  private JButton restart, cont;
  private int level;
  
  public int numOfLevels (String filename) throws FileNotFoundException//returns number of levels based on txt file
    //public since it's accessed in LetterBank Class
  {
    File text = new File (filename);
    Scanner sc = new Scanner (text);
    int count = 0;//storing number of line
    try
    {
      String line = sc.nextLine();//checking if there's a first line
      line = line.trim();//in case input was messed up and there's a space for the first or last characters
      while (!line.equals("")&&!(line.charAt(0)==' '))//adds to count until a line with no char or spaces are found
      {
        count ++;
        line = sc.nextLine();
        line = line.trim();
      }
    }
    catch (NoSuchElementException ex)//program will go here when there are no more lines
    {
      //don't think I need to do anything here...
    }
    
    return count;
  }
  
  private static ImageIcon createImageIcon (String path)
  {
    java.net.URL imgURL = Tester.class.getResource (path);
    if (imgURL != null)
    {
      return new ImageIcon (imgURL);
    }
    else
    {
      JOptionPane.showMessageDialog(null, "ERROR: An image URL is incorrect.", "File Mistake",JOptionPane.ERROR_MESSAGE);
      return null;
    }
  }
  
  private int[] loadData () throws FileNotFoundException
  {
    Scanner sc = new Scanner (new File ("RECORDS.txt"));
    
    String[] vals = new String[2];
    for (int i = 0; i<2; i++)
    {
      vals[i] = sc.nextLine();
      vals[i] = vals[i].trim();
    }
    
    int[] data = new int[2];
    data[0] = Integer.parseInt(vals[0]);//coins
    data[1] = Integer.parseInt(vals[1]);//level
    
    return data;
  }
  private void createAndShowGUI () throws FileNotFoundException
  {
    setTitle ("4 Pic 1 Word");
    setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
    
    JLabel name = new JLabel (createImageIcon ("Don't Sue Me Please.png"));
    
    JLabel legalProtection = new JLabel ("Please don't sue me. I'm only making losses off of this.");
    JPanel picture = new JPanel ();
    picture.setLayout (new BoxLayout (picture, BoxLayout.PAGE_AXIS));
    picture.add(name);
    picture.add(legalProtection);
    
    cont = new JButton ("CONTINUE");//button to start game
    cont.addActionListener (this);
    cont.setActionCommand ("c");
    
    restart = new JButton ("NEW GAME");
    restart.addActionListener (this);
    restart.setActionCommand ("r");
    JButton instructions = new JButton ("INSTRUCTIONS");
    instructions.addActionListener (new ActionListener ()
                                      {
      public void actionPerformed (ActionEvent e)
      {
        File text = new File ("INSTRUCTIONS.txt");
        Scanner sc;
        try
        {
          sc = new Scanner (text);
        }
        catch (FileNotFoundException ex)
        {
          JOptionPane.showMessageDialog(null, "The instructions file could not be found");
          sc = null;
        }
        
        String content;
        
        JTextArea textArea = new JTextArea ();
        try
        {
          while(sc.hasNextLine())
          {
            content = sc.nextLine();    
            textArea.append (content + "\n");
          }
        }
        catch (NoSuchElementException ex)
        {
          JOptionPane.showMessageDialog(null, "There is an error in the instructions file.");
          content = null;
        }
        
        
        textArea.setEditable(false);
        textArea.setLineWrap (true);
        textArea.setWrapStyleWord(true);
        
        
        JScrollPane scrollPane = new JScrollPane(textArea); 
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(250, 250));
        
        JFrame manual = new JFrame ("Instructions");
        manual.setContentPane(scrollPane);
        manual.pack();
        manual.setVisible(true);
        manual.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      }
    });
    
    JPanel buttonPane = new JPanel ();
    buttonPane.setLayout (new BoxLayout (buttonPane, BoxLayout.PAGE_AXIS));
    buttonPane.add(cont);
    buttonPane.add(restart);
    buttonPane.add(instructions);
    
    JPanel finalContentPane = new JPanel ();
    finalContentPane.add (picture);
    finalContentPane.add(buttonPane);
    
    setContentPane (finalContentPane);
    
    pack();
    setVisible (true);
  }
  
  public void actionPerformed (ActionEvent evt)
  {    
    JComponent newContentPane;
    
    try
    {
      int[] data = loadData ();
      int coins = data[0];
      level = data[1];  
      System.out.println (evt.getActionCommand());
      if (evt.getActionCommand().equals("r"))//resetting values
      {
        coins = 200;
        level = 1;
      }
      newContentPane = new Game (level, this, coins);//creating new level
    }
    catch (FileNotFoundException e)
    {
      newContentPane = new JLabel ("ERROR - FILE COULD NOT BE LOCATED");
    }
    
    
    newContentPane.setOpaque (true);
    setContentPane (newContentPane);
    pack();    
    repaint ();
  }
  
  public static void main (String[] args) throws FileNotFoundException
  {      
    Tester prgrm = new Tester ();//note how the constructor without parameters was used
    prgrm.createAndShowGUI();///apparently it's safer to create the GUI outside the main or something? idk really
  }
}

class Game extends JPanel//creating the final game layout
{
  public Game (int level, Tester test, int coins) throws FileNotFoundException///Tester class will be used in LetterBank class
  {
    setLayout (new BoxLayout(this, BoxLayout.PAGE_AXIS));
    add (new Images (level));//adding the 4 images
    add(new LetterBank (level, test, coins));//adding the buttons
  }
}

class Images extends JPanel//getting and laying out the images
{  
  public Images (int level)
  {    
    JPanel panel = new JPanel (new GridLayout (2,2));
    for (int i=1;i<=4;i++)
      panel.add(new JLabel (createImages(level,i), JLabel.CENTER));//adding images to JPanel
    
    setLayout (new BoxLayout (this, BoxLayout.LINE_AXIS));
    add (Box.createHorizontalGlue());//this and the one below effectively center the components horizontally
    add(panel);
    add (Box.createHorizontalGlue());    
  }
  
  private ImageIcon createImages (int level, int num)
  {
    return createImageIcon ("Image" + level + num + ".png");//returns picture for specific position as ImageIcon
  }
  
  private static ImageIcon createImageIcon (String path)
  {
    java.net.URL imgURL = Tester.class.getResource (path);
    if (imgURL != null)
    {
      return new ImageIcon (imgURL);
    }
    else
    {
      JOptionPane.showMessageDialog(null, "ERROR: An image URL is incorrect.", "File Mistake",JOptionPane.ERROR_MESSAGE);
      return null;
    }
  }
}

class LetterBank extends JPanel
  implements ActionListener
{
  private String word;
  private JToggleButton[] options, ansBar;
  private String[] answers;
  private int[] memory;
  private boolean exit = true, on = false;
  private int level, coins, chances = 0;
  private Tester frame;
  private JButton hint, elim, clear, save;
  private JLabel label;
  
  public LetterBank (int lvl, Tester test, int money) throws FileNotFoundException
  {
    frame = test;//allows it to access its JFrame. Useful for repainting later
    level = lvl;
    coins = money;//getting the 
    
    getWords ();//getting words from txt file
    
    try
    {
      word = answers[level-1].toUpperCase();//making sure that string is properly read later
      word = word.trim();//getting rid of any annoying whitespace
    }
    catch (ArrayIndexOutOfBoundsException e)//will occur when there are no more levels
    {
      JOptionPane.showMessageDialog(null, "You have completed the final level! Press OK to exit the game");
      System.exit(0);
    }
    label = new JLabel ("Coins: " + coins);
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    add (label);
    add(getAnsBarLetters());
    add (getWordBankLetters());
  }
  
  private String[] getWords () throws FileNotFoundException
  {
    int count = frame.numOfLevels("ANSWERS.txt");//getting number of lines
    Scanner sc = new Scanner (new File ("ANSWERS.txt"));//creating scanner for file with answers
    answers = new String [count];//creatting string array to contain answers
    for (int i=0;i<count;i++)
    {
      answers[i] = sc.nextLine();//adding answers to string array
    }
    
    return answers;
  }
  
  private String[] letterOptions (String ans)//correct word will be inputted to determine random letters
  {
    String[] lets = new String [12];
    String[] scrambled = new String [12];
    
    int j;//outside of for loop so that random letters can be added
    for (j=0;j<word.length();j++)
    {
      lets[j] = String.valueOf(word.charAt(j));//getting each letter in word
    }
    
    Random r = new Random ();
    
    while (j<12)//runs until there are 12 letters
    {      
      char lett = (char)(r.nextInt(26)+65);
      lets[j] = String.valueOf(lett);
      j++;
    }
    
    for (int i=0;i<scrambled.length;i++)
      scrambled[i] = "";
    
    for (int i=0;i<scrambled.length;i++)
    {
      boolean stop = false;
      while(!stop)//loops until an empty string of the array is found
      {
        int num = r.nextInt (scrambled.length);
        if (scrambled[num].equals(""))//checks if string is empty
        {
          scrambled[num] = lets[i];
          stop = true;//stops loop when string is filled
        }
      }
    }
    
    return scrambled;
  }
  
  private JPanel getWordBankLetters ()
  {
    options = new JToggleButton[12];
    String[] lets = letterOptions (word);//getting each letter
    for (int i=0;i<options.length;i++)
    {
      options[i] = new JToggleButton (lets[i]);
      options[i].setActionCommand ("o"+i);//so that the button pressed can be identified
      options[i].addActionListener(this);
      options[i].setPreferredSize(new Dimension(50,50));
    }
    
    JPanel buttonPane = new JPanel (new GridLayout (2,8));    
    for (int i=0; i<options.length/2; i++)
      buttonPane.add(options[i]);//adding buttons in first row
    
    hint = new JButton (createImageIcon ("Hint.png"));//NOTICE - add ImageIcon later
    hint.setActionCommand("h");
    hint.addActionListener(this);
    hint.setFont(hint.getFont().deriveFont(8f));
    hint.setPreferredSize (new Dimension (60,60));
    buttonPane.add(hint);
    
    clear = new JButton (createImageIcon("Clear.png"));
    clear.setActionCommand("c");
    clear.addActionListener(this);
    clear.setFont(hint.getFont().deriveFont(8f));
    clear.setPreferredSize (new Dimension (60,60));
    buttonPane.add(clear);
    
    for (int i=options.length/2; i<options.length; i++)
      buttonPane.add(options[i]);//adding buttons in second row
    
    elim = new JButton (createImageIcon ("Eliminate.png"));//NOTICE - Add ImageIcon later
    elim.setActionCommand("e");
    elim.addActionListener(this);
    elim.setFont(elim.getFont().deriveFont(6f));
    elim.setPreferredSize (new Dimension (60,60));
    buttonPane.add(elim);
    
    save = new JButton (createImageIcon ("Save.png"));
    save.setActionCommand ("s");
    save.addActionListener (this);
    save.setPreferredSize (new Dimension (60,60));
    buttonPane.add(save);
    
    JPanel finalPane = new JPanel ();
    finalPane.setLayout(new BoxLayout (finalPane, BoxLayout.LINE_AXIS));
    finalPane.add(buttonPane);
    finalPane.add(Box.createHorizontalGlue());//left aligns the buttons
    
    return finalPane;          
  }
  
  private JPanel getAnsBarLetters ()
  {    
    ansBar = new JToggleButton[word.length()];
    memory = new int[ansBar.length];//will be used later to remember where letters came from
    String[] lets = letterOptions ("HI");
    for (int i=0;i<ansBar.length;i++)
    {
      ansBar[i] = new JToggleButton (" ");
      memory[i] = 0;//initializing values
      
      ansBar[i].setEnabled(false);//so that they can't be initially selected
      ansBar[i].setActionCommand ("a"+i);//the a differentiates these buttons from the letter bank ones
      ansBar[i].addActionListener (this);
      ansBar[i].setPreferredSize(new Dimension(50,50));
    }
    JPanel buttonPane = new JPanel ();
    buttonPane.setPreferredSize (new Dimension (50*6, 50*2));    
    for (int i=0; i<ansBar.length; i++)
      buttonPane.add(ansBar[i]);//adding buttons to pane
    
    return buttonPane;    
  }
  
  private ImageIcon createImageIcon (String path)
  {
    java.net.URL imgURL = Tester.class.getResource (path);
    if (imgURL != null)
    {
      return new ImageIcon (imgURL);
    }
    else
    {
      JOptionPane.showMessageDialog(null, "ERROR: An image URL is incorrect.", "File Mistake",JOptionPane.ERROR_MESSAGE);
      return null;
    }
  }
  
  public void actionPerformed (ActionEvent evt)
  {
    String button = evt.getActionCommand();//getting name of button pressed
    char first = button.charAt(0);
    
    if (first == 'a')//true if an answer button was pressed
    {
      //ans bar response
      button = button.substring (1);//cutting out the a
      int num = Integer.parseInt(button);//getting button position
      String let = ansBar[num].getText();///getting letter in that answer box
      
      options[memory[num]].setEnabled (true);//reenabling the button where the letter originallly came from
      options[memory[num]].setSelected (false);
      ansBar[num].setSelected(false);
      ansBar[num].setEnabled(false);//disabling the button pressed
      ansBar[num].setText (" ");//resetting the text
    }
    else if (first == 'o')//if letter bank button was pressed
    {
      //letter bank response
      button = button.substring (1);
      int num = Integer.parseInt(button);//getting button position
      String let = options[num].getText();//getting letter of button
      int size = ansBar.length;
      
      if (exit)//to be completely honest I don't remember why I did this and it's late. Pretty sure that there was an error otherwise though
        options[num].setSelected(false);
      
      exit = false;//resetting value
      for (int i=0;i<size;i++)
      {
        if (!ansBar[i].isSelected())//only adds letter to empty boxes
        {
          ansBar[i].setText(let);//setting letter of box
          ansBar[i].setEnabled(true);//allowing box to be pressed
          ansBar[i].setSelected(true);
          options[num].setEnabled(false);//not allowing the same letter bank button to be pressed multiple times
          
          memory[i] = Integer.parseInt(evt.getActionCommand().substring(1));//remembering the button that was pressed
          exit = true;//resetting value
        }
        if (exit)
          i = size;///to exit the loop
      }
      
      int count = 0;
      for (int i=0;i<ansBar.length;i++)
        if (ansBar[i].isSelected())
        count++;///counting number of boxes with letters
      
      if (count == ansBar.length)//if all are filled
      {
        String ans = "";
        for (int i=0;i<ansBar.length;i++)
        {
          ans = ans.concat(ansBar[i].getText());//creating inputted word
        }
        ans = ans.toUpperCase();
        if (ans.equals(word))//if the answer is correct
        {
          level++;//increase the level
          JOptionPane.showMessageDialog(null, "CORRECT! You can now proceed to Level "+(level));
          coins += 20;
          Game game;
          try
          {
            game = new Game (level, frame, coins);
          }
          catch (FileNotFoundException e)//hopefully won't occur
          {
            game = null;
            JOptionPane.showMessageDialog(null, "A programming error was made (sorry!). The system will" 
                                            + "now shut down");
            System.exit(0);//ehh might be a bit extreme
          }
          
          game.setOpaque (true);//basically remaing frame for new level
          frame.setContentPane(game);          
          frame.pack();
          frame.repaint();
        }
      }
    }
    else if (first == 'h')//if hint button is pressed
    {
      if (coins >=50)
      {
        int n = JOptionPane.showConfirmDialog(
                                              frame, "Would you like one letter to be selected?",
                                              "Add a Letter",
                                              JOptionPane.YES_NO_OPTION);
        if (n==JOptionPane.YES_OPTION)
        {
          coins -= 50;
          label.setText ("Coins: " + coins);
          
          Random r = new Random();
          boolean unselected = false;
          int select;
          for (int i=0; i<ansBar.length; i++)                
            if (ansBar[i].getText().equals (" "))
            unselected = true;
          
          if (unselected)
          {
            unselected = false;
            do
            {
              select = r.nextInt(ansBar.length);
              if (ansBar[select].getText().equals (" "))
                unselected = true;
            } while(!unselected);
            ansBar[select].setText(String.valueOf(word.charAt(select)));
            ansBar[select].setSelected(true);
            ansBar[select].setEnabled(false);
            
            frame.repaint();
          }
          else
            JOptionPane.showMessageDialog(null, "Please leave at least one blank space in the answer bar");
          
          hint.setEnabled (false);
        } //no need to add code if answer is no I think                               
        
      }
      else
        JOptionPane.showMessageDialog(null, "You do not have enough coins for this option");
    }
    else if (first == 'c')
    {
      int n = JOptionPane.showConfirmDialog(
                                            frame, "Would you like to clear all selected letters?",
                                            "Clear All Letters",
                                            JOptionPane.YES_NO_OPTION);
      if(n==JOptionPane.YES_OPTION)
        for (int num = 0; num < ansBar.length; num++)
      {
        options[memory[num]].setEnabled (true);//re-enabling the buttons where the letters came from
        options[memory[num]].setSelected (false);
        ansBar[num].setSelected(false);
        ansBar[num].setEnabled(false);//disabling the buttons in answer bar
        ansBar[num].setText (" ");//resetting the text
      }
    }
    else if (first == 'e')
    {
      if (coins >= 30)
      {
        int n = JOptionPane.showConfirmDialog(
                                              frame, "Would you like one incorrect letter to be removed?",
                                              "Eliminate a Letter",
                                              JOptionPane.YES_NO_OPTION);
        
        if(n==JOptionPane.YES_OPTION)
        {
          if (chances < 3)
          {
            coins -= 30;
            label.setText ("Coins: " + coins);
            
            Random r = new Random();  
            int select;
            boolean unused;
            do
            {
              unused = true;
              select = r.nextInt(options.length);
              for (int i=0; i<ansBar.length;i++)//checking if text matches part of ans bar or word
              {
                if (options[select].getText().equals(ansBar[i].getText()))
                  unused = false;
                if (options[select].getText().equals(String.valueOf(word.charAt(i))))
                  unused = false;
                if (!options[select].isEnabled())
                  unused = false;
              }
            }while (!unused);
            options[select].setEnabled (false);
            for (int i=0; i<ansBar.length; i++)
            {
              if (memory[i] == select)
              {
                ansBar[i].setSelected(false);
                ansBar[i].setEnabled(false);//disabling the button pressed
                ansBar[i].setText (" ");//resetting the text
              }
            }
            chances++;
            frame.repaint();
          }
          else
          {
            JOptionPane.showMessageDialog(null, "You have used up all of your elimnations");
            elim.setEnabled (false);
          }
        }
      }
      else
        JOptionPane.showMessageDialog(null, "You do not have enough coins for this option");
    }
    else if (first == 's')
    {
      int n = JOptionPane.showConfirmDialog(
                                            frame, "Would you like to save your progress?",
                                            "Save progress",
                                            JOptionPane.YES_NO_OPTION);
      
      if(n==JOptionPane.YES_OPTION)
      {
        File f = new File ("RECORDS.txt");
        try 
        {          
          FileWriter fw = new FileWriter (f);
          BufferedWriter bw = new BufferedWriter (fw);
          bw.write (coins + "");
          bw.write ("\n"+level + "");
          bw.close();
        }
        catch (IOException e)
        {
          JOptionPane.showMessageDialog(null, "The file could not be saved correctly.");
        }
        
      }
    }
    else
    {
      JOptionPane.showMessageDialog(null, "A programming error was made (sorry!). The system will " 
                                      + "now shut down");
      System.exit(0);
    }
  }
}