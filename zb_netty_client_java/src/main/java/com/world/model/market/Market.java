package com.world.model.market;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Market {
	
	public String numberBi;
	public	String numberBiEn;
	public	String numberBiNote;
	public	long numberBixNormal;
	public	long numberBixShow;//"+//显示的计量单位是最小存储单位的倍数,不相等的时候会缩小到指定单位（等于小数点位数）
	public int numberBixDian;//=3;"+//小数点后几位，如果有小数的话
	public String exchangeBi;//=\"比特币\";"+
	public String exchangeBiEn;//=\"BTC\";"+
	public String exchangeBiNote;//=\"฿\";"+
	public String market;//=\"btqdefault\";"+
	public long exchangeBixNormal;//=100000000;"+//标准单位是最小存储单位的位数
	public long exchangeBixShow;//=100000000;"+//显示的计量单位是最小存储单位的倍数
	public int exchangeBixDian;//=4;"+//小数点后几位，如果有小数的话
	public String entrustUrlBase;//=\"/\";"+

	public int maxPrice;//最大的档位
	
    public int webId;
	public String ip;
	public int port;
	public String hashSec;//加密公钥	
	
	/**
	 * 是否是简化版
	 * @param isSimple
	 * @return
	 */
	public String toString(){
		String ls="var numberBi=\""+numberBi+"\";"+
				"var numberBiEn=\""+numberBiEn+"\";"+
				"var numberBiNote=\""+numberBiNote+"\";"+
				"var numberBixNormal="+numberBixNormal+";"+//标准单位是最小存储单位的位数
				"var numberBixShow="+numberBixShow+";"+//显示的计量单位是最小存储单位的倍数,不相等的时候会缩小到指定单位（等于小数点位数）
				"var numberBixDian="+numberBixDian+";"+//小数点后几位，如果有小数的话
				"var exchangeBi=\""+exchangeBi+"\";"+
				"var exchangeBiEn=\""+exchangeBiEn+"\";"+
				"var exchangeBiNote=\""+exchangeBiNote+"\";"+
				"var market=\""+market+"\";"+
				"var exchangeBixNormal="+exchangeBixNormal+";"+//标准单位是最小存储单位的位数
				"var exchangeBixShow="+exchangeBixShow+";"+//显示的计量单位是最小存储单位的倍数
				"var exchangeBixDian="+exchangeBixDian+";"+//小数点后几位，如果有小数的话
				"var entrustUrlBase=\""+entrustUrlBase+"\";";
		return ls;
	}
	
	static Map<String , Market> makets = new HashMap<String , Market>();
	
	public static final String btcdefault = getBtcDefault().toString();
	public static final String ltcdefault = getLtcDefault().toString();
	public static final String btqdefault = getBtqDefault().toString();
	
	private static Market marketObj = null;
	
	public static Market getInstance(){
		if(marketObj == null){
			marketObj = new Market();
		}
		return marketObj;
	}

	
	public static Market getBtcDefault(){
		return getMarket("btcdefault");
	}
	
	public static Market getLtcDefault(){
		return getMarket("ltcdefault");
	}
	
	public static Market getBtqDefault(){
		return getMarket("btqdefault");
	}
	
	/**
	 * 获取一个市场
	 * @return
	 */
	public static synchronized Market getMarket(String name){
		try{
			Market m = makets.get(name);
			if(m != null){
				return m;
			}
		ResourceBundle rb= ResourceBundle.getBundle(name);
		m=new Market();
		if(name.equals("btcdefault")){
			m.numberBi="比特币";
			m.numberBiNote="฿";
			m.exchangeBi="人民币";
			m.exchangeBiNote="￥";
		}else if(name.equals("ltcdefault")){
			m.numberBi="莱特币";
			m.numberBiNote="Ł";
			m.exchangeBi="人民币";
			m.exchangeBiNote="￥";
		}else if(name.equals("btqdefault")){
			m.numberBi="比特权";
			m.numberBiNote="BTQ";
			m.exchangeBi="比特币";
			m.exchangeBiNote="฿";
		}
		
		//m.numberBi=new String(rb.getString("numberBi").getBytes("ISO-8859-1"), "UTF-8");
		m.numberBiEn=rb.getString("numberBiEn");
		//m.numberBiNote=new String(rb.getString("numberBiNote").getBytes("ISO-8859-1"), "GBK");
		m.numberBixNormal=Long.parseLong(rb.getString("numberBixNormal"));
		m.numberBixShow=Long.parseLong(rb.getString("numberBixShow"));
		m.numberBixDian=Integer.parseInt((rb.getString("numberBixDian")));
		//m.exchangeBi=new String(rb.getString("exchangeBi").getBytes("ISO-8859-1"), "UTF-8");
		m.exchangeBiEn=new String(rb.getString("exchangeBiEn").getBytes("ISO-8859-1"), "UTF-8");
		//m.exchangeBiNote=rb.getString("exchangeBiNote");
		m.market=rb.getString("market");
		m.exchangeBixNormal=Long.parseLong(rb.getString("exchangeBixNormal"));
		m.exchangeBixShow=Long.parseLong(rb.getString("exchangeBixShow"));
		m.exchangeBixDian=Integer.parseInt(rb.getString("exchangeBixDian"));
		m.entrustUrlBase=rb.getString("entrustUrlBase");
        m.webId=Integer.parseInt(rb.getString("webId"));
		m.ip=rb.getString("ip");
		m.port=Integer.parseInt(rb.getString("port"));
		m.hashSec=rb.getString("hashSec");
		m.maxPrice=Integer.parseInt(rb.getString("maxPrice"));
		makets.put(name, m);
		return m;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	
	
	//双重格式化，保留应有的小数点位数，避免麻烦
	public double formatNumber(double num){
	   double numNew=Arith.div(num, numberBixNormal,numberBixDian);

	   return numNew;
	}
	
	//反向格式化商品，用来将部分double类型的传值转化成long类型进行委托   
		public long formatNumberLong(double num){
		   long numNew=Arith.mul(num, numberBixNormal,numberBixDian);

		   return numNew;
		}
		
		
	//格式化商品,用于显示,将基础整数位的商品格式化成需要的显示
		public long ffNumber(long num){
		   double numNew=Arith.div(num, numberBixNormal,numberBixDian);
		   numNew=Arith.mul(numNew, numberBixNormal);

		   return Math.round(numNew);
		}
//比如给出2345554.返回2345000
		public long ffMoney(long num){
			 double numNew=Arith.div(num, exchangeBixNormal,exchangeBixDian);
			  numNew=Arith.mul(numNew, exchangeBixNormal);

			 return Math.round(numNew);
		}
 
//格式化金钱 3.456 变成3.45
	public double formatMoney(double num){
		 double numNew=Arith.div(num, exchangeBixNormal,exchangeBixDian);
//		 num=parseFloat(num)/exchangeBixNormal;
//			if(exchangeBixNormal!=exchangeBixShow)//不等于就说明取整数的位数
//			      return Math.floor(Math.pow(10,exchangeBixDian)*parseFloat(num));
//			else
//				 return parseFloat(num.toFixed(exchangeBixDian));
		 return numNew;
	}
	
	/**
	 * 格式化出来一个资金和数量乘积之后的结果
	 * @param num
	 * @return   12300*100100000=1231230000000  格式化后为123.123
	 */
	public  double formatMoneyAndNumber(double num){
		 double numNew=Arith.div(num, exchangeBixNormal*numberBixNormal,exchangeBixDian+numberBixDian);

		 return numNew;
	}

	//反向格式化商品，用来将部分double类型的传值转化成long类型进行委托 3.45变成345
		public long formatMoneyLong(double num){
		   long numNew=Arith.mul(num, exchangeBixNormal,exchangeBixDian);

		   return numNew;
		}
		
		/**
		 * 格式化出来总价long形长的
		 * @param unitPrice
		 * @param numbers
		 * @return
		 */
		public long formatTotalMoney(long unitPrice,long numbers){
			return unitPrice*numbers;
//			double unitP=formatMoney(unitPrice);
//			
//			double numb=formatNumber(numbers);
//		
//			double t=Arith.mul(unitP,numb);
//			
//			t=Arith.round(t, Market.exchangeBixDian);
//		
//			t=Arith.mul(t,Market.exchangeBixNormal);
//			
//			long rt=Arith.mul(t,Market.numberBixNormal,0);
//			return rt;
		}
		
		/**
		 * 格式化出来总价 double形用的短的
		 * @param unitPrice
		 * @param numbers
		 * @return
		 */
		public double formatTotalMoneyDoule(long unitPrice,long numbers){
			double unitP=formatMoney(unitPrice);
			
			double numb=formatNumber(numbers);
		
			double t=Arith.mul(unitP,numb);
			
			t=Arith.round(t, exchangeBixDian+numberBixDian);
			
			return t;
		}
}
