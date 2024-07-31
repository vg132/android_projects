package com.vgsoftware.android.sosmap;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.vgsoftware.android.vglib.XMLUtility;

public class SOSParser
{
	private static final String FeedURL = "http://androidsoslive.appspot.com/sos_live";
	
	public static List<Event> parse()
	{
		Element element=XMLUtility.getRootElement(SOSParser.FeedURL);
		if(element!=null)
		{
			return parse(element);
		}
		return null;
	}
	
	public static List<Event> parse(Element root)
	{
		List<Event> events=new ArrayList<Event>();
		NodeList nodeList = root.getElementsByTagName("e");
		if (nodeList.getLength() > 0)
		{
			for (int i = 0; i < nodeList.getLength(); i++)
			{
				events.add(parseItemElement((Element)nodeList.item(i)));
			}
		}
		return events;
	}

	private static Event parseItemElement(Element element)
	{
		Event item=new Event();

		item.setHeading(XMLUtility.getValue(element,"h"));
		item.setLongitude(XMLUtility.getValueDouble(element,"lo"));
		item.setLatitude(XMLUtility.getValueDouble(element,"la"));
		item.setTime(XMLUtility.getValue(element,"t"));
		item.setCallCenter(XMLUtility.getValue(element,"c"));
		item.setLocation(XMLUtility.getValue(element,"l"));
		item.setIssue(XMLUtility.getValue(element,"i"));
		item.setPriority(XMLUtility.getValue(element,"p"));
		item.setLink(XMLUtility.getValue(element,"li"));
		item.setCountry(XMLUtility.getValue(element,"co"));

		return item;
	}
}
