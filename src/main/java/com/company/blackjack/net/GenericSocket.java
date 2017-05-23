package com.company.blackjack.net;

import com.company.blackjack.commands.ByeCommand;
import com.company.blackjack.commands.Command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class GenericSocket implements SocketListener {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    private boolean ready;

    public GenericSocket(Socket socket) {
        this.socket = socket;
    }

    public void connect() {
        new Thread(new SetUpThread()).start();
        new Thread(new ReceiverThread()).start();
    }

    public void close() {
        try {
            if(isOpen()) {
                socket.close();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen() {
        return socket != null && !socket.isClosed();
    }

    public void sendCommand(Object command) {
        try {
            if (isOpen() && out != null) {
                out.writeObject(command);
                out.flush();
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void waitForReady() {
        while(!ready) {
            try {
                wait();
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void notifyReady() {
        ready = true;
        notifyAll();
    }

    private class SetUpThread implements Runnable {

        @Override
        public void run() {
            try {
                if(socket != null) {
                    out = new ObjectOutputStream(socket.getOutputStream());
                    in = new ObjectInputStream(socket.getInputStream());
                    out.flush();
                }
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                notifyReady();
            }
        }
    }

    private class ReceiverThread implements Runnable {

        @Override
        public void run() {
            waitForReady();

            try {
                Command command;
                while((command = (Command) in.readObject()) != null) {
                    onCommand(command);
                    if(command instanceof ByeCommand) {
                        break;
                    }
                }
            } catch(ClassNotFoundException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }
    }
}
