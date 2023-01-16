const crypto = require("crypto");
const { Worker } = require('worker_threads')

class Block {
    //indeks, podatek, casovna znacka, hash bloka, hash prejsnjega bloka
    constructor(timestamp = "", data = "", index, nonceStart) {
        this.index = index
        this.data = data;
        this.timestamp = timestamp;
        this.hash = this.getHash();
        this.prevHash = "0";
        this.difficulty=0;
        this.nonce=nonceStart;   //zacetna vrednost
        this.miner="0000";
    }
    getHash() {
        return crypto.createHash("sha256").update(this.index + this.data + this.timestamp + this.prevHash + this.difficulty + this.nonce).digest("hex");
    }

    //proof of work + rudarjenje
    mine(difficulty) {        
        this.difficulty = difficulty;
        //naredimo string, ki vsebuje toliko nicel kot je difficulty
        var leadingZeros = Array(difficulty + 1).join("0");
        this.hash = this.getHash(); //izracunamo hash....ce bi slucajno bil pri nonce = 0 pravi hash
        //dokler se hash ne zacne s tolikomi niclami, kot je difficulty.....
        while (!this.hash.startsWith(leadingZeros)) {
            // Spremenimo zeton, da dobimo novi hash
            this.nonce++;
            // Ponovno izracunamo hash
            this.hash = this.getHash();
        }
    }
}
module.exports = Block;

/*
const maxNum=Number.MAX_VALUE;
        const numberOfThreads=4;
        var lengthInterval = Math.floor(maxNum / numberOfThreads);
        var start, end;

        for(var i = 0; i < numberOfThreads; i++)
        {   
            start = i * lengthInterval;
            end = (i + 1) * lengthInterval - 1;
            
        }

        var worker = new Worker('./miner_worker.js');
        worker.on('message', (msg) => { 
            console.log(msg);
        });
        worker.postMessage({start: 0,
                            end: maxNum,
                            block: this,
                            leadingZeros: leadingZeros
        });
*/