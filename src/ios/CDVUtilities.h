#import <Cordova/CDVPlugin.h>

@interface CDVUtilities : CDVPlugin {
  // Member variables go here.
}

// https://stackoverflow.com/questions/26047694/detecting-and-intercepting-video-playback-in-uiwebview
// https://github.com/joshkehn/JSMessageExample
- (void)getHtmlSource:(CDVInvokedUrlCommand*)command;
- (void)openURL:(CDVInvokedUrlCommand*)command;

@end