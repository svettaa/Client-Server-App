package com.lukichova.olenyn.app.classes;

import com.google.common.primitives.UnsignedLong;
import com.lukichova.olenyn.app.DB.DataBase;
import com.lukichova.olenyn.app.DB.Goods;
import com.lukichova.olenyn.app.DB.GoodsDao;
import com.lukichova.olenyn.app.entities.Message;
import com.lukichova.olenyn.app.entities.Packet;
import com.lukichova.olenyn.app.network.Network;

import java.util.List;

public class Processor {

    public static Packet process(Packet packet) {
        DataBase.connect();
        String message = packet.getBMsq().getMessage();
        Integer comand =packet.getBMsq().getCType();
        Message  answerMessage=null;
        if(comand==Message.cTypes.GET_PRODUCTS.ordinal()){
            List<Goods> list=GoodsDao.readAll();
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
        } else{
        if(comand==Message.cTypes.DELETE_PRODUCT.ordinal()){
             Goods goos= new Goods(packet.getBMsq().getMessage(),0);
             GoodsDao.delete(goos);
            answerMessage = new Message(1, 1, "Product "+goos.getName()+" was deleted!");

        } else{
        if(comand==Message.cTypes.CREATE_PRODUCT.ordinal()){
            String in = packet.getBMsq().getMessage();
            String[] product = in.split(" ");

            GoodsDao.create(product);
            answerMessage = new Message(1, 1,  "Product "+in+" was created!");
        } else{
        if(comand==Message.cTypes.SET_PRODUCT_PRICE.ordinal()){

            String in = packet.getBMsq().getMessage();
            String[] product = in.split(" ");
            Goods goos= new Goods(product[0],0);
            GoodsDao.update(goos,product);
            answerMessage = new Message(1, 1,  "Product "+in+" was updated!");

        } else{

        if(comand==Message.cTypes.LIST_BY_CRITERIA.ordinal())
        {
            String in = packet.getBMsq().getMessage();
            String[] product = in.split(" ");

            List<Goods> list=GoodsDao.listByCriteria(product);
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


        } else {

            answerMessage = new Message(1, 1, "Ivalid comand");
        }}}}}

        Packet answerPacket = new Packet((byte) 1, packet.getBPktId(), answerMessage);
        answerPacket.setClientInetAddress(packet.getClientInetAddress());
        answerPacket.setClientPort(packet.getClientPort());

        return answerPacket;
    }
}
