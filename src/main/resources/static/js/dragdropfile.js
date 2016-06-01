function doNth(e) {
    e.stopPropagation();
    e.preventDefault();
}
// prevent on non-targeted zone
$(document).on('dragenter', doNth);
$(document).on('dragover',doNth);
$(document).on('drop', doNth);

function dropable(id){
    // handle drag and drop
    var obj = $("#" + id);
    obj.on('dragenter', doNth);
    obj.on('dragover', doNth);
    obj.on('drop', function(e) {
        e.preventDefault();
        var file = e.originalEvent.dataTransfer.files;
        var reader = new FileReader();
        // Read file into memory as UTF-16
        reader.readAsText(file[0], "UTF-8");
        reader.onload = function(e) { obj.html(e.target.result) };
    });
}