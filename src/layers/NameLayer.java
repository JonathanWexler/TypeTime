package layers;

//Jonathan Wexler �2013 jwexman@gmail.com

//import java.applet.AudioClip;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

@SuppressWarnings("serial")
public class NameLayer extends JPanel implements KeyListener, MouseListener,
        MouseMotionListener, ActionListener {

    String hard = "dict.txt";
    String easy = "words.txt";

    File scoreFile;
    double startTime;
    double endTime;

    int numberOfWords = 10;
    String user = "";
    double score = 0;
    String word = "";
    HashMap<Double, String> scores = new HashMap<Double, String>();
    ArrayList<Double> topTwenty = new ArrayList<Double>();

    String blankWord = "";
    Object[] words;

    boolean pause = false;

    Timer timer;
    int x, y, y2, count, stars;
    MouseEvent mouse;
    KeyEvent key;

    // flag0 is name page, flag1 is start page, flag 2 is gameplay
    int flag = 0;
    int lang = 1;

    JTextField t = new JTextField(" Enter Here  ");
    Font font = new Font("Verdana", Font.BOLD, 86);
    Font fontBig = new Font("Verdana", Font.BOLD, 186);

    JLabel text, textWrong, userName, hi, showScores, countDown;

    public NameLayer() {

        File userHome = new File(System.getProperty("user.home"));

        scoreFile = new File(userHome, "LSSScores.txt");

        if (!scoreFile.exists()) {
            System.out.println("NOT EXISTS FIRST");
            createScoreFile();
        }

        this.setLayout(null);
        setBackground(Color.WHITE);
        setDoubleBuffered(true);
        words = dictionary().toArray();
        word = word();
        blankWord = blanker(word);

        y2 = y = 110;
        x = 1075;
        count = 0;
        stars = -200;
        timer = new Timer(25, this);
        timer.start();

        this.addKeyListener(this);
        this.addMouseListener(this);
        if (flag == 0) {
            t.setSize(600, 100);
            t.setFont(font);
            t.setForeground(Color.BLUE);
            add(t);
            t.addKeyListener(this);
        }

        countDown = new JLabel("");
        countDown.setSize(1000, 600);
        countDown.setFont(new Font("Verdana", Font.BOLD, 36));
        countDown.setForeground(Color.red);
        countDown.setLocation(100, 170);
        countDown.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(countDown);
        text = new JLabel("");
        text.setSize(1000, 300);
        text.setFont(fontBig);
        text.setLocation(100, 200);
        text.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(text);
        text.setVisible(false);

        showScores = new JLabel("");
        showScores.setSize(1000, 100);
        showScores.setFont(new Font("Verdana", Font.BOLD, 36));
        showScores.setForeground(Color.red);
        showScores.setLocation(100, 170);
        showScores.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(showScores);
        showScores.setVisible(false);

        try {
            loadScores();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void createScoreFile() {
        System.out.println(scoreFile.getAbsolutePath());
        System.out.println(scoreFile.canRead());
        try {
            boolean b = scoreFile.createNewFile();
        } catch (IOException e) {
            System.out.println("ERROR IN NEW MAKING");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void writeScore() {
        if (scoreFile.exists()) {
            scoreFile.delete();
        }
        createScoreFile();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(scoreFile, true));
            for (double t : topTwenty) {
                writer.newLine();
                writer.append(t + "  " + scores.get(t));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                // Close the writer regardless of what happens...
                writer.close();
            } catch (Exception e1) {
            }
        }
        System.out.println("WROTE");
    }

    public void loadScores() throws FileNotFoundException {
        int max = 20;
        Scanner n = null;
        n = new Scanner(scoreFile);
        while (n.hasNextLine() && max > 0) {

            double temp = 0;
            String temp2 = "Jon";
            String t = n.nextLine();
            Scanner scan = new Scanner(t);
            if (scan.hasNextDouble()) {
                temp = scan.nextDouble();
                temp2 = scan.next();
            }
            scores.put(temp, temp2);
            max--;
        }

        checkScore();
    }

    public void checkScore() {
        // topTwenty = (ArrayList<Integer>) scores.keySet();
        topTwenty.removeAll(topTwenty);
        for (double key : scores.keySet()) {
            topTwenty.add(key);
        }
        sortTopScores();
    }

    public void sortTopScores() {
        Collections.sort(topTwenty);
        // if (topTwenty.size() > 20) {
        // System.out.println("SIZE IS == " + topTwenty.size());
        // topTwenty.remove(topTwenty.size() - 1);
        // System.out.println("New IS == " + topTwenty.size());
        //
        // }

        for (double t : topTwenty) {
            System.out.print("--" + t);
        }
        System.out.println("");

    }

    public String blanker(String word) {
        String blanks = word.substring(0, 1);

        for (int i = 0; i <= word.length() - 2; i++) {
            blanks += " _";
        }

        return blanks;

    }

    public void paint(Graphics g) {
        super.paint(g);

        paintLogo(g);

        if (flag == 2) {
            paintTimer(g);
        } else if (flag == 1) {
            paintStart(g);
        } else {
            paintBegin(g);
        }

    }

    public void paintBegin(Graphics g) {

        if (stars >= 0) {
            if (lang == 1) {
                ImageIcon start = new ImageIcon(this.getClass().getResource(
                        "doneE.png"));
                Image t = start.getImage();
                Graphics2D draw = (Graphics2D) g;
                draw.drawImage(t, 0, 530, this);

                ImageIcon start2 = new ImageIcon(this.getClass().getResource(
                        "typeNameE.png"));
                Image t2 = start2.getImage();
                Graphics2D draw2 = (Graphics2D) g;
                draw2.drawImage(t2, 0, 200, this);
            } else {
                ImageIcon start = new ImageIcon(this.getClass().getResource(
                        "doneH.png"));
                Image t = start.getImage();
                Graphics2D draw = (Graphics2D) g;
                draw.drawImage(t, 0, 530, this);

                ImageIcon start2 = new ImageIcon(this.getClass().getResource(
                        "typeNameH.png"));
                Image t2 = start2.getImage();
                Graphics2D draw2 = (Graphics2D) g;
                draw2.drawImage(t2, 0, 200, this);
            }
        }

    }

    public void paintLogo(Graphics g) {
        super.paint(g);

        Graphics2D hello = (Graphics2D) g;
        Graphics2D name = (Graphics2D) g;
        // Graphics2D num = (Graphics2D) g;

        hello.setFont(new Font("Verdana", Font.BOLD, 16));
        hello.drawString("HELLO, ", 150, 20);
        name.setColor(Color.ORANGE);
        if (user.length() > 5) {
            name.setFont(new Font("Verdana", Font.BOLD, 20));
        } else {
            name.setFont(new Font("Verdana", Font.BOLD, 36));
        }
        // String number = (1000 - count)/10 +"";
        // num.drawString(number, 150, 100);
        name.drawString(user.toUpperCase(), 150, 60);

        InputStream logoURL = this.getClass().getResourceAsStream("logo.png");
        InputStream starURL = this.getClass().getResourceAsStream("star.png");
        InputStream langHURL = this.getClass().getResourceAsStream("LangH.png");
        InputStream langEURL = this.getClass().getResourceAsStream("LangE.png");

        Image logo = null;
        Image star = null;
        Image langH = null;
        Image langE = null;

        try {
            logo = ImageIO.read(logoURL);
            star = ImageIO.read(starURL);
            langH = ImageIO.read(langHURL);
            langE = ImageIO.read(langEURL);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(star, x, y, this);
        Graphics2D g2d2 = (Graphics2D) g;
        g2d2.drawImage(star, x - 100, y2, this);
        Graphics2D g2d3 = (Graphics2D) g;
        g2d3.drawImage(star, x - 200, y, this);
        Graphics2D g2d4 = (Graphics2D) g;
        g2d4.drawImage(star, x - 300, y2, this);
        Graphics2D g2d5 = (Graphics2D) g;
        g2d5.drawImage(star, x - 400, y, this);
        Graphics2D g2d6 = (Graphics2D) g;
        g2d6.drawImage(star, x - 500, y2, this);
        Graphics2D g2d7 = (Graphics2D) g;
        g2d7.drawImage(star, x - 600, y, this);
        Graphics2D g2d8 = (Graphics2D) g;
        g2d8.drawImage(star, x - 700, y2, this);
        Graphics2D g2d9 = (Graphics2D) g;
        g2d9.drawImage(star, x - 800, y, this);
        Graphics2D g2d10 = (Graphics2D) g;
        g2d10.drawImage(star, x - 900, y2, this);
        Graphics2D g2d11 = (Graphics2D) g;
        g2d11.drawImage(star, x - 1000, y, this);
        Graphics2D g2dlogo = (Graphics2D) g;
        g2dlogo.drawImage(logo, 300, 10, this);

        if (lang == 1) {
            Graphics2D laE = (Graphics2D) g;
            laE.drawImage(langE, 20, 10, this);
        } else {
            Graphics2D laH = (Graphics2D) g;
            laH.drawImage(langH, 20, 10, this);
        }

        if (stars < 0) {
            ImageIcon start = new ImageIcon(this.getClass().getResource(
                    "TypeTimeLogo.png"));
            Image t = start.getImage();
            Graphics2D draw = (Graphics2D) g;
            draw.drawImage(t, 0, -50, this);
        }

    }

    public void paintStart(Graphics g) {

        if (lang == 1) {
            ImageIcon start = new ImageIcon(this.getClass().getResource(
                    "play.png"));
            Image t = start.getImage();
            Graphics2D draw = (Graphics2D) g;
            draw.drawImage(t, 0, 530, this);

            ImageIcon inf = new ImageIcon(this.getClass().getResource(
                    "InstructionsE.png"));
            Image i = inf.getImage();
            Graphics2D draw2 = (Graphics2D) g;
            draw2.drawImage(i, 0, 280, this);
        } else {
            ImageIcon start = new ImageIcon(this.getClass().getResource(
                    "playH.png"));
            Image t = start.getImage();
            Graphics2D draw = (Graphics2D) g;
            draw.drawImage(t, 0, 530, this);

            ImageIcon inf = new ImageIcon(this.getClass().getResource(
                    "InstructionsH.png"));
            Image i = inf.getImage();
            Graphics2D draw2 = (Graphics2D) g;
            draw2.drawImage(i, 0, 280, this);
        }

    }

    public void paintTimer(Graphics g) {
        if (word.length() > 4) {
            text.setFont(font);
        } else {
            text.setFont(fontBig);
        }
        text.setText(word.toUpperCase());

        if (count < 0) {
            if (score <= 0) {
                if (count >= -150 && count < -120) {
                    countDown.setVisible(true);
                    countDown.setForeground(Color.RED);
                    countDown.setText("3");
                    countDown.setFont(new Font("Verdana", Font.BOLD,
                            200 - count * 2));
                } else if (count >= -120 && count < -90) {
                    countDown.setText("2");
                    countDown.setFont(new Font("Verdana", Font.BOLD,
                            200 - count * 2));
                } else if (count >= -90 && count < -60) {
                    countDown.setText("1");
                    countDown.setFont(new Font("Verdana", Font.BOLD,
                            200 - count * 2));
                } else if (count >= -60 && count < 0) {
                    countDown.setForeground(Color.GREEN);
                    countDown.setText("GO!");
                    countDown.setFont(new Font("Verdana", Font.BOLD,
                            200 - count * 2));
                } 
            } else {
                paintPause(g);


                if (count % 25 <= -12) {
                    ImageIcon smile = new ImageIcon(this.getClass().getResource(
                            "smileL.png"));
                    Image s = smile.getImage();
                    Graphics2D draws = (Graphics2D) g;
                    draws.drawImage(s, 50, 250, this);
                    draws.drawImage(s, 950, 250, this);
                } else {
                    ImageIcon smile = new ImageIcon(this.getClass().getResource(
                            "smileR.png"));
                    Image s = smile.getImage();
                    Graphics2D draws = (Graphics2D) g;
                    draws.drawImage(s, 50, 250, this);
                    draws.drawImage(s, 950, 250, this);
                }
            }



            ImageIcon time = new ImageIcon(this.getClass().getResource(
                    "nextWord.png"));
            Image t = time.getImage();
            Graphics2D draw = (Graphics2D) g;
            draw.drawImage(t, 950, 20, this);
            if (count == -2) {
                word = word();
            }
        } else if (count >= 0 && count < 999) {
            countDown.setVisible(false);
            if (count == 0) {
                startTime = System.currentTimeMillis();
                this.t.setVisible(true);
                text.setVisible(true);
                t.requestFocusInWindow();
            }
            if (((System.currentTimeMillis() - startTime)/ 1000) < 25) {
                Graphics2D timer = (Graphics2D) g;
                timer.setFont(new Font("Verdana", Font.BOLD, 80));
                timer.setColor(Color.BLUE);
                timer.drawString(
                        ((double) Math.round(((System.currentTimeMillis() - startTime) / 1000) * 100) / 100)
                                + "", 930, 80);
            }else {
                Graphics2D timer = (Graphics2D) g;
                timer.setFont(new Font("Verdana", Font.BOLD, 80));
                timer.setColor(Color.RED);
                timer.drawString(
                        "25.00", 930, 80);
            }

            // ImageIcon time = new ImageIcon(this.getClass()
            // .getResource("20.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 50 && count < 100) {
            // ImageIcon time = new ImageIcon(this.getClass()
            // .getResource("19.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            //
            // } else if (count >= 100 && count < 150) {
            // ImageIcon time = new ImageIcon(this.getClass()
            // .getResource("18.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // // text.setVisible(false);
            //
            // } else if (count >= 150 && count < 200) {
            // ImageIcon time = new ImageIcon(this.getClass()
            // .getResource("17.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 200 && count < 250) {
            // ImageIcon time = new ImageIcon(this.getClass()
            // .getResource("16.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 250 && count < 300) {
            // ImageIcon time = new ImageIcon(this.getClass()
            // .getResource("15.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 300 && count < 350) {
            // ImageIcon time = new ImageIcon(this.getClass()
            // .getResource("14.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 350 && count < 400) {
            // ImageIcon time = new ImageIcon(this.getClass()
            // .getResource("13.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 400 && count < 450) {
            // ImageIcon time = new ImageIcon(this.getClass()
            // .getResource("12.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 450 && count < 500) {
            // ImageIcon time = new ImageIcon(this.getClass()
            // .getResource("11.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 500 && count < 550) {
            // ImageIcon time = new ImageIcon(this.getClass()
            // .getResource("10.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 550 && count < 600) {
            // ImageIcon time = new
            // ImageIcon(this.getClass().getResource("9.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 600 && count < 650) {
            // ImageIcon time = new
            // ImageIcon(this.getClass().getResource("8.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 650 && count < 700) {
            // ImageIcon time = new
            // ImageIcon(this.getClass().getResource("7.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 700 && count < 750) {
            // ImageIcon time = new
            // ImageIcon(this.getClass().getResource("6.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 750 && count < 800) {
            // ImageIcon time = new
            // ImageIcon(this.getClass().getResource("5.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 800 && count < 850) {
            // ImageIcon time = new
            // ImageIcon(this.getClass().getResource("4.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 850 && count < 900) {
            // ImageIcon time = new
            // ImageIcon(this.getClass().getResource("3.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 900 && count < 950) {
            // ImageIcon time = new
            // ImageIcon(this.getClass().getResource("2.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
            // } else if (count >= 950 && count < 1000) {
            // ImageIcon time = new
            // ImageIcon(this.getClass().getResource("1.png"));
            // Image t = time.getImage();
            // Graphics2D draw = (Graphics2D) g;
            // draw.drawImage(t, 1000, 10, this);
        } else if (count >= 1000 && count < 1150) {
            ImageIcon time = new ImageIcon(this.getClass().getResource(
                    "thanks.png"));
            Image t = time.getImage();
            Graphics2D draw = (Graphics2D) g;
            draw.drawImage(t, 0, 0, this);
            text.setVisible(false);
            if (!topTwenty.isEmpty()
                    && score < topTwenty.get(topTwenty.size() - 1)) {
                if (score <= topTwenty.get(0)) {
                    showScores.setLocation(100, 600);
                    showScores.setText("Congratulations! A new High Score: "
                            + score);
                } else {
                    showScores.setLocation(100, 600);
                    showScores.setText("Congratulations! A Top-Twenty Score: "
                            + score);

                }

            }
            showScores.setVisible(true);

        } else {
            ImageIcon time = new ImageIcon(this.getClass().getResource(
                    "HighScores.png"));
            Image t = time.getImage();
            Graphics2D draw = (Graphics2D) g;
            draw.drawImage(t, 0, 0, this);
            text.setVisible(false);
            showScores(g);
        }
    }

    public void showScores(Graphics g) {
        if (topTwenty.size() >= 1) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("1. " + scores.get(topTwenty.get(0)) + " has "
                    + topTwenty.get(0), 150, 250);
        }
        if (topTwenty.size() >= 2) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("2. " + scores.get(topTwenty.get(1)) + " has "
                    + topTwenty.get(1), 150, 300);
        }
        if (topTwenty.size() >= 3) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("3. " + scores.get(topTwenty.get(2)) + " has "
                    + topTwenty.get(2), 150, 350);
        }
        if (topTwenty.size() >= 4) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("4. " + scores.get(topTwenty.get(3)) + " has "
                    + topTwenty.get(3), 150, 400);
        }
        if (topTwenty.size() >= 5) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("5. " + scores.get(topTwenty.get(4)) + " has "
                    + topTwenty.get(4), 150, 450);
        }
        if (topTwenty.size() >= 6) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("6. " + scores.get(topTwenty.get(5)) + " has "
                    + topTwenty.get(5), 150, 500);
        }
        if (topTwenty.size() >= 7) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("7. " + scores.get(topTwenty.get(6)) + " has "
                    + topTwenty.get(6), 150, 550);
        }
        if (topTwenty.size() >= 8) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("8. " + scores.get(topTwenty.get(7)) + " has "
                    + topTwenty.get(7), 150, 600);
        }
        if (topTwenty.size() >= 9) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("9. " + scores.get(topTwenty.get(8)) + " has "
                    + topTwenty.get(8), 150, 650);
        }
        if (topTwenty.size() >= 10) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("10. " + scores.get(topTwenty.get(9)) + " has "
                    + topTwenty.get(9), 150, 700);
        }
        if (topTwenty.size() >= 11) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("11. " + scores.get(topTwenty.get(10)) + " has "
                    + topTwenty.get(10), 750, 250);
        }
        if (topTwenty.size() >= 12) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("12. " + scores.get(topTwenty.get(11)) + " has "
                    + topTwenty.get(11), 750, 300);
        }
        if (topTwenty.size() >= 13) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("13. " + scores.get(topTwenty.get(12)) + " has "
                    + topTwenty.get(12), 750, 350);
        }
        if (topTwenty.size() >= 14) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("14. " + scores.get(topTwenty.get(13)) + " has "
                    + topTwenty.get(13), 750, 400);
        }
        if (topTwenty.size() >= 15) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("15. " + scores.get(topTwenty.get(14)) + " has "
                    + topTwenty.get(14), 750, 450);
        }
        if (topTwenty.size() >= 16) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("16. " + scores.get(topTwenty.get(15)) + " has "
                    + topTwenty.get(15), 750, 500);
        }
        if (topTwenty.size() >= 17) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("17. " + scores.get(topTwenty.get(16)) + " has "
                    + topTwenty.get(16), 750, 550);
        }
        if (topTwenty.size() >= 18) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("18. " + scores.get(topTwenty.get(17)) + " has "
                    + topTwenty.get(17), 750, 600);
        }
        if (topTwenty.size() >= 19) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("19. " + scores.get(topTwenty.get(18)) + " has "
                    + topTwenty.get(18), 750, 650);
        }
        if (topTwenty.size() >= 20) {
            Graphics2D hello = (Graphics2D) g;
            hello.setFont(new Font("Verdana", Font.BOLD, 26));
            hello.setColor(Color.BLACK);
            hello.drawString("20. " + scores.get(topTwenty.get(19)) + " has "
                    + topTwenty.get(19), 750, 700);
        }
    }

    public void paintPause(Graphics g) {
        if (pause) {
            if (count >= -151) {
                count = -201;
            }
            if (count % 50 >= -4 || count % 50 < -46) {
                ImageIcon smile = new ImageIcon(this.getClass().getResource(
                        "pause5.png"));
                Image s = smile.getImage();
                Graphics2D draws = (Graphics2D) g;
                draws.drawImage(s, 0, 0, this);
            } else if (count % 50 >= -8 || count % 50 < -42) {
                ImageIcon smile = new ImageIcon(this.getClass().getResource(
                        "pause4.png"));
                Image s = smile.getImage();
                Graphics2D draws = (Graphics2D) g;
                draws.drawImage(s, 0, 0, this);
            } else if (count % 50 >= -12 || count % 50 < -38) {
                ImageIcon smile = new ImageIcon(this.getClass().getResource(
                        "pause3.png"));
                Image s = smile.getImage();
                Graphics2D draws = (Graphics2D) g;
                draws.drawImage(s, 0, 0, this);
            } else if (count % 50 >= -16 || count % 50 < -34) {
                ImageIcon smile = new ImageIcon(this.getClass().getResource(
                        "pause2.png"));
                Image s = smile.getImage();
                Graphics2D draws = (Graphics2D) g;
                draws.drawImage(s, 0, 0, this);
            } else {
                ImageIcon smile = new ImageIcon(this.getClass().getResource(
                        "pause1.png"));
                Image s = smile.getImage();
                Graphics2D draws = (Graphics2D) g;
                draws.drawImage(s, 0, 0, this);
            }
        }
        // ImageIcon smile = new ImageIcon(this.getClass().getResource(
        // "playButton2.png"));
        // Image s = smile.getImage();
        // Graphics2D draws = (Graphics2D) g;
        // draws.drawImage(s, 520, 610, this);
        // } else {
        // // ImageIcon smile = new ImageIcon(this.getClass().getResource(
        // // "pauseButton2.png"));
        // // Image s = smile.getImage();
        // // Graphics2D draws = (Graphics2D) g;
        // // draws.drawImage(s, 520, 610, this);
        // }
    }

    public void actionPerformed(ActionEvent e) {

        if (key != null && key.getKeyCode() == 32 && !t.getText().equals("")) {
            t.setText(t.getText().substring(0, t.getText().length() - 1));
            if (stars < 0) {
                stars = 0;
            }
        }

        if (flag == 0) {
            showScores.setVisible(false);
            t.setLocation(300, 400);
            if (key != null
                    && key.getKeyCode() == 10
                    || (mouse != null && mouse.getY() >= 530 && mouse.getY() <= 730)) {
                if (t.getText().equals(" Enter Here  ")
                        || t.getText().equalsIgnoreCase("")) {
                    mouse = null;
                } else if (t.getText().equalsIgnoreCase("scores")) {
                    count = 1150;
                    t.setText("");
                    mouse = null;
                    key = null;
                    flag = 2;
                } else {
                    user = t.getText();
                    flag = 2;
                    t.setLocation(300, 500);
                    this.t.setVisible(false);
                    count = -150;
                    t.setText("");
                    showScores.setVisible(false);

                }
            }

        } else if (flag == 1) {
            this.t.setVisible(false);
            if (key != null
                    && key.getKeyCode() == 10
                    || (mouse != null && mouse.getY() >= 530 && mouse.getY() <= 730)) {
                flag = 2;
                t.setLocation(300, 500);
                this.t.setVisible(true);
                text.setVisible(true);
                t.requestFocusInWindow();
            }

        } else if (flag == 2) {
            pause();
            count++;
            if (t.getText().equalsIgnoreCase(word)) {
                score = (double) Math.round((score + (System
                        .currentTimeMillis() - startTime) / 1000) * 100) / 100;
                System.out.println("SCORE IS = " + score);
                // String points = f.format((System.currentTimeMillis() -
                // startTime)/1000);
                // score+= (double) points.t;

                // score += 100 - count / 10;
                word = "Score:" + score;
                t.setText("");
                count = -200;
                numberOfWords -= 1;

            } else if (count == 999) {
                score += 25;
                System.out.println("SCORE IS = " + score);

                word = "Score:" + score;
                t.setText("");
                count = -200;
                numberOfWords -= 1;
            }
        }
        if (numberOfWords == 1) {
            showScores.setText("This is the last word!");
            showScores.setVisible(true);

        } else if (numberOfWords <= 0) {
            numberOfWords = 10;
            count = 1000;
        }

        stars++;

        if (stars % 25 <= 12) {
            y2 = 130;
            y = 110;
        } else {
            y2 = 110;
            y = 130;
        }
        if (count == 1149) {
            scores.put(score, user);
            checkScore();
            writeScore();

        }

        if (count == 1400) {
            showScores.setVisible(false);
            mouse = null;
            word = word();
            t.setText("");
            user = "";
            flag = 0;
            score = 0;
            count = 0;
        }

        if (stars == 100) {
            stars = 0;
        }

        // if (text.isVisible()) {
        // this.t.setVisible(false);
        // } else if (this.t.isVisible() || flag == 1) {
        // } else if (!this.t.isVisible()) {
        // this.t.setVisible(true);
        // }

        key = null;
        repaint();
    }

    public void pause() {
        if (count < 0) {
            if (key != null && key.getKeyCode() == 32) {
                if (pause) {
                    pause = false;
                } else {
                    pause = true;
                }
            }
        }
        // if (mouse != null && mouse.getY() >= 610 && mouse.getY() <= 760
        // && mouse.getX() >= 520 && mouse.getX() <= 670) {
        // if (pause) {
        // pause = false;
        // mouse = null;
        // } else {
        // pause = true;
        // mouse = null;
        // }
        // }
    }

    public String word() {
        Random rand = new Random();
        int r = rand.nextInt(words.length - 1);
        String choice = (String) words[r];
        return choice;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        mouse = e;
        if ((e.getY() >= 10 && e.getY() <= 36)
                && (e.getX() >= 20 && e.getX() <= 120)) {
            lang = 1;
        } else if ((e.getY() >= 37 && e.getY() <= 70)
                && (e.getX() >= 20 && e.getX() <= 120)) {
            lang = 0;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        key = e;
        if (!t.getText().isEmpty()) {
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    // makes the dictionary into a list
    public LinkedList<String> dictionary() {
        java.io.InputStream file = getClass().getResourceAsStream(hard);
        Scanner cns = new Scanner(file);
        LinkedList<String> diction = new LinkedList<String>();
        while (cns.hasNext()) {
            diction.add(cns.next());
        }
        return diction;
    }
}