var fs = require('fs'),
    readline = require('readline');

var rd = readline.createInterface({
    input: fs.createReadStream('/Users/adelimon/desktop/members.csv'),
    output: process.stdout,
    terminal: false
});

function c(s) {
    return s+",";
}

function q(s) {
    return "'" + s + "'";
}

function cq(s) {
    return c(q(s));
}
console.log( "delete from clubmanager.member where id > 0;");
rd.on('line', function(line) {
    var a = line.split(",");
    var insertStatement = "insert into clubmanager.member "
    + "(id, view_online, first_name, last_name, address, city, state, zip, phone, email, birthday, occupation, date_joined) values ("
    + c(a[0]) + c(a[1]);
    for (var index = 2; index < 10; index++) {
        insertStatement += cq(a[index]);
    }
    insertStatement += "STR_TO_DATE("+ q(a[10]) +", '%m/%d/%Y'),"
    insertStatement += cq(a[11]);
    insertStatement += "STR_TO_DATE("+ q(a[12]) +", '%m/%d/%Y'));"
    console.log(insertStatement);
});
