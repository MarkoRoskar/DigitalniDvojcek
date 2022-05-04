'use strict';

const mongoose = require('mongoose')

const url = `mongodb+srv://eriklasic:O9zI3m70JhVt53JP@kolesa.3xx3b.mongodb.net/Kolesa?retryWrites=true&w=majority String`;

const connectionParams={
    //useNewUrlParser: true,
    //useCreateIndex: true,
    //useUnifiedTopology: true
}
mongoose.connect(url,connectionParams)
    .then( () => {
        console.log('Connected to the database ')
    })
    .catch( (err) => {
        console.error(`Error connecting to the database. n${err}`);
    })

const express = require('express');

// Constants
const PORT = 3000;
const HOST = '0.0.0.0';

// App
const app = express();
app.get('/', (req, res) => {
  res.send('Hello world');
});

app.listen(PORT, HOST);
console.log(`Running on http://${HOST}:${PORT}`);