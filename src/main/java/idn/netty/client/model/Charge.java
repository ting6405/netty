package idn.netty.client.model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


import idn.netty.client.utils.MegUtil;

public class Charge extends MegUtil {
	private final static byte[] HEAD1_1 = new byte[] { (byte) 0xa5 };
	private final static byte[] HEAD2_1 = new byte[] { (byte) 0xa1 };
	private byte[] head3_2 = new byte[] { 1, 1 };
	private final static byte[] HEAD4_1 = new byte[] { 0 };
	private byte[] head5_16;
	private byte[] head6_4;
	private byte[] head7_2 = new byte[]{0,0};
	private byte[] head8_n;
	private byte[] head9_2;
	private final static byte[] HEAD10_1 = new byte[] { (byte) 0xED };
    private List<byte[]> list = new ArrayList<byte[]>();
    
   public  Charge(byte[] head3_2){
	    this.head3_2 = head3_2;
    	String serNO = "JTACAAA170307001";
		byte[] head6_4 = intToBytes(1, 4, 0);
		this.head6_4 = head6_4;
		try {
			byte[] bytes = serNO.getBytes("ascii");
			head5_16 = bytes;
		} catch (UnsupportedEncodingException e) {
			System.out.println("转换出现异常了....."+e.getMessage());
			head5_16 = null;
		}
		list.add(HEAD1_1);
		list.add(HEAD2_1);
		list.add(head3_2);
		list.add(HEAD4_1);
		list.add(head5_16);
		list.add(head6_4);
    }
    
    public Charge() {
		String serNO = "0123456789123456";
		byte[] head6_4 = intToBytes(123, 4, 0);
		this.head6_4 = head6_4;
		try {
			byte[] bytes = serNO.getBytes("ascii");
			head5_16 = bytes;
		} catch (UnsupportedEncodingException e) {
			System.out.println("转换出现异常了....."+e.getMessage());
			head5_16 = null;
		}
		list.add(HEAD1_1);
		list.add(HEAD2_1);
		list.add(head3_2);
		list.add(HEAD4_1);
		list.add(head5_16);
		list.add(head6_4);
		
		
	}
	public byte[] getHead5_16() {
		return head5_16;
	}

	public void setHead5_16(byte[] head5_16) {
		this.head5_16 = head5_16;
	}

	public byte[] getHead6_4() {
		return head6_4;
	}

	public void setHead6_4(byte[] head6_4) {
		this.head6_4 = head6_4;
	}

	public byte[] getMeg(){
		list.add(head7_2);
		if(head8_n!=null)
			list.add(head8_n);
		byte[] appendByte = appendByte(list);
		System.out.println("crc校验的长度"+appendByte.length);
		Integer crc = getCRC(appendByte);
		System.out.println("clint----crc:"+crc);
		byte[] bs = intToBytes(crc, 2, 1);
		this.head9_2 = bs;
		list.add(head9_2);
		list.add(HEAD10_1);
		byte[] bs1 = appendByte(list);
		System.out.println(bytesToHexString(bs1));
		return bs1;
	}
	public byte[] getHead8_n() {
		return head8_n;
	}
	public void setHead8_n(byte[] head8_n) {
		this.head8_n = head8_n;
	}
	public byte[] getHead7_2() {
		return head7_2;
	}
	public void setHead7_2(byte[] head7_2) {
		this.head7_2 = head7_2;
	}
	
	
	
}
