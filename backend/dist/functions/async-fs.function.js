"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.readFileAsync = void 0;
const fs_1 = require("fs");
const readFileAsync = async (path) => {
    return new Promise((res, rej) => {
        (0, fs_1.readFile)(path, (err, data) => {
            if (err) {
                rej(err);
            }
            res(data);
        });
    });
};
exports.readFileAsync = readFileAsync;
//# sourceMappingURL=async-fs.function.js.map