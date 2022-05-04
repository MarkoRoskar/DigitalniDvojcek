var MbajkavailabilitiesModel = require('../models/MBajkAvailabilitiesModel.js');

/**
 * MBajkAvailabilitiesController.js
 *
 * @description :: Server-side logic for managing MBajkAvailabilitiess.
 */
module.exports = {

    /**
     * MBajkAvailabilitiesController.list()
     */
    list: function (req, res) {
        MbajkavailabilitiesModel.find(function (err, MBajkAvailabilitiess) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting MBajkAvailabilities.',
                    error: err
                });
            }

            return res.json(MBajkAvailabilitiess);
        });
    },

    /**
     * MBajkAvailabilitiesController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        MbajkavailabilitiesModel.findOne({_id: id}, function (err, MBajkAvailabilities) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting MBajkAvailabilities.',
                    error: err
                });
            }

            if (!MBajkAvailabilities) {
                return res.status(404).json({
                    message: 'No such MBajkAvailabilities'
                });
            }

            return res.json(MBajkAvailabilities);
        });
    },

    /**
     * MBajkAvailabilitiesController.create()
     */
    create: function (req, res) {
        var MBajkAvailabilities = new MbajkavailabilitiesModel({
			bikesAvailable : req.body.bikesAvailable,
			parkSpots : req.body.parkSpots
        });

        MBajkAvailabilities.save(function (err, MBajkAvailabilities) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating MBajkAvailabilities',
                    error: err
                });
            }

            return res.status(201).json(MBajkAvailabilities);
        });
    },

    /**
     * MBajkAvailabilitiesController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        MbajkavailabilitiesModel.findOne({_id: id}, function (err, MBajkAvailabilities) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting MBajkAvailabilities',
                    error: err
                });
            }

            if (!MBajkAvailabilities) {
                return res.status(404).json({
                    message: 'No such MBajkAvailabilities'
                });
            }

            MBajkAvailabilities.bikesAvailable = req.body.bikesAvailable ? req.body.bikesAvailable : MBajkAvailabilities.bikesAvailable;
			MBajkAvailabilities.parkSpots = req.body.parkSpots ? req.body.parkSpots : MBajkAvailabilities.parkSpots;
			
            MBajkAvailabilities.save(function (err, MBajkAvailabilities) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating MBajkAvailabilities.',
                        error: err
                    });
                }

                return res.json(MBajkAvailabilities);
            });
        });
    },

    /**
     * MBajkAvailabilitiesController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        MbajkavailabilitiesModel.findByIdAndRemove(id, function (err, MBajkAvailabilities) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the MBajkAvailabilities.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
