//= wrapped

angular
    .module("playitforward.index")
    .controller("EventController", EventController);

function EventController(contextPath, $scope) {

    var vm = this;
    vm.contextPath = contextPath;

    $scope.fileArray = null;
    $scope.deckList = {

        BLK: {}, // Black
        WHT: {}, // White
        BLU: {}, // Blue
        GRN: {}, // Green
        RED: {}, // Red
        ARF: {}, // Artifact
        MCR: {}, // Multi-colored
        NBL: {}, // Non-basic Land
        BAL: {}  // Basic Land
    };

    $scope.createSealedPool = function () {

        var file = document.getElementById("inputFile").files[0];

        if (file) {

            var aReader = new FileReader();
            aReader.readAsText(file, "UTF-8");

            aReader.onload = function (evt) {
                document.getElementById("inputFile").innerHTML = evt.target.result;
                // $scope.fileContent = aReader.result;
                $scope.fileArray = $scope.csvToArray(aReader.result);
                $scope.fileArray.forEach(function(rowValue) {

                    //noinspection JSUnresolvedVariable
                    var name = rowValue.Cardname;
                    if (name !== null && rowValue.colortype !== null) {

                        $scope.deckList[rowValue.colortype][name] = rowValue.Qty;
                    }
                });
            };

            aReader.onerror = function (evt) {
                document.getElementById("myFileInput").innerHTML = "error";
                $scope.fileContent = "error";
            };
        }
    };

    $scope.csvToArray = function (csvString) {

        var lines = csvString.split('\n');
        var headerValues = lines[0].split(',');
        var dataValues = lines.splice(1).map(function (dataLine) {

            // Remove commas from inside quotes.
            dataLine = dataLine.replace(/"[^"]+"/g, function(v) {
                return v.replace(/,/g, '');
            });

            return dataLine.split(',');
        });

        return dataValues.map(function (rowValues) {

            var row = {};
            headerValues.forEach(function (headerValue, index) {
                row[headerValue] = (index < rowValues.length) ? rowValues[index] : null;
            });
            return row;
        });
    }
}


