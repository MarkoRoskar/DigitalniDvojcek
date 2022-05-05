var MbajkModel = require('../models/MBajkModel.js');

module.exports = {

    list: function (req, res) {
        MbajkModel.find().select("-historyAvailabitilities").exec(function (err, MBajks) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    message: "Error when getting MBikes",
                    error: err
                });
            }
            return res.json(MBajks);
        });
    },

    show: function (req, res) {
        var id = req.params.id;
        MbajkModel.findOne({_id: id}, function (err, MBajk) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    message: "Error when getting MBike",
                    error: err
                });
            }

            if (!MBajk) {
                return res.status(404).json({
                    success: false,
                    message: "Error no such MBajk"
                });
            }

            return res.json(MBajk);
        });
    },

    insert: function (req, res) {
        MbajkModel.findOne({number: parseInt(req.body.number)}, function (err, MBajk) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    error: err
                });
            }
            if(MBajk){
                if(MBajk.lastUpdateSensor != req.body.lastUpdateSensor){
                    var objCurrentAvail = {
                        'bikesAvailable' : parseInt(req.body.bikes),
                        'parkSpots' : parseInt(req.body.capacity),
                        'dateTimeAdded' : Date.now()
                    }
                    MBajk.currentAvailabilities = objCurrentAvail;
                    MBajk.lastUpdateSensor = req.body.lastUpdateSensor;
                    MBajk.historyAvailabitilities.push(objCurrentAvail);
                    MBajk.save(function (err, MBajk) {
                        if (err) {
                            return res.status(500).json({
                                success: false,
                                message: "Error when updating MBike",
                                error: err
                            });
                        }
                        return res.json({success: true, 
                            inserted: false, 
                            updated: true, 
                            MBajk: MBajk
                        })
                    });
                }else{
                    return res.json({success: true, 
                            inserted: false, 
                            updated: false, 
                            MBajk: MBajk
                    })
                }
            }else{
                var objGeometry = {
                    type: 'Point', 
                    coordinates: [parseFloat(req.body.longitude), parseFloat(req.body.latitude)]
                }
                var objCurrentAvail = {
                    'bikesAvailable' : parseInt(req.body.bikes),
                    'parkSpots' : parseInt(req.body.capacity)
                }
                var objHistoryAvailabitilities = [
                    {
                        'bikesAvailable' : parseInt(req.body.bikes),
                        'parkSpots' : parseInt(req.body.capacity),
                        'dateTimeAdded' : Date.now()
                    }
                ]
                
                var MBajk = new MbajkModel({
                    number : parseInt(req.body.number),
                    name : req.body.name,
                    address : req.body.address,
                    geometry : objGeometry,
                    currentStatus : req.body.status,
                    lastUpdateSensor : req.body.lastUpdateSensor,
                    currentAvailabilities : objCurrentAvail,
                    historyAvailabitilities : objHistoryAvailabitilities
                });
                
                MBajk.save(function (err, MBajk) {
                    if (err) {
                        return res.status(500).json({
                            success: false,
                            message: "Error when saving MBike",
                            error: err
                        });
                    }
                    return res.json({success: true, 
                        inserted: true, 
                        updated: false, 
                        MBajk: MBajk
                    })
                });
            }
        });
    },

    /*
    update: function (req, res) {
        var id = req.params.id;

        MbajkModel.findOne({_id: id}, function (err, MBajk) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting MBajk',
                    error: err
                });
            }

            if (!MBajk) {
                return res.status(404).json({
                    message: 'No such MBajk'
                });
            }

            MBajk.number = req.body.number ? req.body.number : MBajk.number;
			MBajk.name = req.body.name ? req.body.name : MBajk.name;
			MBajk.address = req.body.address ? req.body.address : MBajk.address;
			MBajk.currentStatus = req.body.currentStatus ? req.body.currentStatus : MBajk.currentStatus;
			MBajk.number = req.body.number ? req.body.number : MBajk.number;
			
            MBajk.save(function (err, MBajk) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating MBajk.',
                        error: err
                    });
                }

                return res.json(MBajk);
            });
        });
    },
    remove: function (req, res) {
        var id = req.params.id;

        MbajkModel.findByIdAndRemove(id, function (err, MBajk) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the MBajk.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
    */
};
