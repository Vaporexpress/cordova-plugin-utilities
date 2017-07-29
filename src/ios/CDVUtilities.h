#import <Cordova/CDVPlugin.h>

@interface CDVUtilities : CDVPlugin
{
  // Member variables go here.
}

// https://stackoverflow.com/questions/26047694/detecting-and-intercepting-video-playback-in-uiwebview
// https://github.com/joshkehn/JSMessageExample
/* Get Html Source (Don´t work with sites under CloudFlare) */
- (void)getHtmlSource:(CDVInvokedUrlCommand*)command;
/* Get MimeType from URL */
- (void)getMimeType:(CDVInvokedUrlCommand*)command;
/* Open External URL (like Android Intent) */
- (void)openURL:(CDVInvokedUrlCommand*)command;

@end
