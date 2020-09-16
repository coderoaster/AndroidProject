package com.example.RoomChef;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;


public class SendMail extends AppCompatActivity {

    // 보내는 계정의 id
    String user = "dejun210";
    // 보내는 계정의 pw
    String password = "dek147305!";

    public void sendSecurityCode(Context context,String content ,String sendTo) {
        try {
            GMailSender gMailSender = new GMailSender(user, password);
            gMailSender.sendMail("방구석 쉐프 인증번호 입니다.\n인증번호를 확인해주세요.", content, sendTo);

            Toast.makeText(context, "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
        } catch (SendFailedException e) {

            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Log.v("err",e.toString());
            Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}


