cordova.define("cordova-plugin-utilities.utilities", function(require, exports, module) {
var exec = require('cordova/exec');

exports.getHtmlSource = function (url, success, error) {
    exec(success || null, error || null, "Utilities", "getHtmlSource", [url]);
};

exports.getMimeType = function (url, success, error) {
    exec(success || null, error || null, "Utilities", "getMimeType", [url]);
};

exports.openURL = function (url, success, error) {
    exec(success || null, error || null, "Utilities", "openURL", [url]);
};
});
