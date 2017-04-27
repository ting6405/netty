package idn.netty.client.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * @author huihawk
 * 2017.01.19
 * 优化1、信息隐藏 2、封装
 */
public abstract class MegUtil {
	
	private static int tradenum = 100000;
	private String relativelyPath=System.getProperty("user.dir");
	/**
	 * 生成固定值0x00长度的数组
	 * 用于填充报文长度
	 * @param n
	 * @return
	 */
	public byte[] fill0x00(int n){
		byte[] ret = new byte[n];
		byte r = 0x00;
		for(int i=0; i<n; i++){
			ret[i] = r;
		}
		return ret;
	}
	
	/**
	 * 生成crc校验码
	 * @param bytes
	 * @return
	 */
	public  Integer getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;
        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return CRC;
    }
	/**
	 * 根据长度n，填充源数组为固定长度
	 * 用于补充报文长度
	 * @param src
	 * @param n
	 * @return
	 */
	public byte[] fill0x00(byte[] src,int n ){
		int len = 0;
		if(src!=null){
			len = src.length;
		}
		if(n-len<0){
			return src;
		}
		byte[] ret = new byte[n-len];
		byte r = 0x00;
		for(int i=0; i<n-len; i++){
			ret[i] = r;
		}
		
		return mergeByte(src,ret);
	}
	
	/**
	 * 合并两个byte数组
	 * 用于报文头与信息体数组的合并
	 * @param head
	 * @param body
	 * @return
	 */
	public byte[] mergeByte(byte[] head,byte[] body) { 
		
		   if(head==null){
				return body;
			}else{
				if(body==null){
					return head;
				 }
			}
		   
		   byte[] mes = new byte[head.length + body.length];
		   
		   System.arraycopy(head, 0, mes, 0, head.length); 
		   System.arraycopy(body, 0, mes, head.length, body.length);
		   return mes;
	   }
	
	/**
	 * 合并多个byte数组
	 * 用于信息体数组的合并
	 * @param head
	 * @param body
	 * @return
	 */
	public byte[] appendByte(List<byte[]> list) { 
		   
		   byte[] mes = null; //new byte[len];
		   
		   Iterator<byte[]> it = list.iterator();
	        while(it.hasNext()){
	            byte[] item = it.next();
	            mes = mergeByte(mes,item);
	        	//System.out.println(it.next());
	        }
		   
		   //System.arraycopy(head, 0, mes, 0, head.length); 
		   //System.arraycopy(body, 0, mes, head.length, body.length);
		   return mes;
	   }
	
	/**
	 * 数字字符串转成byte[] 存放为bcd码 
	 * 例如：12 转成 0x01 0x02
	 * 用于 数字字符串转成bcd码
	 * @param intStr
	 * @return
	 */
	public  byte[] intStrToBCD(String intStr){
		int len = 0;
		if(intStr ==null||intStr.length()==0){
			return null;
		}
		len = intStr.length();
		byte[] ret = new byte[len];
		
		for (int i=0 ;i<len; i++){
			ret[i] = (byte) Character.getNumericValue(intStr.charAt(i)) ;					
		}
		
		return ret;		
	}
	
	/**
	 * byte[]转数字字符串
	 * 例如 0x01 0x02 转成 数字字符串 12
	 * @param bcd
	 * @return
	 */
	public  String BCDtointStr(byte[] bcd){
		int len = 0;
		if(bcd ==null||bcd.length==0){
			return null;
		}
		len = bcd.length;
		StringBuffer ret = new StringBuffer();
				
		for (int i=0 ;i<len; i++){
			ret.append(bcd[i]&0xff);				
		}
		return ret.toString();		
	}
    

	/**
	 * byte[] 转16进制字符串
	 * 用于 接收的bcd码转换
	 * 例如：0x20 0x17 0x01 0x19 转成 20170119
	 * @param src
	 * @return
	 */
	public String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 十六进制字符串转成byte[]
	 * 例如： 20170119  转成 0x20 0x17 0x01 0x19
	 * 用于 第三方平台提交bcd码
	 * @param hexString
	 * @return
	 */
	public byte[] hexStringToBytes(String hexString) {   
	    if (hexString == null || hexString.equals("")) {   
	        return null;   
	    }   
	    hexString = hexString.toUpperCase();   
	    int length = hexString.length() / 2;   
	    char[] hexChars = hexString.toCharArray();   
	    byte[] d = new byte[length];   
	    for (int i = 0; i < length; i++) {   
	        int pos = i * 2;   
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));   
	    }   
	    return d;   
	}
	
	/**  
	 * 字符转成 byte 
	 *  
	 * @param c char  
	 * @return byte  
	 */  
	 public byte charToByte(char c) {   
	    return (byte) "0123456789ABCDEF".indexOf(c);   
	}  
	
	/**
	 * int 转 byte[] 高字节在前，低字节在后 
	 * @param src int 数字
	 * @param len 数组长度 比如2 则总共2*8 位数
	 * @param order 0 順序 1 倒敘 （倒叙 目的 发送报文用）
	 * @return
	 */
	public byte[] intToBytes(int src,int len,int order){
		
		byte[] ret = new byte[len];
		for(int i=0; i<len; i++){
			ret[i] = (byte) ((src >>> ((len-i-1)*8)) & 0xff);
		}
		
//		return new byte[] {
//		        (byte) ((a >> 24) & 0xFF),
//		        (byte) ((a >> 16) & 0xFF),   
//		        (byte) ((a >> 8) & 0xFF),   
//		        (byte) (a & 0xFF)
//		    };

//		head2[0] = (byte) ((bodyLen >>> 0) & 0xff);
//		head2[1] = (byte) ((bodyLen >>> 8) & 0xff);
		if(order==1){
			return bytesReverseOrder(ret);
		}
		return ret;
	}
	/**
	 * 字节数组倒叙
	 * @param src
	 * @return
	 */
	public byte[] bytesReverseOrder(byte[] src){
		 int length = src.length;  
		 byte[] result = new byte[length];  
		 for(int i=0; i<length; i++) {  
		    result[length-i-1] = src[i];  
		 }  
		 return result;
	}
		
	/**
	 * byte[] 转int 
	 * @param src
	 * @return
	 */
	public int BytesToint(byte[] src){
		
		int len = src.length;
		
		int ret = 0;
		for(int i=len-1; i>=0; i--){
			ret |=(src[i] & 0xFF) << (len-1-i)*8;
		}
		
//		 return   b[3] & 0xFF |
//		            (b[2] & 0xFF) << 8 |
//		            (b[1] & 0xFF) << 16 |
//		            (b[0] & 0xFF) << 24;
		
//		head2[0] = (byte) ((bodyLen >>> 0) & 0xff);
//		head2[1] = (byte) ((bodyLen >>> 8) & 0xff);
		return ret;
	}
	/**
	 * ip地址字符串转 字节数组
	 * 例如：211.149.228.101 转成 0xD3,0x95,0xE4,0x65
	 * @param ipStr
	 * @return
	 */
	public byte[] ipToBytes(String ipStr){
		
		String[] arr= ipStr.split("[.]");
		int len = arr.length;
		byte[] ret = new byte[len];
		for(int i=0; i<len; i++){
			ret[i] = intToByte(Integer.parseInt(arr[i]));
		}
		
		return ret;
	}
	
	public String BytesToip(byte[] ip){
		String ipStr = "";
		for(int i=0; i<ip.length; i++){
			ipStr += byteToInt(ip[i])+".";
		}
		
		return ipStr.substring(0, ipStr.length()-1);
	}
	
	/**
	 * int 转 byte
	 * @param x
	 * @return
	 */
	public byte intToByte(int x) {  
	    return (byte) x;  
	}  
	  
	/**
	 * byte 转int
	 * @param x
	 * @return
	 */
	public int byteToInt(byte b) {  
	    //Java 总是把 byte 当做有符处理；我们可以通过将其和 0xFF 进行二进制与得到它的无符值  
	    return b & 0xFF;  
	}  
    
	/**
	 * 字节流写入文件，文件名定义为流水号(十六进制字符串)
	 * 下标为20到26 共7个字节
	 * @param src
	 * @param rt 接收返回的文件名称 0：发送 1 接收的
	 */
	public void writeBytesToFile(byte[] src,int rt){
		
		//获取19到26位流水号作为文件名称BCD码转换成 十六进制字符串
		//数组复制，static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) 
        //从指定源数组中复制一个数组，复制从指定的位置开始，到目标数组的指定位置结束。
	    
		byte[] fname_bytes = new byte[7];
		System.arraycopy(src, 20, fname_bytes, 0, 7);
		String fname = bytesToHexString(fname_bytes);
		
		byte[] ft_bytes = new byte[1];
		System.arraycopy(src, 19, ft_bytes, 0, 1);
		String ft = bytesToHexString(ft_bytes);
		
		String ftype = "send";
		if(rt==1){
			ftype="rec";
		}
		
		FileOutputStream os;
		try {
			os = new FileOutputStream(relativelyPath+"/"+"pkg/"+ft+"_"+fname+ftype+".txt");
			os.write( src,0,src.length);
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public byte[] readFileTobytes(String filename){
		String fname = "pkg/"+filename+".txt";
		File f = new File(relativelyPath+"/"+fname);  
        if (!f.exists()) {  
              return null;
        }  
        
        
        FileChannel channel = null;  
        FileInputStream fs = null; 
        ByteBuffer byteBuffer = null;
        
        try {  
            fs = new FileInputStream(f);  
            channel = fs.getChannel();  
            byteBuffer = ByteBuffer.allocate((int) channel.size());  
            while ((channel.read(byteBuffer)) > 0) {  
                // do nothing  
                // System.out.println("reading");  
            }  
            
        } catch (IOException e) {  
            e.printStackTrace();   
        } finally {  
            try {  
                channel.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            try {  
                fs.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        } 
        
        return byteBuffer.array();
	}
	/**
	 * 从源数组下标拷贝多少长度到新数组
	 * @param src
	 * @param first
	 * @param len
	 * @return byte[]
	 */
	public byte[] copyBytes(byte[] src,int first,int len) {
		try {
			byte[] ret = new byte[len];			
			System.arraycopy(src, first, ret, 0, len);		
			return ret;
		} catch(Exception e) {
			System.out.println(/*src, */len+","+ src.length);
			return null;
		}
	}
	/**
	 * 通过日期生成报文头流水号
	 * @return
	 */
	public String getSerialNum(){
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyyyyy-MM-dd HH(hh):mm:ss S E D F w W a k K z");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		long times = System.currentTimeMillis();
		//System.out.println(times);
		Date date = new Date(times);
		String tim = sdf.format(date);
		//System.out.println(tim);
		return tim;
	}
	
	/**
	 * 通过日期交易流水号(yyyyMMddHHmmss+6位序列号 共20位 bcd码10个字节)
	 * @return
	 */
	public String getTradeNum(){
		
		return getSerialNum()+tradenum++;
	}
	/**
	 * 通过日期生成状态包当前时间MMddHHmmss 10位  bcd5个字节
	 * @return
	 */
	public String getCurNum(){
		//SimpleDateFormat sdf = new SimpleDateFormat("yyyyyyyy-MM-dd HH(hh):mm:ss S E D F w W a k K z");
		SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
		long times = System.currentTimeMillis();
		//System.out.println(times);
		Date date = new Date(times);
		String tim = sdf.format(date);
		//System.out.println(tim);
		return tim;
	}
	
	/**
	 * byte[]转成二进制字符串
	 * 
	 * @param bString
	 * @return
	 */
	public String bytesToBits(byte[] src) {
		
		int ret = BytesToint(src);
		ret |= 256;
		String str = Integer.toBinaryString(ret);
		
		int len = str.length();
		return str.substring(len - 8, len);
	}
	
	/**
	 * 二进制字符串转成byte
	 * 
	 * @param bString
	 * @return
	 */
	public byte bitStrTobyte(String bString) {
		byte result = 0;
		for (int i = bString.length() - 1, j = 0; i >= 0; i--, j++) {
			result += (Byte.parseByte(bString.charAt(i) + "") * Math.pow(2, j));
		}
		return result;
	}
	/**
	 * 发送前拼装netty字节
	 * @return
	 */
	 public ByteBuf obtainSendBuf(byte[] send){
		  // byte[] ret = this.getByte();
		   //写日志
	    	writeBytesToFile(send,0);             	
	    	ByteBuf resp= Unpooled.copiedBuffer(send);
	    	return resp;
	   }
	 
	 /**
	  * gbk字符数组转汉字
	  * @param src
	  * @return
	  */
	 public String bytesToGBK(byte[] src){
		 try {
			return new String(src,"gbk");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	 }
	 
	 /**
	  * 汉字，字符，转字节数组
	  * @return
	  */
	 public byte[] gbkToBytes(String gbk){
		 
		 try {
			return gbk.getBytes("gbk");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	 }
	 
	   /**
		 * 金霆msgid 获取4个字节 8位 bcd 码 需要倒叙
		 * @return
		 */
		public byte[] getMsgID(){
			//SimpleDateFormat sdf = new SimpleDateFormat("yyyyyyyy-MM-dd HH(hh):mm:ss S E D F w W a k K z");
			SimpleDateFormat sdf = new SimpleDateFormat("ddHHmmss");
			long times = System.currentTimeMillis();
			Date date = new Date(times);
			String tim = sdf.format(date);		
			//return bytesReverseOrder(intStrToBCD(tim));
			//实际中不永倒叙，按照正常的顺序返回给服务器
			return hexStringToBytes(tim);
		}
		/**
	    * 获取数据域list
	    * @return
	    */
	   public List<byte[]> appendBytes(){
		   return null;
	   }
	   /**
	    * 获取crc校验
	    * 多项式 0x1021
	    * @return
	    */
	   public byte[] evalCRC16(byte[] data) { 
		    int crc = 0xFFFF; 
		    
		    for (int i = 0; i < data.length; i++) { 
			    crc = (data[i] << 8) ^ crc; 
			    
			    for (int j = 0; j < 8; ++j){ 
				    if ((crc & 0x8000) != 0) 
				    	crc = (crc << 1) ^ 0x1021; 
				    else 
				    	crc <<= 1; 
			    } 
		    } 
		    return intToBytes((crc ^ 0xFFFF) & 0xFFFF,2,0); 
		}
}
