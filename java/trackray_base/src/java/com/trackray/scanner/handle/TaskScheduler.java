package com.trackray.scanner.handle;

import com.trackray.scanner.utils.StrUtils;
import net.sf.json.JSONObject;

import java.io.*;
import java.net.Socket;

public class TaskScheduler extends Socket {
    public TaskScheduler() throws IOException {
        super("127.0.0.1",1010);
    }

    public static void main(String[] args) throws IOException {
        TaskScheduler taskScheduler = new TaskScheduler();
        JSONObject obj = new JSONObject();
        obj.put("123","321");

        taskScheduler.send(obj);
    }
    public void monitor() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(this.getInputStream()));

        while(this.isConnected() && !this.isClosed() && (reader.readLine())!=null)
        {
        }
    }

    public void send(JSONObject json){
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(this.getOutputStream()));
        }catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.write(StrUtils.base64(json.toString().getBytes("UTF-8")));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
