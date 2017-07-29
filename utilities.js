var exec = require('cordova/exec');

var Utilities = {};

Utilities.getHtmlSource = function (url, success, error) {
    exec(success || null, error || null, "Utilities", "getHtmlSource", [url]);
};

Utilities.getMimeType = function (url, success, error) {
    exec(success || null, error || null, "Utilities", "getMimeType", [url]);
};

Utilities.openURL = function (url, success, error) {
    exec(success || null, error || null, "Utilities", "openURL", [url]);
};

module.exports = Utilities;
