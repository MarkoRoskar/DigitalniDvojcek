var mongoose = require('mongoose');
var Schema   = mongoose.Schema;

/**
 * definition of attributes for our sensor data (camera)
 */
var sensorDataSchema = new Schema({
	'id': {
		type : String,
		required : true
	},
	'bikes' : {
		type : Number,
		required : true
	},
	'people' : {
		type : Number,
		required : true
	},
	'geometry' : {
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
	'frequency' : {
		type : String,
		required : true
	}
}, {
	timestamps : true
});

module.exports = mongoose.model('sensorData', sensorDataSchema);

/*
{
	"bikes" : "10",
	"people" : "4",
	"longitude" : "15.646833",
	"latitude" : "46.5577328",
	"frequency" : "hourly"
}

{
	"bikes" : "8",
	"people" : "6",
	"longitude" : "15.649072",
	"latitude" : "46.558519",
	"frequency" : "daily"
}
*/