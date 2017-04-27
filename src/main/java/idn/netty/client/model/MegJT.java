package idn.netty.client.model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import idn.netty.client.utils.MegUtil;


/**
 * 消息超类
 */
public class MegJT extends MegUtil {
	private final static byte[] meg1_1 = new byte[] { (byte) 0xA5 };
	private final static byte[] meg2_1 = new byte[] { 0x01 };
	private byte[] meg3_2;
	private byte[] meg4_1;
	private byte[] meg5_16;
	private byte[] meg6_4;
	private byte[] meg7_2;
	private byte[] meg8_n;
	private byte[] meg9_2;
	private final static byte[] meg10_1 = new byte[] { (byte) 0xED };

	private DataI dataI;

	private byte[] meg;

	private List<byte[]> listpre = new ArrayList<byte[]>();

	private Map map = new HashMap();

	//我没提交吗？
	public MegJT(DataI dataI, byte[] meg) {
		this.meg = meg;
		this.meg3_2 = dataI.getMeg3_2();
		this.meg4_1 = dataI.getMeg4_1();
		this.meg5_16 = copyBytes(meg, 5, 16);
		this.meg6_4 = copyBytes(meg, 21, 4);
		listpre.add(meg1_1);
		listpre.add(meg2_1);
		listpre.add(meg3_2);
		listpre.add(meg4_1);
		listpre.add(meg5_16);
		listpre.add(meg6_4);
		//
		//System.out.println("crc校验串：" + bytesToHexString(bm));
		map = dataI.getMap();
	}

	public byte[] getBytes() {
		if(meg7_2 == null)
		this.meg7_2 = new byte[] { 0x00, 0x00 };
		listpre.add(meg7_2);
		if(meg8_n != null)
		listpre.add(meg8_n);
		byte[] bm = appendByte(listpre);
		int crc = getCRC(bm);
		meg9_2 = intToBytes(crc, 2, 1);
		listpre.add(meg9_2);
		listpre.add(meg10_1);
		printList();

		return appendByte(listpre);
	}

	public void printList() {
		Iterator<byte[]> it = listpre.iterator();
		int i = 0;
		while (it.hasNext()) {
			byte[] item = it.next();
			i++;
			System.out.println("num:" + i + " value:" + bytesToHexString(item));
		}
	}
	
	public byte[] getMeg8_n() {
		return meg8_n;
	}

	public void setMeg8_n(byte[] meg8_n) {
		this.meg8_n = meg8_n;
	}

	public List<byte[]> getListpre() {
		return listpre;
	}

	public void setListpre(List<byte[]> listpre) {
		this.listpre = listpre;
	}


	public Map getMap() {
		try {
			map.put("charge_pile",new String(meg5_16,"ascii"));
			map.put("msgid",String.valueOf(BytesToint(meg6_4)));
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}

	public byte[] getMeg4_1() {
		return meg4_1;
	}

	public void setMeg4_1(byte[] meg4_1) {
		this.meg4_1 = meg4_1;
	}

	public byte[] getMeg7_2() {
		return meg7_2;
	}

	public void setMeg7_2(byte[] meg7_2) {
		this.meg7_2 = meg7_2;
	}
	
	

	
}