var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

var MBajkSchema = new Schema({
	'number' : Number,
	'name' : String,
	'address' : String,
	'geometry': {
		'type' : {
			type : String,
			enum : ['Point'],
			default : 'Point',
			required : true
		},
		'coordinates' : {
			type : [Number],
			index : "2dsphere",
			required : true
		}
	},
	'currentStatus' : Boolean,
	'currentAvailabilities' : {
		type : Schema.Types.ObjectId,
		ref : 'MBajkAvailabilities'
	},
	'historyAvailabitilities' : [{
		type : Schema.Types.ObjectId,
		ref : 'MBajkAvailabilities'
	}],
	'lastUpdateSensor' : String,
	'refreshRate' : Number
}, {
	timestamps : true
});

module.exports = mongoose.model('MBajk', MBajkSchema);
