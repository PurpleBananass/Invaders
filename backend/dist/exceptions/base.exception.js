"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.BaseException = void 0;
class BaseException extends Error {
    constructor(status = 500, message = 'Unknown Error', error) {
        super(message);
        this.status = status;
        this.message = message;
        console.log(error);
    }
}
exports.BaseException = BaseException;
//# sourceMappingURL=base.exception.js.map