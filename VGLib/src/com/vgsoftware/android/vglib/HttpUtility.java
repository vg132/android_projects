package com.vgsoftware.android.vglib;

import android.annotation.SuppressLint;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.net.ssl.HttpsURLConnection;

public class HttpUtility
{
	@SuppressLint("DefaultLocale")
	public static String getData(final URL url) throws IOException
	{
		TrustManagerManipulator.allowAllSSL();
		HttpURLConnection connection = null;
		HttpURLConnection.setFollowRedirects(true);
		if (url.getProtocol().toLowerCase().equals("https"))
		{
			connection = (HttpsURLConnection) url.openConnection();
		}
		else
		{
			connection = (HttpURLConnection) url.openConnection();
		}
		if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK)
		{
			BufferedReader reader = null;
			if (connection.getContentEncoding() != null)
			{
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), connection.getContentEncoding()));
			}
			else
			{
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			}
			String line = reader.readLine();
			if (line != null)
			{
				String result = line;
				while ((line = reader.readLine()) != null)
				{
					result += line;
				}
				return result;
			}
		}
		return null;
	}

	public static void postData(final String url, final Map<String, String> params) throws IOException
	{
		try
		{
			postData(new URL(url), params);
		}
		catch (MalformedURLException e)
		{
			throw new IllegalArgumentException("invalid url: " + url);
		}
	}

	public static void postData(final URL url, final Map<String, String> params) throws IOException
	{
		StringBuilder bodyBuilder = new StringBuilder();
		Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
		// constructs the POST body using the parameters
		while (iterator.hasNext())
		{
			Entry<String, String> param = iterator.next();
			bodyBuilder.append(param.getKey()).append('=').append(param.getValue());
			if (iterator.hasNext())
			{
				bodyBuilder.append('&');
			}
		}
		String body = bodyBuilder.toString();
		byte[] bytes = body.getBytes();
		HttpURLConnection conn = null;
		try
		{
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setUseCaches(false);
			conn.setFixedLengthStreamingMode(bytes.length);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
			// post the request
			OutputStream out = conn.getOutputStream();
			out.write(bytes);
			out.close();
			// handle the response
			int status = conn.getResponseCode();
			if (status != 200)
			{
				throw new IOException("Post failed with error code " + status);
			}
		}
		finally
		{
			if (conn != null)
			{
				conn.disconnect();
			}
		}
	}
}
