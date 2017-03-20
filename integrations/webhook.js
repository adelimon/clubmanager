var express = require('express');
var bodyParser = require('body-parser');

var app = express();
app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies

app.post('/webhook/rules', function (req, res) {

    var name = req.body.Field3;
    var email = req.body.Field4;
    var agreement = req.body.Field6;

    var emailValid = (email !== '');
    var agrees = ((agreement !== '') && ("i agree" === agreement.toLowerCase()));

    if (emailValid && agrees) {
        console.log(name + " " + email + " " + " agrees!");
    }
    
    var mysql      = require('mysql');  
    var connection = mysql.createConnection({  
        // lol like I'd check in real login info on a public github
        host     : 'localhost',  
        user     : 'root',          
        password : '',  
        database : 'clubmanager'  
    });  
    var sql = "update member set current_year_renewed = 1, last_modified_by = 'wufooWebhook' where email = " + mysql.escape(email);
    var firstName = name.split(" ")[0].toLowerCase();
    var lastName = name.split(" ")[1].toLowerCase();

    var backupSql = "update member set current_year_renewed = 1, last_modified_by = 'wufooWebhook' where lower(first_name) = '" + firstName + "' and lower(last_name) = '" + lastName + "'";

    var query = connection.query( sql, 
        function(err, results) {
            console.log(results.affectedRows + " rows updated for " + email);
            if (results.affectedRows === 0) {
                connection.query(backupSql,
                    function(err, backupResults) {
                        console.log(backupResults.affectedRows + " rows updated for " + name);
                    }
                );
            }
        }
    );

    res.send(200);
});

app.listen(3000, function () {
    console.log('wufoo webhook acknowledgement listener started');
});
