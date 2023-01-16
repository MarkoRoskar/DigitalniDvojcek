const Block = require("./block.js");
const crypto = require("crypto");

//vrne hash dolocenega bloka
function calculateHash(block) {
    return crypto.createHash("sha256").update(block.index + block.data + block.timestamp + block.prevHash + block.difficulty + block.nonce).digest("hex");
}

class Blockchain {
    constructor() {
        this.chain = [new Block(Date.now().toString(), "Data 0", 0)];
        this.diffAdjustInterval = 6;    //na vsakih 5 blokov
        this.difficulty = 1;
        this.blockGenTime = 90;
    }

    //vrne zadnji blok v verigi
    getLastBlock() {
        return this.chain[this.chain.length - 1];
    }

    addBlock(currentblock) {
        currentblock.prevHash = this.getLastBlock().hash;
        var prevBlock = this.getLastBlock();
        //validacija bloka
            //index trenutnega bloka mora biti za 1 večji od prejšnjega
            //zgoscene vrednosti morajo biti ustrezne
            //hash mora biti ustrezen
            //blok je ustrezen, če je njegova časovna značka
                ///največ 1 minuto večja od našega trenutnega časa
            //blok v verigi je ustrezen če je njegova časovna značka 
                ///največ 1 minuto manjša od časovne značke prejšnjega bloka

        var leadingZeros = Array(currentblock.difficulty + 1).join("0");
        if (currentblock.prevHash == prevBlock.hash 
                && (prevBlock.index + 1) == currentblock.index 
                && calculateHash(currentblock) == currentblock.hash 
                && currentblock.hash.startsWith(leadingZeros)
                && currentblock.timestamp <= Date.now()+60000
                && currentblock.timestamp >= prevBlock.timestamp-60000){
            this.chain.push(currentblock);

            if (this.chain.length >= this.diffAdjustInterval) {
                var prevAdjustmentBlock = this.chain[this.chain.length - this.diffAdjustInterval];  //blok pred spremembo
                var timeExpected = this.blockGenTime * this.diffAdjustInterval; //pricakovan cas
                var timeTaken = currentblock.timestamp - prevAdjustmentBlock.timestamp  //porabljen cas

                if (timeTaken < (timeExpected / 2)) {   //ce smo potrebovali manj kot polovico pricakovanega casa se tezavnost poveca
                    this.difficulty++;
                }
                else if (timeTaken > (timeExpected * 2)) {  //ce smo potrebovali vec kot polovico pricakovanega casa se tezvanost zmanjsa.
                    if (this.difficulty > 5) {
                        this.difficulty--;
                    }
                }
            }
            return true;    //vrne true, ce smo blok uspeli validirati
        }
        return false;   //vrne false, ce bloka nismo uspeli validirati
    }

    validateAddBlock(currentblock) {    //ne spreminja tezavnosti
        currentblock.prevHash = this.getLastBlock().hash;
        var prevBlock = this.getLastBlock();
        //validacija bloka
            //index trenutnega bloka mora biti za 1 večji od prejšnjega
            //zgoscene vrednosti morajo biti ustrezne
            //hash mora biti ustrezen
        var leadingZeros = Array(currentblock.difficulty + 1).join("0");
        if (currentblock.prevHash == prevBlock.hash 
                && (prevBlock.index + 1) == currentblock.index 
                && calculateHash(currentblock) == currentblock.hash 
                && currentblock.hash.startsWith(leadingZeros)
                && currentblock.timestamp <= Date.now()+60000
                && currentblock.timestamp >= prevBlock.timestamp-60000){
            this.chain.push(currentblock);
            return true;    //vrne true, ce smo blok uspeli validirati
        }
        return false;   //vrne false, ce bloka nismo uspeli validirati
    }
}

module.exports = Blockchain;