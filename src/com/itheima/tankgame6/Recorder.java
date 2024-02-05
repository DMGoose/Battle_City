package com.itheima.tankgame6;

import java.io.*;
import java.util.Vector;

/**
 * 用于记录相关的信息（击杀数量...）和文件交互
 */
public class Recorder {
    //定义变量，记录我方击毁敌人tank 数
    private static int allEnemyTankNum = 0;//每击毁一个就需要修改，所以给set,get

    //定义IO对象,准备用于写数据到文件里
    private static FileWriter fw = null;
    private static BufferedWriter bw = null;
    //把记录文件保存到src下面，不然拷贝工程的时候不好搞
    //private static String recordFile = "f:\\myRecord.txt";
    private static String recordFile = "src\\myRecord.txt";
    private static FileReader fr = null;
    private static BufferedReader br = null;
    //定义一个Vector,指向Mypanel对象的敌人tank 的Vector
    private static Vector<Tank_enemy> tank_enemies = null;

    //定义一个Node 的集合Vector，用于存储敌人的信息（坐标、方向）
    private static Vector<Node> nodes = new Vector<>();

    //这里提供了一个set方法，意思就是把tank_enemies集合 set成 Mypanel里面的enemyTank 这个集合
    public static void setTank_enemies(Vector<Tank_enemy> enemyTank) {
        Recorder.tank_enemies = enemyTank;
    }

    //返回记录文件的目录
    public static String getRecordFile() {
        return recordFile;
    }

    //提供一个方法，用于读取文件recordFile，恢复相关的信息
    //该方法，在继续上局游戏的时候调用即可
    public static Vector<Node> getNodesAndEnemyTankInfo(){
        try {
            br = new BufferedReader(new FileReader(recordFile));
            allEnemyTankNum = Integer.parseInt(br.readLine()); //读取第一行,这是个字符串，要转成int
            //循环读取文件，生成nodes
            String line = "";//
            while((line = br.readLine()) != null){
                //225 40 0 一行的数据长这样，要用split进行分割
                String[] xyd = line.split(" ");
                Node node = new Node(Integer.parseInt(xyd[0]),  //构造器要求整数，所以要转换一下
                            Integer.parseInt(xyd[1]), Integer.parseInt(xyd[2]));
                nodes.add(node);  //加到nodes集合里面去
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return nodes;
    }

    //增加一个方法，当游戏退出时，我们就将allEnemyTankNum保存到recordFile
    //升级该方法，保存敌人坦克的坐标和方向
    public static void saveRecord(){
        try {
            bw = new BufferedWriter(new FileWriter(recordFile));
            bw.write(allEnemyTankNum + "\r\n");
            //遍历敌人tank集合，把存活的保存下来
            //定义一个属性，通过set方法得到敌人tank 的Vector
            for (int i = 0; i < tank_enemies.size(); i++) {
                //取出敌人tank
                Tank_enemy tank_enemy = tank_enemies.get(i);
                if(tank_enemy.isLive){  //建议判断是否活着
                    //保存该tank_enemy信息
                    String record = tank_enemy.getX() + " " + tank_enemy.getY() + " " + tank_enemy.getDirect();
                    //写入到文件
                    bw.write(record + "\r\n");
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static int getAllEnemyTankNum() {
        return allEnemyTankNum;
    }

    public static void setAllEnemyTankNum(int allEnemyTankNum) {
        Recorder.allEnemyTankNum = allEnemyTankNum;
    }

    //当我方tank击毁一个敌人tank就应该对 allEnemyTankNum++
    public static void addAllEnemyTankNum(){
        Recorder.allEnemyTankNum++;
    }
}
