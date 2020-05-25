package com.lukichova.olenyn.app.practice_2;


public class Worker extends Thread{

    private int id;
    private Data data;

    public Worker(int id, Data data){
        super("Worker"+id);
        this.id = id;
        this.data = data;

        this.start();
    }

    @Override
    public void run() {
        synchronized (data) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (data.getState() != id)
                        data.wait();

                    if (id == 1){
                        data.Tic();
                    } else if(id ==2){
                        data.Tak();
                    } else {
                        data.Toy();
                    }

                    data.notifyAll();
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
