var BikepathModel = require('../models/bikePathModel.js');

module.exports = {

    list: function (req, res) {
        BikepathModel.find(function (err, bikePaths) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    message: 'Error when getting bikePath.',
                    error: err
                });
            }

            return res.json({success: true, "BikePaths": bikePaths});
        });
    },

    show: function (req, res) {
        var id = req.params.id;

        BikepathModel.findOne({_id: id}, function (err, bikePath) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    message: 'Error when getting bikePath.',
                    error: err
                });
            }

            if (!bikePath) {
                return res.status(404).json({
                    success: false,
                    message: 'No such bikePath'
                });
            }

            return res.json({"BikePath": bikePath});
        });
    },

    create: function (req, res) {
        var objGeometry = {
            type: 'LineString', 
            coordinates: [parseFloat(req.body.longitude), parseFloat(req.body.latitude)]
        }
        var bikePath = new BikepathModel({
			name : req.body.name,
			parkSpots : req.body.parkSpots,
            geometry: objGeometry
        });

        bikePath.save(function (err, bikePath) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    message: 'Error when creating bikePath',
                    error: err
                });
            }

            return res.status(201).json({success: true, "BikePath": bikePath});
        });
    },

    removeAll: function (req, res) {
        StandModel.remove({}, function (err, BikePaths) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    message: 'Error when deleting bikePaths.',
                    error: err
                });
            }

            return res.status(204).json({success: true});
        });
    }
};
