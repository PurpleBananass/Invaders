"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.AuthorizationHandler = void 0;
const base_exception_1 = require("exceptions/base.exception");
const jsonwebtoken_1 = require("jsonwebtoken");
const AuthorizationHandler = async (action) => {
    try {
        const token = action.request.headers['authorization'];
        (0, jsonwebtoken_1.verify)(token, 'tmp');
        return true;
    }
    catch (e) {
        throw new base_exception_1.BaseException(400, 'Bad Token', e);
    }
};
exports.AuthorizationHandler = AuthorizationHandler;
//# sourceMappingURL=authorization.handler.js.map