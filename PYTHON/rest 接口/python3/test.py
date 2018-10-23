
from zapi import ZApi





if __name__ == '__main__':
    zb = ZApi(access_key='ffff', secret_key='ddddd')
    # a = zb.all_ticker()
    a = zb.order( "bts_usdt", 1, 0.1, 0.1)
    print(a)


