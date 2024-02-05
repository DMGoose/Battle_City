package com.itheima.tankgame6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Vector;

/**
 * Panel窗口，游戏坦克大战的绘图区域
 */

//为了监听键盘事件，需要实现KeyListener
    //为了让panel不停地重绘子弹,需要将Mypanel实现Runnable接口，（可以直接在画子弹后面repaint），但其他内容需要写到run方法里面去
public class MyPanel extends JPanel implements KeyListener, Runnable{
    //定义我的坦克
    Tank_64 tank_64 = null;

    //定义敌人的坦克集合，放入到Vector集合里
    Vector<Tank_enemy> enemyTank = new Vector<>();
    int enemyTankNumber = 3;
    private KeyEvent e;

    //定义一个存放Node对象的Vector，用于恢复敌人tank的坐标和方向
    Vector<Node> nodes = new Vector<>();

    //定义炸弹集合，存放在Vector里
    Vector<Boom> booms = new Vector<>();

    //放入炸弹（当子弹击中tank时，就加入一个boom对象到炸弹集合）

    //定义三张炸弹图片，用于显示爆炸效果，在构造器里初始化一下
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;

    //定义子弹对象，定义到我的tank类里去了

    //
    public MyPanel(String key) {  //在构造器里设置初始化内容
        //传一个key进来，让你选择是继续游戏还是开新游戏
        //在读取文件之前要判断是否存在，如果存在就正常执行，如果不存在提示只能开启新的游戏，将key = “1”；
        File file = new File(Recorder.getRecordFile());
        if (file.exists()){
            //恢复数据
            nodes = Recorder.getNodesAndEnemyTankInfo();  //返回一个集合
        }else{
            System.out.println("There is no previous record, you can start a new game");
            key = "1";
        }

        //初始化自己的 tank
        tank_64 = new Tank_64(500, 100); //设置我的坦克x，y,设置初始化坐标
        tank_64.setSpeed(5);//设置初始化速度

        switch(key){
            case "1":  //一个新的游戏
                Recorder.setAllEnemyTankNum(0);
                //初始化敌人的tank，放几个进去
                for (int i = 0; i < enemyTankNumber; i++) {
                    //创建一个tank
                    Tank_enemy tank_enemy = new Tank_enemy(100 * (i + 1), 0);
                    //将 enemyTank 集合设置给 敌人tank类中的 tank_enemies 集合（注意：这里的外部在遍历，所以是在遍历中完成的）
                    tank_enemy.setTank_enemies(enemyTank);
                    //设置tank方向
                    tank_enemy.setDirect(2);//设置敌人的tank朝向为下，或者在tank_enemy类的构造器里面设置也可以
                    new Thread(tank_enemy).start();//启动敌人的tank线程，让它动起来
                    //把tank加入集合
                    enemyTank.add(tank_enemy);

                    //定义一颗子弹（构造器里确定子弹的位置）
                    Bullet bullet = new Bullet(tank_enemy.getX() + 20, tank_enemy.getY() + 60,
                            tank_enemy.getDirect());
                    //把子弹加入子弹的集合bulletVector
                    tank_enemy.bulletVector.add(bullet);
                    //启动bullet对象
                    new Thread(bullet).start();
                }
                break;
            case "2":  //继续上一句游戏

                //初始化敌人的tank，放几个进去
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    //创建一个tank
                    Tank_enemy tank_enemy = new Tank_enemy(node.getX(),node.getY());
                    //将 enemyTank 集合设置给 敌人tank类中的 tank_enemies 集合（注意：这里的外部在遍历，所以是在遍历中完成的）
                    tank_enemy.setTank_enemies(enemyTank);
                    //设置tank方向
                    int direct = node.getDirect();
                    tank_enemy.setDirect(direct);//设置敌人的tank朝向为下，或者在tank_enemy类的构造器里面设置也可以
                    new Thread(tank_enemy).start();//启动敌人的tank线程，让它动起来
                    //把tank加入集合
                    enemyTank.add(tank_enemy);

                    //定义一颗子弹（构造器里确定子弹的位置）
                    Bullet bullet = new Bullet(tank_enemy.getX() + 20, tank_enemy.getY() + 60,
                            tank_enemy.getDirect());
                    //把子弹加入子弹的集合bulletVector
                    tank_enemy.bulletVector.add(bullet);
                    //启动bullet对象
                    new Thread(bullet).start();
                }
                break;
            default:
                System.out.println("Something wrong with you input");
        }

        //将MyPanel的 enemyTank集合 设置给 Recorder的 tank_enemies 集合
        Recorder.setTank_enemies(enemyTank);


        //初始化图片对象
        image1 = Toolkit.getDefaultToolkit().getImage("src\\bomb_1.gif");
        image2 = Toolkit.getDefaultToolkit().getImage("src\\bomb_2.gif");
        image3 = Toolkit.getDefaultToolkit().getImage("src\\bomb_3.gif");

        //在这里播放指定的音乐
        new AePlayWave("src\\111.wav").start();
    }

    //编写方法，显示我方击毁敌方坦克的信息
    public void showInfo(Graphics g){ //需要绘图
        //画出玩家的总成绩
        g.setColor(Color.black);
        Font font = new Font("宋体", Font.BOLD, 25);
        g.setFont(font);

        g.drawString("Destroyed Enemy Tanks：",1020,30);
        this.drawTank(1020,60,g,0,0); //画了一个敌方tank
        g.setColor(Color.black); //这里需要重新设置成黑色，因为上面那个drawTank方法把画笔重置成其他的颜色了
        g.drawString(Recorder.getAllEnemyTankNum()+ "",1080,100);
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);//填充矩形，默认是黑色

        showInfo(g);

        //画出自己坦克 -> 封装到方法
        if(tank_64 != null && tank_64.isLive) {
            drawTank(tank_64.getX(), tank_64.getY(), g, tank_64.getDirect(), 1);//画出自己的坦克
        }

        //画出自己的tank射击的子弹
        if(tank_64.bullet != null && tank_64.bullet.isLive == true){
            g.draw3DRect(tank_64.bullet.x,tank_64.bullet.y,1,1,false);
            repaint();
        }

        //将我的tank的子弹集合 bullets， 遍历取出绘制
        for (int i = 0; i < tank_64.bullets.size(); i++) {
            Bullet bullet = tank_64.bullets.get(i);
            if(bullet != null && bullet.isLive){
                g.draw3DRect(bullet.x, bullet.y, 1, 1, false);
            }else{  //如果子弹已经死了，就从bullets集合中拿掉
                tank_64.bullets.remove(bullet);
            }
        }

        //画出敌人的tank，所以要遍历Vector集合(因为不知道集合里面有几个tank)
        for (int i = 0; i < enemyTank.size(); i++) {
            Tank_enemy tank_enemy = enemyTank.get(i);
            //tank可能死了，所以先要判断当前tank死没死,活着的才画
            if(tank_enemy.isLive) {
                drawTank(tank_enemy.getX(), tank_enemy.getY(), g, tank_enemy.getDirect(), 0);
                //画出敌人tank的所有子弹，需要遍历集合取出子弹
                for (int j = 0; j < tank_enemy.bulletVector.size(); j++) {
                    Bullet bullet = tank_enemy.bulletVector.get(j); //取出子弹
                    //绘制敌人的子弹
                    if (bullet.isLive == true) {  //可以直接写成 if(bullet.isLive)
                        g.draw3DRect(bullet.x, bullet.y, 1, 1, false);
                        repaint();
                    } else {   //当子弹死了就要移除
                        //从Vector移除
                        tank_enemy.bulletVector.remove(bullet);
                    }
                }
            }
        }

        //如果炸弹集合中有对象，就画出炸弹
        for (int i = 0; i < booms.size(); i++) {
            //取出炸弹
            Boom boom = booms.get(i);
            //根据当前bomb对象的life值去画出对应的图片(血多画大的，血少画小的)
            if(boom.life > 6){
                g.drawImage(image1,boom.x,boom.y,60,60,this);
            } else if (boom.life > 3) {
                g.drawImage(image2,boom.x,boom.y,60,60,this);
            }else {
                g.drawImage(image3,boom.x,boom.y,60,60,this);
            }
            //让炸弹的生命值减少
            boom.lifeDown();
            //如果boom的life为0，就从炸弹集合中删除
            if(boom.life == 0 ){
                booms.remove(boom);
            }
        }
    }

    /**
     * 画坦克的方法
     *
     * @param x      坦克左上角x横坐标
     * @param y      坦克左上角y纵坐标
     * @param g      画笔，图形
     * @param direct 坦克方向（上下左右）
     * @param type   坦克类型（用不同的数字表示不同的类型：敌人的，我的）
     */
    //编写方法，画出坦克
    public void drawTank(int x, int y, Graphics g, int direct, int type) {
        //根据不同类型的坦克，设计不同颜色
        switch (type) {
            case 0:  //敌人的坦克
                g.setColor(Color.cyan);
                break;
            case 1:  //自己的坦克
                g.setColor(Color.yellow);
                break;
        }

        //根据坦克的方向，来绘制坦克
        //基本坦克绘制好后，要绘制不同方向的tank
        //direct表示方向
        switch (direct) {
            case 0:  //表示向上
                g.fill3DRect(x, y, 10, 60, false); //画出坦克左边的轮子
                g.fill3DRect(x + 30, y, 10, 60, false); //画出坦克右边的轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false); //画出坦克的车体
                g.fillOval(x + 10, y + 20, 20, 20); //画出坦克的圆形盖子
                g.drawLine(x + 20, y, x + 20, y + 30); //画出坦克的炮筒
                break;
            case 1: //表示向右
                g.fill3DRect(x, y, 60, 10, false); //画出坦克上边的轮子
                g.fill3DRect(x, y + 30, 60, 10, false); //画出坦克右边的轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false); //画出坦克的车体
                g.fillOval(x + 20, y + 10, 20, 20); //画出坦克的圆形盖子
                g.drawLine(x + 30, y + 20, x + 60, y + 20); //画出坦克的炮筒
                break;
            case 2: //表示向下
                g.fill3DRect(x, y, 10, 60, false); //画出坦克左边的轮子
                g.fill3DRect(x + 30, y, 10, 60, false); //画出坦克右边的轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false); //画出坦克的车体
                g.fillOval(x + 10, y + 20, 20, 20); //画出坦克的圆形盖子
                g.drawLine(x + 20, y + 20, x + 20, y + 60); //画出坦克的炮筒
                break;
            case 3: //表示向左
                g.fill3DRect(x, y, 60, 10, false); //画出坦克上边的轮子
                g.fill3DRect(x, y + 30, 60, 10, false); //画出坦克右边的轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false); //画出坦克的车体
                g.fillOval(x + 20, y + 10, 20, 20); //画出坦克的圆形盖子
                g.drawLine(x + 30, y + 20, x, y + 20); //画出坦克的炮筒
                break;
            default:
                System.out.println("暂时没有处理");
        }
    }


    //编写方法，判断我方的子弹是否击中tank(进入到tank区域就代表集中了)
    //什么是后判断我方子弹是否击中tank？ --> 放到run方法里面去

    //如果我们的tank可以发射多发子弹，现在判断是否击中，就需要遍历子弹集合，和敌人的所有tank判断
    public void hitEnemyTank(){
        //遍历我方tank子弹集合
        for (int j = 0; j < tank_64.bullets.size(); j++) {
            Bullet bullet = tank_64.bullets.get(j);
            //判断是否击中了敌人tank
            if(bullet != null && bullet.isLive){  //当前子弹存活
                // 遍历敌人所有的tank,因为要把每个tank都判断一下
                for (int i = 0; i < enemyTank.size(); i++) {
                    Tank_enemy tank_enemy = enemyTank.get(i); //取出一个tank
                    hitTank(bullet, tank_enemy);
                }
            }
        }
    }

    public void hitMyTank(){
        //编写击中我的tank 的代码(其实可以写成个方法)
        //先遍历敌人的tank，然后再遍历子弹，判断是否击中
        for (int i = 0; i < enemyTank.size(); i++) {
            Tank_enemy tank_enemy = enemyTank.get(i); //取出敌人tank
            for (int j = 0; j < tank_enemy.bulletVector.size(); j++) {  //遍历敌人坦克的所有子弹
                Bullet bullet = tank_enemy.bulletVector.get(j);
                //判断bullet是否击中我的tank，先判断我的tank是否活着
                if(tank_64.isLive && bullet.isLive){
                    //下面可以直接调用hitTank方法，采用多态，把原先的形参Tank_enemy tank_enemy改成Tank tank_enemy
                    //这样就可以把我的tank传进去了，也可以用方法重载
                    hitTank(bullet, tank_64);
                }
            }
        }
    }


    //编写方法，判断我方的子弹是否击中tank(进入到tank区域就代表集中了)
    //什么是后判断我方子弹是否击中tank？ --> 放到run方法里面去
    public void hitTank(Bullet bullet, Tank tank_enemy){
        switch (tank_enemy.getDirect()){
            //tank和向下，子弹到tank的区域里面，子弹和tank就都死了
            case 0: //向上
            case 2: //向下
                if (bullet.x > tank_enemy.getX() && bullet.x < tank_enemy.getX() + 40 &&
                        bullet.y > tank_enemy.getY() && bullet.y < tank_enemy.getY() + 60){
                    bullet.isLive = false;
                    tank_enemy.isLive = false;

                    //当我的子弹击中敌人tank后，将敌人tank从敌人集合中去掉
                    enemyTank.remove(tank_enemy);
                    enemyTankNumber--;
                    //当机会一个敌人tank时，就对数据allEnemyTankNum++
                    //解读：因为这里的参数是Tank tank_enemy，在hitMytank里面也写了这个方法，所以有可能是敌方tank，也有可能是我的tank
                    //所以必须判断是否击中的是敌方的tank
                    if(tank_enemy instanceof Tank_enemy){
                        Recorder.addAllEnemyTankNum();
                    }

                    //创建Bomb对象，加到炸弹集合
                    Boom boom = new Boom(tank_enemy.getX(), tank_enemy.getY());
                    booms.add(boom);
                }
            case 3: //Left
            case 1:  //Right
                if(bullet.x > tank_enemy.getX() && bullet.x < tank_enemy.getX()+ 60 &&
                bullet.y > tank_enemy.getY() && bullet.y < tank_enemy.getY()+ 40 ){
                    bullet.isLive = false;
                    tank_enemy.isLive = false;

                    //当我的子弹击中敌人tank后，将敌人tank从敌人集合中去掉
                    enemyTank.remove(tank_enemy);
                    enemyTankNumber--;

                    //创建Bomb对象，加到炸弹集合
                    Boom boom = new Boom(tank_enemy.getX(), tank_enemy.getY());
                    booms.add(boom);

                    if(tank_enemy instanceof Tank_enemy){
                        Recorder.addAllEnemyTankNum();
                    }
                }
                break;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S) {  //KeyEvent.VK_S就是s对应的code
            tank_64.setDirect(2);  //方向朝下
            //修改tank 的坐标，写成一个方法,封装在tank父类里面
            if(tank_64.getY() + 60 < 750)
            tank_64.moveDown();  //向下移动
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            tank_64.setDirect(0);
            if(tank_64.getY() > 0){
                tank_64.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            tank_64.setDirect(1);
            if(tank_64.getX()+60 < 1000){
                tank_64.moveRight();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            tank_64.setDirect(3);
            if(tank_64.getX() > 0){
                tank_64.moveLeft();
            }
        }
        this.repaint();//如果不重绘，就不会动

        if (e.getKeyCode() == KeyEvent.VK_J) {
            System.out.println("User pressed J, started to shooting");

            //判断我的tank子弹是否销毁(发射一颗子弹，意思就是没消亡，就打不出第二颗子弹)
            //if (tank_64.bullet == null || !tank_64.bullet.isLive) {
//                tank_64.shotEnemyTank();
//            }
            //按下J，表达现在我的tank需要发射子弹了
            //发射多颗子弹
            tank_64.shotEnemyTank();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {   //每隔100毫秒，重绘区域,刷新绘图区域，子弹就移动
        while(true){
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            //判断是否击中敌人tank
            hitEnemyTank();

            //判断是否击中我的tank
            hitMyTank();

            this.repaint();
        }
    }
}
