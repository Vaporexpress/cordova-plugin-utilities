/********* DeviceMeta.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import "CDVUtilities.h"

@interface CDVUtilities () {}
@end

@implementation CDVUtilities


// http://codewithchris.com/tutorial-how-to-use-ios-nsurlconnection-by-example/
// https://www.objc.io/issues/5-ios7/from-nsurlconnection-to-nsurlsession/

- (void)getHtmlSource:(CDVInvokedUrlCommand*)command
{
    NSString *urlarg = [command.arguments objectAtIndex:0];
    if (urlarg != nil && [urlarg length] > 0) {
        [self getCode:urlarg:command];
    } else {
        [self pluginResultado:urlarg:command:NO];
    }
}
- (void)getMimeType:(CDVInvokedUrlCommand*)command
{
    NSString *urlarg = [command.arguments objectAtIndex:0];
    NSString* MIMEType;
    if (urlarg != nil && [urlarg length] > 0) {
        NSURL *URL = [NSURL URLWithString:urlarg];
        NSMutableURLRequest *newRequest = [NSMutableURLRequest requestWithURL:URL];
        NSURLResponse *response=nil;
        NSError *error=nil;
        
        [newRequest setValue:@"HEAD" forKey:@"HTTPMethod"];
        [NSURLConnection sendSynchronousRequest:newRequest returningResponse:&response error:&error];
        //Get MIME type from the response
        MIMEType = [response MIMEType];
    } else {
        MIMEType = nil;
    }
    [self pluginResultado:MIMEType:command:YES];
}

- (void)pluginResultado:(NSString *)result :(CDVInvokedUrlCommand*) command :(BOOL) estado
{
    CDVPluginResult* pluginResult = nil;
    if (estado) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:result];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:result];
    }
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
- (void)getCode:(NSString *)urlarg :(CDVInvokedUrlCommand*) command
{
    NSURL *URL = [NSURL URLWithString:urlarg];
    NSURLRequest *request = [NSURLRequest requestWithURL:URL];
    
    [NSURLConnection
     sendAsynchronousRequest:request
     queue:[[NSOperationQueue alloc] init]
     completionHandler:^(NSURLResponse *response,
                         NSData *data,
                         NSError *error)
     {
         
         if ([data length] >0 && error == nil)
         {
             
              /* Get the Source - No funciona con CoudFlare */
             //NSString *newStr = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
             NSString *newStr = [[NSString alloc] initWithData:data encoding:NSASCIIStringEncoding];
             //NSLog(@"responseDataConvers: %@", newStr);
             [self pluginResultado:newStr:command:YES];
             /* Get the MimeType */
             //NSString* mimeType = [response MIMEType];
             //[self pluginResultado:mimeType:command:YES];
             
         }
         else if ([data length] == 0 && error == nil)
         {
             NSLog(@"Nothing was downloaded.");
             [self pluginResultado:@"Nothing was downloaded.":command:NO];
         }
         else if (error != nil){
             //NSLog(@"Error = %@", error);
             [self pluginResultado:error.localizedDescription:command:NO];
         }
         
     }];
}

- (void)getHtmlSource1:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString *urlarg = [command.arguments objectAtIndex:0];
    
    if (urlarg != nil && [urlarg length] > 0) {
        NSURL *url = [NSURL URLWithString:urlarg];
        // Deprecated NSString *webData= [NSString stringWithContentsOfURL:url];
        NSString *webData = [NSString stringWithContentsOfURL: url encoding:NSUTF8StringEncoding error:nil];
        //NSLog(@"%@",webData);
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:webData];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
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
