# encoding: UTF-8

from vnzb import *

# 在zb网站申请这两个Key，分别对应用户名和密码
apiKey = 'apiKey'
secretKey = 'secretKey'

# 创建API对象
api = ZB_Sub_Spot_Api()

api.connect_Subpot(apiKey, secretKey, True)

sleep(3)

# api.login()
# api.subscribeSpotTicker("ltc_btc")
# api.subscribeSpotDepth("ltc_btc")
# api.subscribeSpotTrades("ltc_btc")
#api.subscribeSpotKlines("bch_btc","30min")

#api.spotTrade("btc_qc", 1 , "50" , "0.01")
'''
{"message":"操作成功","no":"0","data":"{entrustId:2017121051685}","code":1000,"channel":"btcqc_order","success":true}
{"message":"操作成功","no":"0","data":"{entrustId:2017121051713}","code":1000,"channel":"btcqc_order","success":true}
'''
#api.spotUserInfo()

#api.spotCancelOrder("btc_qc", "2017121051685")

api.spotOrderInfo("btc_qc","2017121051713")
