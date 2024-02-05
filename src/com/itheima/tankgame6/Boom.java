package com.itheima.tankgame6;

/**
 * tank爆炸
 */
public class Boom {
    int x;  //炸弹的坐标
    int y;
    int life = 10; //炸弹生命周期
    boolean isLive = true;

    public Boom(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Boom() {
    }

    //减少生命值
    public void lifeDown(){   //配合出现图片的爆炸效果
        if(life > 0){
            life--;
        }else{
            isLive = false;
        }
    }
}
