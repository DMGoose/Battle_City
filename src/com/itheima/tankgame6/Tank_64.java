package com.itheima.tankgame6;

import java.util.Vector;

/**
 * 坦克
 */
public class Tank_64 extends Tank {
    //每一个类默认有一个无参构造器，在子类所有的构造器都会先默认调用父类无参构造器，super()，这个写不写都有的
    //此时，父类没有无参构造器，因此在创建子类构造器的时候会报错
    //如何解决？ -->手动调用父类的有参构造器 super(...),这样就可以初始化继承自父类的数据
    public Tank_64(int x, int y){   //通过构造器，完成初始化，在构造器里调用父类构造器
       super(x, y);
    }

    public int speed = 5;

    //定义一个Bullet对象,因为是自己的tank发射子弹，所以封装在tank里
    //表示一个射击（线程）
    Bullet bullet = null;

    //可以发射多颗子弹
    Vector<Bullet> bullets = new Vector<>();

    //射击方法，封装在我的tank类里面
    public void shotEnemyTank(){
        //发射多颗子弹，太多了，限制只能五颗
        if (bullets.size() == 5){
            return;
        }

        //创建 Bullet 对象，要根据tank的位置和方向来创建子弹对象
        //bullet对象的坐标就是炮筒口的坐标
        switch (getDirect()){   //得到自己的tank 对象的方向,根据不同的方向创建不同的bullet对象
            case 0:  //UP
                bullet = new Bullet(getX()+20,getY(),0);
                break;
            case 1:  //RIGHT
                bullet = new Bullet(getX()+60,getY()+ 20,1);
                break;
            case 2:  //DOWN
                bullet = new Bullet(getX()+20,getY()+ 60,2);
                break;
            case 3:  //LEFT
                bullet = new Bullet(getX(),getY()+ 20,3);
                break;
        }
        //把新建的bullet放到集合中
        bullets.add(bullet);

        //启动射击的线程
        new Thread(bullet).start();
    }
}
