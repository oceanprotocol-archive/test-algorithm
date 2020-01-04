const fs = require("fs")
const path = require("path")


var input_folder="/data/inputs";
var output_folder="/data/output"


async function processfolder(Path) {
        var files = fs.readdirSync(Path)
        for (var i = 0; i < files.length; i++) {
           var file=files[i];
           var fullpath=Path + "/" + file;
           if (fs.statSync(fullpath).isDirectory()) {
                await processfolder(fullpath)
           } else {
                await countrows(fullpath)
           }
       }
}


async function countrows(file){
        var fileBuffer =  fs.readFileSync(file);
        var to_string = fileBuffer.toString();
        var split_lines = to_string.split("\n");
        var rows=split_lines.length-1;
        fs.appendFileSync(output_folder+'/output.log', 'File '+file+' contains '+rows+' lines'+"\r\n");
        console.log('File '+file+' contains '+rows+' lines')
}





processfolder(input_folder)
