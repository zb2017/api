<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>zb</title>
</head>
<?php

//$zb=new zbAPI ;
//$Fond_zb=$zb->Fund();
//var_dump($Fond_zb);

class zbAPI {
var  $access_key="***";
var  $secret_key="***";

function httpRequest($pUrl, $pData){
    $tCh = curl_init();
    curl_setopt($tCh, CURLOPT_POST, true);
    curl_setopt($tCh, CURLOPT_POSTFIELDS, $pData);
    curl_setopt($tCh, CURLOPT_HTTPHEADER, array("Content-type: application/x-www-form-urlencoded"));
    curl_setopt($tCh, CURLOPT_URL, $pUrl);
    curl_setopt($tCh, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($tCh, CURLOPT_SSL_VERIFYPEER, false);
    $tResult = curl_exec($tCh);
    curl_close($tCh);
    $tResult=json_decode ($tResult,true);
    return $tResult;
}

function HangqingRequest($pUrl){
    $tCh = curl_init();
    curl_setopt($tCh, CURLOPT_URL, $pUrl);
    curl_setopt($tCh, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($tCh, CURLOPT_SSL_VERIFYPEER, false);
    curl_setopt($tCh, CURLOPT_TIMEOUT, 1);
    $tResult = curl_exec($tCh);
    curl_close($tCh);
    $tResult=json_decode ($tResult,true);
    return $tResult;
}

function createSign($pParams = array()){    
    $tPreSign = http_build_query($pParams, '', '&');
    $SecretKey = sha1($this->secret_key);
    $tSign=hash_hmac('md5',$tPreSign,$SecretKey);
    $pParams['sign'] = $tSign;
    $pParams['reqTime'] = time()*1000;
    $tResult=http_build_query($pParams, '', '&');
    return $tResult;
}

function Hangqing(){
	$Url_btc="http://api.zb.com/data/v1/ticker?currency=btc";
	$res=array();
	$res=$this->HangqingRequest($Url_btc);
	return $res;
}

function MarketDepth($N=20){
	 $res=$this->HangqingBtc();
   $res_ask=array_reverse(array_slice($res["asks"] , -$N, $N));
	 $res_bid=array_slice($res["bids"] , 0, $N) ;
	 $ans=array("asks"=>$res_ask,"bids"=>$res_bid);
	 return $ans;
  }

   
//BTC 下单
function  Trade($Price,$Amount,$Tradetype){					  
	 $parameters=array("accesskey"=>$this->access_key,"amount"=>$Amount,"currency"=>"btc","method"=>"order","price"=>$Price,"tradeType"=>$Tradetype);	 
   $url= "https://trade.zb.com/api/order";
   $post=$this->createSign($parameters);
	 $res=$this->httpRequest($url,$post);
	 return $res;
}		

//BTC 取消订单
  function	CancelOrder($OrderID){
	$parameters=array("accesskey"=>$this->access_key,"currency"=>"btc","id"=>$OrderID,"method"=>"cancelOrder");
  $url='https://trade.zb.com/api/cancelOrder';
	$post=$this->createSign($parameters);
	$res=$this->httpRequest($url,$post);
	return $res;
	}

//LTC 取消订单 
  function	CancelOrder_ltc($OrderID){
	$parameters=array("accesskey"=>$this->access_key,"currency"=>"ltc","id"=>$OrderID,"method"=>"cancelOrder");
  $url='https://trade.zb.com/api/cancelOrder';
	$post=$this->createSign($parameters);
	$res=$this->httpRequest($url,$post);
	return $res;
	}


 function Fund(){
 $parameters=array("accesskey"=>$this->access_key,"method"=>"getAccountInfo");
 $url='https://trade.zb.com/api/getAccountInfo';
 $post=$this->createSign($parameters);
 $res=$this->httpRequest($url,$post);
 return $res;
}	

//获取订单信息
 function  GetOrder($OrderID){
 $parameters=array("accesskey"=>$this->access_key,"currency"=>"btc","id"=>$OrderID,"method"=>"getOrder");
 $url= 'https://trade.zb.com/api/getOrder';
 $post=$this->createSign($parameters);
 $res=$this->httpRequest($url,$post);
 return $res;
}

//获取订单信息
 function  GetOrder_ltc($OrderID){
 $parameters=array("accesskey"=>$this->access_key,"currency"=>"ltc","id"=>$OrderID,"method"=>"getOrder");
 $url= 'https://trade.zb.com/api/getOrder';
 $post=$this->createSign($parameters);
 $res=$this->httpRequest($url,$post);
 return $res;
}

}
?>
<body>
</body>
</html>
