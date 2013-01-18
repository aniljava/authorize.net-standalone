authorize.net-standalone
========================

simple authorize.net aim without any external dependencies, in one java class.

Like [stripe](http://stripe.com), authorize.net provides simple HTTP Post
based charging of the credit card. This project is a single java class that
provides short-hand methods to make a such post requests.

Usage
-----
Charging a credit card

    //Map of login settings
    Map<String, String> settings = Authorize.map(
    			"x_Delim_Data", "TRUE",
    			"x_login", x_login,
    			"x_tran_key", x_tran_key,
    			"x_version","3.1"
    			);