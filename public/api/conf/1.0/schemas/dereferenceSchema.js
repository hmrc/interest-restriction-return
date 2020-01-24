const $RefParser = require("json-schema-ref-parser");
const fs = require("fs");

var args = process.argv.slice(2);

args.forEach(function(schemaName) {
    $RefParser.dereference(schemaName, (err, schema) => {
        if (err) {
            console.error(err);
        } else {
            fs.writeFile("dereferenced/" + schemaName, JSON.stringify(schema, null, 4), function (err) {
                if (err) throw err;
                console.log("Dereferences JSON schema file created");
            });
        }
    });
});
