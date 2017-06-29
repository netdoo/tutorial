'use strict';

function grepAttrib(obj) {
    var desc = '';
    for (var attrib in obj) {
        desc += attrib + ': ' + obj[attrib] + ' ';
    }

    desc += '\n';
    return desc;
}

module.exports = {
    grepResult: function(title, res) {
        var desc = title + '\n';

        if (200 != res.statusCode) {
            desc += '검색 결과가 없습니다. ' + res.body.toString() + '\n';
            return desc;
        } 

        var cars = JSON.parse(res.getBody('utf8'));

        /// 결과가 1개인 경우.
        if (undefined === cars.hits) {
            desc += grepAttrib(cars._source);
        } else {
            for (var idx in cars.hits.hits) {
                if (undefined === cars.hits.hits[idx].fields) {
                    /// Query에 field 한정자가 없는 경우
                    desc += grepAttrib(cars.hits.hits[idx]._source);
                } else {
                    /// Query에 field 한정자가 포함된 경우
                    desc += grepAttrib(cars.hits.hits[idx].fields);
                }
            }
        }

        return desc;
    },

    grepAggResult: function(title, res) {
        let desc = title + '\n';

        if (200 != res.statusCode) {
            desc += '검색 결과가 없습니다. ' + res.body.toString() + '\n';
            return desc;
        } 

        let body = JSON.parse(res.getBody('utf8'));

        for (let agg in body.aggregations) {
            let buckets = body.aggregations[agg].buckets;
        
            for (let i in buckets) {
                desc += grepAttrib(buckets[i]);
            }
        }

        return desc;
    }
};


