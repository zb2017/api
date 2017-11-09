//
//  CBExamples.m
//  JXMovableCellTableView
//
//  Created by 丁 on 2017/9/14.
//  Copyright © 2017年 jiaxin. All rights reserved.
//

#import "CBExamples.h"
#import "CBSignEncodeTools.h"

@implementation CBExamples

//加密sign
-(void)encode{
    
    /*
     1) 先对 secretKey 进行 SHA 加密
     2) 严格按照CHBTC文档 将参数按照顺序拼成字符串
     3) 以 SHA加密后得到的字符串 为 key 对参数拼接字符串进行 HmacMD5 加密
     3) 得到sign参数
     */
    NSString *signStr = [CBSignEncodeTools HMACMD5WithString:@"参数拼接字符串" WithKey:[CBSignEncodeTools getShaString:@"Secret Key"]];
    
}

@end
