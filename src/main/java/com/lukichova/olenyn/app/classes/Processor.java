package com.lukichova.olenyn.app.classes;

import com.lukichova.olenyn.app.DB.Dao;
import com.lukichova.olenyn.app.DB.DataBase;
import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.DB.GoodsDao;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;

import java.util.List;

public class Processor {

    public static Packet process(Packet packet) {
        DataBase.connect();
        String message = packet.getBMsq().getMessage();
        Integer comand =packet.getBMsq().getCType();
        Message  answerMessage=null;
        if(comand==Message.cTypes.GET_PRODUCTS.ordinal()){
            Dao<Goods> dao = new GoodsDao();
            List<Goods> list = dao.readAll();
            StringBuilder sb = new StringBuilder();
            String delim = "  ";
            int i = 0;
            while (i < list.size() - 1) {

                sb.append(list.get(i));
                sb.append(delim);
                i++;
            }
            sb.append(list.get(i));

            String res = sb.toString();
           answerMessage = new Message(1, 1, res);
        }
        if(comand==Message.cTypes.DELETE_PRODUCT.ordinal()){
             Goods goos= new Goods(packet.getBMsq().getMessage(),0);
             Dao<Goods> dao = new GoodsDao();
             dao.delete(goos);
            answerMessage = new Message(1, 1, "Product "+goos.getName()+" was deleted!");

        }
        if(comand==Message.cTypes.CREATE_PRODUCT.ordinal()){
            String in = packet.getBMsq().getMessage();
            String[] product = in.split(" ");
            Dao<Goods> dao = new GoodsDao();
            dao.create(product);
            answerMessage = new Message(1, 1,  "Product "+in+" was created!");
        }
        if(comand==Message.cTypes.SET_PRODUCT_PRICE.ordinal()){

            String in = packet.getBMsq().getMessage();
            String[] product = in.split(" ");
            Goods goos= new Goods(product[0],0);
            Dao<Goods> dao = new GoodsDao();
            dao.update(goos,product);
            answerMessage = new Message(1, 1,  "Product "+in+" was updated!");

        }

        if(comand==Message.cTypes.LIST_BY_CRITERIA.ordinal())
        {
            String in = packet.getBMsq().getMessage();
            String[] product = in.split(" ");

            List<Goods> list= Dao.listByCriteria(product);
            StringBuilder sb = new StringBuilder();
            String delim = "  ";
            int i = 0;
            while (i < list.size() - 1) {

                sb.append(list.get(i));
                sb.append(delim);
                i++;
            }
            sb.append(list.get(i));

            String res = sb.toString();
            answerMessage = new Message(1, 1, res);


        }
     /*   if (message.equals("time")) {
            answerMessage = new Message(1, 1, "OK");
        } else {
            answerMessage = new Message(1, 1, "other");
        }
        */
        Packet answerPacket = new Packet((byte) 1, packet.getBPktId(), answerMessage);
        answerPacket.setClientInetAddress(packet.getClientInetAddress());
        answerPacket.setClientPort(packet.getClientPort());

        return answerPacket;
    }
}
