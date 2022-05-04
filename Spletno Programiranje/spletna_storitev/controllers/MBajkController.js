var MbajkModel = require('../models/MBajkModel.js');

/**
 * MBajkController.js
 *
 * @description :: Server-side logic for managing MBajks.
 */
module.exports = {

    /**
     * MBajkController.list()
     */
    list: function (req, res) {
        MbajkModel.find(function (err, MBajks) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting MBajk.',
                    error: err
                });
            }

            return res.json(MBajks);
        });
    },

    /**
     * MBajkController.show()
     */
    show: function (req, res) {
        var id = req.params.id;

        MbajkModel.findOne({_id: id}, function (err, MBajk) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when getting MBajk.',
                    error: err
                });
            }

            if (!MBajk) {
                return res.status(404).json({
                    message: 'No such MBajk'
                });
            }

            return res.json(MBajk);
        });
    },

    /**
     * MBajkController.create()
     */
    create: function (req, res) {
        var MBajk = new MbajkModel({
			number : req.body.number,
			name : req.body.name,
			address : req.body.address,
			currentStatus : req.body.currentStatus,
			number : req.body.number
        });

        MBajk.save(function (err, MBajk) {
            if (err) {
                return res.status(500).json({
                    message: 'Error when creating MBajk',
                    error: err
                });
            }

            return res.status(201).json(MBajk);
        });
    },

    /**
     * MBajkController.update()
     */
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

    /**
     * MBajkController.remove()
     */
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
};
