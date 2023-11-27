"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.execAsync = void 0;
const shelljs_1 = require("shelljs");
function execAsync(command, options = {}) {
    return new Promise((resolve, reject) => {
        (0, shelljs_1.exec)(command, Object.assign(Object.assign({}, options), { async: false }), (code, stdout, stderr) => {
            if (code !== 0) {
                const e = new Error();
                e.message = stderr;
                e.name = String(code);
                reject(e);
            }
            else {
                resolve(stdout);
            }
        });
    });
}
exports.execAsync = execAsync;
//# sourceMappingURL=async-shell.function.js.map