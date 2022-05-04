var BikeshedModel = require('../models/bikeShedModel.js');

/**
 * bikeShedController.js
 *
 * @description :: Server-side logic for managing bikeSheds.
 */
module.exports = {

    /**
     * bikeShedController.list()
     */
    list: function (req, res) {
        BikeshedModel.find(function (err, bikeSheds) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting bikeShed.',
                    error: err
                });
            }

            return res.json(bikeSheds);
        });
    },

    /**
     * bikeShedController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        BikeshedModel.findOne({_id: id}, function (err, bikeShed) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting bikeShed.',
                    error: err
                });
            }

            if (!bikeShed) {
                return res.status(404).json({
                    message: 'No such bikeShed'
                });
            }

            return res.json(bikeShed);
        });
    },

    /**
     * bikeShedController.create()
     */
    create: function (req, res) {
        var bikeShed = new BikeshedModel({
			providerName : req.body.providerName,
			providerLink : req.body.providerLink,
			address : req.body.address,
			quantity : req.body.quantity
        });

        bikeShed.save(function (err, bikeShed) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating bikeShed',
                    error: err
                });
            }

            return res.status(201).json(bikeShed);
        });
    },

    /**
     * bikeShedController.update()
     */
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

    /**
     * bikeShedController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        BikeshedModel.findByIdAndRemove(id, function (err, bikeShed) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the bikeShed.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
