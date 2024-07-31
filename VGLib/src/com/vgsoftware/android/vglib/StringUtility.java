package com.vgsoftware.android.vglib;

import java.util.Locale;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

public class StringUtility
{
	public static String stipHtml(final String html)
	{
		SpannableStringBuilder spannedStr = (SpannableStringBuilder) Html.fromHtml(html.trim());
		Object[] spannedObjects = spannedStr.getSpans(0, spannedStr.length(), Object.class);
		for (int i = 0; i < spannedObjects.length; i++)
		{
			if (spannedObjects[i] instanceof ImageSpan)
			{
				ImageSpan imageSpan = (ImageSpan) spannedObjects[i];
				spannedStr.replace(spannedStr.getSpanStart(imageSpan), spannedStr.getSpanEnd(imageSpan), "");
			}
		}
		return spannedStr.toString().trim();
	}

	public static boolean isEqual(final String strA, final String strB)
	{
		return StringUtility.isEqual(strA, strB, false);
	}

	public static boolean isEqual(final CharSequence strA, final CharSequence strB)
	{
		return StringUtility.isEqual((strA != null ? strA.toString() : ""), (strB != null ? strB.toString() : ""));
	}

	public static boolean isEqual(final CharSequence strA, final CharSequence strB, boolean ignoreCase)
	{
		return StringUtility.isEqual((strA != null ? strA.toString() : ""), (strB != null ? strB.toString() : ""), ignoreCase);
	}

	public static boolean isEqual(final CharSequence strA, final String strB)
	{
		return StringUtility.isEqual((strA != null ? strA.toString() : ""), strB);
	}

	public static boolean isEqual(final CharSequence strA, final String strB, boolean ignoreCase)
	{
		return StringUtility.isEqual((strA != null ? strA.toString() : ""), strB, ignoreCase);
	}

	public static boolean isEqual(final String strA, final CharSequence strB)
	{
		return StringUtility.isEqual(strA, (strB != null ? strB.toString() : ""));
	}

	public static boolean isEqual(final String strA, final CharSequence strB, boolean ignoreCase)
	{
		return StringUtility.isEqual(strA, (strB != null ? strB.toString() : ""), ignoreCase);
	}
	
	public static boolean isEqual(final String strA, final String strB, boolean ignoreCase)
	{
		if (StringUtility.isNullOrEmpty(strA) || StringUtility.isNullOrEmpty(strB))
		{
			return StringUtility.isNullOrEmpty(strA) && StringUtility.isNullOrEmpty(strB);
		}
		if (ignoreCase)
		{
			return strA.toLowerCase(Locale.ENGLISH).equals(strB.toLowerCase(Locale.ENGLISH));
		}
		return strA.equals(strB);
	}

	public static boolean isNullOrEmpty(final String text)
	{
		return text == null || text.equals("");
	}
	
	public static boolean isNullOrEmpty(final CharSequence text)
	{
		return text == null || text.equals("");
	}
}