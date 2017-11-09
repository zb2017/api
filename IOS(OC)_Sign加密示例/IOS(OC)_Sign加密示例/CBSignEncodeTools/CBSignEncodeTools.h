//
//  SHA256.h
//  chbtc
//
//  Created by 丁 on 2017/9/13.
//  Copyright © 2017年 丁. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CommonCrypto/CommonDigest.h>

@interface CBSignEncodeTools : NSObject

/** sha加密方式 */
+ (NSString *)getShaString:(NSString *)srcString;
/** HmacMD5 加密 */
+ (NSString *)HMACMD5WithString:(NSString *)toEncryptStr WithKey:(NSString *)keyStr;

@end
