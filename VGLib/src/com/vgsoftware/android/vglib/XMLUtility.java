package com.vgsoftware.android.vglib;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;
import android.util.Log;

public class XMLUtility
{
	public static Element getRootElement(final URL feedUrl)
	{
		try
		{
			if (feedUrl != null)
			{
				Log.v("VGLib", "XML Url: " + feedUrl.toString());
			}
			else
			{
				Log.v("VGLib", "XML Url is null");
			}
			return getRootElement(HttpUtility.getData(feedUrl));
		}
		catch (Exception e)
		{
			Log.e("VGLib", "Unable to parse XML", e);
		}
		return null;
	}

	public static Element getRootElement(final String content)
	{
		if (!StringUtility.isNullOrEmpty(content))
		{
			try
			{
				DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				Document document;
				document = documentBuilder.parse(IOUtils.toInputStream(content));
				Element root = document.getDocumentElement();
				root.normalize();
				return root;
			}
			catch (Exception e)
			{
				Log.e("VGLib", "Unable to parse XML", e);
			}
		}
		return null;
	}

	public static String getAttributeValue(Node node, String attributeName)
	{
		if (node != null && node.hasAttributes())
		{
			Node attribute = node.getAttributes().getNamedItem(attributeName);
			if (attribute != null)
			{
				return attribute.getNodeValue();
			}
		}
		return "";
	}

	public static boolean getAttributeValueBoolean(Node node, String attributeName)
	{
		String value = XMLUtility.getAttributeValue(node, attributeName);
		if (value != null && !TextUtils.isEmpty(value))
		{
			return Boolean.valueOf(value).booleanValue();
		}
		return false;
	}

	public static String getValue(Element root, String tagName)
	{
		if (root != null && tagName != null && !TextUtils.isEmpty(tagName))
		{
			NodeList elements = root.getElementsByTagName(tagName);
			if (elements != null && elements.getLength() > 0 && elements.item(0) instanceof Element)
			{
				Element element = (Element) elements.item(0);
				if (element.getFirstChild() != null)
				{
					return element.getFirstChild().getNodeValue();
				}
			}
		}
		return null;
	}

	public static String getValue(NodeList nodes, String tagName)
	{
		for (int i = 0; i < nodes.getLength(); i++)
		{
			Node node = nodes.item(i);
			if (node instanceof Element)
			{
				Element element = (Element) node;
				if (element.getNodeName().equals(tagName) && element.getFirstChild() != null)
				{
					return element.getFirstChild().getNodeValue();
				}
			}
		}
		return null;
	}

	public static int getValueInt(Element root, String tagName)
	{
		String value = XMLUtility.getValue(root, tagName);
		if (value != null && !TextUtils.isEmpty(value))
		{
			try
			{
				return Integer.parseInt(value);
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		return 0;
	}

	public static double getValueDouble(Element root, String tagName)
	{
		String value = XMLUtility.getValue(root, tagName);
		if (value != null && !TextUtils.isEmpty(value))
		{
			try
			{
				return Double.parseDouble(value);
			}
			catch (NumberFormatException nfe)
			{
			}
		}
		return 0;
	}

	public static boolean getValueBoolean(Element root, String tagName)
	{
		String value = XMLUtility.getValue(root, tagName);
		if (value != null && !TextUtils.isEmpty(value))
		{
			return Boolean.valueOf(value).booleanValue();
		}
		return false;
	}

	public static Date getValueDate(Element root, String tagName)
	{
		return DateUtility.parse(XMLUtility.getValue(root, tagName));
	}

	public static void skip(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		if (parser.getEventType() != XmlPullParser.START_TAG)
		{
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0)
		{
			switch (parser.next())
			{
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}
	}

	public static String readText(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException
	{
		return readText(parser, null, tagName);
	}

	public static String readText(XmlPullParser parser, String ns, String tagName) throws IOException, XmlPullParserException
	{
		parser.require(XmlPullParser.START_TAG, ns, tagName);
		String result = "";
		if (parser.next() == XmlPullParser.TEXT)
		{
			result = parser.getText();
			parser.nextTag();
		}
		parser.require(XmlPullParser.END_TAG, ns, tagName);
		return result;
	}

	public static int readInteger(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException
	{
		return readInteger(parser, null, tagName);
	}

	public static int readInteger(XmlPullParser parser, String ns, String tagName) throws IOException, XmlPullParserException
	{
		String value = readText(parser, tagName);
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception ex)
		{
		}
		return 0;
	}
}
