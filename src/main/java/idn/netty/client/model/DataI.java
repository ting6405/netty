package idn.netty.client.model;

import java.util.Map;

public interface DataI 
{
   /**
    * 获取报文操作码
    * @return
    */
   public byte[] getMeg3_2();
   
   /**
    * 获取报文参数码
    * @return
    */
   public byte[] getMeg4_1();
   
   /**
    * 把解析的byte转成str装到Map
    * @return
    */
   public Map getMap();
  
}
