const multer = require('multer');

const storage = multer.diskStorage({
    destination : function(req, file, cb){    
  
      cb(null, 'publics/uploads/');
    },
  
    filename : function(req, file, cb){
      var mimeType;
      var filename = file.originalname.split('.')[0];
  
      switch (file.mimetype) {
        case "image/png":
          mimeType = "png";
        break;
        case "chose/javascript":
            mimeType = "js";
        break
        default:
          mimeType = "jpg";
        break;
      } 
      cb(null, filename + "." + mimeType);
    }
  });
  
module.exports = multer({
    storage: storage
});