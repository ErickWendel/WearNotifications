package br.com.erickwendel.wearnotifications;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {


    //notification manager
    NotificationManagerCompat mNotificationManager;

    final int NOTIFICATION_ID_1 = 1;
    final int NOTIFICATION_ID_2 = 2;
    final int NOTIFICATION_ID_3 = 3;
    final int NOTIFICATION_ID_4 = 4;
    final int NOTIFICATION_ID_5 = 5;
    final int NOTIFICATION_ID_6 = 6;
    final int NOTIFICATION_ID_7 = 7;
    final String  NOTIFICATION_TAG = "Minha Notificação";
    final String GROUP = "NotificationGroup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity_main);


        try {
            //recebe o contexto da notificacao
            mNotificationManager = NotificationManagerCompat.from(this);

            //constroi a notificacões de texto (simples)
            NotificationCompat.Builder notificationText = getNotificationBuilder()
                    .setStyle(getNotificationText());

            //constroi a notificacões com uma pagina de imagem
            NotificationCompat.Builder notificationImage = getNotificationBuilder()
                    .setStyle(getNotificationImage())
                    .setContentText("Com imagens");

            //constroi a notificacões com imagem de fundo
            NotificationCompat.Builder notificationBackGround = getNotificationBuilder()
                    .extend(getNotificationBackGroundImage());

            //notificacao com açao para abrir uma acao
            NotificationCompat.Builder notificationIntent = getNotificationIntent();

            //notificacao para abrir diversas acoes
            NotificationCompat.Builder notificationMultiplesActions = getNotificationMultiplesActions();

            //envia notificacao com paginas

            List<Notification> pages = new ArrayList<>();
            pages.add(notificationText.build());
            pages.add(notificationImage.build());
            pages.add(notificationBackGround.build());

            NotificationCompat.Builder notificationPages = sendNotificationWithPages(pages);



            //dispara as notificacões
            mNotificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID_1, notificationText.build());

            mNotificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID_2, notificationImage.build());

            mNotificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID_3, notificationBackGround.build());

            mNotificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID_4, notificationPages.build());

            mNotificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID_5, notificationIntent.build());

                mNotificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID_6, notificationMultiplesActions.build());

            //atualiza notificacoes
            updateNotification();

            //remove notificacao escolhida
            removeNotification(NOTIFICATION_ID_2, NOTIFICATION_TAG);


            //fecha o aplicativo enviando a mensagem
            closeApp();
        } catch (Exception ex) {
            TextView text = (TextView) findViewById(R.id.txtMessage);
            text.setText(ex.getMessage());
        }

    }

    private NotificationCompat.Builder getNotificationBuilder() {
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_goku)
                        .setContentTitle("Testando notificações")
                        .setContentText("Mais de 8 mil !!!!")
                        .setGroup(GROUP);
        return notification;
    }

    private NotificationCompat.Builder getNotificationIntent() {

        //reabre o aplicativo
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return getNotificationBuilder()
                .setContentIntent(pendingIntent)
                .setContentText("Notificação clicavel");
    }

    private NotificationCompat.Builder getNotificationMultiplesActions(){
        Intent action1 = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent1 = PendingIntent.getActivity(this, 0, action1, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent action2 = new Intent(Intent.ACTION_DIAL);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, action2, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setContentText("Multiple Actions")
                .setContentTitle("Notification")
                .setSmallIcon(R.drawable.ic_goku)
                .addAction(R.drawable.ic_goku_background, "Abrir Aplicativo", pendingIntent1)
                .addAction(R.drawable.ic_goku_head, "Ligar", pendingIntent2);

        return notification;



    }
    private NotificationCompat.WearableExtender getNotificationBackGroundImage() {
        NotificationCompat.WearableExtender extender = new NotificationCompat
                .WearableExtender()
                .setBackground(BitmapFactory.decodeResource(getResources(), R.drawable.ic_goku_sky));
        return extender;
    }

    private NotificationCompat.Style getNotificationText() {
        NotificationCompat.Style bigTextNotification = new NotificationCompat
                .BigTextStyle()
                .setBigContentTitle("Texto Grande!!")
                .setSummaryText("Sumario do texto")
                .bigText(getText());

        return bigTextNotification;
    }

    private NotificationCompat.Style getNotificationImage() {
        NotificationCompat.Style imageStyle = new NotificationCompat.BigPictureStyle()
                .setBigContentTitle("Texto com imagem!")
                .setSummaryText("Sumario do texto")
                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.ic_goku_background));

        return imageStyle;
    }
    private void removeNotification(Integer id, String tag) {
        Handler handler = new Handler();
        final Integer idItem = id;
        final String tagItem = tag;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNotificationManager.cancel(tagItem, idItem);
            }
        }, 5000);
    }
    private void updateNotification() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationCompat.Builder updateNotification = new NotificationCompat.Builder(MainActivity.this)
                        .setContentText("Update notification")
                        .setSmallIcon(R.drawable.ic_goku)
                        .setContentTitle("Updating notification...");


                mNotificationManager.notify(NOTIFICATION_ID_1, updateNotification.build());
            }
        }, 5000);
    }

    private NotificationCompat.Builder sendNotificationWithPages(List<Notification> pages) {
        NotificationCompat.WearableExtender extender = new NotificationCompat
                .WearableExtender()
                .addPages(pages);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_goku)
                .setContentTitle("Testing pages")
                .setContentText("With pages")
                .extend(extender);

        return notification;

    }

    private void closeApp() {
        Toast.makeText(MainActivity.this, "Notificacao enviada", Toast.LENGTH_SHORT).show();
        finish();
    }

    private String getText() {

        //frase da dilma
        return "Ai você fala o seguinte: \"- Mas vocês acabaram isso?\"" +
                " Vou te falar: -\"Não, está em andamento!\" Tem obras que \"vai\"" +
                " durar pra depois de 2010. Agora, por isso, nós já não desenhamos, não começamos a " +
                "fazer projeto do que nós \"podêmo fazê\"? 11, 12, 13, 14... Por que é que não?\n" +
                "\n" +
                "Primeiro eu queria cumprimentar os internautas. -Oi Internautas! Depois " +
                "dizer que o meio ambiente é sem dúvida nenhuma uma ameaça ao desenvolvimento sustentável. " +
                "E isso significa que é uma ameaça pro futuro do nosso planeta e dos nossos países. " +
                "O desemprego beira 20%, ou seja, 1 em cada 4 portugueses.";
    }
}
