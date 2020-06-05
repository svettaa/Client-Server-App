package com.lukichova.olenyn.app.classes;

import com.lukichova.olenyn.app.DB.Dao;
import com.lukichova.olenyn.app.DB.DataBase;
import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.DB.GoodsDao;
import com.lukichova.olenyn.app.Exceptions.wrongDataBaseConnection;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;

import java.util.List;

public class Processor {

    public static Packet process(Packet packet) throws wrongDataBaseConnection {
        /*DataBase.connect();
        String message = packet.getBMsq().getMessage();

        Integer comand =packet.getBMsq().getCType();
        Message  answerMessage=null;
        Dao<Goods> dao = new GoodsDao();
        String in = packet.getBMsq().getMessage();
        String[] product = in.split(" ");
        List<Goods> list= dao.listByCriteria(product);
        StringBuilder sb = new StringBuilder();
        String delim = "  ";
        int i = 0;
        String res = sb.toString();
        switch (packet.getBMsq().getCType()){
            case 0:
                while (i < list.size() - 1) {
                    sb.append(list.get(i));
                    sb.append(delim);
                    i++;
                }
                sb.append(list.get(i));
                answerMessage = new Message(1, 1, res);
                break;
            case 1:
                Goods good= new Goods(product[0],0);
                dao.update(good,product);
                answerMessage = new Message(1, 1,  "Product "+in+" was updated!");
                break;
            case 2:
                Goods good2= new Goods(packet.getBMsq().getMessage(),0);
                dao.delete(good2);
                answerMessage = new Message(1, 1, "Product "+good2.getName()+" was deleted!");
                break;
            case 3:
                dao.create(product);
                answerMessage = new Message(1, 1,  "Product "+in+" was created!");
                break;
 case 4:
                while (i < list.size() - 1) {
                    sb.append(list.get(i));
                    sb.append(delim);
                    i++;
                }
                sb.append(list.get(i));
                answerMessage = new Message(1, 1, res);

        }*/

        /*Packet answerPacket = new Packet((byte) 1, packet.getBPktId(), answerMessage);
        answerPacket.setClientInetAddress(packet.getClientInetAddress());
        answerPacket.setClientPort(packet.getClientPort());

        return answerPacket;*/
        return null;
    }
}
