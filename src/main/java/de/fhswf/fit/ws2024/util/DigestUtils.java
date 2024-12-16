/*
 * $RCSFile$
 *
 * Created on 06.12.2006
 * for Project: 
 * by steins
 *
 * (C) 2005-2006 by 
 */
package de.fhswf.fit.ws2024.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestUtils
{
   private static final String HEX_DIGITS = "0123456789abcdef";

   private DigestUtils()
   {
   }

   public static String bin2hex(byte[] bin)
   {
      StringBuilder sb = new StringBuilder(32);
      for (int i = 0; i < bin.length; ++i)
      {
         byte b = bin[i];
         int h = (b & 0xf0) >> 4;
         sb.append(HEX_DIGITS.charAt(h));
         h = b & 0x0f;
         sb.append(HEX_DIGITS.charAt(h));
      }

      return sb.substring(0);
   }

   public static String md5(String s)
   {
      MessageDigest md = null;
      try
      {
         md = MessageDigest.getInstance("MD5");
         md.update(s.getBytes("ISO-8859-1"));
      }
      catch (NoSuchAlgorithmException e)
      {
         e.printStackTrace();
      }
      catch (UnsupportedEncodingException e)
      {
         e.printStackTrace();
      }

      return bin2hex(md.digest());
   }
}
