package com.example.wifirssi.tasks;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TcpTask extends AsyncTask<Void, Void, Void> {

    TextView tvLocation;
    String location = "Waiting for response...";

    public TcpTask(TextView tvLocation) {
        this.tvLocation = tvLocation;
    }

    private String requestLocation(String observedRSSValues) {

        try {
            String address = "192.168.0.112";
            String myLocation;

            Socket socket = new Socket(address, 5000);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            try {
                dos.writeUTF(observedRSSValues);
                myLocation = dis.readUTF();
                return myLocation;

            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                socket.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        return "Server Down";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        tvLocation.setText(location);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        location = requestLocation("-35_-60_-56_-56_");
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        tvLocation.setText(location);
    }
}
