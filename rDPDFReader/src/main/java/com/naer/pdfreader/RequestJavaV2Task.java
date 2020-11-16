package com.naer.pdfreader;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.QueryParameters;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;

//public class RequestJavaV2Task extends AsyncTask<Void, Void, DetectIntentResponse> {
public class RequestJavaV2Task {

    public  interface TaskCompletedListener{
        void onTaskCompleted(DetectIntentResponse response);
    }

    public static class CallRequestJavaV2Task extends  AsyncTask<Void, Void, DetectIntentResponse> {

        Activity activity;
        private SessionName session;
        private SessionsClient sessionsClient;
        private QueryInput queryInput;
        private QueryParameters queryParameters;
        private TaskCompletedListener listener;

        protected void setOnTaskCompletedListener(TaskCompletedListener listener) {
            this.listener = listener;
        }

        public CallRequestJavaV2Task(Activity activity, SessionName session, SessionsClient sessionsClient, QueryInput queryInput, QueryParameters queryParameters, TaskCompletedListener listener) {
            this.activity = activity;
            this.session = session;
            this.sessionsClient = sessionsClient;
            this.queryInput = queryInput;
            this.queryParameters = queryParameters;
            setOnTaskCompletedListener(listener);
        }

//        RequestJavaV2Task(Activity activity, SessionName session, SessionsClient sessionsClient, QueryInput queryInput) {
//            this.activity = activity;
//            this.session = session;
//            this.sessionsClient = sessionsClient;
//            this.queryInput = queryInput;
//        }

        @Override
        protected DetectIntentResponse doInBackground(Void... voids) {
            try {
                DetectIntentRequest detectIntentRequest =
                        DetectIntentRequest.newBuilder()
                                .setSession(session.toString())
                                .setQueryInput(queryInput)
                                .setQueryParams(queryParameters)
                                .build();
                return sessionsClient.detectIntent(detectIntentRequest);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(DetectIntentResponse response) {
//            ((ConversationPractice) activity).callbackV2(response);
            this.listener.onTaskCompleted(response);
        }
    }
}
