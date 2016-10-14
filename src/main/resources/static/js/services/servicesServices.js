
angular.module('app').factory('serviceAPI', function(http, $q) {

    var services = {};

    services.getNetworks = function() {
        var deferred = $q.defer();
        http.get(url + '/networks').success(function(data, status) {
            deferred.resolve(data);
        });
        return deferred.promise;
    };
    /*
     * 
     * @returns {List} List of Services
     */
    services.getServices = function() {
        var deferred = $q.defer();
        http.get(url + '/services').success(function(data, status) {
            deferred.resolve(data);
        });
        return deferred.promise;
    };

    /* Random number
     * 
     * @returns {Number}
     */
    services.getRandom = function() {
        return Math.floor((Math.random() * 100) + 1);
    };
    /** return an array of keys by value
     *
     * @param {type} obj
     * @param {type} value
     * @returns {Array}
     */
    services.returnKeys = function returnKeys(obj, value) {
        var keys = [];
        _.each(obj, function(val, key) {
            if (val === value) {
                keys.push(key);
            }
        });
        return keys;
    };
    
    return services;
});