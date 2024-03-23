// Developed By E.D. - 20.03.2024
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

/*
    Fire and Water Game (en) - TUTORIAL
    This is a simple game that you can play with the arrow keys and the space key.
    The aim of the game is to reach the end of the map without touching the monsters.
    You can change the color of the player with the 1, 2, 3 keys.
    You can shoot with the space key.
    You can pause the game with the P key.
    You can save the game with the K key.
    You can load the game by running the program with the save.txt file as an argument.
 */

public class FireAndWaterGame extends JPanel implements ActionListener, KeyListener {
    private static final boolean DEBUG = false;
    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 600;
    private static final int CHARACTER_SIZE = 15;
    private static final ArrayList<Monster> monsters = new ArrayList<>();
    private static final ArrayList<Platform> platforms = new ArrayList<>();
    private static final ArrayList<Point> points = new ArrayList<>();
    private static final ArrayList<Orb> orbs = new ArrayList<>();
    private final ArrayList<Ammo> bullets = new ArrayList<>();
    private static int playerX = 0;
    private static int playerY = CANVAS_HEIGHT - 20 - CHARACTER_SIZE;
    private static int playerColor = 1;
    private static boolean playerShield = false;
    private static boolean monsterFreeze = false;
    private static int score = 0;
    private int playerSpeed = 0;
    private int jumpSpeed = 0;
    private int jumpCapacity = 10;
    private int playerRotation = 0;
    private final Timer timer;
    private static Spawner spawner;
    private boolean gameOver = false;
    private boolean gameStop = false;

    public FireAndWaterGame() {
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
        setBackground(Color.white);
        setFocusable(true);
        addKeyListener(this);
        timer = new Timer(20, this);
        timer.start();
        platformReady();
    }

    public ArrayList<Monster> getMonsters() {
        return monsters;
    }

    public void setjumpCapacity(int jumpCapacity) {
        this.jumpCapacity = jumpCapacity;
    }

    public void setMonsterFreeze(boolean monsterFreeze) {
        this.monsterFreeze = monsterFreeze;
    }

    public void setplayerShield(boolean playerShield) {
        this.playerShield = playerShield;
    }

    public void setAmmoShootSpeed(int shootSpeed) {
        for (Ammo bullet : bullets) {
            bullet.setShootSpeed(shootSpeed);
        }
    }

    private static void platformReady() {
        platforms.add(new Platform(0, CANVAS_HEIGHT - 20, CANVAS_WIDTH, 20, Color.white));
        platforms.add(new Platform(CANVAS_WIDTH - 60, CANVAS_HEIGHT - 60, 60, 60, Color.white));
        
        platforms.add(new Platform(0, 500, 250, 20, Color.white));
        platforms.add(new Platform(315, 500, 350, 20, Color.white));
        
        platforms.add(new Platform(80, 430, 320, 20, Color.white));
        platforms.add(new Platform(400, 430, 80, 20, Color.red));
        platforms.add(new Platform(480, 430, 70, 20, Color.white));
        platforms.add(new Platform(550, 430, 80, 20, Color.blue));
        platforms.add(new Platform(630, 430, CANVAS_WIDTH - 630, 20, Color.white));
        
        platforms.add(new Platform(0, 360, 250, 20, Color.white));
        platforms.add(new Platform(250, 360, 80, 20, Color.blue));
        platforms.add(new Platform(330, 360, 390, 20, Color.white));
        platforms.add(new Platform(0, 320, 60, 40, Color.white));
        platforms.add(new Platform(0, 280, 30, 40, Color.white));
        
        platforms.add(new Platform(80, 250, CANVAS_WIDTH - 80, 20, Color.white));
        
        platforms.add(new Platform(500, 190, 60, 20, Color.white));
        platforms.add(new Platform(560, 130, 60, 20, Color.white));
        platforms.add(new Platform(620, 70, 60, 20, Color.white));
        platforms.add(new Platform(680, 10, 60, 20, Color.white));
        
        platforms.add(new Platform(0, 100, 475, 20, Color.white));
        
        platforms.add(new Platform(0, 45, 30, 55, Color.pink));
        
        spawner = new Spawner(110, 60, 10000);
        
        monsters.add(new Monster(100, 80, 1));
        monsters.add(new Monster(100, 410, 3));
        monsters.add(new Monster(200, 480, 1));
        monsters.add(new Monster(550, 560, 2));

        for (int i = 50; i < 750; i += 50) {
            points.add(new Point(i, 565));
        }
        points.add(new Point(750, 525));
        points.add(new Point(780, 525));

        for (int i = 50; i <= 600; i += 50) {
            points.add(new Point(i, 485));
        }

        for (int i = 100; i <= 750; i += 50) {
            points.add(new Point(i, 415));
        }

        points.add(new Point(10, 265));
        points.add(new Point(40, 305));
        for (int i = 100; i <= 700; i += 50) {
            points.add(new Point(i, 345));
        }

        for (int i = 100; i <= 750; i += 50) {
            points.add(new Point(i, 235));
        }

        for (int i = 520, j = 175; i <= 640; i += 60, j -= 60) {
            points.add(new Point(i, j));
        }

        for (int i = 50; i <= 450; i += 50) {
            points.add(new Point(i, 85));
        }

        orbs.add(new PowerOrb(280, 510));
        orbs.add(new DefenceOrb(10, 410));
        orbs.add(new JumpOrb(765, 475));
        orbs.add(new HunterOrb(515, 395));
        orbs.add(new FreezeOrb(15,149));
        orbs.add(new DefenceOrb(745, 120));
        orbs.add(new FreezeOrb(785, 285));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Fire And Water Game");
            FireAndWaterGame game = new FireAndWaterGame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.getContentPane().add(game);
            frame.pack();
            frame.setVisible(true);
            if (args.length == 1) {
                loadGame(args[0]);
                game.gameStop = true;
            }
        });
    }

    private static void loadGame(String dosyaYolu) {
        try {
            Scanner scanner = new Scanner(new File(dosyaYolu));
            playerX = scanner.nextInt();
            playerY = scanner.nextInt();
            playerColor = scanner.nextInt();
            score = scanner.nextInt();
            int monsterNumber = scanner.nextInt();
            monsters.clear();
            for (int i = 0; i < monsterNumber; i++) {
                monsters.add(new Monster(scanner.nextInt(), scanner.nextInt(), scanner.nextInt()));
            }
            int pointNumber = scanner.nextInt();
            points.clear();
            for (int i = 0; i < pointNumber; i++) {
                points.add(new Point(scanner.nextInt(), scanner.nextInt()));
            }
            int orbNumber = scanner.nextInt();
            orbs.clear();
            for (int i = 0; i < orbNumber; i++) {
                int x = scanner.nextInt();
                int y = scanner.nextInt();
                int type = scanner.nextInt();
                switch (type) {
                    case 1:
                        orbs.add(new PowerOrb(x, y));
                        break;
                    case 2:
                        orbs.add(new DefenceOrb(x, y));
                        break;
                    case 3:
                        orbs.add(new JumpOrb(x, y));
                        break;
                    case 4:
                        orbs.add(new HunterOrb(x, y));
                        break;
                    case 5:
                        orbs.add(new FreezeOrb(x, y));
                        break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Platform getDownPlatform(int x, int y, int scale) {
        for (Platform platform : platforms) {
            if (platform.contains(x + scale / 2, y + scale + 1)) {
                return platform;
            }
        }
        return new Platform(0, 0, 0, 0, Color.white);
    }

    public static boolean platformIsDown(int x, int y, int scale) {
        for (Platform platform : platforms) {
            for (int i = 1; i < scale - 1; i++) {
                if (platform.contains(x + i, y + scale + 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean platformIsUp(int x, int y, int scale) {
        for (Platform platform : platforms) {
            for (int i = 0; i < scale; i++) {
                if (platform.contains(x + i, y - 1)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean platformIsRight(int x, int y, int scale) {
        for (Platform platform : platforms) {
            for (int i = 0; i < scale; i++) {
                if (platform.contains(x + scale + 1, y + i)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean platformIsLeft(int x, int y, int scale) {
        for (Platform platform : platforms) {
            for (int i = 0; i < scale; i++) {
                if (platform.contains(x - 1, y + i)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // =====[ background ]=====
        setBackground(Color.black);

        // =====[ platform ]=====
        for (Platform platform : platforms) {
            platform.draw(g);
        }

        // =====[ player ]=====
        g.setColor(getColor(playerColor));
        g.fillRect(playerX, playerY, CHARACTER_SIZE, CHARACTER_SIZE);

        // =====[ shield ]=====
        if (playerShield) {
            g.setColor(Color.green);
            g.drawOval(playerX - 5, playerY - 5, CHARACTER_SIZE + 10, CHARACTER_SIZE + 10);
        }

        // =====[ points ]=====
        for (Point Point : points) {
            Point.draw(g);
        }

        // =====[ orbs ]=====
        for (Orb Orb : orbs) {
            Orb.draw(g);
        }

        // =====[ monsters ]=====
        for (Monster Monster : monsters) {
            Monster.draw(g);
        }

        // =====[ ammo ]=====
        for (Ammo bullet : bullets) {
            g.setColor(getColor(bullet.getColor()));
            g.fillRect(bullet.getX(), bullet.getY(), 5, 5);
        }

        // =====[ score ]=====
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 15));
        g.drawString("Score: " + score, 10, 20);

        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Game Finish!", CANVAS_WIDTH / 2 - 50, CANVAS_HEIGHT / 2);
        }

        if (gameStop) {
            g.setColor(Color.blue);
            g.drawString("Game Stop", CANVAS_WIDTH / 2 - 50, CANVAS_HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameStop) {
            if (DEBUG) {
                System.out.println("player X: " + playerX + ", Y: " + playerY + ", Color: " + playerColor + ", Skor: " + score + ", monsters: " + monsters.size() + ", points: " + points.size() + ", Küreler: " + orbs.size());
            }

            if (!platformIsDown(playerX, playerY, CHARACTER_SIZE)) {
                playerY += 2;
            }

            if (jumpSpeed != 0) {
                if (platformIsUp(playerX, playerY, CHARACTER_SIZE)) {
                    jumpSpeed = 0;
                }
                if (jumpSpeed < 0) {
                    playerY += jumpSpeed;
                    jumpSpeed++;
                }
            }

            if (playerX + playerSpeed >= 0 && playerX + CHARACTER_SIZE + playerSpeed <= CANVAS_WIDTH) {
                if (playerSpeed > 0 && !platformIsRight(playerX, playerY, CHARACTER_SIZE) || playerSpeed < 0 && !platformIsLeft(playerX, playerY, CHARACTER_SIZE)) {
                    playerX += playerSpeed;
                }
            }

            if (platformIsDown(playerX, playerY, CHARACTER_SIZE) &&
                    ((Objects.requireNonNull(getDownPlatform(playerX, playerY, CHARACTER_SIZE)).getColor().equals(Color.red) && playerColor == 2) ||
                            (Objects.requireNonNull(getDownPlatform(playerX, playerY, CHARACTER_SIZE)).getColor().equals(Color.blue) && playerColor == 1) ||
                            ((Objects.requireNonNull(getDownPlatform(playerX, playerY, CHARACTER_SIZE)).getColor().equals(Color.red) ||
                                    Objects.requireNonNull(getDownPlatform(playerX, playerY, CHARACTER_SIZE)).getColor().equals(Color.blue)) && playerColor == 3))) {
                gameOver = true;
            }

            if (playerX >= 0 && playerX <= 30 && playerY >= 30 && playerY <= 100) {
                gameOver = true;
                gameExit();
            }

            for (int i = 0; i < monsters.size(); i++) {
                if (monsters.get(i).getHealth() <= 0) {
                    score += 10;
                    monsters.remove(i);
                    i--;
                }
            }

            if (!monsterFreeze) {
                for (Monster Monster : monsters) {
                    Monster.move();
                }
            }

            for (int i = 0; i < bullets.size(); i++) {
                bullets.get(i).move();
                if (bullets.get(i).isBound() || bullets.get(i).isActive() || platformIsRight(bullets.get(i).getX(), bullets.get(i).getY(), 5) || platformIsLeft(bullets.get(i).getX(), bullets.get(i).getY(), 5)) {
                    bullets.remove(i);
                    i--;
                }
            }

            for (Monster Monster : monsters) {
                for (Ammo bullet : bullets) {
                    if (bullet.getX() + 5 >= Monster.getX() && bullet.getX() <= Monster.getX() + Monster.getScale() &&
                            bullet.getY() + 5 >= Monster.getY() && bullet.getY() <= Monster.getY() + Monster.getScale()) {
                        if (Monster.getColor() == 3) {
                            bullet.hit();
                        } else if (bullet.getColor() == Monster.getColor()) {
                            Monster.healthUp();
                            bullet.hit();
                        } else if (bullet.getColor() != Monster.getColor()) {
                            Monster.healthDown();
                            bullet.hit();
                        }
                    }
                }
            }

            for (Monster Monster : monsters) {
                if (playerX + CHARACTER_SIZE >= Monster.getX() && playerX <= Monster.getX() + Monster.getScale() &&
                        playerY + CHARACTER_SIZE >= Monster.getY() && playerY <= Monster.getY() + Monster.getScale()) {
                    if (playerShield) {
                        playerShield = false;
                        Monster.setRotation(Monster.getRotation() == 0 ? 1 : 0);
                    } else {
                        gameOver = true;
                    }
                }
            }

            for (int i = 0; i < points.size(); i++) {
                if (playerX + CHARACTER_SIZE >= points.get(i).getX() && playerX <= points.get(i).getX() + points.get(i).getScale() &&
                        playerY + CHARACTER_SIZE >= points.get(i).getY() && playerY <= points.get(i).getY() + points.get(i).getScale()) {
                    score += 5;
                    points.remove(i);
                    i--;
                }
            }

            for (int i = 0; i < orbs.size(); i++) {
                if (playerX + CHARACTER_SIZE >= orbs.get(i).getX() && playerX <= orbs.get(i).getX() + 5 &&
                        playerY + CHARACTER_SIZE >= orbs.get(i).getY() && playerY <= orbs.get(i).getY() + 5) {
                    orbs.get(i).activate(this);
                    orbs.remove(i);
                    i--;
                }
            }

            if (spawner != null && spawner.timeFinish()) {
                monsters.add(new Monster(spawner.getX(), spawner.getY(), randomColor()));
                spawner.timeReset();
            }

            if (gameOver) {
                timer.stop();
                gameRestart();
            }

            repaint();
        }
    }

    private void gameExit() {
        JOptionPane.showMessageDialog(this, "You win!\nScore: " + score);
        System.exit(0);
    }

    private void gameRestart() {
        playerX = 0;
        playerY = CANVAS_HEIGHT - 20 - CHARACTER_SIZE;
        playerSpeed = 0;
        jumpSpeed = 0;
        jumpCapacity = 10;
        playerColor = 1;
        playerShield = false;
        monsterFreeze = false;
        setAmmoShootSpeed(5);
        score = 0;

        bullets.clear();
        monsters.clear();
        points.clear();
        orbs.clear();

        platformReady();

        gameOver = false;

        timer.start();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameStop) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    playerSpeed = -5;
                    playerRotation = 1;
                    break;
                case KeyEvent.VK_RIGHT:
                    playerSpeed = 5;
                    playerRotation = 0;
                    break;
                case KeyEvent.VK_UP:
                    if (platformIsDown(playerX, playerY, CHARACTER_SIZE) && Objects.requireNonNull(getDownPlatform(playerX, playerY, CHARACTER_SIZE)).getColor().equals(Color.white)) {
                        if (playerColor == 3) {
                            jumpSpeed = -(jumpCapacity + 4);
                        } else {
                            jumpSpeed = -(jumpCapacity);
                        }
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    if (playerColor != 3) {
                        bullets.add(new Ammo(playerX + CHARACTER_SIZE / 2, playerY + CHARACTER_SIZE / 4, playerColor, playerRotation));
                    }
                    break;
                case KeyEvent.VK_1:
                    playerColor = 1;
                    break;
                case KeyEvent.VK_2:
                    playerColor = 2;
                    break;
                case KeyEvent.VK_3:
                    playerColor = 3;
                    break;
            }
        }

        switch (e.getKeyCode()) {
            case KeyEvent.VK_P:
                gameStop = !gameStop;
                break;
            case KeyEvent.VK_K:
                if (gameStop) {
                    saveGame();
                    JOptionPane.showMessageDialog(this, "The game is saved. (save.txt)");
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            playerSpeed = 0;
        }
    }

    private void saveGame() {
        try {
            String currentDirectory = System.getProperty("user.dir");
            PrintWriter writer = new PrintWriter(currentDirectory + "/save.txt", "UTF-8");
            writer.println(playerX);
            writer.println(playerY);
            writer.println(playerColor);
            writer.println(score);
            writer.println(monsters.size());
            for (Monster Monster : monsters) {
                writer.println(Monster.getX());
                writer.println(Monster.getY());
                writer.println(Monster.getColor());
            }
            writer.println(points.size());
            for (Point Point : points) {
                writer.println(Point.getX());
                writer.println(Point.getY());
            }
            writer.println(orbs.size());
            for (Orb Orb : orbs) {
                writer.println(Orb.getX());
                writer.println(Orb.getY());
                if (Orb instanceof PowerOrb) {
                    writer.println(1);
                } else if (Orb instanceof DefenceOrb) {
                    writer.println(2);
                } else if (Orb instanceof JumpOrb) {
                    writer.println(3);
                } else if (Orb instanceof HunterOrb) {
                    writer.println(4);
                } else if (Orb instanceof FreezeOrb) {
                    writer.println(5);
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int randomX() {
        return (int) (Math.random() * (CANVAS_WIDTH - 40));
    }

    private int randomY() {
        return (int) (Math.random() * (CANVAS_HEIGHT - 40));
    }

    private int randomColor() {
        return (int) (Math.random() * 3) + 1;
    }

    private Color getColor(int color) {
        switch (color) {
            case 1:
                return Color.red;
            case 2:
                return Color.blue;
            case 3:
                return Color.magenta;
            default:
                return Color.black;
        }
    }
}

class Monster {
    private int x;
    private int y;
    private int color;
    private int rotation;
    private int health = 3;
    private int scale = 20;

    public Monster(int x, int y, int color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.rotation = (int) (Math.random() * 2);
    }

    public void draw(Graphics g) {
        switch (color) {
            case 1:
                g.setColor(Color.red);
                break;
            case 2:
                g.setColor(Color.blue);
                break;
            case 3:
                g.setColor(Color.orange);
                break;
        }
        g.fillRect(x, y, scale, scale);
    }
    
    public void move() {
        if (FireAndWaterGame.platformIsRight(x, y, scale) || x + scale >= FireAndWaterGame.CANVAS_WIDTH) {
            rotation = 1;
        } else if (FireAndWaterGame.platformIsLeft(x, y, scale) || x <= 0) {
            rotation = 0;
        }

        if (rotation == 0) {
            x += 2;
        } else {
            x -= 2;
        }
        
        if (!FireAndWaterGame.platformIsDown(x, y, scale)) {
            y += 2;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getColor() {
        return color;
    }
    public int getHealth() {
        return health;
    }
    public int getScale() {
        return scale;
    }

    public void healthDown() {
        health--;
    }

    public void healthUp() {
        health++;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getRotation() {
        return rotation;
    }
}

class Point {
    private int x;
    private int y;
    private int scale = 5;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        g.setColor(Color.yellow);
        g.fillOval(x, y, scale, scale);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getScale() {
        return scale;
    }
}

class Ammo {

    private int x;
    private int y;
    private int color;
    private int rotation;
    private int shootSpeed = 5;
    private boolean isActive = false;

    public Ammo(int x, int y, int renk, int yon) {
        this.x = x;
        this.y = y;
        this.color = renk;
        this.rotation = yon;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move() {
        if (rotation == 0) {
            x += shootSpeed;
        } else {
            x -= shootSpeed;
        }
    }

    public boolean isBound() {
        return x >= FireAndWaterGame.CANVAS_WIDTH || x <= 0;
    }

    public boolean isActive() {
        return isActive;
    }

    public void hit() {
        isActive = true;
    }

    public int getColor() {
        return color;
    }

    public void setShootSpeed(int shootSpeed) {
        this.shootSpeed = shootSpeed;
    }
}

class Spawner {
    private int x;
    private int y;
    private int timeout;
    private long startTime;

    public Spawner(int x, int y, int timeout) {
        this.x = x;
        this.y = y;
        this.timeout = timeout;
        startTime = System.currentTimeMillis();
    }

    public boolean timeFinish() {
        return System.currentTimeMillis() - startTime > timeout;
    }

    public void timeReset() {
        startTime = System.currentTimeMillis();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

class Platform {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;

    public Platform(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        if (color == null) {
            this.color = Color.white;
        } else {
            this.color = color;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public boolean contains(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
}

abstract class Orb {
    private final int x;
    private final int y;

    public Orb(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract void draw(Graphics g);

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract void activate(FireAndWaterGame game);
}

class DefenceOrb extends Orb {
    private int duration = 20000;

    public DefenceOrb(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.green);
        g.drawOval(getX(), getY(), 5, 5);
    }

    @Override
    public void activate(FireAndWaterGame game) {
        game.setplayerShield(true);
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                game.setplayerShield(false);
            }
        }, duration);
    }
}

class PowerOrb extends Orb {
    private int duration = 5000;
    public PowerOrb(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.drawOval(getX(), getY(), 5, 5);
    }

    @Override
    public void activate(FireAndWaterGame game) {
        game.setAmmoShootSpeed(8);
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                game.setAmmoShootSpeed(5);
            }
        }, duration);
    }
}

class JumpOrb extends Orb {
    private int duration = 10000;
    public JumpOrb(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        g.drawOval(getX(), getY(), 5, 5);
    }

    @Override
    public void activate(FireAndWaterGame game) {
        game.setjumpCapacity(15);
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                game.setjumpCapacity(10);
            }
        }, duration);
    }
}

class HunterOrb extends Orb {
    public HunterOrb(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.drawOval(getX(), getY(), 5, 5);
    }

    @Override
    public void activate(FireAndWaterGame game) {
        if (!game.getMonsters().isEmpty()) {
            game.getMonsters().remove(new Random().nextInt(game.getMonsters().size()));
        }
    }
}

class FreezeOrb extends Orb {
    private int duration = 5000;
    public FreezeOrb(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.CYAN);
        g.drawOval(getX(), getY(), 5, 5);
    }

    @Override
    public void activate(FireAndWaterGame game) {
        game.setMonsterFreeze(true);
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                game.setMonsterFreeze(false);
            }
        }, duration);
    }
}