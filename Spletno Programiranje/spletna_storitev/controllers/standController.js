var StandModel = require('../models/standModel.js');

/**
 * standController.js
 *
 * @description :: Server-side logic for managing stands.
 */
module.exports = {

    /**
     * standController.list()
     */
    list: function (req, res) {
        StandModel.find(function (err, stands) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting stand.',
                    error: err
                });
            }

            return res.json(stands);
        });
    },

    /**
     * standController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        StandModel.findOne({_id: id}, function (err, stand) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting stand.',
                    error: err
                });
            }

            if (!stand) {
                return res.status(404).json({
                    message: 'No such stand'
                });
            }

            return res.json(stand);
        });
    },

    /**
     * standController.create()
     */
    create: function (req, res) {
        var stand = new StandModel({
			name : req.body.name,
			parkSpots : req.body.parkSpots
        });

        stand.save(function (err, stand) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating stand',
                    error: err
                });
            }

            return res.status(201).json(stand);
        });
    },

    /**
     * standController.update()
     */
    update: function (req, res) {
        var id = req.params.id;

        StandModel.findOne({_id: id}, function (err, stand) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting stand',
                    error: err
                });
            }

            if (!stand) {
                return res.status(404).json({
                    message: 'No such stand'
                });
            }

            stand.name = req.body.name ? req.body.name : stand.name;
			stand.parkSpots = req.body.parkSpots ? req.body.parkSpots : stand.parkSpots;
			
            stand.save(function (err, stand) {
                if (err) {
                    return res.status(500).json({
                        message: 'Error when updating stand.',
                        error: err
                    });
                }

                return res.json(stand);
            });
        });
    },

    /**
     * standController.remove()
     */
    remove: function (req, res) {
        var id = req.params.id;

        StandModel.findByIdAndRemove(id, function (err, stand) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when deleting the stand.',
                    error: err
                });
            }

            return res.status(204).json();
        });
    }
};
