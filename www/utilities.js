var argscheck = require('cordova/argscheck'),
    utils = require('cordova/utils'),
    exec = require('cordova/exec');


var Utilities = function () {};

/**
 * getHtmlSource
 *
 * @param {Function} successCallback The function to call when the heading data is available
 * @param {Function} errorCallback The function to call when there is an error getting the heading data. (OPTIONAL)
 */
Utilities.prototype.getHtmlSource = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Utilities.getHtmlSource', arguments);
    exec(successCallback, errorCallback, "Utilities", "getHtmlSource", [url]);
};

/**
 * getMimeType
 *
 * @param {Function} successCallback The function to call when the heading data is available
 * @param {Function} errorCallback The function to call when there is an error getting the heading data. (OPTIONAL)
 */
Utilities.prototype.getMimeType = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Utilities.getMimeType', arguments);
    exec(successCallback, errorCallback, "Utilities", "getMimeType", [url]);
};

/**
 * openURL
 *
 * @param {Function} successCallback The function to call when the heading data is available
 * @param {Function} errorCallback The function to call when there is an error getting the heading data. (OPTIONAL)
 */
Utilities.prototype.openURL = function(successCallback, errorCallback) {
    argscheck.checkArgs('fF', 'Utilities.openURL', arguments);
    exec(successCallback, errorCallback, "Utilities", "openURL", [url]);
};

module.exports = Utilities;

