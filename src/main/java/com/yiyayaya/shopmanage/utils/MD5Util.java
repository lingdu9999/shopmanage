package com.yiyayaya.shopmanage.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
  /**
   * 1.MD5（message-digest algorithm 5）信息摘要算法，
   *   它的长度一般是32位的16进制数字符串（如81dc9bdb52d04dc20036dbd8313ed055）
   * 2.由于系统密码明文存储容易被黑客盗取
   * 3.应用：注册时，将密码进行md5加密，存到数据库中，防止可以看到数据库数据的人恶意篡改。
   *       登录时,将密码进行md5加密,与存储在数据库中加密过的密码进行比对
   * 4.md5不可逆，即没有对应的算法，从产生的md5值逆向得到原始数据。
   *   但是可以使用暴力破解，这里的破解并非把摘要还原成原始数据，如暴力枚举法。
   *
   */
  public static String getMD5(String str){
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(str.getBytes());
      byte[] mdBytes = md.digest();

      StringBuilder hash = new StringBuilder();
      for (byte mdByte : mdBytes) {
        hash.append(String.format("%02x", mdByte));
      }
      return hash.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException("MD5加密失败", e);
    }
  }

  public static void main(String[] args) {
    String md5 = getMD5("e10adc3949ba59abbe56e057f20f883e");
    System.out.println(md5);
  }
}
