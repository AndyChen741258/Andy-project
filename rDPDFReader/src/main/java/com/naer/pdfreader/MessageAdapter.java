package com.naer.pdfreader;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import static com.naer.pdfreader.Config.ServerHost;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context context;
    private List<chat> mChat;
    public static TextToSpeech mTTs;

    public FirebaseUser firebaseUser;
    private String fileToDownload;
    private String TAG = "Riamber";

    public MessageAdapter(Context context, List<chat> mChat) {
        this.context = context;
        this.mChat = mChat;

        mTTs = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Locale locale = Locale.US;
                    mTTs.setLanguage(locale);
                    mTTs.setSpeechRate(0.75f);
                }
            }
        });
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // 設定本機端使用者的發言顯示在右邊　好友(對方)的發言在左邊　
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        chat chat = mChat.get(position);

        holder.show_message.setText(chat.getMessage());
        holder.uptime.setText(chat.getUptime());

        // 顯示已讀功能
        if (chat.isSeen()) {
            holder.text_seen.setText("已讀");
        } else {
            holder.text_seen.setText(" ");
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public Button show_message;
        public ImageView item_image;
        public TextView uptime;

        public TextView text_seen;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            item_image = itemView.findViewById(R.id.item_image);
            text_seen = itemView.findViewById(R.id.text_seen);
            uptime = itemView.findViewById(R.id.uptime);

            show_message.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (show_message.getText().toString().contains("voice")) {
                        fileToDownload = show_message.getText().toString().toLowerCase();
                        new DownloadFile().execute();
                        Log.d(TAG, "onClick: " + fileToDownload);
                    } else {
                        mTTs.speak(show_message.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            });
        }
    }

    private class DownloadFile extends AsyncTask<String, Integer, String> {
        String videoToDownload = ServerHost + "/uploads/" + fileToDownload + ".3gp";

        @Override
        protected String doInBackground(String... params) {
            try {
                loadVoice(videoToDownload);
                Log.d(TAG, "doInBackground: " + videoToDownload);
            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
//            Log.d(TAG, "onPostExecute: " + result);
            try {
                MediaPlayer mediaPlayer = new MediaPlayer();
                String path = Environment.getExternalStorageDirectory() + "/RiamberVoice/" + fileToDownload + ".3gp";
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void loadVoice(String voiceUrl) {
            try {
                URL url = new URL(voiceUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();

                String PATH = Environment.getExternalStorageDirectory() + "/RiamberVoice";
                String tempName = "test.3gp";

                File outputFile = new File(PATH, tempName);

                FileOutputStream fos = new FileOutputStream(outputFile);

                InputStream is = con.getInputStream();
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "The file doesn't exist, sorry!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        // 本機端使用者的發言 顯示在右邊
        if (mChat.get(position).getSender().equals(firebaseUser.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }

}
