var BikeshedModel = require('../models/bikeShedModel.js');

module.exports = {

    list: function (req, res) {
        BikeshedModel.find(function (err, bikeSheds) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    message: 'Error when getting bikeShed.',
                    error: err
                });
            }

            return res.json(bikeSheds);
        });
    },

    show: function (req, res) {
        var id = req.params.id;

        BikeshedModel.findOne({_id: id}, function (err, bikeShed) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    message: 'Error when getting bikeShed.',
                    error: err
                });
            }

            if (!bikeShed) {
                return res.status(404).json({
                    success: false,
                    message: 'No such bikeShed'
                });
            }

            return res.json(bikeShed);
        });
    },

    create: function (req, res) {
        var objGeometry = {
            type: 'Point', 
            coordinates: [parseFloat(req.body.longitude), parseFloat(req.body.latitude)]
        }
        var bikeShed = new BikeshedModel({
			providerName : req.body.providerName,
			providerLink : req.body.providerLink,
			address : req.body.address,
			quantity : req.body.quantity,
            geometry : objGeometry
        });

        bikeShed.save(function (err, bikeShed) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    message: 'Error when creating bikeShed',
                    error: err
                });
            }

            return res.status(201).json(bikeShed);
        });
    },

    removeAll: function (req, res) {
        BikeshedModel.remove({}, function (err, bikeShed) {
            if (err) {
                return res.status(500).json({
                    success: false,
                    message: 'Error when deleting the stand.',
                    error: err
                });
            }

            return res.status(204).json({success: true});
        });
    }

    /*
        update: function (req, res) {
        var id = req.params.id;

        BikeshedModel.findOne({_id: id}, function (err, bikeShed) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting bikeShed',
                    error: err
                });
            }

            if (!bikeShed) {
                return res.status(404).json({
                    message: 'No such bikeShed'
                });
            }

            bikeShed.providerName = req.body.providerName ? req.body.providerName : bikeShed.providerName;
			bikeShed.providerLink = req.body.providerLink ? req.body.providerLink : bikeShed.providerLink;
			bikeShed.address = req.body.address ? req.body.address : bikeShed.address;
			bikeShed.quantity = req.body.quantity ? req.body.quantity : bikeShed.quantity;
			
            bikeShed.save(function (err, bikeShed) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating bikeShed.',
                        error: err
                    });
                }

                return res.json(bikeShed);
            });
        });
    },
    */
};
