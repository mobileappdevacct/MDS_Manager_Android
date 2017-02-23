package com.mymdsmanager.task;

import android.os.AsyncTask;

import org.apache.http.client.ClientProtocolException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public abstract class BasicTask extends AsyncTask<String,Void,Object> {

	
	protected CompleteListener mCompleteListener;

	public BasicTask(CompleteListener pCompleteListener)
	{
		mCompleteListener = pCompleteListener;		
	}
	
	public String getJson(String url) throws ClientProtocolException, IOException{
		
//		Log.d("BasicTask", url);
//
//		String result = null;
//		InputStream is = null;
//		DefaultHttpClient client = new DefaultHttpClient();
//
//		HttpParams httpParameters = new BasicHttpParams();
//		// Set the timeout in milliseconds until a connection is established.
//		// The default value is zero, that means the timeout is not used.
//		int timeoutConnection = 60000;
//		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
////		HttpConnectionParams.setHeader("Accept", "application/json");
//
//		// Set the default socket timeout (SO_TIMEOUT)
//		// in milliseconds which is the timeout for waiting for data.
//		int timeoutSocket = 60000;
//		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
//
//		client.setParams(httpParameters);
//
//		try {
//
//			HttpResponse response = client.execute(new HttpGet(url.replaceAll(" ", "").trim()));
//			StatusLine statusLine = response.getStatusLine();
//			if(statusLine.getStatusCode() == 200){
//
//				StringBuilder builder = new StringBuilder();
//
//				HttpEntity entity = response.getEntity();
//				is = entity.getContent();
//
//				BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//				String data;
//				while((data = reader.readLine()) != null ){
//					builder.append(data + '\n');
//				}
//
//				result = builder.toString();
//
//			}else{
//				Log.d("", "response code " + statusLine.getStatusCode());
//			}
//
//		}finally{
//			try {
//				if(is != null)	is.close();
//
//				//Shuts down this connection manager and releases allocated resources.
//				//This includes closing all connections, whether they are currently used or not.
//				client.getConnectionManager().shutdown();
//			} catch (IOException e) {
//
//				e.printStackTrace();
//			}
//		}
		URL url1;
		String response = "";
		try {
			url1 = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
			conn.setReadTimeout(15000);
			conn.setConnectTimeout(15000);
			conn.setRequestMethod("GET");

			conn.setDoInput(true);
			conn.setDoOutput(true);

			int responseCode = conn.getResponseCode();
//			if (responseCode == HttpsURLConnection.HTTP_OK) {
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				while ((line = br.readLine()) != null) {
					response += line;
				}
//			} else {
//				response = "";
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	
	
	
}
