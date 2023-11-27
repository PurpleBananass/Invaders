import { getToday } from 'functions/date.function';
import multer from 'multer';

export const MulterManager = multer({
  dest: 'uploads',
  storage: multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, 'uploads/');
    },
    filename: function (req, file, cb) {
      cb(null, getToday() + '_' + file.originalname);
    },
  }),
});
