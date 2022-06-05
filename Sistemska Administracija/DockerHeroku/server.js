'use strict';

const mongoose = require('mongoose')
const express = require('express');
const app = express();

// Constants
var PORT = process.env.PORT || 3000;
const HOST = '0.0.0.0';

var mongoDB= (process.env.MongoDBURL || "mongodb+srv://eriklasic:O9zI3m70JhVt53JP@kolesa.3xx3b.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");

const connectionParams={
    //useNewUrlParser: true,
    //useCreateIndex: true,
    //useUnifiedTopology: true
}
mongoose.connect(mongoDB,connectionParams)
    .then( () => {
        console.log('Connected to the database ')
    })
    .catch( (err) => {
        console.error(`Error connecting to the database. n${err}`);
    })
        const visitorSchema = new mongoose.Schema({
          name: String,
          count: Number
        })
        // Creating Visitor Table in visitCounterDB
        const Visitor = mongoose.model("Visitor",visitorSchema)

        app.get('/', async function(req,res){
          
            // Storing the records from the Visitor table
            let visitors = await Visitor.findOne({name: 'localhost'});
          
            // If the app is being visited first
            // time, so no records
            if(visitors == null) {
                  
                // Creating a new default record
                const beginCount = new Visitor({
                    name : 'localhost',
                    count : 1
                });
          
                // Saving in the database
                beginCount.save();
          
                // Sending thee count of visitor to the browser
                res.send(`<h2>Counter: `+1+'</h2>\n<h1>Hello World<h1>');
          
                // Logging when the app is visited first time
                console.log("First visitor arrived");
            }
            else{
                  
                // Incrementing the count of visitor by 1
                visitors.count += 1;
          
                // Saving to the database
                visitors.save();
          
                // Sending thee count of visitor to the browser
                res.send(`<h2>Counter: `+visitors.count+'</h2><h1>Hello World<h1>');
          
                // Logging the visitor count in the console
                console.log("visitor arrived: ",visitors.count);
            }
        })

app.listen(PORT, HOST);
console.log(`Running on http://${HOST}:${PORT}`);