package com.vgsoftware.android.realtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.UnsupportedCharsetException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class Utility
{
	public static String getContent(String url)
	{
		Log.v("SL RealTime","getContent");
		try
		{
			HttpClient hc = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			HttpResponse rp = hc.execute(get);

			if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				String content= getString(rp.getEntity().getContent());
				return content;
			}
			else
			{
				Log.d("[RealTimeData]","No Data found");
				return  "No Data Found";
			}
		}
		catch (IOException e)
		{
			Log.d("[RealTimeData]","IO Exception",e);
			return "Anka Error: " + e.getMessage();
		}
	}
	
	public static String getString(InputStream stream)
	{
		Log.v("SL RealTime","getString");
		StringBuilder sb = new StringBuilder();
		try
		{
			InputStreamReader reader = new InputStreamReader(stream,"ISO-8859-1");
			BufferedReader buffer = new BufferedReader(reader,8192);

			String line;
			while ((line = buffer.readLine()) != null)
			{
				sb.append(line + "\n");
			}
		}
		catch(UnsupportedCharsetException uce)
		{
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return "Error: "+e.getMessage();
		}
		try
		{
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return "Kalle Error: "+e.getMessage();
		}
		return sb.toString();
	}
}
