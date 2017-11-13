package com.world.common;

public enum SystemCode implements SysEnum{
	
	code_1000(1000, "操作成功", "success"),
	code_1001(1001, "一般错误提示", "error tips"),
	
	code_1002(1002, "内部错误", "Internal Error"),
	code_1003(1003, "验证不通过", "Validate No Pass"),
	code_1004(1004, "安全密码锁定", "Safe Password Locked"),
	code_1005(1005, "安全密码错误", "Safe Password Error"),
	code_1006(1006, "实名认证等待审核或审核不通过", "Audit Or Audit No Pass"),
	
	code_2001(2001 , "人民币账户余额不足" , "Insufficient CNY Balance"), 
	code_2002(2002 , "比特币账户余额不足" , "Insufficient BTC Balance"),
	code_2003(2003 , "莱特币账户余额不足" , "Insufficient LTC Balance"),
	code_2004(2004 , "比特权账户余额不足" , "Insufficient BTQ Balance"),
	
	code_3001(3001 , "挂单没有找到" , "Not Found Order"),
	code_3002(3002 , "无效的金额" , "Invalid Money"),
	code_3003(3003 , "无效的数量" , "Invalid Amount"),
	code_3004(3004 , "用户不存在" , "No Such User"),
	code_3005(3005 , "无效的参数" , "Invalid Arguments"),
	code_3006(3006 , "无效的IP或与绑定的IP不一致", "Invalid Ip Address"),
	code_3007(3007 , "请求时间已失效", "Invalid Ip Request Time"),
	
	code_4001(4001 , "API接口被锁定或未启用", "API Locked Or Not Enabled"),
	code_4002(4002 , "请求过于频繁", "Request Too Frequently");
	
	private SystemCode(int key, String value , String className) {
		this.key = key;
		this.value = value;
		this.className = className;
	}

	private int key;
	private String value;
	private String className;

	public int getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className){
		this.className = className;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	public static SystemCode getCode(int key){
		SystemCode code = null;
		for (SystemCode s : SystemCode.values()){
			if(s.getKey() == key){
				code = s;
				break;
			}
		}
		return code;
	}
	
	public static SystemCode getSystemCode(int key){
		switch(key){
			case 2: { 
				return SystemCode.code_1002; 
			}
			case 100: {
				return SystemCode.code_1000;
			}
			case 130: { 
				SystemCode code = SystemCode.code_1001;
				code.setClassName("委托失败-未知的委托类型");
				code.setValue("委托失败-未知的委托类型");
				return code;
			}
			case 131: { 
				return SystemCode.code_3005;
			}
			case 132: { 
				SystemCode code = SystemCode.code_1001;
				code.setClassName("委托失败-还有卖比特币委托已经达到您本次购买的报价");
				code.setValue("委托失败-还有卖比特币委托已经达到您本次购买的报价");
				return code;
			}
			case 133: { 
				SystemCode code = SystemCode.code_1001;
				code.setClassName("委托失败-还有买比特币委托已经达到您本次卖出的报价");
				code.setValue("委托失败-还有买比特币委托已经达到您本次卖出的报价");
				return code;
			}
			case 135: { 
				return SystemCode.code_2001;
			}
			case 136: { 
				return SystemCode.code_1002;
			}
			case 137: { 
				SystemCode code = SystemCode.code_1001;
				code.setClassName("委托失败-价格太高、暂不支持");
				code.setValue("委托失败-价格太高、暂不支持");
				return code;
			}
			case 200: {
				return SystemCode.code_1000;
			}
			case 211: {
				return SystemCode.code_3001;
			}
			case 212: { 
				SystemCode code = SystemCode.code_1001;
				code.setClassName("委托取消单个失败-已经提交过取消请求");
				code.setValue("委托取消单个失败-已经提交过取消请求");
				return code;
			}
			case 213: { 
				SystemCode code = SystemCode.code_1001;
				code.setClassName("委托取消单个失败-取消程序");
				code.setValue("委托取消单个失败-取消程序");
				return code;
			}
			case 221: {
				return SystemCode.code_3001;
			}
			case 222: { 
				SystemCode code = SystemCode.code_1001;
				code.setClassName("委托取消区间订单失败-区间价格可能颠倒导致错误");
				code.setValue("委托取消区间订单失败-事物处理失败");
				return code;
			}
			case 300: {
				return SystemCode.code_1000;
			}
			case 301: {
				return SystemCode.code_1000;
			}
			case 311: { 
				SystemCode code = SystemCode.code_1001;
				code.setClassName("委托失败-未知的委托类型");
				code.setValue("委托失败-未知的委托类型");
				return code;
			}
			case 312: { 
				SystemCode code = SystemCode.code_1001;
				code.setClassName("委托失败-事物处理失败");
				code.setValue("委托失败-事物处理失败");
				return code;
			}
			case 400: {
				return SystemCode.code_1000;
			}
			case 411: {
				return SystemCode.code_3001;
			}
			case 412: { 
				SystemCode code = SystemCode.code_1001;
				code.setClassName("处理取消委托失败-已经成功委托");
				code.setValue("处理取消委托失败-已经成功委托");
				return code;
			}
			case 413: { 
				SystemCode code = SystemCode.code_1001;
				code.setClassName("处理取消委托失败-事物处理失败");
				code.setValue("处理取消委托失败-事物处理失败");
				return code;
			}
		}
		return null;
	}
}
