var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var MBajkAvailabilitiesSchema = new Schema({
	'bikesAvailable' : Number,
	'parkSpots' : Number
}, {
	timestamps : true
});

module.exports = mongoose.model('MBajkAvailabilities', MBajkAvailabilitiesSchema);
