var request = require("request");

request({
  uri: "https://palmyraracing.wufoo.com/api/v3/forms/m8zk4qh0ojkqj4/entries.json?sort=EntryId&sortDirection=DESC&pageSize=10",
  method: "GET",
  auth: {
    'username': 'BNV6-AGEA-P0I4-NFP2',
    'password': 'LOL SRY',
    'sendImmediately': false
  }
}, function(error, response, body) {
    var entries = JSON.parse(body);
    var entryList = entries["Entries"];
    console.log("set autocommit=0;")
    for (var index = 0; index < entryList.length; index++) {
        var entry = entryList[index];
        var name = entry.Field3;
        
        var email = entry.Field4.toLowerCase();
        var agreement = entry.Field6.toLowerCase();
        var insurance = entry.Field8;
        var firstName = name.split(" ")[0].toLowerCase();
        var lastName = name.split(" ")[1].toLowerCase();

        // check for agreement
        if (agreement.indexOf("i agree") > -1) {
            var updateViaEmail = "update member set current_year_renewed = 1 where lower(email) = '"+email+"';";
            console.log(updateViaEmail);
            var updateViaName = "update member set current_year_renewed = 1 where lower(first_name) ='"+firstName+"' and lower(last_name) = '"+lastName+"';";
            console.log(updateViaName);
        }
        

    }

});
