package com.grappetite.zoya.postboy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

class NetworkUtils {

    private static final String TAG = "NetworkUtils";

    private static final String LINE_END = "\r\n";
    private static final String TWO_HYPHENS= "--";
    static final String BOUNDARY = "*****";


    static String convertMapToGETString(Map<String,String> map) throws UnsupportedEncodingException{
        if (map==null || map.size()==0)
            return "?";
            String getLink = "";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (getLink.isEmpty()) {
                    getLink = "?" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                } else {
                    getLink = getLink + "&" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                }
            }
            return getLink;
    }

    static String convertMapToPostXXXString(Map<String,String> map) throws UnsupportedEncodingException{
        if (map==null)
            return "";
            String getLink = "";
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (getLink.isEmpty()) {
                    getLink = URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                } else {
                    getLink = getLink + "&" + URLEncoder.encode(entry.getKey(), "UTF-8") + "=" + URLEncoder.encode(entry.getValue(), "UTF-8");
                }
            }
            return getLink;
    }

    static void addHeadersToHttpURLConnection(HttpURLConnection conn,@Nullable Map<String, String> headers)
    {
        if (headers!=null)
            for (Map.Entry<String,String> entry: headers.entrySet())
                conn.setRequestProperty(entry.getKey(),entry.getValue());
    }

    static void addFilesToHttpURLConnection(HttpURLConnection conn, @Nullable Map<String,File> files) throws IOException
    {
        if (files==null)
            return;

        OutputStream os = conn.getOutputStream();
        for (Map.Entry<String,File> entry: files.entrySet())
        {
            String key   = entry.getKey();
            File file       = entry.getValue();

            Log.i(TAG, "file: " + file.getAbsolutePath() + " || key: " + key);

            os.write((TWO_HYPHENS + BOUNDARY + LINE_END).getBytes());
            os.write(("Content-Disposition: form-data; name=\"" + key + "\";filename=\"" + file.getName() + "\"" + LINE_END).getBytes());
            os.write(("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName()) + LINE_END).getBytes());
            os.write(("Content-Transfer-Encoding: binary" + LINE_END).getBytes());
            os.write(LINE_END.getBytes());
            FileInputStream fis = new FileInputStream(file);

            byte[] buffer = new byte[1024];
            int c;
            while ((c = fis.read(buffer, 0, buffer.length)) > -1)
            {
                os.write(buffer, 0, c);
            }

            os.write(LINE_END.getBytes());
            os.flush();
            fis.close();
        }
    }

    static void addFormDataPostToHttpURLConnection(HttpURLConnection conn,@Nullable Map<String, String> posts) throws IOException
    {
        OutputStream os = conn.getOutputStream();
        if (posts==null)
        {
            os.close();
            return;
        }
        for (Map.Entry<String,String> entry : posts.entrySet())
        {
            String key = entry.getKey();
            String value = entry.getValue();

            Log.i(TAG,"post: "+key + " = " + value);

            os.write((TWO_HYPHENS + BOUNDARY + LINE_END).getBytes());
            os.write(("Content-Disposition: form-data; name=\""+key +"\"" + LINE_END).getBytes());
            os.write(LINE_END.getBytes());
            os.write((value + LINE_END).getBytes());
            os.flush();
        }

        os.flush();
        os.close();
    }

    static void addPostXWwwFormUrlencodedPostToHttpURLConnection(HttpURLConnection conn, @NonNull String postParams) throws IOException
    {
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(postParams);
        out.close();
    }



    static HttpURLConnection getHttpURLConnection(String link, int connectionTimeout, int readTimeout, boolean triggerPost) throws IOException{
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(triggerPost);
            conn.setUseCaches(false);
            conn.setReadTimeout(connectionTimeout);
            conn.setConnectTimeout(readTimeout);
            return conn;
    }

    static ResponseData getResponseFromHttpURLConnection(HttpURLConnection conn)
    {
        try {
            int responseCode=conn.getResponseCode();
            String response;
            if (responseCode >= HttpsURLConnection.HTTP_OK && responseCode<=299)
            {
                String line;
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);
                response = "";
                while ((line=br.readLine()) != null)
                {
                    response+=line;
                }
            }
            else if (conn.getErrorStream()!=null)
            {
                String line;
                InputStream is = conn.getErrorStream();
                if (is==null)
                    return new ResponseData(responseCode,"");
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br=new BufferedReader(isr);
                response="";
                while ((line=br.readLine()) != null)
                {
                    response+=line;
                }
            }
            else
                return new ResponseData(responseCode,"");
            return new ResponseData(responseCode,response);
        }catch (IOException e) {
            Log.e(TAG,e.toString());
            return null;
        }
    }
}
