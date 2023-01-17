var SensorTempModel = require('../models/sensorTempModel.js');

module.exports = {

    list: function (req, res) {
        SensorTempModel.find(function (err, temps) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting temperature.',
                    error: err
                });
            }

            return res.json(temps);
        });
    },

    show: function (req, res) {
        var id = req.params.id;

        SensorTempModel.findOne({_id: id}, function (err, temp) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting temperature.',
                    error: err
                });
            }

            if (!event) {
                return res.status(404).json({
                    message: 'No such temperature'
                });
            }

            return res.json(temp);
        });
    },

    insert: function (req, res) {
        SensorTempModel.findOne({temperature: req.body.temperature}, function (err, data) {
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
                // event location
                var objGeometry = {
                    type: 'Point',
                    coordinates: [parseFloat(req.body.longitude), parseFloat(req.body.latitude)]
                }
                // create new event
                var temp = new SensorTempModel({
                    id : require("crypto").randomBytes(64).toString('hex'),
                    temperature : req.body.temperature,
                    frequency : req.body.frequency,
                    geometry : objGeometry,
                });
                // save event
                temp.save(function (err, data) {
                    if (err) {
                        return res.status(500).json({
                            success: false,
                            message: "Error saving event",
                            error: err
                        });
                    }
                    return res.json({
                        success: true,
                        inserted: true,
                        event: data
                    })
                });
            } 
        });
    }
};
