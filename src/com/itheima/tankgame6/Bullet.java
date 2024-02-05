package com.itheima.tankgame6;

/**
 * 实现子弹发射的线程,按下J时，线程启动，子弹不停移动
 */
public class Bullet implements Runnable{
    int x;
    int y;
    int direct = 0;
    int speed = 2;
    boolean isLive = true;  //子弹是否还存活

    public Bullet(int x, int y,int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }


    @Override
    public void run() {
        while(true){
            //射击行为一定要休眠，这样才会有动态效果，不然一下就没了
            try {
                Thread.sleep(1000/60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //根据方向来改变x或者y坐标，每隔50ms改变自己的方向
            switch (direct){
                case 0 :  //UP
                    y -= speed;
                    break;
                case 1:  //RIGHT
                    x += speed;
                    break;
                case 2:  //DOWN
                    y += speed;
                    break;
                case 3:  //LEFT
                    x -= speed;
                    break;
            }

            //输出X和Y的坐标
            //System.out.println("子弹x：" + x + "子弹y："+ y);
            //如果子弹移动到边界，就要销毁子弹(后续增加条件：当子弹碰到敌人的tank，也销毁)
            if(!(x >= 0 && x<1000 && y>=0 && y<= 750 && isLive)){
                isLive = false; //子弹死了
                break; //退出
            }
        }
    }
}
