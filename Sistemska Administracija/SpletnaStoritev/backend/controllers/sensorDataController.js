var SensordataModel = require('../models/sensorDataModel.js');

/**
 * sensorDataController.js
 *
 * @description :: Server-side logic for managing sensorDatas.
 */
module.exports = {

    /**
     * sensorDataController.list()
     */
    list: function (req, res) {
        SensordataModel.find(function (err, sensorData) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting sensorData.',
                    error: err
                });
            }

            return res.json(sensorData);
        });
    },

    /**
     * sensorDataController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        SensordataModel.findOne({_id: id}, function (err, sensorData) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting sensorData.',
                    error: err
                });
            }

            if (!sensorData) {
                return res.status(404).json({
                    message: 'No such sensorData'
                });
            }

            return res.json(sensorData);
        });
    },

    /**
     * sensorDataController.create()
     */
    insert: function (req, res) {
        SensordataModel.findOne({bikes: parseInt(req.body.bikes)}, function (err, data) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    error: err
                });
            }
            if (data) {
                return res.status(500).json({
                    inserted: false,
                    message: "data already inserted",
                    data: data
                });
            }
            else {
                // location
                var objGeometry = {
                    type: 'Point',
                    coordinates: [parseFloat(req.body.longitude), parseFloat(req.body.latitude)]
                }
                // create new data
                var data = new SensordataModel({
                    id : require("crypto").randomBytes(64).toString('hex'),
                    bikes : req.body.bikes,
                    people : req.body.people,
                    geometry: objGeometry,
                    frequency : req.body.frequency
                });
                // save data
                data.save(function (err, data) {
                    if (err) {
                        return res.status(500).json({
                            success: false,
                            message: "Error saving sensor data",
                            error: err
                        });
                    }
                    return res.json({
                        success: true,
                        inserted: true,
                        data: data
                    })
                });
            }
        });

        /*var sensorData = new SensordataModel({
			bikes : req.body.bikes,
			people : req.body.people,
			time : req.body.time
        });

        sensorData.save(function (err, sensorData) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating sensorData',
                    error: err
                });
            }

            return res.status(201).json(sensorData);
        });*/
    },

    /**
     * sensorDataController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        SensordataModel.findOne({_id: id}, function (err, sensorData) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting sensorData',
                    error: err
                });
            }

            if (!sensorData) {
                return res.status(404).json({
                    message: 'No such sensorData'
                });
            }

            sensorData.bikes = req.body.bikes ? req.body.bikes : sensorData.bikes;
			sensorData.people = req.body.people ? req.body.people : sensorData.people;
			sensorData.time = req.body.time ? req.body.time : sensorData.time;
			
            sensorData.save(function (err, sensorData) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating sensorData.',
                        error: err
                    });
                }

                return res.json(sensorData);
            });
        });
    },

    /**
     * sensorDataController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        SensordataModel.findByIdAndRemove(id, function (err, sensorData) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the sensorData.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
