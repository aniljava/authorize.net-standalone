authorize.net-standalone
========================

simple authorize.net aim without any external dependencies, in one java class.

Like [stripe](http://stripe.com), authorize.net provides simple HTTP Post
based charging of the credit card. This project is a single java class that
provides short-hand methods to make a such post requests.

Usage
-----
Charging a credit card

    //Map of login settings, map method is a shorthand to create java Map
    Map<String, String> settings = Authorize.map(
    			"x_Delim_Data", "TRUE",
    			"x_login", x_login, //Get from authorize.net
    			"x_tran_key", x_tran_key, // Get from authorize.net
    			"x_version","3.1"
    			);

    //actual transaction
    Map<String, String> transaction = Authorize.map(settings, 
    			"x_type"        , "AUTH_CAPTURE",
    			"x_amount"      , "27.00", 
    			"x_card_num"    , "4111111111111111", 
    			"x_exp_date"    , "1215", 
    			"x_card_code"   , "000", 
    			"x_invoice_num" , "001", 
    			"x_address"     , "123 TEST ST", 
    			"x_zip"         , "75244"
    			);
    //CHARGE
    Map result = Authorize.process(Authorize.SANDBOX_URL, transaction);	
    
    if(result.get("Response Code").equals("1")){
    	System.out.println("CHARGED !");
    }


Notes 
----- 

- The direct response with **|** delimiter is required. Authorize.net web control panel has option to change delimiter to pipe.
- either copy paste Authroize.java file, or jar file after `mvn package` or use maven after `mvn install` to use.

Resources
---------
- [Authorize.Net Advance Integration Method (AIM)](http://developer.authorize.net/api/aim/)
- [AIM Manual PDF](http://www.authorize.net/support/AIM_guide.pdf)