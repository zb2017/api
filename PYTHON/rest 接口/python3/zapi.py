import hashlib
import struct
# import sha
import time
import requests


suc_codes = ["1000"]

DEBUG = True

errcode = {
    "1000": "success",
    "1001": "normal error",
    "1002": "internal error",
    "1003": "verify not passed",
    "1004": "fund security password locked",
    "1005": "fund security password not right",
    "1006": "real-name authentication verifying or not passed",
    "1009": "current api not in service",
    "2001": "RMB not sufficient",
    "2002": "BTC not sufficient",
    "2003": "LTC not sufficient",
    "2005": "ETH not sufficient",
    "2006": "ETC not sufficient",
    "2007": "BTS not sufficient",
    "2009": "balance not sufficient",
    "3001": "order not found",
    "3002": "invalid amount of money",
    "3003": "invalid count",
    "3004": "user not exists",
    "3005": "illegal argument",
    "3006": "IP error",
    "3007": "time expired",
    "3008": "trade history not found",
    "4001": "API locked or not opened",
    "4002": "requests too frequently"
}

ORDER_TYPE_BUY = 1
ORDER_TYPE_SELL = 0

ORDER_STATUS_CANCELED = 1
ORDER_STATUS_DONE = 2
ORDER_STATUS_WAITING = 3

TPL_MARKETS = 'http://api.zb.com/data/v1/markets'
TPL_ALL_MARKET = 'http://api.zb.cn/data/v1/allTicker'
TPL_MARKET = 'http://api.zb.com/data/v1/ticker?market={MARKET}'
TPL_DEPTH = 'http://api.zb.com/data/v1/depth?market={MARKET}&size={SIZE}'
TPL_TRADES = 'http://api.zb.com/data/v1/trades?market={MARKET}'
TPL_KLINE = 'http://api.zb.com/data/v1/kline?market={MARKET}'

URL_ORDER = 'https://trade.zb.com/api/order'
URL_CANCEL_ORDER = 'https://trade.zb.com/api/cancelOrder'
URL_GET_ORDER = 'https://trade.zb.com/api/getOrder'
URL_GET_ORDERS = 'https://trade.zb.com/api/getOrders'
URL_GET_ORDERS_NEW = 'https://trade.zb.com/api/getOrdersNew'
URL_GET_ORDERS_IGNORE_TRADE_TYPE = 'https://trade.zb.com/api/getOrdersIgnoreTradeType'
URL_GET_UNFINISHED_ORDERS_IGNORE_TRADE_TYPE = 'https://trade.zb.com/api/getUnfinishedOrdersIgnoreTradeType'
URL_GET_ACCOUNT_INFO = 'https://trade.zb.com/api/getAccountInfo'
URL_GET_USER_ADDRESS = 'https://trade.zb.com/api/getUserAddress'
URL_GET_WITHDRAW_ADDRESS = 'https://trade.zb.com/api/getWithdrawAddress'
URL_GET_WITHDRAW_RECORD = 'https://trade.zb.com/api/getWithdrawRecord'
URL_GET_CHARGE_RECORD = 'https://trade.zb.com/api/getChargeRecord'
URL_WITHDRAW = 'https://trade.zb.com/api/withdraw'

URL_doTransferFunds = 'https://trade.zb.cn/api/doTransferFunds'

class ZApi:
    def __init__(self, access_key, secret_key):
        """
        opened markets and status, format:
            {
                "btc_usdt": {
                    "amountScale": 4,
                    "priceScale": 2
                },
                "ltc_usdt": {
                    "amountScale": 3,
                    "priceScale": 2
                }
                ...
            }
        """
        self._access_key_ = access_key
        self._secret_key_ = secret_key
        self._markets_ = self.markets()
        if len(self._markets_) < 1:
            raise Exception("Get markets status failed")

    def get(self, url):
        while True:
            try:
                r = requests.get(url)
            except Exception:
                time.sleep(0.5)
                continue
            if r.status_code != 200:
                time.sleep(0.5)
                continue
            r_info = r.json()
            r.close()
            return r_info

    def check_market_code(self, market):
        if not market:
            return False
        if market not in self._markets_:
            return False
        else:
            return True

    def markets(self):
        """
        markets data, result format:
            {
                "btc_usdt": {
                    "amountScale": 4,
                    "priceScale": 2
                },
                "ltc_usdt": {
                    "amountScale": 3,
                    "priceScale": 2
                }
                ...
            }
        :return: result data of markets
        """
        url = TPL_MARKETS
        return self.get(url)
    def all_ticker(self,):
        """
        ticker data, result format:
            {
                "ticker": {
                    "vol": "40.463",
                    "last": "0.899999",
                    "sell": "0.5",
                    "buy": "0.225",
                    "high": "0.899999",
                    "low": "0.081"
                },
                "date": "1507875747359"
            }
        :param market: market code string
        :return: ticker result
        """
        url = TPL_ALL_MARKET
        return self.get(url)

    def ticker(self, market):
        """
        ticker data, result format:
            {
                "ticker": {
                    "vol": "40.463",
                    "last": "0.899999",
                    "sell": "0.5",
                    "buy": "0.225",
                    "high": "0.899999",
                    "low": "0.081"
                },
                "date": "1507875747359"
            }
        :param market: market code string
        :return: ticker result
        """
        url = TPL_MARKET.format(MARKET=market)
        return self.get(url)

    def depth(self, market, size):
        """
        depth data of specific market, result format:
            {
                "asks": [
                    [
                        83.28,
                        11.8
                    ]...
                ],
                "bids": [
                    [
                        81.91,
                        3.65
                    ]...
                ],
                "timestamp" : timestamp
            }
        :param market: market code string
        :param size: depth size
        :return: result depth data
        """
        url = TPL_DEPTH.format(MARKET=market, SIZE=str(size))
        return self.get(url)

    def trades(self, market):
        """
        trade history, result format:
            [
                {
                    "amount": 0.541,
                    "date": 1472711925,
                    "price": 81.87,
                    "tid": 16497097,
                    "trade_type": "ask",
                    "type": "sell"
                }...
            ]
        :return: trade history data
        """
        url = TPL_TRADES.format(MARKET=market)
        return self.get(url)

    def kline(self, market):
        """
        kline data, result format:
            {
                "data": [
                    [
                        1472107500000,
                        3840.46,
                        3843.56,
                        3839.58,
                        3843.3,
                        492.456
                    ]...
                ],
                "moneyType": "btc",
                "symbol": "ltc"
            }
        :param market: market code
        :return: kline data
        """
        url = TPL_KLINE.format(MARKET=market)
        return self.get(url)

    def order(self, market, type, amount, price):
        """
        order api, result format:
            {
                "code": "1000",
                "message": "***",
                "id": "20131228361867"
            }
        :param market: currency
        :param type: ORDER_TYPE_BUY or ORDER_TYPE_SELL
        :param amount: order amount
        :param price: order price
        :return: order result
        """
        params = 'accesskey=%s&amount=%s&currency=%s&method=order&price=%s&tradeType=%s' % (self._access_key_, amount, market, price, type)
        return self.call_api(url=URL_ORDER, params=params)

    def cancel_order(self, market, order_id):
        """
        cancel an order, result format:
            {
                "code": "1000",
                "message": "***"
            }
        :param market: currency
        :param order_id: order id
        :return: result
        """
        params = 'accesskey=%s&currency=%s&id=%s&method=cancelOrder' % (self._access_key_, market, order_id)
        return self.call_api(url=URL_CANCEL_ORDER, params=params)
    
    def cancel_allorder(self,market,page_index=1, page_size=5):
        flag = False
        orders = self.get_unfinished_orders_ignore_trade_type(market,page_index, page_size)
        #print('order',orders)
        if isinstance(orders,list) and len(orders) > 0 and 'id' in orders[0].keys():
            flag = True
        while flag :
            time.sleep(0.1)
            for order in orders:
                #print(market,orders)
                self.cancel_order(market,order['id'])
            orders = self.get_unfinished_orders_ignore_trade_type(market,page_index, page_size)
            if isinstance(orders,list) and len(orders) > 0 and 'id' in orders[0].keys():
                flag = True
            else :
                print('%s撤销所有订单完成！返回%s：'%(market,orders))  
                flag = False

    def get_order(self, market, order_id):
        """
        get order detail, result format:
            {
                "currency": "btc",
                "id": "20150928158614292",
                "price": 1560,
                "status": 3,
                "total_amount": 0.1,
                "trade_amount": 0,
                "trade_price" : 6000,
                "trade_date": 1443410396717,
                "trade_money": 0,
                "type": 0,
            }
        :param market: currency
        :param order_id: order id
        :return: result
        """
        params = 'accesskey=%s&currency=%s&id=%s&method=getOrder' % (self._access_key_, market, order_id)
        return self.call_api(url=URL_GET_ORDER, params=params)

    def get_orders(self, market, page, type):
        """
        get multiple orders, result format:
            [
                {
                    "currency": "btc",
                    "id": "20150928158614292",
                    "price": 1560,
                    "status": 3,
                    "total_amount": 0.1,
                    "trade_amount": 0,
                    "trade_price" : 6000,
                    "trade_date": 1443410396717,
                    "trade_money": 0,
                    "type": 0
                }...
            ]
        :param market: currency
        :param page: page index
        :param type: order type
        :return: result
        """
        params = 'accesskey=%s&currency=%s&method=getOrders&pageIndex=%s&tradeType=%s' % (self._access_key_, market, page, type)
        return self.call_api(url=URL_GET_ORDERS, params=params)

    def get_orders_new(self, market, page_index, page_size, type):
        """
        get multiple orders, result format same as get_orders
        :param market: currency
        :param page_index: page index
        :param page_size: page size
        :param type: order type
        :return: result
        """
        params = 'accesskey=%s&currency=%s&method=getOrdersNew&pageIndex=%s&pageSize=%s&tradeType=%s' % (self._access_key_, market, page_index, page_size, type)
        return self.call_api(url=URL_GET_ORDERS_NEW, params=params)

    def get_orders_ignore_tader_type(self, market, page_index, page_size):
        """
        get multiple orders ignore type, result format same as get_orders
        :param market: currency
        :param page_index: page index
        :param page_size: page size
        :return: result
        """
        params = 'accesskey=%s&currency=%s&method=getOrdersIgnoreTradeType&pageIndex=%s&pageSize=%s' % (self._access_key_, market, page_index, page_size)
        return self.call_api(url=URL_GET_ORDERS_IGNORE_TRADE_TYPE, params=params)

    def get_unfinished_orders_ignore_trade_type(self, market, page_index, page_size):
        """
        get multiple unfinished orders ignore type, result format same as get_orders
        :param market: currency
        :param page_index: page index
        :param page_size: page size
        :return: result
        """
        params = 'accesskey=%s&currency=%s&method=getUnfinishedOrdersIgnoreTradeType&pageIndex=%s&pageSize=%s' % (self._access_key_, market, page_index, page_size)
        # print(params)
        return self.call_api(url=URL_GET_UNFINISHED_ORDERS_IGNORE_TRADE_TYPE, params=params)

    def zhuanzhang_zi(self,amount,currency,fromUerName,toUserName):
        """
        get multiple unfinished orders ignore type, result format same as get_orders
        :param market: currency
        :param page_index: page index
        :param page_size: page size
        :return: result
        """
        params = 'accesskey=%s&amount=%s&currency=%s&fromUserName=%s&method=doTransferFunds&toUserName=%s' % (self._access_key_, amount,currency,fromUerName,toUserName)
        return self.call_api(url='https://trade.zb.cn/api/doTransferFunds', params=params)
    def huoquzizhanghao(self):
        """:
        """
        # params = 'accesskey=%s&method=getAccountInfo' % self._access_key_
        params = 'accesskey=%s&method=getSubUserList' % self._access_key_

        return self.call_api(url='https://trade.zb.cn/api/getSubUserList', params=params)

    def chuangjianzi(self):
        """:
        """
        # params = 'accesskey=%s&method=getAccountInfo' % self._access_key_
        params = 'accesskey=%s&method=getSubUserList' % self._access_key_
        params = 'accesskey=%s&memo=%s&password=%s&method=addSubUser&subUserName=ceshi11' % (self._access_key_, 'ceshi11', '1234qwer')
        return self.call_api(url='https://trade.zb.cn/api/addSubUser', params=params)


    def get_account_info(self):
        """
        get account information, result format:
            {
                "result": {
                    "coins": [
                        {
                            "freez": "0.00000000",
                            "enName": "BTC",
                            "unitDecimal": 8,
                            "cnName": "BTC",
                            "unitTag": "*",
                            "available": "0.00000000",
                            "key": "btc"
                        },
                        {
                            "freez": "0.00000000",
                            "enName": "LTC",
                            "unitDecimal": 8,
                            "cnName": "LTC",
                            "unitTag": "*",
                            "available": "0.00000000",
                            "key": "ltc"
                        },
                       ...
                    ],
                    "base": {
                        "username": "134150***",
                        "trade_password_enabled": true,
                        "auth_google_enabled": false,
                        "auth_mobile_enabled": true
                    }
                }
            }
        :return: result
        """
        params = 'accesskey=%s&method=getAccountInfo' % self._access_key_
        return self.call_api(url=URL_GET_ACCOUNT_INFO, params=params)

    def get_user_address(self, currency):
        """
        get user address, result format:
            {
                "code": 1000,
                "message": {
                    "des": "success",
                    "isSuc": true,
                    "datas": {
                        "key": "0x0af7f36b8f09410f3df62c81e5846da673d4d9a9"
                    }
                }
            }
        :param currency: currency
        :return: result
        """
        params = 'accesskey=%s&currency=%s&method=getUserAddress' % (self._access_key_, currency)
        return self.call_api(url=URL_GET_USER_ADDRESS, params=params)

    def get_withdraw_address(self, currency):
        """
        get withdraw address, result format:
            {
                "code": 1000,
                "message": {
                    "des": "success",
                    "isSuc": true,
                    "datas": {
                        "key": "0x0af7f36b8f09410f3df62c81e5846da673d4d9a9"
                    }
                }
            }
        :param currency: currency
        :return: result
        """
        params = 'accesskey=%s&currency=%s&method=getWithdrawAddress' % (self._access_key_, currency)
        return self.call_api(url=URL_GET_WITHDRAW_ADDRESS, params=params)

    def get_withdraw_record(self, currency, page_index, page_size):
        """
        get withdraw record, result format:
            {
                "code": 1000,
                "message": {
                    "des": "success",
                    "isSuc": true,
                    "datas": {
                        "list": [
                            {
                                "amount": 0.01,
                                "fees": 0.001,
                                "id": 2016042556231,
                                "manageTime": 1461579340000,
                                "status": 3,
                                "submitTime": 1461579288000,
                                "toAddress": "14fxEPirL9fyfw1i9EF439Pq6gQ5xijUmp"
                            }...
                        ],
                        "pageIndex": 1,
                        "pageSize": 10,
                        "totalCount": 4,
                        "totalPage": 1
                    }
                }
            }
        :param currency:
        :param page_index:
        :param page_size:
        :return:
        """
        params = 'accesskey=%s&currency=%s&method=getWithdrawRecord&pageIndex=%s&pageSize=%s' % (self._access_key_, currency, page_index, page_size)
        return self.call_api(url=URL_GET_WITHDRAW_RECORD, params=params)

    def get_charge_record(self, currency, page_index, page_size):
        """
        get charge record, result format:
            {
                "code": 1000,
                "message": {
                    "des": "success",
                    "isSuc": true,
                    "datas": {
                        "list": [
                            {
                                "address": "1FKN1DZqCm8HaTujDioRL2Aezdh7Qj7xxx",
                                "amount": "1.00000000",
                                "confirmTimes": 1,
                                "currency": "BTC",
                                "description": "***",
                                "hash": "7ce842de187c379abafadd64a5fe66c5c61c8a21fb04edff9532234a1dae6xxx",
                                "id": 558,
                                "itransfer": 1,
                                "status": 2,
                                "submit_time": "2016-12-07 18:51:57"
                            }...
                        ],
                        "pageIndex": 1,
                        "pageSize": 10,
                        "total": 8
                    }
                }
            }
        :param currency: currency
        :param page_index: page index
        :param page_size: page size
        :return: result
        """
        params = 'accesskey=%s&currency=%s&method=getChargeRecord&pageIndex=%s&pageSize=%s' % (self._access_key_, currency, page_index, page_size)
        return self.call_api(url=URL_GET_CHARGE_RECORD, params=params)

    def withdraw(self, currency, amount, fees, itransfer, addr, pwd):
        """
        withdraw coins, result format:
            {
                "code": 1000,
                "message": "success",
                "id": "***"
            }
        :param currency: currency
        :param amount: amount
        :param fees: fees
        :param itransfer: itransfer
        :param addr: receive address
        :param pwd: safe password of current account
        :return: result
        """
        params = 'accesskey=%s&amount=%s&currency=%s&fees=%s&itransfer=%s&method=withdraw&receiveAddr=%s&safePwd=%s' % (self._access_key, amount, currency, fees, itransfer, addr, pwd)
        return self.call_api(url=URL_WITHDRAW, params=params)

    def call_api(self, url, params=''):
        full_url = url
        if params:
            sha_secret = self.digest(self._secret_key_)
            sign = self.hmac_sign(params, sha_secret)
            req_time = int(round(time.time() * 1000))
            params += '&sign=%s&reqTime=%d' % (sign, req_time)
            full_url += '?' + params
        result = {}
        while True:
            try:
                r = requests.get(full_url, timeout=2)
            except Exception:
                time.sleep(0.5)
                continue
            if r.status_code != 200:
                time.sleep(0.5)
                r.close()
                continue
            else:
                result = r.json()
                r.close()
                break
        return result

    @staticmethod
    def fill(value, lenght, fill_byte):
        if len(value) >= lenght:
            return value
        else:
            fill_size = lenght - len(value)
        return value + chr(fill_byte) * fill_size

    @staticmethod
    def xor(s, value):
        slist = list(s.decode('utf-8'))
        for index in range(len(slist)):
            slist[index] = chr(ord(slist[index]) ^ value)
        return "".join(slist)

    def hmac_sign(self, arg_value, arg_key):
        keyb = struct.pack("%ds" % len(arg_key), arg_key.encode('utf-8'))
        value = struct.pack("%ds" % len(arg_value), arg_value.encode('utf-8'))
        k_ipad = self.xor(keyb, 0x36)
        k_opad = self.xor(keyb, 0x5c)
        k_ipad = self.fill(k_ipad, 64, 54)
        k_opad = self.fill(k_opad, 64, 92)
        m = hashlib.md5()
        m.update(k_ipad.encode('utf-8'))
        m.update(value)
        dg = m.digest()

        m = hashlib.md5()
        m.update(k_opad.encode('utf-8'))
        subStr = dg[0:16]
        m.update(subStr)
        dg = m.hexdigest()
        return dg
  
    def digest(self, arg_value):
        value = struct.pack("%ds" % len(arg_value), arg_value.encode('utf-8'))
        h = hashlib.sha1()
        h.update(value)
        dg = h.hexdigest()
        return dg



