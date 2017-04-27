package idn.netty.client.model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import idn.netty.client.utils.MegUtil;

/**
 * 
 *<p>title :server下发操作指令转换bean</p>
 *<p>Description : 3.6Server操作指令</p>
 *<p>Company : 广州爱电牛科技有限公司</p>
 * @date 2017年3月17日
 * @author 小吴
 */
public class Data_0205 extends MegUtil implements DataI {

	private List<byte[]> list = new ArrayList<byte[]>();
	private Map map  = new HashMap();
	private byte[] head4_1;
	private byte[] data1_16;
	private byte[] data2_16;
	private byte[] data3_1;
	private byte[] data4_1;
	private byte[] data5_8;
	private byte[] data6_1;
	private byte[] data7_2;
	private byte[] data8_4;
	
	
	public Data_0205() {
		try{
		data1_16 = "".getBytes("ascii");
		data2_16 = "idn1234567891234".getBytes("ascii");
		data3_1 = new byte[]{1};
		data4_1 = new byte[]{1};
		data5_8 = "12345678".getBytes("ascii");
		data6_1 = new byte[]{1};
		data7_2 = intToBytes(120, 2, 1);
		data8_4 = intToBytes(100000, 4, 1);
		}catch(Exception e){
			
		}
		finally{
			
		}
	}
	@Override
	public byte[] getMeg3_2() {
		// TODO Auto-generated method stub
		return new byte[]{(byte) 0xA2,0x05};
	}

	@Override
	public byte[] getMeg4_1() {
		// TODO Auto-generated method stub
		return head4_1;
	}

	@Override
	public Map getMap() {
		// TODO Auto-generated method stub
		return map;
	}
	

	public byte[] getHead4_1() {
		return head4_1;
	}
	public void setHead4_1(byte[] head4_1) {
		this.head4_1 = head4_1;
	}
	public byte[] getData1_16() {
		return data1_16;
	}
	
	public void setData1_16(String data1_16){
		try {
			this.data1_16 = data1_16.getBytes("ascii");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		}
	}
	public void setData1_16(byte[] data1_16) {
		this.data1_16 = data1_16;
	}
	public byte[] getData2_16() {
		return data2_16;
	}
	public void setData2_16(byte[] data2_16) {
		this.data2_16 = data2_16;
	}
	public byte[] getData3_1() {
		return data3_1;
	}
	public void setData3_1(byte[] data3_1) {
		this.data3_1 = data3_1;
	}
	public byte[] getData4_1() {
		return data4_1;
	}
	public void setData4_1(byte[] data4_1) {
		this.data4_1 = data4_1;
	}
	public byte[] getData5_8() {
		return data5_8;
	}
	public void setData5_8(byte[] data5_8) {
		this.data5_8 = data5_8;
	}
	public byte[] getData6_1() {
		return data6_1;
	}
	public void setData6_1(byte[] data6_1) {
		this.data6_1 = data6_1;
	}
	public byte[] getData7_2() {
		return data7_2;
	}
	public void setData7_2(byte[] data7_2) {
		this.data7_2 = data7_2;
	}
	public byte[] getData8_4() {
		return data8_4;
	}
	public void setData8_4(byte[] data8_4) {
		this.data8_4 = data8_4;
	}
	public byte[] getMsg8_n(){
		list.add(data1_16);
		list.add(data2_16);
		list.add(data3_1);
		list.add(data4_1);
		list.add(data5_8);
		list.add(data6_1);
		list.add(data7_2);
		list.add(data8_4); 
	    return appendByte(list);
	}
	
}
