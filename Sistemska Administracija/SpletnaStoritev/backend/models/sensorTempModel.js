var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

/**
 * definition of SensorTempSchema attributes
 */
var SensorTempSchema = new Schema({
	'id': {
		type: String,
		required: true
	},
	'temperature' : {
		type : Number,
		required : true
	},
	'frequency' : {
	    type: Number,
	    required : true
	},
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
	}
}, {
	timestamps : true
});

module.exports = mongoose.model('SensorTemp', SensorTempSchema);


/*

{
    "temperature" : "10",
    "frequency" : "na 1 minuto",
    "longitude" : "15.646833",
    "latitude" : " 46.5577328",
}

*/