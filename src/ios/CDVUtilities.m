/********* DeviceMeta.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import "CDVUtilities.h"

@interface CDVUtilities () {}
@end

@implementation CDVUtilities

- (void)getHtmlSource:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSDictionary* objectProperties = [self objectProperties];

    pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK  messageAsDictionary:objectProperties];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (NSDictionary*)objectProperties
{
    NSMutableDictionary* devProps = [NSMutableDictionary dictionaryWithCapacity:1];
    [devProps setObject:[self getHtmlCode] forKey:@"result"];

    NSDictionary* devReturn = [NSDictionary dictionaryWithDictionary:devProps];
    return devReturn;
}

- (NSString *)getHtmlCode
{
    return @"<div>Testing</div>";
}

- (void)openURL:(CDVInvokedUrlCommand*)command
{
    // https://github.com/PaoloMessina/OpenUrlExt
    CDVPluginResult* pluginResult = nil;
    NSString *url = [command.arguments objectAtIndex:0];
    
    if (url != nil && [url length] > 0) {
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:url];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

@end
