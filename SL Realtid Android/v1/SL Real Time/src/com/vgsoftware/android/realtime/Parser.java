package com.vgsoftware.android.realtime;

import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.util.Log;

public class Parser
{
	private static void ParseHTMLSubway(String html)
	{
	}
	
	public static ArrayList<Departure> getRealTimeInformationCommuterTrain(String url)
	{
		Log.v("SL RealTime","getRealTimeInformaitonCommuterTrain");
		ArrayList<Departure> departures=new ArrayList<Departure>();

		Object[] nodes=getNodes(Utility.getContent(url));
		for(int i=0;i<nodes.length;i++)
		{
			TagNode node=(TagNode)nodes[i];
			if(node.getChildTags().length==3 && !node.getChildTags()[0].getText().toString().equals(""))
			{
				departures.add(extractDeparture(node.getChildTagList()));
			}
		}
		return departures;
	}
	
	public static ArrayList<Departure> getRealTimeInformationSubway(String url)
	{
		//
		ArrayList<Departure> departures=new ArrayList<Departure>();
		Object[] nodes=getNodes(Utility.getContent(url));
		for(int i=0;i<nodes.length;i++)
		{
			TagNode node=(TagNode)nodes[i];
			if(node.getChildTags().length==1 && !node.getChildTags()[0].getText().toString().equals(""))
			{
				departures.addAll(extractSubwayDeparture(node.getChildTagList()));
			}
		}
		return departures;
	}

	private static Object[] getNodes(String html)
	{
		if(html!=null && !html.equals(""))
		{
			try
			{
				html=html.replace("&nbsp;","");
				
				HtmlCleaner cleaner = new HtmlCleaner();
		
				CleanerProperties props = cleaner.getProperties();
				props.setAllowHtmlInsideAttributes(true);
				props.setAllowMultiWordAttributes(true);
				props.setRecognizeUnicodeChars(true);
				props.setOmitComments(true);
		
				TagNode root = cleaner.clean(html);
				return root.evaluateXPath("//table//tr");
			}
			catch(Exception ex)
			{
			}
		}
		return new Object[0];
	}
	
	@SuppressWarnings("unchecked")
	private static Departure extractDeparture(List nodes)
	{
		Departure departure=new Departure();

		departure.setDestination(((TagNode)nodes.get(1)).getText().toString());
		departure.setTime(((TagNode)nodes.get(0)).getText().toString());
		departure.setStatus(((TagNode)nodes.get(2)).getText().toString());		
		
		return departure;
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<Departure> extractSubwayDeparture(List nodes)
	{
		ArrayList<Departure> departures=new ArrayList<Departure>();
		Departure departure=new Departure();
		String data=((TagNode)nodes.get(0)).getText().toString().trim();
		String[] parts=data.split("(,|\\.   )");
		if(parts.length>1)
		{
			for(int i=0;i<parts.length;i++)
			{
				data=parts[i].trim();
				if(!data.equals(""))
				{
					departures.add(createDeparture(parts[i]));
				}
			}
		}
		else if(!data.equals(""))
		{
			departures.add(createDeparture(data));
		}
		return departures;
	}
	
	private static Departure createDeparture(String data)
	{
		Departure departure=new Departure();
		departure.setDestination(data.trim().replace("  "," "));
		departure.setStatus("");
		departure.setStatus("");
		return departure;
	}
}
