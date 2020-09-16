package com.example.RoomChef;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ConnectFTP extends AsyncTask<Integer, String, Boolean> {

    private final String TAG = "Connect FTP";
    public FTPClient mFTPClient = null;

    Context context;
    String host;
    String username;
    String password;
    String timeStamp =new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    int port;
    Uri file;

    public ConnectFTP(Context context, String host, String username, String password, int port, Uri file) {
        this.context = context;
        this.host = host;
        this.username = username;
        this.password = password;
        this.port = port;
        this.file = file;
        mFTPClient = new FTPClient();
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        // FTP 접속 체크
        boolean status = false;
        // FTP 접속 시
        if (status = ftpConnect(host, username, password, port)) {
            String currentPath = ftpGetDirectory() + "imgs";

            // 파일 업로드시


            if (ftpUploadFile(file, timeStamp+".jpg", currentPath)) {
                Log.v("ConnectFTP", "Success");
            }
        }

        if (status == true) {
            ftpDisconnect();
        }

        return true;
    }

    public String imgname(){

        Log.v("imgname",timeStamp+".jpg");
        return timeStamp+".jpg";
    }

    public boolean ftpConnect(String host, String username, String password, int port) {
        boolean result = false;

        try {
            mFTPClient.connect(host, port);
            if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
                result = mFTPClient.login(username, password);
                mFTPClient.enterLocalPassiveMode();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean ftpDisconnect() {
        boolean result = false;

        try {
            mFTPClient.logout();
            mFTPClient.disconnect();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String ftpGetDirectory() {
        String directory = null;
        try {
            directory = mFTPClient.printWorkingDirectory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory;
    }

    public boolean ftpChangeDirectory(String directory) {
        try {
            mFTPClient.changeWorkingDirectory(directory);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //                                     저장할 파일 이름        저장할 FTP 폴더 경로
    public boolean ftpUploadFile(Uri file, String desFileName, String desDriectroy) {
        boolean result = false;
        try {
            InputStream fis = context.getContentResolver().openInputStream(file);
            if (ftpChangeDirectory(desDriectroy)) {
                // FTP File 타입 설정
                mFTPClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                result = mFTPClient.storeFile(desFileName, fis);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}


