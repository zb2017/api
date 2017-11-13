package com.world.common;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//
//import net.sf.json.JSONObject;
//
//import org.apache.commons.lang.StringUtils;
//
//import com.chbtc.api.ChbtcResponse;
//import com.chbtc.api.user.UserManager;
//import com.world.cache.Cache;
//import com.world.controller.Base;
//import com.world.controller.api.Index;
//import com.world.util.ip.IpUtil;
//import com.world.util.sign.EncryDigestUtil;
//
///**
// * API接口验证工具
// * @author guosj
// */
public class VerifiUtil {
//	
//	private Map<String,Object> map;
//	//API方法固定的参数
//	private String[] order = {"method", "accesskey", "price", "amount",  "tradeType", "currency"};
//	private String[] cancelOrder = {"method", "accesskey", "id", "currency"};
//	private String[] getOrder = {"method", "accesskey", "id", "currency"};
//	private String[] getOrders = {"method", "accesskey", "tradeType", "currency", "pageIndex"};
//	private String[] getOrdersNew = {"method", "accesskey", "tradeType", "currency", "pageIndex", "pageSize"};
//	private String[] getOrdersIgnoreTradeType = {"method", "accesskey", "currency", "pageIndex", "pageSize"};
//	private String[] getUnfinishedOrdersIgnoreTradeType = {"method", "accesskey", "currency", "pageIndex", "pageSize"};
//	private String[] getAccountInfo = {"method", "accesskey"};
//	
//	private static VerifiUtil verifiUtil;
//	
//	public static Map<String, Object> users;
//	
//	//
//	public static VerifiUtil getInstance(){
//		if(verifiUtil != null)
//			return verifiUtil;
//		else 
//			return new VerifiUtil();
//	}
//	
//	public VerifiUtil(){
//		map = new HashMap<String,Object>();
//		map.put("order", order);
//		map.put("cancelOrder", cancelOrder);
//		map.put("getOrder", getOrder);
//		map.put("getOrders", getOrders);
//		map.put("getOrdersNew", getOrdersNew);
//		map.put("getOrdersIgnoreTradeType", getOrdersIgnoreTradeType);
//		map.put("getUnfinishedOrdersIgnoreTradeType", getUnfinishedOrdersIgnoreTradeType);
//		map.put("getAccountInfo", getAccountInfo);
//	}
//	
//	/**
//	 * 获取指定按顺序的参数
//	 * @return
//	 */
//	public String[] getFixedArguments(String method){
//		return (String[]) map.get(method);
//	}
//	
//	/**
//	 * API访问请求验证
//	 * @param info
//	 * @param key
//	 * @param secret
//	 * @return
//	 */
//	public Object validateAuthAccess(Index index, HttpServletRequest request){
//		boolean flag = false;
//		try{
//			String key = request.getParameter("accesskey");	//用户传递访问key
//			long reqTime = new Long(request.getParameter("reqTime"));	//用记的请求时间
//			String authInfo = request.getParameter("sign"); 	//用户根据URL生成的加密串
//			
//			//强制参数存在为空，不通过
//			if(StringUtils.isEmpty(authInfo) || StringUtils.isEmpty(key) || StringUtils.isEmpty(String.valueOf(reqTime))){
//				return SystemCode.code_3005;
//			}
//			
//			//==============================Memory Cache 进行IP、访问限制验证===================================================
//			//用户IP过滤
//			boolean ipLimit = true;
//			if(!ipLimit){
//				SystemCode code = SystemCode.code_1001;
//				code.setClassName("当前IP请求过于频繁，已列入黑名单，API交易接口已自动关闭");
//				code.setValue("当前IP请求过于频繁，已列入黑名单，API交易接口已自动关闭");
//				//关闭API交易接口
//				ChbtcResponse response = UserManager.getInstance().closeUserAutoApi(key);
//				JSONObject json = JSONObject.fromObject(response.getMsg());
//				if(response.taskIsFinish()){
//					if(json.getBoolean("isSuc")){
//						return code;
//					}else{
//						return false;
//					}
//				}else{
//					return false;
//				}
//			}
//			
//			//用户访问时间跟次数限制(相当必要)
//			Object accessLimit = accessLimit(request, key);
//			if(accessLimit instanceof SystemCode){
//				return accessLimit;
//			}else{
//				if(!((Boolean)accessLimit))
//					return SystemCode.code_4002;
//			}
//			//================================================================================================================================================
//			
//			if(users == null) users = new HashMap<String, Object>();
//			
//			JSONObject userObject = (JSONObject) users.get("user_object_json_" + key);
//			//是否需要重新查询用户
//			if(userObject == null){
//				ChbtcResponse response = UserManager.getInstance().getUserByAccessKey(key, 0);
//				if(response == null || !response.getMsg().startsWith("{")){
//					return SystemCode.code_1003;
//				}
//				if(response.taskIsFinish()){
//					JSONObject json = JSONObject.fromObject(response.getMsg());
//					if(json.getBoolean("isSuc")){
//						userObject = JSONObject.fromObject(json.getString("datas"));
//						//最后一次查询时间
//						users.put("user_object_json_" + key, userObject);
//					}
//				}
//			}
//			
//			if(userObject == null){
//				return SystemCode.code_1003;
//			}
//			//System.out.println("----------用户当前状态------------" + userObject.getInt("apiStatus"));
//			//设置已经查询到的用户对象
//			index.setJsonObject(userObject);
//			
//			if(userObject.getInt("apiStatus") == 2){//API接口开放交易
////				String apiIpBind = userObject.getString("apiIpBind");
////				//交易IP绑定
////				if(StringUtils.isNotEmpty(apiIpBind)){
////					//如果申请了绑定交易IP则验证IP审核状态
////					if(userObject.getInt("apiIpStatus") == 2){
////						//如果请求的IP与绑定的IP不一致
////						if(!apiIpBind.equals(IpUtil.getIp(request))){//IP不一致
////							return SystemCode.code_3006;
////						}
////					}else{
////						return SystemCode.code_4001;
////					}
////				}
//				
//				//服务器当前时间
//				long currTime = System.currentTimeMillis();
//				//用户请求时间在必须在服务器时间前后5分钟
//				if(reqTime >= (currTime - 5*60*1000) && reqTime <= (currTime + 5*60*1000)){
//					//获取用户加密的原信息
//					Object info = getAuthorization(request);
//					if(info instanceof SystemCode){
//						return info;
//					}else{
//						//与用户执行相同的加密方法，传入相同的secret
//						info = EncryDigestUtil.hmacSign((String)info, userObject.getString("apiSecret"));
//						//与用户的加密信息作比较
//						if(authInfo.equals(info)){
//							flag = true;
////							//验证通过，记录本次请求时间
////							Data.Update("update btcuser set apiAuthTime=? where userId=?", new Object[]{reqTime, userObject.getString("userId")});
//						}
//					}
//				}
//			}else{
//				//Cache.Set("user_"+key+"_api_locked", String.valueOf("locked"), 30*60);
//				return SystemCode.code_4001;
//			}
//		}catch(Exception ex){
//			ex.printStackTrace();
//			return SystemCode.code_1002;
//		}
//		return flag;
//	}
//	
//	/**
//	 * 用户加密原信息
//	 * @param request
//	 * @return
//	 */	
//	private Object getAuthorization(HttpServletRequest request){
//		String method = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
//		String[] fixedArguments = getFixedArguments(method);
//		StringBuffer buffer = new StringBuffer();
//		int i = 0;
//		String value = "";
//		for (String string : fixedArguments) {
//			if(i != 0){
//				buffer.append("&");
//			}
//			value = request.getParameter(string);
//			if(StringUtils.isEmpty(value)){
//				return SystemCode.code_3005;
//			}
//			buffer.append(string).append("=").append(value);
//			i++;
//		}
//		return buffer.toString();
//	}
//	
//	/**
//	 * 访问限制
//	 * 请求同一个方法，1s内只能请求一次
//	 * 请求不同的方法，1s可以请求多次
//	 * 请求同一个方法，1m内超过规定次数，视为恶意攻击，自动关闭API交易接口
//	 * @param request
//	 * @return
//	 */
//	public Object accessLimit(HttpServletRequest request, String key) throws Exception{
//		//请求method
//		String method = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
//		//上一次的请求时间
//		String oldReqTime = Cache.Get(method + "_"+key);
//		//服务器当前时间
//		long currTime = System.currentTimeMillis();
//		//缓存时间周期
//		int cycle = 30*60;
//		//一分钟内限制次数
//		int limitTimes = 1000;
//		//限制时间1分钟
//		int limitTime = 60 * 1000;
//		//一秒钟内限制次数 
//		int slimitTimes = 20;
//		//还未请求过，或缓存已失效
//		if(StringUtils.isEmpty(oldReqTime)){	
//			Cache.Set(method+"_"+key, String.valueOf(currTime), cycle);
//			Cache.Set(method+"_"+key+"_one_min_times", "1", cycle);
//			Cache.Set(method+"_"+key+"_one_sec_times", "1", cycle);
//			Cache.Set(method+"_"+key+"_one_min_startTime", String.valueOf(currTime), cycle);
//			//Cache.Set("user_"+key+"_api_locked", String.valueOf("unlocked"), cycle);
//			return true;
//		}else{
//			//判断该用户是否被锁住API了
////			if("locked".equals(Cache.Get("user_"+key+"_api_locked"))){
////				return SystemCode.code_4001;
////			}
//			
//			//上一次的请求时间
//			long old = new Long(oldReqTime);
//			//--------------------------------------限制一分钟内请求次数------------------------------------
//			//请求次数
//			String times = Cache.Get(method+"_"+key+"_one_min_times");
//			//根据用户建议，method=order其实需要分开为2个方法BTC/LTC
//			if(method.equalsIgnoreCase("order")){
//				//限制次数增加300次
//				limitTimes += 300;
//			}
//			//第一次请求开始时间
//			String startTime = Cache.Get(method+"_"+key+"_one_min_startTime");
//			
//			if(StringUtils.isEmpty(startTime)){
//				//重新设置请求次数
//				Cache.Set(method+"_"+key+"_one_min_times", "1", cycle);
//				return true;
//			}
//			
//			//1分钟内请求超过limitTimes次，关闭该用户API交易接口，并清空缓存
//			if(Integer.parseInt(times) > limitTimes && currTime - new Long(startTime) < limitTime){
//				Cache.Delete(method+"_"+key);
//				Cache.Delete(method+"_"+key+"_one_min_times");
//				Cache.Delete(method+"_"+key+"_one_sec_times");
//				Cache.Delete(method+"_"+key+"_one_min_startTime");
//				//Cache.Set("user_"+key+"_api_locked", String.valueOf("locked"), cycle);
//				//关闭API交易接口
//				ChbtcResponse response = UserManager.getInstance().closeUserAutoApi(key);
//				JSONObject json = JSONObject.fromObject(response.getMsg());
//				if(response.taskIsFinish()){
//					if(json.getBoolean("isSuc")){
//						return SystemCode.code_4001;
//					}
//				}
//			}else{
//				int t = 0;
//				//如果缓存时间与当前请求时间超过1分钟，则更新缓存的开始时间，请求次数归0
//				if(currTime - new Long(startTime) >= limitTime){
//					Cache.Set(method+"_"+key+"_one_min_startTime", String.valueOf(currTime), cycle);
//					t = 1;
//				}else{
//					//如果请求次数到达limitTimes次，则重新数数
//					if(Integer.parseInt(times) == (limitTimes + 1)){
//						t = 1;
//					}else{
//						t = Integer.parseInt(times) + 1;
//					}
//				}
//				//重新设置请求次数
//				Cache.Set(method+"_"+key+"_one_min_times", String.valueOf(t), cycle);
//			}
//			//===========================================================================================
//			//如果当前求比上一次请求时间>=1s
//			if(currTime - old >= 1000){
//				Cache.Set(method + "_"+key, String.valueOf(currTime), cycle);
//				Cache.Set(method + "_"+key+"_one_sec_times", "1", cycle);
//				return true;
//			}else{
//				//1秒钟内请求次数
//				int ost = Integer.parseInt(Cache.Get(method + "_"+key+"_one_sec_times"));
//				if(ost > slimitTimes){
//					//请求过于频繁
//					return SystemCode.code_4002;
//				}else{
//					Cache.Set(method + "_"+key, String.valueOf(currTime), cycle);
//					Cache.Set(method + "_"+key+"_one_sec_times", String.valueOf(ost+1), cycle);
//					return true;
//				}
//			}
//		}
//	}
}
