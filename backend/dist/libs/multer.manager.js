"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.MulterManager = void 0;
const date_function_1 = require("functions/date.function");
const multer_1 = __importDefault(require("multer"));
exports.MulterManager = (0, multer_1.default)({
    dest: 'uploads',
    storage: multer_1.default.diskStorage({
        destination: function (req, file, cb) {
            cb(null, 'uploads/');
        },
        filename: function (req, file, cb) {
            cb(null, (0, date_function_1.getToday)() + '_' + file.originalname);
        },
    }),
});
//# sourceMappingURL=multer.manager.js.map