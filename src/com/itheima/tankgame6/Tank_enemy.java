package com.itheima.tankgame6;

import java.util.Vector;

/**
 * 敌人的tank
 */
public class Tank_enemy extends Tank implements Runnable{
    //在敌人tank类，使用Vector保存多个子弹
    Vector<Bullet> bulletVector = new Vector<>();

    //增加成员，Tank_enemy可以得到敌人tank的Vector，碰撞要用到
    Vector<Tank_enemy> tank_enemies = new Vector<>();
    boolean isLive = true;
    public Tank_enemy(int x, int y) {
        super(x, y);
    }

    //这里提供一个方法，可以将MyPanel的成员 Vector<Tank_enemy> enemyTank = new Vector<>();
    //设置到Tank_enemy的成员 tank_enemies(集合)，碰撞要用到
    //意思就是：让Mypanel里面的tank集合 赋值到 该类的新定义的一个tank集合
    public void setTank_enemies(Vector<Tank_enemy> enemyTank) {
        this.tank_enemies = enemyTank;
    }

    //编写方法，判断当前的这个坦克，是否和Vector<Tank_enemy> enemyTank中的其他tank发生了重叠或碰撞
    public boolean isTouchEnemy(){
        //判断当前敌人tank(this)的方向
        switch (this.getDirect()){

            case 0: //Up
                //让当前的敌人tank和其他所有的敌人tank比较
                //要把所有的tank取出来
                for (int i = 0; i < tank_enemies.size(); i++) {
                    //从vector取出一个敌人tank_enemy
                    Tank_enemy tank_enemy = tank_enemies.get(i);

                    //现在为当前的敌人tank（方向为上）和其他所有的敌人tank进行碰撞比较，具体看图
                    //不和自己进行碰撞比较,所以写个if过滤这种情况
                    if(tank_enemy != this){
                        //1.如果敌人坦克是上/下方向
                        if(tank_enemy.getDirect() == 0 || tank_enemy.getDirect() == 2){
                            //敌人坦克是上/下方向: 需要考虑其x，y的范围， 为：getX+40,getY+60
                            //当前坦克是上，需要考虑其边界的x，y(左上角和右上角)

                            // 左上角为getX，getY
                            if(this.getX() >= tank_enemy.getX()
                                    && this.getX() <= tank_enemy.getX()+40
                                    && this.getY() >= tank_enemy.getY()
                                    && this.getY() <= tank_enemy.getY()+60){
                                return true; //在这个范围内了，那就碰撞了
                            }

                            //右上角为 getX+40,getY
                            if(this.getX()+40 >= tank_enemy.getX()
                                    && this.getX()+40 <= tank_enemy.getX()+40
                                    && this.getY() >= tank_enemy.getY()
                                    && this.getY() <= tank_enemy.getY()+60){
                                return true; //在这个范围内了，那就碰撞了
                            }

                        }

                        //2.如果敌人坦克是左/右方向
                        if(tank_enemy.getDirect() == 1 || tank_enemy.getDirect() == 3){
                            //左上角为getX，getY
                            if(this.getX() >= tank_enemy.getX()
                                    && this.getX() <= tank_enemy.getX()+60
                                    && this.getY() >= tank_enemy.getY()
                                    && this.getY() <= tank_enemy.getY()+40){
                                return true; //在这个范围内了，那就碰撞了
                            }
                            //右上角为 getX+40,getY
                            if(this.getX() >= tank_enemy.getX()
                                    && this.getX()+40 <= tank_enemy.getX()+60
                                    && this.getY() >= tank_enemy.getY()
                                    && this.getY()+40 <= tank_enemy.getY()+40){
                                return true; //在这个范围内了，那就碰撞了
                            }
                        }
                    }
                }
                break;

            case 1: //Right
                for (int i = 0; i < tank_enemies.size(); i++) {
                    Tank_enemy tank_enemy = tank_enemies.get(i);
                    if(tank_enemy != this){
                        //1.敌人的tank为上，下方向，x，y为getX(),getY()
                        if(tank_enemy.getDirect() == 0 || tank_enemy.getDirect() == 2){
                             //当前tank为右，考虑碰撞
                             //右上角，x = getX +60, y = getY
                            if(this.getX() + 60 >= tank_enemy.getX()
                            && this.getX() + 60 <= tank_enemy.getX() + 40
                            && this.getY() >= tank_enemy.getY()
                            && this.getY() <= tank_enemy.getY() +60){
                                return true;
                            }

                            //右下角，x = getX + 60, y = getY + 40
                            if(this.getX()+60 >= tank_enemy.getX()
                                    && this.getX() + 60 <= tank_enemy.getX() + 40
                                    && this.getY() + 40 >= tank_enemy.getY()
                                    && this.getY() + 40 <= tank_enemy.getY() +60){
                                return true;
                            }
                        }

                        //2.敌人的tank为左，右方向，x，y为getX(),getY()
                            //右上角，x = getX +60, y = getY
                        if(tank_enemy.getDirect() == 1 || tank_enemy.getDirect() == 3){
                            if(this.getX() + 60 >= tank_enemy.getX()
                                    && this.getX() + 60 <= tank_enemy.getX() + 60
                                    && this.getY() >= tank_enemy.getY()
                                    && this.getY() <= tank_enemy.getY() + 40){
                                return true;
                            }
                                //右下角，x = getX + 60, y = getY + 40
                            if(this.getX() + 60 >= tank_enemy.getX()
                                    && this.getX() + 60 <= tank_enemy.getX() + 60
                                    && this.getY() + 40 >= tank_enemy.getY()
                                    && this.getY() + 40 <= tank_enemy.getY() + 40) {
                                return true;
                            }
                        }
                    }
                }
                break;

            case 2: //Down
                for (int i = 0; i < tank_enemies.size(); i++) {
                    Tank_enemy tank_enemy = tank_enemies.get(i);
                    if(tank_enemy != this){
                        //1.敌人的tank为上，下方向，x，y为getX(),getY()
                        if(tank_enemy.getDirect() == 0 || tank_enemy.getDirect() == 2){
                            //当前tank为下，考虑碰撞
                            //左下角，x = getX, y = getY + 60
                            if(this.getX() >= tank_enemy.getX()
                                    && this.getX() <= tank_enemy.getX() + 40
                                    && this.getY() + 60 >= tank_enemy.getY()
                                    && this.getY() + 60<= tank_enemy.getY() +60){
                                return true;
                            }

                            //右下角，x = getX + 40, y = getY + 60
                            if(this.getX() + 40 >= tank_enemy.getX()
                                    && this.getX() + 40 <= tank_enemy.getX() + 40
                                    && this.getY() + 60 >= tank_enemy.getY()
                                    && this.getY() + 60 <= tank_enemy.getY() +60){
                                return true;
                            }
                        }

                        //2.敌人的tank为左，右方向，x，y为getX(),getY()
                         //左下角，x = getX, y = getY + 60
                        if(tank_enemy.getDirect() == 1 || tank_enemy.getDirect() == 3){
                            if(this.getX() >= tank_enemy.getX()
                                    && this.getX() <= tank_enemy.getX() + 60
                                    && this.getY() + 60 >= tank_enemy.getY()
                                    && this.getY() + 60 <= tank_enemy.getY() + 40){
                                return true;
                            }
                            //右下角，x = getX + 40, y = getY + 60
                            if(this.getX() + 40 >= tank_enemy.getX()
                                    && this.getX() + 40 <= tank_enemy.getX() + 60
                                    && this.getY() + 60 >= tank_enemy.getY()
                                    && this.getY() + 60 <= tank_enemy.getY() + 40) {
                                return true;
                            }
                        }
                    }
                }
                break;

            case 3:  //Left
                for (int i = 0; i < tank_enemies.size(); i++) {
                    Tank_enemy tank_enemy = tank_enemies.get(i);
                    if(tank_enemy != this){
                        //1.敌人的tank为上，下方向，x，y为getX(),getY()
                        if(tank_enemy.getDirect() == 0 || tank_enemy.getDirect() == 2){
                            //当前tank为左，考虑碰撞
                            //左上角，x = getX, y = getY
                            if(this.getX() >= tank_enemy.getX()
                                    && this.getX() <= tank_enemy.getX() + 40
                                    && this.getY() >= tank_enemy.getY()
                                    && this.getY() <= tank_enemy.getY() +60){
                                return true;
                            }

                            //左下角，x = getX, y = getY + 40
                            if(this.getX() >= tank_enemy.getX()
                                    && this.getX() <= tank_enemy.getX() + 40
                                    && this.getY() + 40 >= tank_enemy.getY()
                                    && this.getY() + 40 <= tank_enemy.getY() +60){
                                return true;
                            }
                        }

                        //2.敌人的tank为左，右方向，x，y为getX(),getY()
                            //左上角，x = getX, y = getY
                        if(tank_enemy.getDirect() == 1 || tank_enemy.getDirect() == 3){
                            if(this.getX() >= tank_enemy.getX()
                                    && this.getX() <= tank_enemy.getX() + 60
                                    && this.getY() >= tank_enemy.getY()
                                    && this.getY() <= tank_enemy.getY() + 40){
                                return true;
                            }
                            //左下角，x = getX, y = getY + 40
                            if(this.getX() >= tank_enemy.getX()
                                    && this.getX() <= tank_enemy.getX() + 60
                                    && this.getY() + 40 >= tank_enemy.getY()
                                    && this.getY() + 40 <= tank_enemy.getY() + 40) {
                                return true;
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }


    @Override
    public void run() {
        while(true){
            //我现在想要敌方子弹消亡后，还能再发射子弹
            //为什么放run方法里：放入run里是因为可以一直不停地创建子弹，并放到集合里
            //这里我们判断如果bulletVector.size() == 0，说明集合里面已经没子弹了
            if(bulletVector.size() < 2 ){  //如果敌方的子弹数小于等于2，就可以发射（一共可以发2颗）,为什么我的没变？
                //那就创建一颗子弹，放入到集合里，并启动
                if(isLive && bulletVector.size() == 0){
                    //判断tank的方向，创建对应的子弹
                    Bullet bullet = null;
                    switch (getDirect()){
                        case 0:
                            bullet = new Bullet(getX()+20,getY(),0);  //子弹发射出来的位置，也就是炮筒的位置
                            break;
                        case 1:
                            bullet = new Bullet(getX()+60,getY()+20,1);
                            break;
                        case 2:
                            bullet = new Bullet(getX()+20,getY()+60,2);
                            break;
                        case 3:
                            bullet = new Bullet(getX(),getY()+20,3);
                            break;
                    }
                    //加入到集合里面去
                    bulletVector.add(bullet);

                    //启动
                    new Thread(bullet).start();

                }
            }
            //根据tank的方向来继续移动
            switch (getDirect()){
                case 0:// UP
                    //让tank保持一个方向，走随机的步数
                    for (int i = 0; i < (int)(Math.random() *500); i++) {
                        if (getY() > 0  && !isTouchEnemy()) {
                            moveUp();
                        }
                        //休眠一下
                        try {
                            Thread.sleep(1000/60);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 1:// Right
                    for (int i = 0; i < (int)(Math.random() *500); i++) {
                        if( getX()+ 60 < 1000 && !isTouchEnemy()){
                            moveRight();
                        }
                        try {
                            Thread.sleep(1000/60);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i <  (int)(Math.random() *500); i++) {
                        if( getY() +40 < 750 && !isTouchEnemy()){
                            moveDown();
                        }
                        try {
                            Thread.sleep(1000/60);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i <  (int)(Math.random() *30); i++) {
                        if(getX() > 0 && !isTouchEnemy()){
                            moveLeft();
                        }
                        try {
                            Thread.sleep(1000/60);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
            }
            //然后随机改变方向
            setDirect((int)(Math.random() *4));

            //写并发程序的时候，一定要考虑清楚什么时候结束这个线程
            if(!isLive){
                break; //退出线程
            }
        }
    }
}
