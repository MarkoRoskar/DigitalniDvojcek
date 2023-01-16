//node index.js --port 3001 --p2p_port 3002
/* VHODNI ARGUMENTI */
var minimist = require('minimist')
var args = minimist(process.argv.slice(2), {
  port: 'port',
  p2p_port: 'p2p_port'
})

if(args.port == undefined){
  console.log("node.js --port --p2p_port");
  process.exit(1);
}

if(args.p2p_port == undefined){
  console.log("node.js --port --p2p_port");
  process.exit(1);
}

/* P2P client */
var c_p2p_io = require('socket.io-client');

/* P2P server */
const s_p2p_server = require('http').Server();
const s_p2p_io =require('socket.io')(s_p2p_server);
var s_p2p = require('socket.io-p2p-server').Server;
s_p2p_io.use(s_p2p);
s_p2p_server.listen(args.p2p_port);

/* Server za GUI */
const express = require('express');
var app = express();
const http = require('http').Server(app);
const io = require('socket.io')(http);

/* Listening to GUI */
http.listen(args.port, () => {
    console.log(`Socket.IO server running at http://localhost:${args.port}/`);
});

//vzpostavimo spletno stran, ob requestu HTTP GET
app.get('/', (req, res) => {
    res.sendFile(__dirname + '/index.html');
});

const { Worker } = require('worker_threads')

const Blockchain = require("./blockchain.js")
var blockchain = new Blockchain();  //tukaj se ustvari prvi blok

//calculate hash
const crypto = require("crypto");

function isRecivedBlockchainBetter(blockchainHome, blockChainRecived) {
    var comulativeDifficultyRecived=0;
    for(var i = 1; i < blockChainRecived.chain.length; i++){
        comulativeDifficultyRecived+=Math.pow(2,blockChainRecived.chain[i].difficulty);
    }
    var comulativeDifficultyHome=0;
    for(var i = 1; i < blockchainHome.chain.length; i++){
        comulativeDifficultyHome+=Math.pow(2,blockchainHome.chain[i].difficulty);
    }
    console.log(comulativeDifficultyHome);
    console.log(comulativeDifficultyRecived);
    return comulativeDifficultyRecived > comulativeDifficultyHome;
}

var workers = new Map()

io.on('connection', (socket) => {

    socket.on('mine', () => {
        socket.emit("blockchain", blockchain);  //posljemo prvi blok
        
        workers = new Map()
        var nonceRange=Math.floor(Number.MAX_SAFE_INTEGER / minerCount)
        var nonceStart=0
        for (var i = 0; i < workerCount; i++) {
            const worker = new Worker('./mine.js');
            worker.postMessage({
                block: blockchain.getLastBlock(),
                difficulty: blockchain.difficulty,
                miner: args.port,
                nonceStart: nonceStart
            });
            worker.on("message", async (data) => {
                var block = data.block;
                if (blockchain.addBlock(block)) {
                    console.log("New block added");
                    socket.emit("added", "Dodan nov blok: " + block.hash);
                    socket.emit("blockchain", blockchain);
                } else {
                    console.log("New block not added");
                    socket.emit("notadded", "Nov blok ni bil dodan: " + JSON.stringify(block));
                }
                worker.postMessage({
                    block: blockchain.getLastBlock(),
                    difficulty: blockchain.difficulty,
                    miner: args.port
                });
            });
        
            worker.on('error', (msg) => { console.log(msg) });
            worker.on('exit', (code) => {
                if (code !== 0)
                    console.log(code);
            })
            nonceStart += nonceRange
        }
    })

    socket.on("connect_port", async(port) => {
        var c_p2p_socket = c_p2p_io("http://localhost:" + port);
        c_p2p_socket.emit("message", "P2P_client_connected");
        c_p2p_socket.on("blockchain", (recivedBlockchain) => {
            console.log("CHECKING RECIVED BLOCKCHAIN");
            socket.emit("newblockchain");
            //ce je prejeta veriga boljsa:
           if(isRecivedBlockchainBetter(blockchain, recivedBlockchain)){
               var OKblockchain = true;
               var emptyBlockchain = new Blockchain();
               //preverimo ce je celotna veriga ok
               if(recivedBlockchain.chain[0].index == 0 && recivedBlockchain.chain[0].prevHash == "0"){
                   emptyBlockchain.chain[0] = recivedBlockchain.chain[0];
                    for(var i = 1; i < recivedBlockchain.chain.length; i++){
                        if(!emptyBlockchain.validateAddBlock(recivedBlockchain.chain[i])){
                            OKblockchain = false;
                            break;
                        }
                    }
                }
               else{
                   //ce prvi blok ni ok
                   OKblockchain = false;
               }
               if(OKblockchain){
                   //ce je veriga ok
                   console.log("RECIVED BLOCKCHAIN OK");
                   blockchain.chain = recivedBlockchain.chain;
                   socket.emit("accepted");
                   socket.emit("blockchain", blockchain);
                   c_p2p_socket.emit("send_difficulty");
                   c_p2p_socket.on("difficulty_send", (difficulty)=>{
                        blockchain.difficulty = difficulty;
                   });
               }else{
                   //ce veriga ni ok
                   console.log("RECIVED BLOCKCHAIN NOT OK");
                   socket.emit("notaccepted");

               }
            //ce je trenutna veriga boljsa:
           }else if(!isRecivedBlockchainBetter(blockchain, recivedBlockchain)){
               console.log("KEEPING HOME BLOCKCHAIN");
               socket.emit("notaccepted");
            //ce sta  verigi enako dobri
           }else{
               console.log("SAME LENGTH....LETS WAIT");
               socket.emit("same_length");
           }
        });
    });

    s_p2p_io.on("connection", (p2p_socket) =>{
        console.log("connected");
        p2p_socket.on("message", (msg) => {
            console.log(msg);
        });

        var interval_worker = new Worker('./interval.js');
        interval_worker.on('message', async () => {
            console.log("chain sent");
            p2p_socket.emit('blockchain', blockchain);
            p2p_socket.on("send_difficulty", ()=>{
                p2p_socket.emit("difficulty_send", blockchain.difficulty);
            })
        });
    });
});